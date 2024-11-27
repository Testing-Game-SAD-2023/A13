package com.example.db_setup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;

import lombok.Data;

import java.time.LocalDateTime;

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
    private boolean isRead;

    public Notification() {}

    public Notification(int playerID, String titolo, String message, LocalDateTime timestamp, boolean isRead) {
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
