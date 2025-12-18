package com.groom.manvsclass.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TeamEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String idTeam;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "numero_studenti", nullable = false)
    private int numeroStudenti;

    @Type(type = "jsonb")
    @Column(name = "id_studenti", columnDefinition = "jsonb")
    // Questa lista contiene SOLO gli ID degli studenti, non gli oggetti completi. [BONDEND CONTEXT]
    private List<String> idStudenti;

    // Costruttore
    public TeamEntity(String idTeam, String name) {
        this.idTeam = idTeam;
        this.name = name;
        this.numeroStudenti = 0; //Default
        this.creationDate = new Date(); //Data attuale
        this.idStudenti = new ArrayList<>();
    }
}
