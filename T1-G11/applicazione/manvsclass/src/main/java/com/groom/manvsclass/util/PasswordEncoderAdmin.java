package com.groom.manvsclass.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderAdmin {

    private static final int STRENGTH = 10; // La forza dell'algoritmo di hashing

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordEncoderAdmin() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder(STRENGTH);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public String encode(String rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
