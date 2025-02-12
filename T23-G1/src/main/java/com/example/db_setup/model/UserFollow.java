package com.example.db_setup.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "user_follow", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"followerId", "followingId"})
})
public class UserFollow {

    // La chiave primaria composta è incorporata nella classe UserFollow
    @EmbeddedId
    private UserFollowId id;

    // L'attributo follower è parte della chiave primaria
    @ManyToOne
    @JoinColumn(name = "followerId", referencedColumnName = "ID")
    private UserProfile follower;

    // L'attributo following è parte della chiave primaria
    @ManyToOne
    @JoinColumn(name = "followingId", referencedColumnName = "ID")
    private UserProfile following;

    // Costruttore senza argomenti
    public UserFollow() {
    }

    // Costruttore con parametri
    public UserFollow(UserProfile follower, UserProfile following) {
        this.follower = follower;
        this.following = following;
        this.id = new UserFollowId(follower.getID(), following.getID());
    }

    // Getters e Setters
    public UserProfile getFollower() {
        return follower;
    }

    public void setFollower(UserProfile follower) {
        this.follower = follower;
    }

    public UserProfile getFollowing() {
        return following;
    }

    public void setFollowing(UserProfile following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "UserFollow{"
                + "follower=" + (follower != null ? follower.getID() : null)
                + ", following=" + (following != null ? following.getID() : null)
                + '}';
    }

    public UserFollowId getId() {
        return id;
    }

}
