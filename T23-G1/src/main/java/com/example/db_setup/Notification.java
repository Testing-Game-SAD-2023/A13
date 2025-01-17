package com.example.db_setup;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Notifications", schema = "studentsrepo")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Genera un ID univoco automaticamente
    private Long id;

    private int playerID;
    private String titolo;
    private String message;
    private LocalDateTime timestamp;
    private Boolean isRead;

    public Notification() {}

    public Notification(int playerID, String titolo, String message, LocalDateTime timestamp, Boolean isRead) {
        this.playerID = playerID;
        this.titolo = titolo;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now(); // Imposta automaticamente il timestamp
    }
}
