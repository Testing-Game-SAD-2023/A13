package com.example.db_setup;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.db_setup.FriendRepository;

import java.util.List;
import java.util.Map;

// LISTA DELLE AMICIZIE PER OGNI GIOCATORE (NO RICERCA AMICI, QUELLA SI FA IN STUDENTS)
@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    // Metodo per ottenere la lista degli amici con i dettagli (nickname e avatar) dalla tabella students
    @Query(value = "SELECT uf.friend_id AS friendId, s.nickname AS nickname, s.avatar AS avatar " +
               "FROM user_friends uf " +
               "JOIN students s ON uf.friend_id = s.ID " +
               "WHERE uf.user_id = :userId", nativeQuery = true)
    List<Map<String, Object>> findFriendDetailsByUserId(@Param("userId") int userId);




      // Aggiunge una relazione
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_friends (user_id, friend_id, nickname) " +
               "SELECT :userId, :friendId, s.nickname " +
               "SELECT :userId, :friendId " +
               "WHERE NOT EXISTS (SELECT 1 FROM user_friends WHERE user_id = :userId AND friend_id = :friendId)", 
       nativeQuery = true)
    void addFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    // Metodo per vedere se esiste relazione
     @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE f.id.userId = :userId AND f.id.friendId = :friendId")
    boolean existsFriendship(@Param("userId") Integer userId, @Param("friendId") Integer friendId);


 
    
  @Modifying
  @Transactional
  @Query("DELETE FROM Friend f WHERE f.id.userId = :userId AND f.nickname = :nickname")
  void deleteByUserIdAndNickname(@Param("userId") Integer userId, @Param("nickname") String nickname);






}
