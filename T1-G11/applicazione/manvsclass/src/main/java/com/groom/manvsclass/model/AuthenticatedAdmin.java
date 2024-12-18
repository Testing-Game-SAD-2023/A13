package com.groom.manvsclass.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

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
