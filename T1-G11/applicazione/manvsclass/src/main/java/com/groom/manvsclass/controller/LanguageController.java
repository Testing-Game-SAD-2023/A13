package com.groom.manvsclass.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

@CrossOrigin
@RestController
public class LanguageController {
    private final LocaleResolver localeResolver;

    public LanguageController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    // Gestione della lingua
    @PostMapping("/changeLanguage")
    public ResponseEntity<Void> changeLanguage(@RequestParam("lang") String lang,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        Cookie cookie = new Cookie("lang", lang);
        cookie.setMaxAge(3600); // Imposta la durata del cookie a 1 ora
        cookie.setPath("/"); // Imposta il percorso per il cookie
        response.addCookie(cookie); // Aggiungi il cookie alla risposta
        Locale locale = Locale.forLanguageTag(lang);
        localeResolver.setLocale(request, response, locale);
        // Restituisce una risposta vuota con codice di stato 200 OK
        return ResponseEntity.ok().build();
    }
}
