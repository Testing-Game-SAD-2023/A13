package com.groom.manvsclass.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
public class OtherViewController {

    /*
    *   Non esiste la pagina gestione_assignments.html
    public ModelAndView showGestioneAssignments(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) {return new ModelAndView("gestione_assignments");}
        return new ModelAndView("redirect:/loginAdmin");
    }

     */

    @GetMapping("/reportClasse")
    public ModelAndView showReportClasse(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        return new ModelAndView("reportClasse");
    }

    @GetMapping("/Reports")
    public ModelAndView showReports(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        return new ModelAndView("Reports");
    }


}
