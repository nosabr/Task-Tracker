FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN GRADLE_USER_HOME=/root/.gradle-task gradle :task-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/task-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]