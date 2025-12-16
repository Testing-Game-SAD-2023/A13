@echo off

echo Rimozione di tutti gli elementi nel docker (anche quelli non inerenti all'applicazione)
set /p confirm=Vuoi procedere con la rimozione di tutti gli elementi di Docker (s/n)? 
if /i "%confirm%" neq "s" goto :eof
:: Stoppare tutti i container in esecuzione
echo Stopping all running containers...
for /f %%i in ('docker ps -q') do (
    echo Stopping container: %%i
    docker stop %%i
)

:: Rimuovere tutti i container fermi
echo Removing all stopped containers...
for /f %%i in ('docker ps -aq') do (
    echo Removing container: %%i
    docker rm %%i
)

:: Rimuovere tutti i volumi
echo Removing all volumes...
for /f %%i in ('docker volume ls -q') do (
    echo Removing volume: %%i
    docker volume rm %%i
)

:: Rimuovere tutte le immagini
echo Removing all images...
for /f %%i in ('docker images -aq') do (
    echo Removing image: %%i
    docker rmi -f %%i
)

:: Rimuovere tutte le reti
echo Removing all networks...
for /f %%i in ('docker network ls -q') do (
    echo Removing network: %%i
    docker network rm %%i
)

:: Rimuovere tutti i container, volumi, immagini e reti orfani
echo Performing system prune...
docker system prune -af

echo Disinstallazione completata
pause