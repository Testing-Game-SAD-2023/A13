FROM maven:3.9.4-eclipse-temurin-17

ARG JAR_FILE=target/RemoteCCC-0.0.1.jar
COPY ${JAR_FILE} app.jar
COPY ClientProject ClientProject 
ENTRYPOINT ["java", "-jar", "/app.jar"]