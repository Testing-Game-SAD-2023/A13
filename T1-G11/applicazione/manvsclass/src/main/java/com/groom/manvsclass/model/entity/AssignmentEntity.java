package com.groom.manvsclass.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "assignment")
public class AssignmentEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String idAssignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private TeamEntity team;

    @Column(name = "titolo", nullable = false)
    private String titolo;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "data_creazione", nullable = false)
    private Date dataCreazione;

    @Column(name = "data_scadenza", nullable = false)
    private Date dataScadenza;

    public AssignmentEntity(String titolo, String descrizione, Date dataScadenza) {
        this.idAssignment = null;
        this.team.setIdTeam(null);
        this.team.setName(null);
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataCreazione = new Date(); // Creazione all'istante corrente
        this.dataScadenza = dataScadenza;
    }
}
