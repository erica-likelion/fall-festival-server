# ===== 1단계: 빌드 =====
FROM gradle:8.9-jdk17 AS build
WORKDIR /home/gradle/project

COPY settings.gradle build.gradle gradle.properties* ./
COPY gradle ./gradle
RUN gradle --no-daemon build -x test || true

COPY . .
RUN gradle --no-daemon clean bootJar -x test

# plain 제외하고 단일 파일로 고정
RUN JAR_FILE="$(ls build/libs | grep -E '\.jar$' | grep -v 'plain' | head -n 1)" \
 && cp "build/libs/${JAR_FILE}" /home/gradle/project/app.jar


# ===== 2단계: 런타임 =====
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

# 헬스체크에 필요하다면 wget 설치
RUN apk add --no-cache wget

COPY --from=build /home/gradle/project/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
