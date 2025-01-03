FROM ubuntu:24.04

# Set the working directory
WORKDIR /app

# Copy your Java application JAR file to the container
COPY gc.jar /app/gc.jar
COPY randoop-all-4.3.3.jar /app/randoop-all-4.3.3.jar
COPY evosuite-1.0.6.jar /app/evosuite-1.0.6.jar


# Install Ollama
RUN apt-get update && apt-get install -y   openjdk-21-jdk openjdk-8-jdk
#Expose the port Ollama will run on
EXPOSE 8002

# Run your Java application
CMD ["java", "-jar", "/app/gc.jar"]