package com.example.db_setup;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    
    User findByEmail(String email);
    User findByName(String name);
    User findByResetToken(String resetToken);
    User findByID(Integer ID);
    //MODIFICA
    User findByisRegisteredWithFacebook(boolean isRegisteredWithFacebook);
    //FINE MODIFICA
    //Modifica 18/06/2024
    User findByisRegisteredWithGoogle(boolean isRegisteredWithGoogle);
    List<User> findAll();

    // INIZIO MODIFICHE cami
List<User> findBySurname(String surname); // Ricerca utenti per cognome
List<User> findByBiographyContaining(String keyword); // Ricerca utenti con una parola chiave nella biografia
// FINE MODIFICHE

 // INIZIO MODIFICA avatar cami
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.avatar = :avatar WHERE u.id = :userId")
    void updateAvatar(@Param("userId") Long userId, @Param("avatar") String avatar);
    // FINE MODIFICA avatar
}