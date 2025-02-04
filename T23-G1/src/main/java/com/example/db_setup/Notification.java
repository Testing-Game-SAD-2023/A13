/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.example.db_setup;

import java.time.LocalDateTime;

import javax.persistence.Column;
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
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private Long id;

    @Column(nullable = false)
    private int playerID;

    @Column(length = 100, nullable = false)
    private String titolo;

    @Column(length = 500, nullable = false)
    private String message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean isRead = false;

    public Notification() {}

    public Notification(int playerID, String titolo, String message, boolean isRead) {
        this.playerID = playerID;
        this.titolo = titolo;
        this.message = message;
        this.isRead = isRead;
    }

    public Notification(int playerID, String titolo, String message, LocalDateTime timestamp, boolean isRead) {
        this.playerID = playerID;
        this.titolo = titolo;
        this.message = message;
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
