#!/bin/bash

PORT=${1:-80}
HEALTH_URL="http://localhost:$PORT/actuator/health"
MAX_ATTEMPTS=30
ATTEMPT=1

echo "헬스체크 시작 (포트: $PORT)"
echo "URL: $HEALTH_URL"

while [ "$ATTEMPT" -le "$MAX_ATTEMPTS" ]; do
    echo "시도 $ATTEMPT/$MAX_ATTEMPTS..."

    # curl로 헬스체크 수행
    if RESPONSE=$(curl -s -w "%{http_code}" -o /tmp/health_response "$HEALTH_URL" 2>/dev/null); then
        HTTP_CODE=${RESPONSE: -3}

        if [ "$HTTP_CODE" = "200" ]; then
            echo "헬스체크 성공 (HTTP $HTTP_CODE)"

            # 응답 내용 확인
            if command -v jq &> /dev/null; then
                if STATUS=$(jq -r '.status' /tmp/health_response 2>/dev/null); then
                    if [ "$STATUS" = "UP" ]; then
                        echo "애플리케이션 상태: UP"
                        # 임시 파일 정리
                        rm -f /tmp/health_response
                        exit 0
                    else
                        echo "애플리케이션 상태: $STATUS"
                    fi
                else
                    echo "JSON 파싱 실패, HTTP 200으로 성공 판단"
                    rm -f /tmp/health_response
                    exit 0
                fi
            else
                # jq가 없으면 단순히 HTTP 200만 확인
                echo "jq가 설치되지 않음, HTTP 200으로 성공 판단"
                rm -f /tmp/health_response
                exit 0
            fi
        else
            echo "대기 중... (HTTP $HTTP_CODE)"
        fi
    else
        echo "대기 중... (curl 실패)"
        HTTP_CODE="000"
    fi

    sleep 10
    ATTEMPT=$((ATTEMPT + 1))
done

echo "헬스체크 실패: $MAX_ATTEMPTS번 시도 후 실패"
# 임시 파일 정리
rm -f /tmp/health_response
exit 1
