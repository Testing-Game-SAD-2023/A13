#!/bin/bash

set -e  # Ferma lo script se un comando fallisce

echo "Build process started"

ROOT_DIR=$(pwd)  # Salva la directory di partenza

# Chiedi all'utente quali servizi costruire
echo "Enter the numbers of the services to build, separated by spaces (0-10) or type 'all' to build them all:"
echo "0 - T-shared"
echo "1 - T1-G11"
echo "2 - T23-G1"
echo "3 - T4"
echo "4 - T5-G2"
echo "5 - T7-G31"
echo "6 - T8-G21"
echo "7 - ui_gateway"
echo "8 - api_gateway"
echo "9 - T0"
echo "10 - db-backup"
echo "Select: "
read -r -a SELECTION

# Se l'utente ha scelto "all", builda tutto
if [[ "${SELECTION[0]}" == "all" ]]; then
    SELECTION=( {0..10} )
fi

for i in "${SELECTION[@]}"; do
    case $i in
        0)
            echo "Building commons"
            cd "$ROOT_DIR/T-shared"
            mvn install || { echo "Error in commons build"; exit 1; }
            cd "$ROOT_DIR"
            ;;
        1)
            echo "Building T1-G11"
            cd "$ROOT_DIR/T1-G11/applicazione/manvsclass"
            mvn clean package -Dspring.profiles.active=prod || { echo "Error in T1-G11 build"; exit 1; }
            docker build -t mick0974/a13:t1-g11 .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        2)
            echo "Building T23-G1"
            cd "$ROOT_DIR/T23-G1"
            mvn clean package || { echo "Error in T23-G1 build"; exit 1; }
            docker build -t mick0974/a13:t23-g1 .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        3)
            echo "Building T4"
            cd "$ROOT_DIR/T4/gamerepo"
            mvn clean package -DskipTests=true -Dspring.profiles.active=prod || { echo "Error in T4 build"; exit 1; }
            docker build -t mick0974/a13:t4-g18 .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        4)
            echo "Building T5-G2"
            cd "$ROOT_DIR/T5-G2/t5"
            mvn clean package -DskipTests=true -Dspring.profiles.active=prod || { echo "Error in T5-G2 build"; exit 1; }
            docker build -t mick0974/a13:t5-g2 .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        5)
            echo "Building T7-G31"
            cd "$ROOT_DIR/T7-G31/RemoteCCC"
            mvn clean package -DskipTests=true || { echo "Error in T7-G31 build"; exit 1; }
            docker build -t mick0974/a13:t7-g31 .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        6)
            echo "Building T8-G21"
            cd "$ROOT_DIR/T8-G21/T8"
            mvn clean package || { echo "Error in T8-G21 build"; exit 1; }
            docker build -t mick0974/a13:t8-g21 .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        7)
            echo "Building ui_gateway"
            cd "$ROOT_DIR/ui_gateway"
            docker build -t mick0974/a13:ui-gateway .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        8)
            echo "Building api_gateway"
            cd "$ROOT_DIR/apiGateway"
            mvn clean package || { echo "Error in api_gateway build"; exit 1; }
            docker build -t mick0974/a13:api-gateway .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        9)
            echo "Building T0"
            cd "$ROOT_DIR/T0/RandoopGenerator"
            mvn clean package || { echo "Error in RandoopGenerator mvn package"; exit 1; }
            cd ..
            docker build -t mick0974/a13:t0 .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        10)
            echo "Building db-backup"
            cd "$ROOT_DIR/db-backup"
            docker build -t mick0974/a13:db-backup .
            docker compose up -d
            cd "$ROOT_DIR"
            ;;
        *)
            echo "Servizio $i non valido, ignorato."
            ;;
    esac
done

echo "Build and deploy process completed"
