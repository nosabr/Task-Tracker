FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN --mount=type=cache,target=/root/.gradle-gateway \
    GRADLE_USER_HOME=/root/.gradle-gateway gradle :api-gateway:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/api-gateway/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]