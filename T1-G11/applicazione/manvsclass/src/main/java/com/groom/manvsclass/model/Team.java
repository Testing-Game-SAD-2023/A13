package com.groom.manvsclass.model;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.Assignment;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.FetchType;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private int numStudents;
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "admin_email", referencedColumnName = "email")
    private Admin admin;

    // la repository degli studenti è gestita da t23
    // ho solo una lista di id di studenti
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "teams_students", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "student_id")
    private List<String> studentIds = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Assignment> assignments;
}