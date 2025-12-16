package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByUsername(String username);

    Optional<Admin> findByResetToken(String resetToken);

    Optional<Admin> findByInvitationToken(String invitationToken);
}
