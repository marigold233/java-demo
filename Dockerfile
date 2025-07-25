FROM docker.io/openjdk:21-jdk-slim

WORKDIR /app

RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY target/demo-0.0.3-SNAPSHOT.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
