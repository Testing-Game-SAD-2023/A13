package com.groom.manvsclass.model;

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

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
enum InteractionType {
    REPORT,
    LIKE
}
*/

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

    private int type; // 0 Report - 1 Like
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "admin_email", referencedColumnName = "email")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "class_name", referencedColumnName = "name")
    private ClassUT classUT;
}