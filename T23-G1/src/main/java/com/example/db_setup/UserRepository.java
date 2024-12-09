package com.example.db_setup;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Metodi di ricerca
    User findByEmail(String email);
    User findByName(String name);
    User findByResetToken(String resetToken);
    User findByID(Integer ID);
    User findByNickname(String nickname);
    User findByisRegisteredWithFacebook(boolean isRegisteredWithFacebook);
    User findByisRegisteredWithGoogle(boolean isRegisteredWithGoogle);

    // Metodi di ricerca avanzata
    List<User> findBySurname(String surname);
    List<User> findByBiographyContaining(String keyword);

    // Modifiche per avatar
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.avatar = :avatar WHERE u.ID = :userId")
    void updateAvatar(@Param("userId") Integer userId, @Param("avatar") String avatar);

    @Query("SELECT u.avatar FROM User u WHERE u.ID = :userId")
    String findAvatarByUserId(@Param("userId") Integer userId);
}
    // FINE MODIFICA avatar



 


