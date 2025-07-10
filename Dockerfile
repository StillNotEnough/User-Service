FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY /target/user-service-0.0.1-SNAPSHOT.jar /app/amazing-shop-user-service.jar
ENTRYPOINT ["java", "-jar", "amazing-shop-user-service.jar"]