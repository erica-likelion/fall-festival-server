# Stage 1: Build the application
FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/project
COPY build.gradle settings.gradle ./
RUN gradle build -x test --build-cache || true

#소스코드 복사
COPY . .
RUN gradle build -x test

# Stage 2: Create the final image
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
