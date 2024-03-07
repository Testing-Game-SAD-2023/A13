package com.groom.manvsclass.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Operation")
public class Operation {
    private int id_op;
    private String username_admin;
    private String nome_classe;
    private int type;
    private String date;

    public Operation(int id_op, String username_admin, String nome_classe, int type, String date) {
        this.id_op = id_op;
        this.username_admin = username_admin;
        this.nome_classe = nome_classe;
        this.type = type;
        this.date= date;
    }

    // Getter e setter per i campi

    public int getId_op() {
        return id_op;
    }

    public void setId_op(int id_op) {
        this.id_op = id_op;
    }

    public String getUsername_admin() {
        return username_admin;
    }

    public void setUsername_admin(String username_admin) {
        this.username_admin = username_admin;
    }

    public String getNome_classe() {
        return nome_classe;
    }

    public void setNome_classe(String nome_classe) {
        this.nome_classe = nome_classe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id_op=" + id_op +
                ", username_admin='" + username_admin + '\'' +
                ", nome_classe='" + nome_classe + '\'' +
                ", type=" + type +
                ", date='" + date + '\'' +
                '}';
    }
}