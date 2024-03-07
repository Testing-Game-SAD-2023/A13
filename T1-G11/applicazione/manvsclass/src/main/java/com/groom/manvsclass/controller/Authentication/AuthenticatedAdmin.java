package com.groom.manvsclass.controller.Authentication;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

//@Data consente di generare automaticamente i metodi getter e setter, 
//il metodo toString(), equal(), hashCode(), clone() ed altri metodi utili

import com.groom.manvsclass.model.Admin;

// Individuazione del path della classe Admin.java "applicazione\manvsclass\src\main\java\com\groom\manvsclass\model\Admin.java"

@Document(collection = "authenticated_admins")
@Data
public class AuthenticatedAdmin {
    
    @Field("authToken")
    private String authToken;
    
    @Field("admin")
    private Admin admin;

    public AuthenticatedAdmin(Admin admin, String authToken) {
        this.admin = admin;
        this.authToken = authToken;
    }

    public AuthenticatedAdmin() {
    }
}
