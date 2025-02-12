// Questo componente gestisce la logica di autenticazione per l'accesso con Google. 
//Quando un utente si autentica con successo tramite Google, il metodo onAuthenticationSuccess viene chiamato. 
//Il metodo recupera l'utente dal database utilizzando l'email fornita da Google. 
//Se l'utente non esiste, viene creato un nuovo utente utilizzando i dettagli forniti da Google. 
//Viene quindi generato un token JWT per l'utente e salvato nel cookie. Infine, l'utente viene reindirizzato alla pagina di login. 


package com.example.db_setup;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.db_setup.Service.UserService;
import com.example.db_setup.model.User;

@Component
public class GoogleSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;
    
    // Questo metodo viene chiamato quando un utente si autentica con successo tramite Google nella 
    // configurazione di Spring Security.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                // Recupera l'utente autenticato da OAuth2, getPrincipal è la funzione di Authentication per
                // recuperare le informazioni dell'utente autenticato
                OAuthUserGoogle oauthUser = (OAuthUserGoogle) authentication.getPrincipal();
                String email = oauthUser.getEmail();
                User user = userService.getUserByEmail(email);
                
                // Se l'utente non esiste, crea un nuovo utente utilizzando i dettagli forniti da Google
                if (user == null) {
                    user = userService.createUserFromOAuth(oauthUser);                   
                }
                // Genera un token JWT per l'utente e lo salva nel cookie
                String token = userService.saveToken(user);
                Cookie jwtTokenCookie = new Cookie("jwt", token);
                jwtTokenCookie.setMaxAge(3600);
                jwtTokenCookie.setPath("/");
                response.addCookie(jwtTokenCookie);
                // Reindirizza l'utente alla pagina di login
                // TODO: Redirect to the homepage
                response.sendRedirect("/main");

    }

    
}
