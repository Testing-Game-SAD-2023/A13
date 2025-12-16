#!/bin/bash

# Stoppare tutti i container in esecuzione
docker stop $(docker ps -aq)

# Rimuovere tutti i container fermi
docker rm $(docker ps -aq)
