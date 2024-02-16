# Usa un'immagine di base con Java installato
FROM openjdk:17-jdk

# Copia il tuo JAR nel container
COPY ./target/t5-0.0.1-SNAPSHOT.jar /app/

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Comando di avvio dell'applicazione
CMD ["java", "-jar", "t5-0.0.1-SNAPSHOT.jar"]