
--
-- Name: team; Type: TABLE; Schema: public; Owner: t1-g11
--

CREATE TABLE public.team (
    id character varying(255) NOT NULL DEFAULT gen_random_uuid()::text, -- modifica manuale
    creation_date timestamp without time zone NOT NULL,
    id_studenti jsonb,
    name character varying(255) NOT NULL,
    numero_studenti integer NOT NULL
);


ALTER TABLE public.team OWNER TO "t1-g11";

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);