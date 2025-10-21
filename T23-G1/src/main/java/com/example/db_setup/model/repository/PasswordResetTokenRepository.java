package com.example.db_setup.model.repository;

import com.example.db_setup.model.Admin;
import com.example.db_setup.model.PasswordResetToken;
import com.example.db_setup.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    List<PasswordResetToken> findByPlayer(Player player);

    List<PasswordResetToken> findByAdmin(Admin admin);

    Optional<PasswordResetToken> findByToken(String passwordResetToken);
}
