package com.example.t9profileservice.repository;

import com.example.t9profileservice.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByPlayerId(Long playerId);

    Optional<UserProfile> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    @Query("""
            SELECT p FROM UserProfile p
            WHERE (
                   LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(p.nickname, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(p.name, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(p.surname, '')) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            """)
    Page<UserProfile> search(@Param("q") String q, Pageable pageable);

    @Query("""
            SELECT p FROM UserProfile p
            WHERE (
                   LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(p.nickname, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(p.name, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(p.surname, '')) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            AND (p.userId IS NULL OR p.userId <> :excludeUserId)
            AND (p.playerId IS NULL OR p.playerId <> :excludeUserId)
            """)
    Page<UserProfile> searchExcluding(@Param("q") String q,
                                     @Param("excludeUserId") Long excludeUserId,
                                     Pageable pageable);
}
