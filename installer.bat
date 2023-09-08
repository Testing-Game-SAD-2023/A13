@echo off
echo "Installazione avviata"

echo "Creazione VolumeT9"
docker volume create VolumeT9 || echo "C'è stato un errore"
echo "Creazione global-network"
docker network create global-network || echo "C'è stato un errore"

set list="./T1-G11/applicazione/manvsclass" "./T23-G1" "./T4-G18" "./T5-G2/t5" "./T6-G12/T6" "./T7-G31/RemoteCCC" "./T9-G19\Progetto-SAD-G19-master" "./api_gateway" "./ui_gateway"

(for %%a in (%list%) do ( 
   pushd .
   cd %%a
   echo "Installazione in corso in %%a"
   docker compose up -d --build || echo "C'è stato un errore"
   popd 
))

echo "Installazione terminata"
pause