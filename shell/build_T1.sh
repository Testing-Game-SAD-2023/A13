#!/bin/bash

set -e  # Ferma lo script se un comando fallisce

echo "Build process started"

ROOT_DIR=$(dirname "$(pwd)")  # Salva la directory di partenza

# Build commons
export JAVA_HOME=${JAVA_HOME:-"/usr/lib/jvm/java-21-openjdk-amd64"}

# Build T1-G11
echo "Building T1-G11"
cd "$ROOT_DIR/T1-G11/applicazione/manvsclass"
mvn clean package -Dspring.profiles.active=prod || { echo "Error in T1-G11 build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:t1-g11 .
cd "$ROOT_DIR"

echo "Build process completed"
