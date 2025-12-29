-- Aggiunge la colonna user_id alla tabella interactions
-- Usiamo BIGINT perché in Java mappiamo su Long
ALTER TABLE public.interactions
ADD COLUMN user_id BIGINT;

-- Aggiunge un indice per velocizzare le ricerche basate su user_id
CREATE INDEX idx_interactions_user_id ON public.interactions(user_id);