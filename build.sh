#!/bin/bash

set -e  # Ferma lo script se un comando fallisce

echo "Build process started"

ROOT_DIR=$(pwd)  # Salva la directory di partenza

# Build commons
echo "Building T-shared"
export JAVA_HOME=${JAVA_HOME:-"/usr/lib/jvm/java-21-openjdk-amd64"}
cd "$ROOT_DIR/T-shared"
mvn install || { echo "Error in T-shared build during mvn install. Check JAVA_HOME." ; exit 1; }
cd "$ROOT_DIR"

# Build T1-G11
echo "Building T1-G11"
cd "$ROOT_DIR/T1-G11/applicazione/manvsclass"
mvn clean package -Dspring.profiles.active=prod || { echo "Error in T1-G11 build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:t1-g11 .
cd "$ROOT_DIR"

# Build T23-G1
echo "Building T23-G1"
cd "$ROOT_DIR/T23-G1"
mvn clean package || { echo "Error in T23-G1 build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:t23-g1 .
cd "$ROOT_DIR"

# Build T4-G18
echo "Building T4"
cd "$ROOT_DIR/T4/gamerepo"
mvn clean package -DskipTests=true -Dspring.profiles.active=prod || { echo "Error in T4 build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:t4-g18 .
cd "$ROOT_DIR"

# Build T5-G2
echo "Building T5-G2"
cd "$ROOT_DIR/T5-G2/t5"
mvn clean package -DskipTests=true -Dspring.profiles.active=prod || { echo "Error in T5-G2 build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:t5-g2 .
cd "$ROOT_DIR"

# Build T7-G31
echo "Building T7-G31"
cd "$ROOT_DIR/T7-G31/RemoteCCC"
mvn clean package -DskipTests=true || { echo "Error in T7-G31 build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:t7-g31 .
cd "$ROOT_DIR"

# Build T8-G21
echo "Building T8-G21"
cd "$ROOT_DIR/T8-G21/T8"
mvn clean package || { echo "Error in T8-G21 build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:t8-g21 .
cd "$ROOT_DIR"

# Build ui_gateway
echo "Building ui_gateway"
cd "$ROOT_DIR/ui_gateway"
docker build -t mick0974/a13:ui-gateway .
cd "$ROOT_DIR"

# Build api_gateway
echo "Building api_gateway"
cd "$ROOT_DIR/apiGateway"
mvn clean package || { echo "Error in api_gateway build during mvn clean package" ; exit 1; }
docker build -t mick0974/a13:api-gateway .
cd "$ROOT_DIR"

# Build T0
echo "Building T0"
cd "$ROOT_DIR/T0/RandoopGenerator"
mvn clean package || { echo "Error in RandoopGenerator mvn package"; exit 1; }
cd ..
docker build -t mick0974/a13:t0 .
cd "$ROOT_DIR"

# Build T0
echo "Building db-backup"
cd "$ROOT_DIR/db-backup"
docker build -t mick0974/a13:db-backup .
cd "$ROOT_DIR"

echo "Build process completed"
