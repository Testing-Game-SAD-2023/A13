package com.example.db_setup.model.repository;

import com.example.db_setup.model.Admin;
import com.example.db_setup.model.Player;
import com.example.db_setup.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByPlayer(Player player);

    List<RefreshToken> findByAdmin(Admin admin);
}
