package com.groom.manvsclass.model;

import com.groom.manvsclass.model.InteractionType;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interactions")
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InteractionType type;

    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "admin_email", referencedColumnName = "email")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "class_name", referencedColumnName = "name")
    private ClassUT classUT;
}