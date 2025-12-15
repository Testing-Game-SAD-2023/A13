@echo off
setlocal enabledelayedexpansion

set SERVICE=%1
set BACKUP_FILE=%2
set CONTAINER_NAME=db-backup

if "%SERVICE%"=="" (
    echo Usage: restore_backup.bat T23^|T4 backup_filename.sql.gz
    exit /b 1
)

if "%BACKUP_FILE%"=="" (
    echo Usage: restore_backup.bat T23^|T4 backup_filename.sql.gz
    exit /b 1
)

echo Restoring backup %BACKUP_FILE% for service %SERVICE%...

if /I "%SERVICE%"=="T23" (
    docker exec -i %CONTAINER_NAME% sh -c "gunzip < /Backups/%BACKUP_FILE% | mysql -h t23-mysql_db -u user -ppassword studentsrepo"
) else if /I "%SERVICE%"=="T4" (
    docker exec -i %CONTAINER_NAME% sh -c "PGPASSWORD=postgres psql -h t4-postgres_db -U postgres -d postgres -c \"DROP SCHEMA public CASCADE; CREATE SCHEMA public;\" && gunzip < /Backups/%BACKUP_FILE% | PGPASSWORD=postgres psql -h t4-postgres_db -U postgres -d postgres"
) else (
    echo Invalid service: choose T23 or T4.
    exit /b 1
)

if errorlevel 1 (
    echo Error encountered restoring backup.
    exit /b 1
) else (
    echo Restore completed successfully.
)
