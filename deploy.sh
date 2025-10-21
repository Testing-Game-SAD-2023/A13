#!/bin/bash

set -e

echo "Deployment started"

export JAVA_HOME=${JAVA_HOME:-"/usr/lib/jvm/java-21-openjdk-amd64"}

# Creazione dei volumi Docker se non esistono
docker volume create VolumeT0 || echo "VolumeT0 already exists"

# Creazione della rete Docker se non esiste
if ! docker network ls | grep -q "global-network"; then
    docker network create global-network
fi

ROOT_DIR=$(pwd)  # Salva la directory di partenza

# Deploy dei componenti
echo "Deploying T1-G11"
cd "$ROOT_DIR/T1-G11/applicazione/manvsclass"
docker compose up -d || { echo "Error deploying T1-G11"; exit 1; }
cd "$ROOT_DIR"

echo "Deploying T23-G1"
cd "$ROOT_DIR/T23-G1"
docker compose up -d || { echo "Error deploying T23-G1"; exit 1; }
cd "$ROOT_DIR"

echo "Deploying T4"
cd "$ROOT_DIR/T4/gamerepo"
docker compose up -d || { echo "Error deploying T4"; exit 1; }

echo "Deploying T5-G2"
cd "$ROOT_DIR/T5-G2/t5"
docker compose up -d || { echo "Error deploying T5-G2"; exit 1; }
cd "$ROOT_DIR"

echo "Deploying T7-G31"
cd "$ROOT_DIR/T7-G31/RemoteCCC"
docker compose up -d || { echo "Error deploying T7-G31"; exit 1; }
cd "$ROOT_DIR"

echo "Deploying T8-G21"
cd "$ROOT_DIR/T8-G21/T8"
docker compose up -d || { echo "Error deploying T8-G21"; exit 1; }
cd "$ROOT_DIR"

echo "Deploying api_gateway"
cd "$ROOT_DIR/apiGateway"
docker compose up -d || { echo "Error deploying apiGateway"; exit 1; }
cd "$ROOT_DIR"

echo "Deploying ui_gateway"
cd "$ROOT_DIR/ui_gateway"
docker compose up -d || { echo "Error deploying ui_gateway"; exit 1; }
cd "$ROOT_DIR"

echo "Deploying T0"
cd "$ROOT_DIR/T0"
docker compose up -d || { echo "Error deploying T0"; exit 1; }
cd "$ROOT_DIR"

# Build T0
echo "Deploying db-backup"
cd "$ROOT_DIR/db-backup"
docker compose up -d || { echo "Error deploying db-backup"; exit 1; }
cd "$ROOT_DIR"

# Avvio script di configurazione finale
commands="""
use manvsclass
db.createCollection(\"ClassUT\");
db.createCollection(\"interaction\");
db.createCollection(\"Admin\");
db.createCollection(\"Operation\");
db.ClassUT.createIndex({ difficulty: 1 });
db.interaction.createIndex({ name: \"text\", type: 1 });
db.Operation.createIndex({ name: \"text\" });
db.Admin.createIndex({ username: 1 });
"""

echo "$commands" | docker exec -i t1-mongo_db mongosh

echo "Deployment completed"
