package com.groom.manvsclass.model;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.ClassUTScalata;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scalate")
public class Scalata {

    @Id
    private String name;

    private int numLevels;
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "admin_email", referencedColumnName = "email")
    private Admin admin;

    @OneToMany(mappedBy = "scalata", cascade = CascadeType.REMOVE)
    private List<ClassUTScalata> associations;
}
