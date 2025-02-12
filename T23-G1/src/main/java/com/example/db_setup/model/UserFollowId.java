package com.example.db_setup.model;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class UserFollowId implements Serializable { 

    private Integer follower;
    private Integer following;

    // Costruttore senza argomenti
    public UserFollowId() {}

    // Costruttore con parametri
    public UserFollowId(Integer follower, Integer following) {
        this.follower = follower;
        this.following = following;
    }

    // Getter e Setter
    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    // Equals e HashCode per comparare le chiavi primarie
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFollowId that = (UserFollowId) o;
        return Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }
}