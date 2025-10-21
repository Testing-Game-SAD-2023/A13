package com.example.db_setup.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Notifications", schema = "studentsrepo")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private long playerID;

    @Column(length = 100, nullable = false)
    private String titolo;

    @Column(length = 500, nullable = false)
    private String message;

    @Column(length = 50, nullable = false) // Nuovo campo per il tipo di notifica
    private String type = "info"; // Default: "info"

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean isRead = false;

    public Notification() {
    }

    public Notification(int playerID, String titolo, String message, String type, boolean isRead) {
        this.playerID = playerID;
        this.titolo = titolo;
        this.message = message;
        this.type = type != null ? type : "info"; // Imposta il valore di default se null
        this.isRead = isRead;
    }

    public Notification(long playerID, String titolo, String message, String type, LocalDateTime timestamp, boolean isRead) {
        this.playerID = playerID;
        this.titolo = titolo;
        this.message = message;
        this.type = type != null ? type : "info";
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now(); // Imposta automaticamente il timestamp alla creazione
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
