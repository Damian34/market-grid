FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY ./build/quarkus-app/ ./
EXPOSE 8082
ENTRYPOINT ["java", "-Dquarkus.profile=dev", "-jar", "quarkus-run.jar"]
