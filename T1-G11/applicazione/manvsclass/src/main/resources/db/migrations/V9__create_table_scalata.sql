
--
-- Name: scalata; Type: TABLE; Schema: public; Owner: t1-g11
--

CREATE TABLE public.scalata (
    scalataname character varying(255) NOT NULL,
    number_of_rounds integer NOT NULL,
    scalata_description character varying(255) NOT NULL,
    username character varying(255)
);


ALTER TABLE public.scalata OWNER TO "t1-g11";

--
-- Name: scalata_classi; Type: TABLE; Schema: public; Owner: t1-g11
--

CREATE TABLE public.scalata_classi (
    scalata_id character varying(255) NOT NULL,
    class_ut_id character varying(255) NOT NULL
);


ALTER TABLE public.scalata_classi OWNER TO "t1-g11";



--
-- Name: scalata scalata_pkey; Type: CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.scalata
    ADD CONSTRAINT scalata_pkey PRIMARY KEY (scalataname);




--
-- Name: scalata_classi fk1brc00xwslpj5twt5m9khcymg; Type: FK CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.scalata_classi
    ADD CONSTRAINT fk1brc00xwslpj5twt5m9khcymg FOREIGN KEY (class_ut_id) REFERENCES public.class_ut(name);




--
-- Name: scalata_classi fk8k167h9bxmnx7jfqgi1xbtxg2; Type: FK CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.scalata_classi
    ADD CONSTRAINT fk8k167h9bxmnx7jfqgi1xbtxg2 FOREIGN KEY (scalata_id) REFERENCES public.scalata(scalataname);