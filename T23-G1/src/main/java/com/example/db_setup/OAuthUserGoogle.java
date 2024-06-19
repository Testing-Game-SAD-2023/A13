// Questa classe è responsabile di gestire le richieste relative all'autenticazione e al login degli utenti.
// Contiene metodi per mostrare il modulo di login, ottenere le informazioni di accesso, effettuare il login con Google,
// verificare lo stato di autenticazione dell'utente e gestire il logout.

package com.example.db_setup;

import java.util.Collection;
import java.util.Map;
 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
 
public class OAuthUserGoogle implements OAuth2User {
 
    private OAuth2User oauth2User;
     
    public OAuthUserGoogle(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;
    }
    // Questo metodo restituisce gli attributi dell'utente autenticato
    // ovvero i dettagli dell'utente recuperati dal provider OAuth2
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }
    // Questo metodo restituisce le autorizzazioni dell'utente autenticato
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }
 
    @Override
    public String getName() {  
        return oauth2User.getAttribute("name");
    }
 
    public String getEmail() {
        return oauth2User.<String>getAttribute("email");     
    }
    // Le authorities e gli attributi sono informazioni aggiuntive che possono essere 
    // recuperate dall'utente autenticato
}