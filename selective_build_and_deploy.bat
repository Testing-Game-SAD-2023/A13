@echo off
setlocal enabledelayedexpansion

echo Build and deploy process started

set ROOT_DIR=%cd%

rem Creazione dei volumi Docker se non esistono
docker volume create VolumeT9 || echo VolumeT9 already exists
docker volume create VolumeT8 || echo VolumeT8 already exists
docker volume create VolumeT0 || echo VolumeT0 already exists
docker volume create logs || echo logs already exists

rem Creazione della rete Docker se non esiste
docker network ls | findstr /C:"global-network" >nul || docker network create global-network

:: Chiedi all'utente quali servizi costruire
echo Enter the numbers of the services to build and deploy, separated by spaces (0-10) or type 'all' to build them all:
echo 0 - commons
echo 1 - T1-G11
echo 2 - T23-G1
echo 3 - T4-G18
echo 4 - T5-G2
echo 5 - T7-G31
echo 6 - T8-G21
echo 7 - ui_gateway
echo 8 - api_gateway
echo 9 - T0
echo 10 - db-backup
set /p SELECTION=Scelte (es. 0 1 2 o 'all'):

:: Se l'utente ha scelto "all", builda tutto
if /i "%SELECTION%"=="all" (
    set SELECTION=0 1 2 3 4 5 6 7 8 9 10
)

:: Loop per ciascuna selezione
for %%i in (%SELECTION%) do (
    if %%i==0 (
        echo Building commons
        cd /d "%ROOT_DIR%\T-shared"
        call mvn install || (echo Error in commons build & exit /b 1)
        cd /d "%ROOT_DIR%"
    ) else if %%i==1 (
        echo Building T1-G11
        cd /d "%ROOT_DIR%\T1-G11\applicazione\manvsclass"
        call mvn clean package -Dspring.profiles.active=prod || (echo Error in T1-G11 build & exit /b 1)
        docker build -t mick0974/a13:t1-g11 .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying T1-G11
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==2 (
        echo Building T23-G1
        cd /d "%ROOT_DIR%\T23-G1"
        call mvn clean package || (echo Error in T23-G1 build & exit /b 1)
        docker build -t mick0974/a13:t23-g1 .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying T23-G1
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==3 (
        echo Building T4-G18
        cd /d "%ROOT_DIR%\T4"
        docker build -t mick0974/a13:t4-g18 .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying T4-G18
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==4 (
        echo Building T5-G2
        cd /d "%ROOT_DIR%\T5-G2\t5"
        call mvn clean package -DskipTests=true -Dspring.profiles.active=prod || (echo Error in T5-G2 build & exit /b 1)
        docker build -t mick0974/a13:t5-g2 .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying T5-G2
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==5 (
        echo Building T7-G31
        cd /d "%ROOT_DIR%\T7-G31\RemoteCCC"
        call mvn clean package -DskipTests=true || (echo Error in T7-G31 build & exit /b 1)
        docker build -t mick0974/a13:t7-g31 .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying T7-G31
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==6 (
        echo Building T8-G21
        cd /d "%ROOT_DIR%\T8-G21\T8"
        call mvn clean package || (echo Error in T8-G21 build & exit /b 1)
        cd /d "%ROOT_DIR%"
        docker build -t mick0974/a13:t8-g21 .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying T1-G11
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==7 (
        echo Building ui_gateway
        cd /d "%ROOT_DIR%\ui_gateway"
        docker build -t mick0974/a13:ui-gateway .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying ui gateway
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==8 (
        echo Building api_gateway
        cd /d "%ROOT_DIR%\apiGateway"
        call mvn clean package || (echo Error in api_gateway build & exit /b 1)
        docker build -t mick0974/a13:api-gateway .
        if ERRORLEVEL 1 (
            echo Error in API gateway build
            exit /b 1
        )
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
            echo Error deploying T1-G11
            exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==9 (
        echo Building T0
        cd /d "%ROOT_DIR%\T0\RandoopGenerator"
        call mvn clean package || (echo Error in T0 build & exit /b 1)
        cd ..
        docker build -t mick0974/a13:t0 .
        if ERRORLEVEL 1 (
          echo Error in T0 build during call mvn clean package
          exit /b 1
        )
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
          echo Error deploying T0
          exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else if %%i==10 (
        echo Building db-backup
        cd /d "%ROOT_DIR%\db-backup
        docker build -t mick0974/a13:db-backup .
        docker compose up -d
        if %ERRORLEVEL% neq 0 (
        echo Error deploying db-backup
        exit /b 1
        )
        cd /d "%ROOT_DIR%"
    ) else (
        echo Servizio %%i non valido, ignorato.
    )
)

echo Build and deploy processes completed
endlocal