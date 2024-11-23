package com.example.db_setup;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Table (name = "Profiles", schema = "profilesrepo")
@Data 
@Entity
public class UserProfile {

    public String bio;
    public List<User> followersList;
    public List<User> followingList;
    public String profilePicturePath; 
    List<Statistic> allStatistics;
}
