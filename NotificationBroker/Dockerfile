FROM eclipse-temurin:17-jdk
WORKDIR /app
# Copy the jar file from the target directory
COPY target/NotificationBroker-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
