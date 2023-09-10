@echo off
REM Avvio dell'installazione
echo "Installazione avviata"

REM Creazione del volume Docker 'VolumeT9'
docker volume create VolumeT9 || echo "Errore nella creazione del volume"

REM Creazione della rete Docker 'global-network'
docker network create global-network || echo "Errore nella creazione della rete"

REM Definizione dei percorsi delle directory da visitare
set list="./T1-G11/applicazione/manvsclass" "./T23-G1" "./T4-G18" "./T5-G2/t5" "./T6-G12/T6" "./T7-G31/RemoteCCC" "./T9-G19\Progetto-SAD-G19-master" "./api_gateway" "./ui_gateway"

REM Ciclo attraverso le directory specificate
(for %%a in (%list%) do (
   pushd .
   cd %%a
   echo "Installazione in corso in %%a"
   
   REM Avvio dei container Docker e gestione degli errori
   docker compose up -d --build || echo "Errore nell'installazione del Task %%a"
   
   popd 
))

REM Messaggio di completamento dell'installazione
echo "Installazione terminata"
pause