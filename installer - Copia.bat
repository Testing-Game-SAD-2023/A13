@echo off
REM Avvio dell'installazione
echo "Installazione avviata"

REM Creazione del volume Docker 'VolumeT9'
docker volume create VolumeT9 || echo "Errore nella creazione del volume"

REM Creazione del volume Docker 'VolumeT8'
docker volume create VolumeT8 || echo "Errore nella creazione del volume"

REM Creazione della rete Docker 'global-network'
docker network create global-network || echo "Errore nella creazione della rete"

REM Definizione dei percorsi delle directory da visitare
set list="./T1-G11/applicazione/manvsclass"

REM Ciclo attraverso le directory specificate
(for %%a in (%list%) do (
   pushd .
   cd %%a
   echo "Installazione in corso in %%a"
   
   REM Avvio dei container Docker e gestione degli errori
   docker compose up -d --build || echo "Errore nell'installazione del Task %%a"
   
   popd 
))

REM RunScript.bat
echo "Esecuzione script di inizializzazione"
powershell -ExecutionPolicy Bypass -File RunCommands.ps1

REM Messaggio di completamento dell'installazione
echo "Installazione terminata"
pause