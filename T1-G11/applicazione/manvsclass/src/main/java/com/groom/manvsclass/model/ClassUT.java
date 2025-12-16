package com.groom.manvsclass.model;

import com.groom.manvsclass.model.Category;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.model.Operation;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.ClassUTScalata;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classes_ut")
public class ClassUT {

    @Id
    @Column(name = "name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private OpponentDifficulty difficulty;

    private String uri;
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "classes_categories",
            joinColumns = @JoinColumn(name = "class_name", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "classUT", cascade = CascadeType.REMOVE)
    private List<Interaction> interactions;

    @OneToMany(mappedBy = "classUT", cascade = CascadeType.REMOVE)
    private List<Operation> operations;

    @OneToMany(mappedBy = "classUT", cascade = CascadeType.REMOVE)
    private List<Suggestion> suggestions;

    @OneToMany(mappedBy = "classUT", cascade = CascadeType.REMOVE)
    private List<Opponent> opponents;

    @OneToMany(mappedBy = "classUT", cascade = CascadeType.REMOVE)
    private List<ClassUTScalata> classUTScalata;

}
