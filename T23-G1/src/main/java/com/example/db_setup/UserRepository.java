package com.example.db_setup;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Metodi di ricerca esistenti
    User findByEmail(String email);
    User findByName(String name);
    User findByResetToken(String resetToken);
    User findByID(Integer ID);

    List<User> findAll();

    // MODIFICA 18/06/2024
    User findByNickname(String nickname);

    User findByisRegisteredWithFacebook(boolean isRegisteredWithFacebook);
    User findByisRegisteredWithGoogle(boolean isRegisteredWithGoogle);

    // INIZIO MODIFICHE cami
    List<User> findBySurname(String surname); // Ricerca utenti per cognome
    List<User> findByBiographyContaining(String keyword); // Ricerca utenti con una parola chiave nella biografia
    // FINE MODIFICHE cami

    // INIZIO MODIFICA avatar cami
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.avatar = :avatar WHERE u.ID = :userId")
    void updateAvatar(@Param("userId") Integer userId, @Param("avatar") String avatar);
    // FINE MODIFICA avatar

    //byGabman --- GESTIONE AMICI ---

    /*// Recupera gli amici di un utente dato il suo ID
    @Query("SELECT new map(f.name as name, f.status as status) FROM User u JOIN u.friends f WHERE u.ID = :userId")
    List<Map<String, String>> findFriendsByUserId(@Param("userId") Integer userId);

    // Aggiungi un amico a un utente
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_friends (user_id, friend_id) VALUES (:userId, :friendId)", nativeQuery = true)
    void addFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    // Rimuovi un amico da un utente
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_friends WHERE user_id = :userId AND friend_id = :friendId", nativeQuery = true)
    void removeFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);*/
    
    // Recupera gli amici di un utente dato il suo ID
    @Query(value = "SELECT f.name, f.surname FROM students f " +
                   "JOIN user_friends uf ON f.id = uf.friend_id " +
                   "WHERE uf.user_id = :userId", nativeQuery = true)
    List<Map<String, String>> findFriendsByUserId(@Param("userId") Integer userId);

    // Aggiungi un amico a un utente
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_friends (user_id, friend_id) VALUES (:userId, :friendId)", nativeQuery = true)
    void addFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    // Rimuovi un amico da un utente
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_friends WHERE user_id = :userId AND friend_id = :friendId", nativeQuery = true)
    void removeFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

}

