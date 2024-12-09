package com.example.db_setup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Table (name = "Profiles", schema = "studentsrepo")
@Data
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer ID;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(length = 500)
    public String bio = "Test addicted...";

    // Nome dell'immagine, usata come parte finale del path nel servizio dove sono salvate le immagini
    public String profilePicturePath = "default.png";

    @ElementCollection
    @CollectionTable(
        name = "user_following",
        joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "following_id")
    private List<Integer> followingIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "follower_id")
    private List<Integer> followerIds = new ArrayList<>();

    public void setUser(User user){
        this.user = user;
    }

    //List<Statistic> allStatistics;
}
