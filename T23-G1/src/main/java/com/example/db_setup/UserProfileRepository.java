package com.example.db_setup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.db_setup.model.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Integer>{

    UserProfile findByID(Integer ID);

    // Ricerca per nome, cognome, email o nickname con LIKE
    @Query("SELECT u FROM UserProfile u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.surname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UserProfile> searchByNameSurnameEmailOrNickname(String searchTerm, Pageable pageable);
}
