# Usa un'immagine base con Java 8
FROM openjdk:8-jdk

# Copia il file pom.xml per scaricare le dipendenze in cache
COPY pom.xml .

# Scarica le dipendenze Maven per migliorare le performance di build
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline -B

# Copia il contenuto del progetto
COPY src ./src

COPY testCompiler testCompiler

# Compila il progetto Spring Boot
RUN mvn clean package -DskipTests

# Espone la porta 8576 per l'applicazione
EXPOSE 8756

# Comando predefinito per avviare l'applicazione Spring Boot
CMD ["java", "-jar", "target/EvoSuiteCompiler-0.0.1-SNAPSHOT.jar"]
