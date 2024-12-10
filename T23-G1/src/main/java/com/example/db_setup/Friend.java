package com.example.db_setup;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_friends", schema = "studentsrepo")
public class Friend {
    @EmbeddedId
    private FriendId id;

    @Column(name = "nickname", nullable = false) // Assicurati che questa colonna esista nel database
    private String nickname;


    public Friend() {}

    public Friend(FriendId id,String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    // Getter e Setter
    public FriendId getId() {
        return id;
    }

    public void setId(FriendId id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
