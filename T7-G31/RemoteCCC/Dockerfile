FROM maven:3.9.4-eclipse-temurin-17

ARG JAR_FILE=target/RemoteCCC-0.0.1.jar
COPY ${JAR_FILE} app.jar
COPY ClientProject ClientProject 
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Esegui mvn clean install -U per forzare l'aggiornamento delle dipendenze e installare il progetto
# questo funziona, ma solo in parte, penso che si debba rielabborare sto file .jar o installare tutto qui da cmd 
# che forse è pure più facile
#RUN mvn clean install -U