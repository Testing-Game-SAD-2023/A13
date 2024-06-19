package com.example.db_setup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    
    User findByEmail(String email);
    User findByName(String name);
    User findByResetToken(String resetToken);
    //MODIFICA
    User findByisRegisteredWithFacebook(boolean isRegisteredWithFacebook);
    //FINE MODIFICA
    //Modifica 18/06/2024
    User findByisRegisteredWithGoogle(boolean isRegisteredWithGoogle);
    List<User> findAll();
}