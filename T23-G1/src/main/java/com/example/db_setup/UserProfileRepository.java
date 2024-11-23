package com.example.db_setup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileRepository extends JpaRepository<User,Integer>{
    
}
