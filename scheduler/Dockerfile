FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN --mount=type=cache,target=/root/.gradle \
    gradle :scheduler:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/scheduler/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]