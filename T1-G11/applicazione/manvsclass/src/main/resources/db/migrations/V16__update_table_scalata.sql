ALTER TABLE public.scalata
ADD CONSTRAINT fk_scalata_admin
FOREIGN KEY (username)
REFERENCES public.admin (email)
ON DELETE SET NULL
ON UPDATE CASCADE;