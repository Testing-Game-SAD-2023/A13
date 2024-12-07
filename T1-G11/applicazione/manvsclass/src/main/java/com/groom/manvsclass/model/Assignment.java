package com.groom.manvsclass.model;

import java.util.Date;

public class Assignment {

    // Attributi
    private String idAssignment;
    private String titolo;
    private String descrizione;
    private Date dataCreazione;
    private Date dataScadenza;

    // Costruttore
    public Assignment(String idAssignment, String titolo, String descrizione, Date dataCreazione, Date dataScadenza) {
        this.idAssignment = idAssignment;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataCreazione = dataCreazione;
        this.dataScadenza = dataScadenza;
    }

    // Getter e Setter
    public String getIdAssignment() {
        return idAssignment;
    }

    public void setIdAssignment(String idAssignment) {
        this.idAssignment = idAssignment;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "idAssignment='" + idAssignment + '\'' +
                ", titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataCreazione=" + dataCreazione +
                ", dataScadenza=" + dataScadenza +
                '}';
    }
}
