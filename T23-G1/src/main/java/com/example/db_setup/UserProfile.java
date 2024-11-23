package com.example.db_setup;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Profile;

import lombok.Data;


@Table (name = "Profiles", schema = "studentsrepo")
@Data
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer ID;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "ID")
    private User user;

    @Column(length = 500)
    public String bio = "Test addicted...";

    public String profilePicturePath = "t23/images/profile_images/defaultProfilePicture.png";

    @ManyToMany
    @JoinTable(
        name = "user_following",
        joinColumns = @JoinColumn(name = "profile_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    public List<UserProfile> followingList;

    @ManyToMany
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "profile_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    public List<Profile> followersList;

    //List<Statistic> allStatistics;
}
