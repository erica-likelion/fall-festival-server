#!/bin/bash

# 환경변수 및 설정
PROJECT_DIR="/home/festival"
CONTAINER_NAME="festival-server-prod"
NEW_CONTAINER_NAME="festival-server-new"
DOCKER_IMAGE="festival-app"
LOG_DIR="$PROJECT_DIR/logs"
BACKUP_DIR="$PROJECT_DIR/backup"

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m'

# 로깅 함수
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}" | tee -a "$LOG_DIR/deploy.log"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}" | tee -a "$LOG_DIR/deploy.log"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}" | tee -a "$LOG_DIR/deploy.log"
}

# 디렉토리 생성
mkdir -p "$LOG_DIR" "$BACKUP_DIR"

log "Festival Spring Boot 서버 무중단 배포 시작..."
log "배포 경로: $PROJECT_DIR"

# 1. Docker 이미지 로드
log "Docker 이미지 로드 중..."
if [ -f "$PROJECT_DIR/festival-app.tar" ]; then
    if docker load < "$PROJECT_DIR/festival-app.tar"; then
        log "Docker 이미지 로드 완료"
    else
        error "Docker 이미지 로드 실패"
        exit 1
    fi
else
    error "Docker 이미지 파일을 찾을 수 없습니다"
    exit 1
fi

# 2. 현재 실행 중인 컨테이너 확인
CURRENT_CONTAINER=$(docker ps --format "{{.Names}}" | grep -E "festival-server" || true)

if [ -n "$CURRENT_CONTAINER" ]; then
    log "현재 실행 중인 컨테이너: $CURRENT_CONTAINER"

    # 3. 기존 컨테이너 백업
    log "기존 컨테이너 이미지 백업 중..."
    BACKUP_IMAGE="festival-backup-$(date +'%Y%m%d_%H%M%S')"
    if docker commit "$CURRENT_CONTAINER" "$BACKUP_IMAGE"; then
        if docker save "$BACKUP_IMAGE" > "$BACKUP_DIR/${BACKUP_IMAGE}.tar"; then
            log "백업 완료: $BACKUP_DIR/${BACKUP_IMAGE}.tar"
        else
            warn "백업 파일 저장 실패, 계속 진행합니다."
        fi
    else
        warn "컨테이너 백업 실패, 계속 진행합니다."
    fi
else
    log "실행 중인 컨테이너가 없습니다. 첫 배포를 진행합니다."
fi

# 4. 새 컨테이너로 서비스 시작
log "새 컨테이너 시작 중..."
cd "$PROJECT_DIR" || { error "디렉토리 이동 실패"; exit 1; }

# 새 컨테이너를 다른 포트로 시작 (8081)
if docker run -d \
    --name "$NEW_CONTAINER_NAME" \
    -p 8081:8080 \
    --env-file .env.prod \
    --restart unless-stopped \
    -v "$LOG_DIR:/app/logs" \
    "$DOCKER_IMAGE"; then
    log "새 컨테이너 시작 성공"
else
    error "새 컨테이너 시작 실패"
    exit 1
fi

# 5. 새 컨테이너 헬스체크
log "새 서버 헬스체크 중..."
chmod +x scripts/health-check.sh

if ./scripts/health-check.sh 8081; then
    log "새 서버 헬스체크 성공"
else
    error "새 서버 헬스체크 실패. 롤백을 진행합니다."
    docker stop "$NEW_CONTAINER_NAME" || true
    docker rm "$NEW_CONTAINER_NAME" || true
    exit 1
fi

# 6. 트래픽 전환 (포트 변경)
log "트래픽 전환 중..."
if [ -n "$CURRENT_CONTAINER" ]; then
    if docker stop "$CURRENT_CONTAINER"; then
        docker rm "$CURRENT_CONTAINER" || true
        log "기존 컨테이너 중지 완료"
    else
        warn "기존 컨테이너 중지 실패, 계속 진행합니다."
    fi
fi

# 새 컨테이너를 80포트로 재시작
docker stop "$NEW_CONTAINER_NAME" || true
docker rm "$NEW_CONTAINER_NAME" || true

if docker run -d \
    --name "$CONTAINER_NAME" \
    -p 80:8080 \
    --env-file .env.prod \
    --restart unless-stopped \
    -v "$LOG_DIR:/app/logs" \
    "$DOCKER_IMAGE"; then
    log "프로덕션 컨테이너 시작 성공"
else
    error "프로덕션 컨테이너 시작 실패"
    exit 1
fi

# 7. 최종 헬스체크
log "최종 헬스체크 중..."
sleep 10

if ./scripts/health-check.sh 80; then
    log "배포 완료. 서비스가 정상적으로 실행 중입니다."

    # 8. 이전 이미지 정리
    log "이전 이미지 정리 중..."
    docker image prune -f || true

    # 9. 배포 로그 저장
    {
        echo "$(date): Deployment successful"
        docker images --format 'table {{.Repository}}\t{{.Tag}}\t{{.ID}}' | grep festival-app || true
    } >> "$PROJECT_DIR/deployment-history.log"

    log "Festival Spring Boot 서버 배포 완료"
else
    error "최종 헬스체크 실패"
    exit 1
fi
