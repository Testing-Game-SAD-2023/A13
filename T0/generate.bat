@echo off
setlocal enabledelayedexpansion

:: Verifico il numero di parametri
if "%~4"=="" (
    echo Usage: %~nx0 ^<tool^> ^<class_name^> [^<package_name^>] ^<class_path^> ^<host_output_dir^>
    echo ^<tool^>: randoop ^| evosuite
    exit /b 1
)

if not "%~6"=="" (
    echo Usage: %~nx0 ^<tool^> ^<class_name^> [^<package_name^>] ^<class_path^> ^<host_output_dir^>
    echo ^<tool^>: randoop ^| evosuite
    exit /b 1
)

set CONTAINER_ID=t0-generator
set TOOL=%1
set CLASS_NAME=%2
set NUM_LEVELS=3

:: Se ci sono esattamente 4 parametri, il package name è vuoto
if "%~5"=="" (
    set PACKAGE_NAME=
    set CLASS_PATH=%3
    set HOST_OUTPUT_DIR=%4
) else (
    set PACKAGE_NAME=%3
    set CLASS_PATH=%4
    set HOST_OUTPUT_DIR=%5
)

:: Definisco delle directory in base al tipo del robot
if /I "%TOOL%"=="randoop" (
    set ROOT_DIR=/generator/RandoopGenerator
    set CLASS_DEST_DIR=!ROOT_DIR!/FolderTree/!CLASS_NAME!/!CLASS_NAME!SourceCode
    set CLASS_DEST_PATH=!CLASS_DEST_DIR!/!CLASS_NAME!.java
    set RESULTS_DIR=!ROOT_DIR!/FolderTree/!CLASS_NAME!/RobotTest/RandoopTest
    set SCRIPT=!ROOT_DIR!/generate.sh
) else if /I "%TOOL%"=="evosuite" (
    set ROOT_DIR=/generator/EvosuiteGenerator
    set CLASS_DEST_DIR=!ROOT_DIR!/evosuite
    set CLASS_DEST_PATH=!CLASS_DEST_DIR!/!CLASS_NAME!.java
    set RESULTS_DIR=/generator/FolderTreeEvo/!CLASS_NAME!/RobotTest/EvoSuiteTest
    set SCRIPT=!ROOT_DIR!/generate.sh
) else (
    echo Error: Invalid tool specified. Use 'randoop' or 'evosuite'.
    exit /b 1
)

echo CONTAINER_ID=!CONTAINER_ID!
echo TOOL=!TOOL!
echo CLASS_NAME=!CLASS_NAME!
echo PACKAGE_NAME=!PACKAGE_NAME!
echo CLASS_PATH=!CLASS_PATH!
echo NUM_LEVELS=!NUM_LEVELS!
echo HOST_OUTPUT_DIR=!HOST_OUTPUT_DIR!
echo ROOT_DIR=!ROOT_DIR!
echo CLASS_DEST_DIR=!CLASS_DEST_DIR!
echo CLASS_DEST_PATH=!CLASS_DEST_PATH!
echo RESULTS_DIR=!RESULTS_DIR!
echo SCRIPT=!SCRIPT!

:: Creo la directory nel container
docker exec !CONTAINER_ID! mkdir -p !CLASS_DEST_DIR! || (
    echo Error: Failed to create directory !CLASS_DEST_DIR! in container !CONTAINER_ID!.
    exit /b 1
)

:: Copio la classe nel container
docker cp "!CLASS_PATH!" !CONTAINER_ID!:!CLASS_DEST_PATH! || (
    echo Error: Failed to copy class !CLASS_NAME! to container !CONTAINER_ID!.
    exit /b 1
)

echo Class !CLASS_NAME! successfully copied to !CLASS_DEST_PATH! in container !CONTAINER_ID!.

:: Imposto i permessi di esecuzione sullo script
docker exec !CONTAINER_ID! chmod +x !SCRIPT! || (
    echo Error: Failed to set execute permissions for !SCRIPT! in container !CONTAINER_ID!.
    exit /b 1
)

:: Eseguo lo script
if /I "%TOOL%"=="randoop" (
    docker exec !CONTAINER_ID! bash -c "!SCRIPT!" || (
        echo Error: Failed to execute !SCRIPT! in container !CONTAINER_ID!.
        exit /b 1
    )
) else (
    docker exec !CONTAINER_ID! bash -c "!SCRIPT! '!CLASS_NAME!' '!PACKAGE_NAME!' '!CLASS_DEST_DIR!' '!NUM_LEVELS!'" || (
        echo Error: Failed to execute !SCRIPT! in container !CONTAINER_ID!.
        exit /b 1
    )
)

echo Generation script executed successfully for class !CLASS_NAME! using !TOOL!.

:: Copio i risultati sull'host
docker cp !CONTAINER_ID!:!RESULTS_DIR! "!HOST_OUTPUT_DIR!" || (
    echo Error: Failed to copy test results from container !CONTAINER_ID! to host !HOST_OUTPUT_DIR!.
    exit /b 1
)

echo Test results successfully copied to host at !HOST_OUTPUT_DIR!.

:: Cleanup
docker exec !CONTAINER_ID! bash -c "rm -rf /generator/RandoopGenerator/FolderTree/!CLASS_NAME!/*"
docker exec !CONTAINER_ID! bash -c "rm -rf /generator/FolderTreeEvo/!CLASS_NAME!/*"
docker exec !CONTAINER_ID! bash -c "rm -f !CLASS_DEST_PATH!"

echo Cleanup completed.
