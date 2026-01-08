
--
-- Name: assignment; Type: TABLE; Schema: public; Owner: t1-g11
--

CREATE TABLE public.assignment (
    id character varying(255) DEFAULT (gen_random_uuid())::text NOT NULL,
    data_creazione timestamp without time zone NOT NULL,
    data_scadenza timestamp without time zone NOT NULL,
    descrizione character varying(255),
    titolo character varying(255) NOT NULL,
    team_id character varying(255)
);


ALTER TABLE public.assignment OWNER TO "t1-g11";


--
-- Name: assignment assignment_pkey; Type: CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.assignment
    ADD CONSTRAINT assignment_pkey PRIMARY KEY (id);



--
-- Name: assignment fkdetrh6pu9ojx5htmct8jirhof; Type: FK CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.assignment
    ADD CONSTRAINT fkdetrh6pu9ojx5htmct8jirhof FOREIGN KEY (team_id) REFERENCES public.team(id);