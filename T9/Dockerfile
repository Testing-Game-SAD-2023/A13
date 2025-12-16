FROM eclipse-temurin:17-jdk-alpine

# Nome del jar generato da Maven (finalName nel pom.xml)
ARG JAR_FILE=target/t9-profile-service.jar

# Copia il jar dentro l'immagine
COPY ${JAR_FILE} app.jar

# Espone la porta dell'app
EXPOSE 8089

# Avvia Spring Boot con profilo prod
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
