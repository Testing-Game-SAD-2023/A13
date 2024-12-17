package com.example.db_setup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    
    User findByEmail(String email);
    List<User> findByName(String name);
    User findByResetToken(String resetToken);
    User findByID(Integer ID);
    //MODIFICA
    User findByisRegisteredWithFacebook(boolean isRegisteredWithFacebook);
    //FINE MODIFICA
    //Modifica 18/06/2024
    User findByisRegisteredWithGoogle(boolean isRegisteredWithGoogle);
    List<User> findAll();
    
    //Modifica 12/12/2024
    List<User> findBySurnameAndName(String surname,String name); //SELECT * FROM User WHERE surname = ? AND name = ?
    List<User> findBySurname(String surname);

}