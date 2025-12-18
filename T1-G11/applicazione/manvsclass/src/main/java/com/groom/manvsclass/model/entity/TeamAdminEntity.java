package com.groom.manvsclass.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "team_admin")
public class TeamAdminEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_email", referencedColumnName = "email")
    private AdminEntity admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private TeamEntity team;

    @Column(name = "role", nullable = false)
    private String role; // Ruolo dell'Admin nel Team

    @Column(name = "is_active", nullable = false)
    private boolean isActive; // Stato attuale della relazione
}
