-- Crea la tabella interactions
CREATE TABLE public.interactions (
    id SERIAL PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    interaction_type INTEGER NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE
);

-- Imposta il proprietario (facoltativo, ma coerente con il tuo file precedente)
ALTER TABLE public.interactions OWNER TO "t1-g11";

-- Aggiunge l'indice per la ricerca veloce per email utente (Cruciale per i microservizi)
CREATE INDEX idx_interactions_user_email ON public.interactions(user_email);

-- Aggiunge la Foreign Key verso la tabella delle classi (class_ut)
-- ATTENZIONE: Assicurati che la tabella 'class_ut' esista già e che la PK sia 'name'.
ALTER TABLE ONLY public.interactions
    ADD CONSTRAINT fk_interactions_class_name
    FOREIGN KEY (class_name) REFERENCES public.class_ut(name)
    ON DELETE CASCADE;