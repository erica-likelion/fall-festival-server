#!/bin/bash

# === 설정 ===
PROJECT_DIR="/home/festival"
CONTAINER_NAME="festival-server-prod"
NEW_CONTAINER_NAME="festival-server-new"
DOCKER_IMAGE="festival-app:latest"
LOG_DIR="$PROJECT_DIR/logs"
BACKUP_DIR="$PROJECT_DIR/backup"

# --- Nginx 연동에 필요한 변수 (추가)---
COMPOSE_FILE="$PROJECT_DIR/docker-compose-prod.yml"
NGINX_CONTAINER="festival-nginx-prod"
UPSTREAM_CONF_FILE="$PROJECT_DIR/nginx/conf.d/upstream.conf"
NETWORK_NAME="festival_festival-network"

# === 색상 및 로깅 함수 ===
RED='\033[0;31m';
GREEN='\033[0;32m';
YELLOW='\033[0;33m';
NC='\033[0m'

log() { echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}" | tee -a "$LOG_DIR/deploy.log"; }
warn() { echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}" | tee -a "$LOG_DIR/deploy.log"; }
error() { echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}" | tee -a "$LOG_DIR/deploy.log"; }

# === 스크립트 시작 ===
mkdir -p "$LOG_DIR" "$BACKUP_DIR"
cd "$PROJECT_DIR" || { error "디렉토리 이동 실패"; exit 1; }

log "Festival Spring Boot 서버 무중단 배포 시작..."

# --- 배포 시작 시 Nginx 컨테이너 실행 보장(추가) ---
log "Nginx 및 네트워크 상태 확인..."
if ! docker compose -f "$COMPOSE_FILE" up -d nginx; then
    error "Nginx 컨테이너 시작 실패."
    exit 1
fi

# 1. Docker 이미지 로드
log "Docker 이미지 로드 중..."
if ! docker load < "$PROJECT_DIR/festival-app.tar"; then
    error "Docker 이미지 로드 실패"; exit 1
fi
log "Docker 이미지 로드 완료."

# 2. 현재 실행 중인 컨테이너 확인
CURRENT_CONTAINER=$(docker ps --format "{{.Names}}" | grep -E "^${CONTAINER_NAME}$" || true)

# 3. 기존 컨테이너 백업
if [ -n "$CURRENT_CONTAINER" ]; then
    log "현재 실행 중인 컨테이너: $CURRENT_CONTAINER"
    log "기존 컨테이너 이미지 백업 중..."
    BACKUP_IMAGE="festival-backup-$(date +'%Y%m%d_%H%M%S')"
    if docker commit "$CURRENT_CONTAINER" "$BACKUP_IMAGE" && docker save "$BACKUP_IMAGE" > "$BACKUP_DIR/${BACKUP_IMAGE}.tar"; then
        log "백업 완료: $BACKUP_DIR/${BACKUP_IMAGE}.tar"
    else
        warn "컨테이너 백업 실패, 계속 진행합니다."
    fi
else
    log "실행 중인 컨테이너가 없습니다. 첫 배포를 진행합니다."
fi

# 4. 새 컨테이너로 서비스 시작
log "새 컨테이너($NEW_CONTAINER_NAME) 시작 중..."
# --- network 옵션 추가 (추가)---
docker run -d \
    --name "$NEW_CONTAINER_NAME" \
    -p 127.0.0.1:8081:8080 \
    --network "$NETWORK_NAME" \
    --env-file .env.prod \
    --restart unless-stopped \
    -v "$LOG_DIR:/app/logs" \
    "$DOCKER_IMAGE"
if [ $? -ne 0 ]; then
    error "새 컨테이너 시작 실패"; exit 1
fi

# 5. 새 컨테이너 헬스체크
log "새 서버 헬스체크 중..."
chmod +x scripts/health-check.sh
if ! ./scripts/health-check.sh 8081; then
    error "새 서버 헬스체크 실패. 롤백을 진행합니다."
    docker stop "$NEW_CONTAINER_NAME" && docker rm "$NEW_CONTAINER_NAME"
    exit 1
fi
log "새 서버 헬스체크 성공"

# --- [수정 4] 트래픽 전환 방식을 Nginx 리로드로 변경 ---
log "Nginx 트래픽을 새 컨테이너로 전환합니다..."
mkdir -p "$(dirname "$UPSTREAM_CONF_FILE")"
echo "server $NEW_CONTAINER_NAME:8080;" > "$UPSTREAM_CONF_FILE"
docker exec "$NGINX_CONTAINER" nginx -s reload
if [ $? -ne 0 ]; then
    error "Nginx 리로드 실패. 롤백합니다."
    # 원래 upstream으로 되돌리는 로직 추가 가능
    docker stop "$NEW_CONTAINER_NAME" && docker rm "$NEW_CONTAINER_NAME"
    exit 1
fi
log "트래픽 전환 완료. 기존 컨테이너를 정리합니다."
sleep 5 # 트래픽이 완전히 전환될 때까지 잠시 대기

# 6. 기존 컨테이너 중지 및 이름 변경
if [ -n "$CURRENT_CONTAINER" ]; then
    log "기존 컨테이너($CURRENT_CONTAINER)를 중지/삭제합니다."
    docker stop "$CURRENT_CONTAINER" && docker rm "$CURRENT_CONTAINER"
fi
log "새 컨테이너를 '$CONTAINER_NAME'으로 리네임합니다."
docker rename "$NEW_CONTAINER_NAME" "$CONTAINER_NAME"

# 7. 최종 헬스체크 및 정리
log "배포 완료. 서비스가 정상적으로 실행 중입니다."
log "이전 이미지 정리 중..."
docker image prune -f

log "Festival Spring Boot 서버 배포 완료"
exit 0
