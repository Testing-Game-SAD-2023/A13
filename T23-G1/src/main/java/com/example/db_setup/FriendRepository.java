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

    // Metodo per ottenere tutti gli amici di un utente specifico
    @Query("SELECT f FROM Friend f WHERE f.userId = :userId")
    List<Friend> findFriendsByUserId(@Param("userId") int userId);

    // Aggiunta di un amico (query nativa per ottimizzare le prestazioni)
    @Modifying
    @Transactional // Necessario per query di modifica
    @Query(value = "INSERT INTO Friend (user_id, friend_id, friend_username, friend_avatar) " +
                   "VALUES (:userId, :friendId, :friendUsername, :friendAvatar)", nativeQuery = true)
    void addFriend(@Param("userId") Integer userId,
                   @Param("friendId") Integer friendId,
                   @Param("friendUsername") String friendUsername,
                   @Param("friendAvatar") String friendAvatar);
    
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE f.userId = :userId AND f.friendId = :friendId")
    boolean existsFriendship(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
                   

    // Rimozione di un amico
    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f WHERE f.userId = :userId AND f.friendId = :friendId")
    void removeFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

}