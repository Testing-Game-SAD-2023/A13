package com.example.db_setup;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_friends", schema = "studentsrepo")
public class Friend {
    @EmbeddedId
    private FriendId id;

    public Friend() {}

    public Friend(FriendId id) {
        this.id = id;
    }

    // Getter e Setter
    public FriendId getId() {
        return id;
    }

    public void setId(FriendId id) {
        this.id = id;
    }
}
