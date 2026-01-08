-- 1. Rinominamento tabella associativa
ALTER TABLE team_admin RENAME TO team_management;

-- 2. Pulizia tabelle Admin e Hint (Audit totale)
ALTER TABLE admin DROP COLUMN created_at, DROP COLUMN updated_at, DROP COLUMN deleted_at;
ALTER TABLE hint DROP COLUMN updated_at, DROP COLUMN deleted_at;

-- 3. Pulizia tabelle con date funzionali (Resta solo la data di business)
ALTER TABLE class_ut DROP COLUMN created_at, DROP COLUMN updated_at, DROP COLUMN deleted_at;
ALTER TABLE interactions DROP COLUMN created_at, DROP COLUMN updated_at, DROP COLUMN deleted_at;
