@echo off
setlocal EnableDelayedExpansion

rem Imposta JAVA_HOME se non è già definito
IF NOT DEFINED JAVA_HOME SET JAVA_HOME=C:\Program Files\Java\jdk-21

echo Build process started

rem Salva la directory di root
set "ROOT_DIR=%CD%"

rem Build commons
echo Building commons
cd /d "%ROOT_DIR%\T-shared"
call mvn install
echo Current directory: %CD%
if ERRORLEVEL 1 (
    echo Error in commons build during call mvn install. Check JAVA_HOME.
    exit /b 1
)
cd /d "%ROOT_DIR%"


rem Build T1-G11
echo Building T1-G11
echo Current directory: %CD%
cd /d "%ROOT_DIR%\T1-G11\applicazione\manvsclass"
echo Current directory: %CD%
call mvn clean package -Dspring.profiles.active=prod
if ERRORLEVEL 1 (
    echo Error in T1-G11 build during call mvn clean package
    exit /b 1
)
docker build -t mick0974/a13:t1-g11 .
cd /d "%ROOT_DIR%"

rem Build T23-G1
echo Building T23-G1
cd /d "%ROOT_DIR%\T23-G1"
call mvn clean package
if ERRORLEVEL 1 (
    echo Error in T23-G1 build during call mvn clean package
    exit /b 1
)
docker build -t mick0974/a13:t23-g1 .
cd /d "%ROOT_DIR%"

rem Build T4-G18
echo Building T4
cd /d "%ROOT_DIR%\T4\gamerepo"
call mvn clean package -DskipTests=true
if ERRORLEVEL 1 (
    echo Error in T4 build during call mvn clean package
    exit /b 1
)
docker build -t mick0974/a13:t4-g18 .
cd /d "%ROOT_DIR%"

rem Build T5-G2
echo Building T5-G2
cd /d "%ROOT_DIR%\T5-G2\t5"
call mvn clean package -DskipTests=true -Dspring.profiles.active=prod
if ERRORLEVEL 1 (
    echo Error in T5-G2 build during call mvn clean package
    exit /b 1
)
docker build -t mick0974/a13:t5-g2 .
cd /d "%ROOT_DIR%"

rem Build T7-G31
echo Building T7-G31
cd /d "%ROOT_DIR%\T7-G31\RemoteCCC"
call mvn clean package -DskipTests=true
if ERRORLEVEL 1 (
    echo Error in T7-G31 build during call mvn clean package
    exit /b 1
)
docker build -t mick0974/a13:t7-g31 .
cd /d "%ROOT_DIR%"

rem Build T8-G21
echo Building T8-G21
cd /d "%ROOT_DIR%\T8-G21\T8"
call mvn clean package
if ERRORLEVEL 1 (
    echo Error in T8-G21 build during call mvn clean package
    exit /b 1
)
docker build -t mick0974/a13:t8-g21 .
cd /d "%ROOT_DIR%"

rem Build ui_gateway
echo Building ui_gateway
cd /d "%ROOT_DIR%\ui_gateway"
docker build -t mick0974/a13:ui-gateway .
cd /d "%ROOT_DIR%"

rem Build api_gateway
echo Building api_gateway
cd /d "%ROOT_DIR%\apiGateway"
call mvn clean package
if ERRORLEVEL 1 (
    echo Error in api_gateway build during call mvn clean package
    exit /b 1
)
docker build -t mick0974/a13:api-gateway .
cd /d "%ROOT_DIR%"

rem Build T0
echo Building T0
cd /d "%ROOT_DIR%\T0\RandoopGenerator"
call mvn clean package
if ERRORLEVEL 1 (
    echo Error in T0 build during call mvn clean package in RandoopGenerator
    exit /b 1
)
cd ..
docker build -t mick0974/a13:t0 .
cd /d "%ROOT_DIR%"

rem Build db-backup
echo Building db-backup
cd /d "%ROOT_DIR%\db-backup"
docker build -t mick0974/a13:db-backup .
cd /d "%ROOT_DIR%"

echo Build process completed
