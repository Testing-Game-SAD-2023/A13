
--
-- Name: team_admin; Type: TABLE; Schema: public; Owner: t1-g11
--

CREATE TABLE public.team_admin (
    id character varying(255) NOT NULL DEFAULT gen_random_uuid()::text,
    is_active boolean NOT NULL,
    role character varying(255) NOT NULL,
    admin_email character varying(255),
    team_id character varying(255)
);


ALTER TABLE public.team_admin OWNER TO "t1-g11";



--
-- Name: team_admin team_admin_pkey; Type: CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.team_admin
    ADD CONSTRAINT team_admin_pkey PRIMARY KEY (id);




--
-- Name: team_admin fk6o2aw5wvh13butlj49xkr5cfo; Type: FK CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.team_admin
    ADD CONSTRAINT fk6o2aw5wvh13butlj49xkr5cfo FOREIGN KEY (admin_email) REFERENCES public.admin(email);




--
-- Name: team_admin fkd7bpifcx6mgsp6rc019axr1ic; Type: FK CONSTRAINT; Schema: public; Owner: t1-g11
--

ALTER TABLE ONLY public.team_admin
    ADD CONSTRAINT fkd7bpifcx6mgsp6rc019axr1ic FOREIGN KEY (team_id) REFERENCES public.team(id);
