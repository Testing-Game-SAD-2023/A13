package com.example.db_setup.Service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.db_setup.OAuthUserGoogle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Questa classe è un servizio che fornisce un metodo per caricare i dettagli dell'utente da un OAuth2UserRequest
@Service
public class OAuthUserGoogleService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthUserGoogleService.class);

    // Questo metodo viene chiamato quando un utente tenta di autenticarsi tramite Google
    // Carica i dettagli dell'utente da OAuth2UserRequest e li restituisce come un oggetto OAuthUserGoogle
    // una classe che estende OAuth2User
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        // Utilizza il servizio di default offerto da Spring per caricare i dettagli dell'utente
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        LOGGER.info("loadUser method in OAuthUserGoogleService is called");
        return new OAuthUserGoogle(oauth2User);
    }
}
