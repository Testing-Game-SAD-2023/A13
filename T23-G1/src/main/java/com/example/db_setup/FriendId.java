package com.example.db_setup;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FriendId implements Serializable {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "friend_id")
    private Integer friendId;

    public FriendId() {}

    public FriendId(Integer userId, Integer friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return Objects.equals(userId, friendId.userId) &&
               Objects.equals(friendId, friendId.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }

    // Getter e Setter
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }
}


