# Usa un'immagine di base con supporto per Java
FROM openjdk:17-jdk-alpine

# Copia i file del progetto nella directory /app del container
COPY codemirror /app/codemirror
COPY T6 /app/T6
COPY Icone /app/Icone
COPY index.html /app/index.html

# Copia il file JAR nella directory del progetto
COPY T6/target/T6-0.0.1-SNAPSHOT.jar /app/T6-0.0.1-SNAPSHOT.jar

# Compila il progetto
WORKDIR /app/T6

# Espone la porta del progetto Spring
EXPOSE 80

# Avvia il progetto Spring
CMD ["java", "-jar", "/app/T6-0.0.1-SNAPSHOT.jar"]
