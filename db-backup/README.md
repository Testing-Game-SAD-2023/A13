# DB-Backup - Database Backup Service
The DB-Backup service is a standalone component of the system responsible for performing and maintaining periodic backups, in the form of dumps, of the databases used by the T23 and T4 services.

The service requires that the databases used by the T23 and T4 services are active in order to function.

## Execution
The service periodically (every 30 minutes) executes the script `scripts/backup.sh`, which performs a dump of the relevant databases. Each dump is saved in `.tar.gz` format inside the Docker volume `db-backup_Backups`, using the following naming convention:
`service_db-type_date_time.tar.gz`.
For example, a backup performed on 25/05/2025 at 10:36 (and 56 seconds) for the T23 database will be named:
`T23_mysql_backup_2025-05-25_10-35-56.sql.gz`.

## Restore
The backups can be restored from the host machine by running the `restore_backup.bat` and `restore_backup.sh` scripts, for Windows and Linux hosts respectively.

Both scripts require the following input parameters:
- Service: specifies the service for which the database should be restored. Use `T23` for the T23 service and `T4` for the T4 service;
- Backup to restore: the dump file to be restored, e.g. `T23_mysql_backup_2025-05-25_10-35-56.sql.gz`.

