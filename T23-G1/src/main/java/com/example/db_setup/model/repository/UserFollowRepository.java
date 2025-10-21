package com.example.db_setup.model.repository;

import com.example.db_setup.model.UserFollow;
import com.example.db_setup.model.UserFollowId;
import com.example.db_setup.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {

    @Query("SELECT uf.follower FROM UserFollow uf WHERE uf.following = :userProfile")
    List<UserProfile> findFollowersByUserProfile(@Param("userProfile") UserProfile userProfile);

    @Query("SELECT uf.following FROM UserFollow uf WHERE uf.follower = :userProfile")
    List<UserProfile> findFollowingByUserProfile(@Param("userProfile") UserProfile userProfile);

    @Query("SELECT COUNT(uf) > 0 FROM UserFollow uf WHERE uf.follower = :follower AND uf.following = :following")
    boolean existsByFollowerAndFollowing(@Param("follower") UserProfile follower, @Param("following") UserProfile following);

    void deleteByFollowerAndFollowing(UserProfile follower, UserProfile following);
}

