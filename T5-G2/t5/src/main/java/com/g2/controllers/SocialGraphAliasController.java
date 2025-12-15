package com.g2.controllers;

import com.g2.interfaces.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Alias per esporre le API social di T5 sotto /t5/**,
 * perché UI Gateway inoltra /t5 -> T5 ma NON inoltra /social -> T5.
 */
@RestController
@RequestMapping("/t5/social")
public class SocialGraphAliasController {

    private final SocialGraphController delegate;

    public SocialGraphAliasController(ServiceManager serviceManager) {
        // riuso la logica già esistente in SocialGraphController
        this.delegate = new SocialGraphController(serviceManager);
    }

    @GetMapping("/me/counts")
    public ResponseEntity<?> myCounts(@CookieValue(name = "jwt", required = false) String jwt) {
        return delegate.myCounts(jwt);
    }
}
