-- Rimuove il vincolo di chiave esterna che lega 'operation.name' a 'class_ut.name'
ALTER TABLE public.operation DROP CONSTRAINT fk92b1pcxgs353qh9sca288oo0d;

-- (Opzionale) Se vuoi rinominare la colonna per renderla più chiara (visto che ora è solo testo)
-- Se fai questo, ricordati di aggiornare @Column(name = "class_name") nel Java!
ALTER TABLE public.operation RENAME COLUMN name TO class_name;