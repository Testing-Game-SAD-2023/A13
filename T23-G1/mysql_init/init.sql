-- Questo file inizializza il database e corregge l'errore di sintassi SQL e l'errore del plugin.
-- La sintassi 'IDENTIFIED BY' ora utilizzerà il plugin di autenticazione predefinito (caching_sha2_password).

-- 1. CREAZIONE DEL DATABASE (il nome viene iniettato dalla variabile d'ambiente)
CREATE DATABASE IF NOT EXISTS `${MYSQL_DATABASE}`;

-- 2. ELIMINAZIONE UTENTE (opzionale, per garantire un avvio pulito)
DROP USER IF EXISTS '${MYSQL_USER}'@'%';

-- 3. CREAZIONE DELL'UTENTE CON PASSWORD
-- Utilizza la sintassi standard 'IDENTIFIED BY', che ora si affida al plugin di default.
CREATE USER '${MYSQL_USER}'@'%' IDENTIFIED BY '${MYSQL_PASSWORD}';

-- 4. ASSEGNAZIONE DEI PERMESSI
-- Assegna tutti i permessi al database da cui l'applicazione dipende
GRANT ALL PRIVILEGES ON `${MYSQL_DATABASE}`.* TO '${MYSQL_USER}'@'%';

-- 5. AGGIORNAMENTO DEI PRIVILEGI
FLUSH PRIVILEGES;