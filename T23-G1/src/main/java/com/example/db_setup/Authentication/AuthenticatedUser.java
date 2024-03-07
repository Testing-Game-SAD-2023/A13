package com.example.db_setup.Authentication;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.db_setup.User;

import lombok.Data;

@Entity
@Table(name = "authenticated_users")
@Data
public class AuthenticatedUser {
    
    @Id
    private String authToken;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AuthenticatedUser(User user, String authToken) {
        this.user = user;
        this.authToken = authToken;
    }

    public AuthenticatedUser() {
    }
}