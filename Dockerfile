# Stage 1: Build the application
FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/project
COPY . .
RUN gradle build -x test

# Stage 2: Create the final image
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/festival-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "festival-0.0.1-SNAPSHOT.jar"]
