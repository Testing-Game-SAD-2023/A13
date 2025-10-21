package com.example.db_setup.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "Admins", schema = "studentsrepo")
@Entity
@Data
@Getter
@Setter
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private String surname;
    private String password;

    public Admin(String name, String surname, String email, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public Admin() {

    }
}