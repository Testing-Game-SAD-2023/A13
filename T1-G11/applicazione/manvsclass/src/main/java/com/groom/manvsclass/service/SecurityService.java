package com.groom.manvsclass.service;

import org.springframework.stereotype.Service;
import com.groom.manvsclass.security.JwtRequestContext;

@Service
public class SecurityService {

    public String getJwtToken() {

        return JwtRequestContext.getJwtToken();
    }
}