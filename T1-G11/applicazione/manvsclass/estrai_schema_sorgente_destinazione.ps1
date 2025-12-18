# --- CONFIGURAZIONE ---
$ContainerName = "t1-postgres-db"
$PgUser = "t1-g11"
$SourceDb = "manvsclass"
$TargetDb = "temp_db_for_migrations"
$DumpPath = "D:\Desktop\SAD_28_11_2025\dumps"

# Assicurati che la cartella di dump esista
if (-not (Test-Path $DumpPath)) {
    New-Item -Path $DumpPath -ItemType Directory | Out-Null
}

$Timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$SourceFile = Join-Path $DumpPath "as_is.sql"
$TargetFile = Join-Path $DumpPath "to_be.sql"

Write-Host "Inizio estrazione schema DDL da Docker..."

# 1. Estrazione del DB Sorgente (V4)
Write-Host "--> Estrazione schema Sorgente (V4 - $SourceDb)..."
docker exec $ContainerName pg_dump -U $PgUser -s -d $SourceDb | Out-File $SourceFile -Encoding UTF8

if (Test-Path $SourceFile) {
    Write-Host "Schema Sorgente salvato in: $SourceFile" -ForegroundColor Green
} else {
    Write-Host "ERRORE: Impossibile estrarre lo schema sorgente." -ForegroundColor Red
    exit 1
}

# 2. Estrazione del DB Target (V5)
Write-Host "--> Estrazione schema Target (V5 - $TargetDb)..."
docker exec $ContainerName pg_dump -U $PgUser -s -d $TargetDb | Out-File $TargetFile -Encoding UTF8

if (Test-Path $TargetFile) {
    Write-Host "Schema Target salvato in: $TargetFile" -ForegroundColor Green
} else {
    Write-Host "ERRORE: Impossibile estrarre lo schema target." -ForegroundColor Red
    exit 1
}

Write-Host "`nDump completati. Pronto per il confronto DDL."
Write-Host "--------------------------------------------------------"
Write-Host "File Sorgente (V4): $SourceFile"
Write-Host "File Target (V5): $TargetFile"