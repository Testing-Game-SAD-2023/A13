package com.example.db_setup;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


//LISTA DELLE AMICIZIE PER OGNI GIOCATORE (NO RICERCA AMICI QUELLA SI FA IN STUDENTS)
@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    // Metodo per ottenere la lista degli amici con i dettagli (nickname e avatar) dalla tabella students
    @Query("SELECT new map(f.friendId as friendId, s.nickname as nickname, s.avatar as avatar) " +
           "FROM Friend f " +
           "JOIN User s ON f.friendId = s.ID " + // Join con la tabella User (students)
           "WHERE f.userId = :userId")
    List<Map<String, Object>> findFriendDetailsByUserId(@Param("userId") int userId);

    // Metodo per aggiungere un amico senza salvare l'avatar
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_friends (user_id, friend_id, friend_username) " + // Rimosso il campo friend_avatar
                   "VALUES (:userId, :friendId, :friendUsername)", nativeQuery = true)
    void addFriend(@Param("userId") Integer userId,
                   @Param("friendId") Integer friendId,
                   @Param("friendUsername") String friendUsername);

    // Verifica se una relazione di amicizia esiste
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE f.userId = :userId AND f.friendId = :friendId")
    boolean existsFriendship(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    // Metodo per rimuovere un amico
    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f WHERE f.userId = :userId AND f.friendId = :friendId")
    void removeFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
}
