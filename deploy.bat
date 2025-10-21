@echo off
setlocal EnableDelayedExpansion

echo Deployment started

set JAVA_HOME=%JAVA_HOME%
if "%JAVA_HOME%"=="" set JAVA_HOME=C:\Program Files\Java\jdk-21

rem Creazione dei volumi Docker se non esistono
docker volume create VolumeT0 || echo VolumeT0 already exists

rem Creazione della rete Docker se non esiste
docker network ls | findstr /C:"global-network" >nul || docker network create global-network

set ROOT_DIR=%CD%

rem Deploy dei componenti
echo Deploying T1-G11
cd /d "%ROOT_DIR%\T1-G11\applicazione\manvsclass"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying T1-G11
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying T23-G1
cd /d "%ROOT_DIR%\T23-G1"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying T23-G1
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying T4
cd /d "%ROOT_DIR%\T4\gamerepo"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying T4-G18
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying T5-G2
cd /d "%ROOT_DIR%\T5-G2\t5"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying T5-G2
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying T7-G31
cd /d "%ROOT_DIR%\T7-G31\RemoteCCC"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying T7-G31
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying T8-G21
cd /d "%ROOT_DIR%\T8-G21\T8"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying T8-G21
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying api_gateway
cd /d "%ROOT_DIR%\apiGateway"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying api_gateway
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying ui_gateway
cd /d "%ROOT_DIR%\ui_gateway"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying ui_gateway
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying T0
cd /d "%ROOT_DIR%\T0"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying T0
    exit /b 1
)
cd /d "%ROOT_DIR%"

echo Deploying db-backup
cd /d "%ROOT_DIR%\db-backup"
docker compose up -d
if %ERRORLEVEL% neq 0 (
    echo Error deploying db-backup
    exit /b 1
)
cd /d "%ROOT_DIR%"

REM RunScript.bat
echo "Executing RunCommands.ps1"
powershell -ExecutionPolicy Bypass -File RunCommands.ps1

echo Deployment completed
