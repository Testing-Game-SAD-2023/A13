package com.groom.manvsclass.model;

import com.groom.manvsclass.model.Achievement;
import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.model.Operation;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.Team;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Admin {

    @Id
    private String email;

    private String nome;
    private String cognome;

    private String username;
    private String password;
    private String resetToken;
    private String invitationToken;


    @OneToMany(mappedBy = "admin")
    private List<Achievement> achievements;

    @OneToMany(mappedBy = "admin")
    private List<Interaction> interactions;

    @OneToMany(mappedBy = "admin")
    private List<Operation> operations;

    @OneToMany(mappedBy = "admin")
    private List<Scalata> scalate;;

    @OneToMany(mappedBy = "admin")
    private List<Team> teams;
}