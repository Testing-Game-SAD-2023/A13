package com.groom.manvsclass.model;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.ClassUTScalataId;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "classes_scalate")
public class ClassUTScalata {

    @EmbeddedId
    private ClassUTScalataId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("className")
    @JoinColumn(name = "class_name")
    private ClassUT classUT;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scalataName")
    @JoinColumn(name = "scalata_name")
    private Scalata scalata;

    private int level;
    private int timeLimit;

    public ClassUTScalata(ClassUT classUT, Scalata scalata, int level, int timeLimit) {

        this.id = new ClassUTScalataId(classUT.getName(), scalata.getName());
        this.classUT = classUT;
        this.scalata = scalata;
        this.level = level;
        this.timeLimit = timeLimit;
    }

}