package com.groom.manvsclass.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@Controller
public class AdminViewController {

    @GetMapping("/dashboard")
    public ModelAndView showDashboard() {
        return new ModelAndView("dashboard");
    }

    @GetMapping("/dashboard/admins")
    public ModelAndView showInfo() {
        return new ModelAndView("admins_info");
    }

    @GetMapping("/dashboard/players")
    public ModelAndView showPlayer() {
        return new ModelAndView("players_info");
    }

    /*
    @GetMapping("/invite_admins")
    @ResponseBody
    public ModelAndView showInviteAdmins() {
        return new ModelAndView("invite_admins");
    }

    @GetMapping("/login_with_invitation")
    @ResponseBody
    public ModelAndView showLoginWithInvitation() {
        //if (jwtService.isJwtValid(jwt)) return new ModelAndView("redirect:/login_admin");
        return new ModelAndView("login_with_invitation");
    }

     */
}
