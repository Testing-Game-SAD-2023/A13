package com.example.db_setup.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "profiles", schema = "studentsrepo")
@Data
@Entity
public class UserProfile {

    @Column(length = 30, nullable = false)
    public String name;
    @Column(length = 30, nullable = false)
    public String surname;
    @Column(length = 45)
    public String email;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;
    @OneToOne
    @JoinColumn(name = "player_id")
    @JsonIgnore
    private Player player;
    @Column(length = 500)
    private String bio = "Test addicted...";
    // Nome dell'immagine, usata come parte finale del path nel servizio dove sono salvate le immagini
    private String profilePicturePath = "default.png";
    //  le relazioni in cui questo profilo segue altri utenti
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // Evita la ricorsione e Evita la serializzazione di questa parte della relazione
    private Set<UserFollow> followings = new HashSet<>();
    // le relazioni in cui questo profilo è seguito da altri utenti
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // Evita la ricorsione e Evita la serializzazione di questa parte della relazione
    private Set<UserFollow> followers = new HashSet<>();
    @Column(length = 30, nullable = false)
    private String nickname = "default_nickname";

    @PrePersist
    public void prePersist() {
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "default_nickname";
        }
    }

    // Setter per user (eventualmente utile se vuoi aggiungere logica personalizzata)
    public void setPlayer(Player player) {
        this.player = player;
    }

    // Metodo helper per aggiungere una relazione di follow
    public void follow(UserProfile userToFollow) {
        UserFollow userFollow = new UserFollow(this, userToFollow);
        followings.add(userFollow);
        userToFollow.getFollowers().add(userFollow);
    }

    // Metodo helper per rimuovere una relazione di follow
    public void unfollow(UserProfile userToUnfollow) {
        followings.removeIf(uf -> uf.getFollowing().equals(userToUnfollow));
        userToUnfollow.getFollowers().removeIf(uf -> uf.getFollower().equals(this));
    }

    @Override
    public String toString() {
        return "UserProfile{"
                + "ID=" + ID
                + ", bio='" + bio + '\''
                + ", profilePicturePath='" + profilePicturePath + '\''
                + '}';
    }

    @JsonProperty("userId")
    public Long getUserId() {
        return player != null ? player.getID() : null;
    }
}
