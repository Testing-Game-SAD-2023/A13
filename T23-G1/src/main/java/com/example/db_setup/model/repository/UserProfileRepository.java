package com.example.db_setup.model.repository;

import com.example.db_setup.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    UserProfile findByID(Integer ID);

    /**
     * Lookup del profilo tramite l'ID del Player (cioè l'userId che circola nel resto del sistema e nel JWT).
     *
     * Nota: l'ID primario del profilo (Integer) non coincide con playerId (Long).
     * Questa query permette al servizio social (follow/unfollow) di funzionare anche se gli viene passato
     * direttamente il playerId.
     */
    UserProfile findByPlayer_ID(Long playerId);

    // Ricerca per nome, cognome, email o nickname con LIKE
    @Query("SELECT u FROM UserProfile u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.surname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UserProfile> searchByNameSurnameEmailOrNickname(String searchTerm, Pageable pageable);
}
