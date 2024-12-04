package com.example.db_setup;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_friend", schema = "studentsrepo") // Adatta lo schema se necessario
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "friend_id", nullable = false)
    private Integer friendId;

    @Column(name = "friend_username", nullable = false)
    private String friendUsername;

    @Column(name = "friend_avatar")
    private String friendAvatar;
}
