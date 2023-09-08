package com.example.db_setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table (name = "Students", schema = "STUDENTSREPO")
@Data 
@Entity
public class User {
 
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    public Integer ID;

    public String name;

    public String surname;
    
    public String email;

    public String password;
    
    @Enumerated (EnumType.STRING)
    public Studies studies;
    
    @Column(name = "reset_token")
    private String resetToken;
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public String getResetToken() {
        return resetToken;
    }

}
