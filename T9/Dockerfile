# Usa un'immagine compatibile con tutte le architetture
FROM eclipse-temurin:17-jre

# Copia il JAR corretto
COPY ./target/t9-notification-0.0.1-SNAPSHOT.jar /app/app.jar

# Copia i file di configurazione (se necessari)
#COPY configuration/* /app/configuration/

WORKDIR /app

# Comando corretto - SENZA profilo hardcoded
CMD ["java", "-jar", "app.jar"]