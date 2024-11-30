package com.example.db_setup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Map;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT new map(f.name as name, f.status as status) FROM Friend f WHERE f.userId = :userId")
    List<Map<String, String>> findFriendsByUserId(@Param("userId") String userId);
}
