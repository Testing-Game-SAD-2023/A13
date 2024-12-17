@echo off

GOTO :MAIN

REM Installer function for T5-G2
:function4
echo "Installing T5-G2"
cd "./T5-G2/t5"
call mvn package || ( echo "Error in T5-G2 installation during mvn package" && pause && exit /b 1 )
docker compose up -d --build || ( echo "Error in T5-G2 installation during docker compose up" && pause && exit /b 1 )
exit /b 0


REM Installer function for ui_gateway
:function9
echo "Installing ui_gateway"
cd "./ui_gateway"
docker compose up -d --build || ( echo "Error in ui_gateway installation during docker compose up" && pause && exit /b 1 )
exit /b 0

:MAIN
REM Avvio dell'installazione
echo "Installation started"



REM Chiamata alla funzione per T5-G2
echo "Calling function for T5-G2:"
call :function4
if errorlevel 1 (
   echo "Error detected in T5-G2 installation, exiting script."
   pause
   exit /b 1
)

REM Messaggio di completamento dell'installazione
echo "Installation completed for T5-G2"
pause

