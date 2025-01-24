@echo off

GOTO :MAIN

REM Installer function for commons
:function0
echo "Installing commons"
IF NOT DEFINED JAVA_HOME SET JAVA_HOME=C:\Program Files\Java\jdk-21
cd "./commons"
call mvn install || ( echo "Error in commons installation during mvn package. Check that the JAVA_HOME variable will point to a valid JDK" &&  exit /b 1 )
exit /b 0


REM Installer function for T1-G11
:function1
echo "Installing T1-G11"
cd "./T1-G11/applicazione/manvsclass"
call mvn package || ( echo "Error in T1-G11 installation during mvn package" &&  exit /b 1 )
docker compose up -d --build || ( echo "Error in T1-G11 installation during docker compose up" &&  exit /b 1 )
exit /b 0

REM Installer function for T23-G1
:function2
echo "Installing T23-G1"
cd "./T23-G1"
call mvn package || ( echo "Error in T23-G1 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T23-G1 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T4-G18
:function3
echo "Installing T4-G18"
cd "./T4-G18"
docker compose up -d --build || ( echo "Error in T4-G18 installation during docker compose up"  && exit /b 1 )
exit /b 0

REM Installer function for T5-G2
:function4
echo "Installing T5-G2"
cd "./T5-G2/t5"
call mvn package -DskipTests=true || ( echo "Error in T5-G2 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T5-G2 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T6-G12
:function5
echo "Installing T6-G12"
cd "./T6-G12/T6"
call mvn package || ( echo "Error in T6-G12 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T6-G12 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T7-G31
:function6
echo "Installing T7-G31"
cd "./T7-G31/RemoteCCC"
call mvn package || ( echo "Error in T7-G31 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T7-G31 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T8-G21
:function7
echo "Installing T8-G21"
cd "./T8-G21/Progetto_SAD_GRUPPO21_TASK8/Progetto_def/opt_livelli/Prototipo2.0"
cd "Serv"
call mvn package || ( echo "Error in T8-G21 installation during mvn package" && exit /b 1 )
cd ..
docker compose up -d --build || ( echo "Error in T8-G21 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T9-G19
:function8
echo "Installing T9-G19"
cd "./T9-G19\Progetto-SAD-G19-master"
call mvn package || ( echo "Error in T9-G19 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T9-G19 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for ui_gateway
:function9
echo "Installing ui_gateway"
cd "./ui_gateway"
docker compose up -d --build || ( echo "Error in ui_gateway installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for api_gateway
:function10
echo "Installing api_gateway"
cd "./api_gateway"
call mvn package || ( echo "Error in api_gateway installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in ui_gateway installation during docker compose up" && exit /b 1 )
exit /b 0


:MAIN
REM Avvio dell'installazione
echo "Installation started"

REM Creazione del volume Docker 'VolumeT9'
docker volume create VolumeT9 || ( echo "Error in creating the volume T9" && exit /b 1 )

REM Creazione del volume Docker 'VolumeT8'
docker volume create VolumeT8 || ( echo "Error in creating the volume T8" && exit /b 1 )

REM Creazione della rete Docker 'global-network'
docker network create global-network || ( echo "Error in creating the network global-network" && exit /b 1 )

for /l %%i in (0,1,10) do (

   pushd .
   echo "Calling function # %%i:"
   call :function%%i
   if errorlevel 1 (
      echo "Error detected on function %%i, exiting script."
      exit /b 1
   )
   popd
   echo.
)


REM RunScript.bat
echo "Executing RunCommands.ps1"
powershell -ExecutionPolicy Bypass -File RunCommands.ps1

REM Messaggio di completamento dell'installazione
echo "Installation completed"
pause
