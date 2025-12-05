FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY ./target/t9-notification-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
