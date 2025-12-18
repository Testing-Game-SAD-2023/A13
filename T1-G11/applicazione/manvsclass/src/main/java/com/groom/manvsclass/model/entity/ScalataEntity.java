package com.groom.manvsclass.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "scalata")
public class ScalataEntity {

    @Id
    private String scalataName;

    private String username;
    @Column(name = "scalata_description", nullable = false)
    private String scalataDescription;

    @Column(name = "number_of_rounds", nullable = false)
    private int numberOfRounds;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "scalata_classi",
            // Colonna FK in 'scalata_classi' che punta a 'scalata'
            joinColumns = @JoinColumn(name = "scalata_id"),
            // Colonna FK in 'scalata_classi' che punta a 'classe_ut'
            inverseJoinColumns = @JoinColumn(name = "class_ut_id")
    )
    private List<ClassUTEntity> selectedClasses;
}
