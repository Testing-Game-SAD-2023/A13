package com.groom.manvsclass.controller.viewcontroller;

import com.groom.manvsclass.service.JwtService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
@RequestMapping("/scalata")
public class ScalataViewController {

    private final JwtService jwtService;

    public ScalataViewController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/main")
    public ModelAndView showScalata(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        return new ModelAndView("/scalata/scalata_main");
    }
}
