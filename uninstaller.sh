#!/bin/bash

# Stoppare tutti i container in esecuzione
docker stop $(docker ps -aq)

# Rimuovere tutti i container fermi
docker rm $(docker ps -aq)

# Rimuovere tutti i volumi
docker volume prune -af

# Rimuovere tutte le immagini
docker rmi -af $(docker images -aq)

# Rimuovere tutte le reti
docker network prune -af

# Rimuovere tutti i container, volumi, immagini e reti orfani
docker system prune -af
