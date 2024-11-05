package com.example.api_gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class JWTService {	
    private final RestTemplate restTemplate;

    @Autowired
    public JWTService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

	public boolean verifyToken(String token) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", token);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);
	
		if(isAuthenticated == null) return false;

		return isAuthenticated.booleanValue();
	}
}