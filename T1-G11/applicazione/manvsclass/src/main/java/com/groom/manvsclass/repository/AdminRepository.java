package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {

    // findAdminByUsername (da SearchRepositoryImpl)
    Optional<Admin> findByUsername(String username);

    // Sostituisce findAdminByResetToken (da SearchRepositoryImpl)
    Optional<Admin> findByResetToken(String resetToken);

    // Sostituisce findAdminByInvitationToken (da SearchRepositoryImpl)
    Optional<Admin> findByInvitationToken(String invitationToken);
}
