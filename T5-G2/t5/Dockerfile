# Usa un'immagine di base con Java installato
FROM eclipse-temurin:17-jdk-alpine

# Copia il tuo JAR nel container
COPY ./target/t5-0.0.1-SNAPSHOT.jar /app/

# Copia i file di configurazione
COPY configuration/* /app/configuration/

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Comando di avvio dell'applicazione
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "t5-0.0.1-SNAPSHOT.jar"]