package com.groom.manvsclass.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@Controller
@RequestMapping("team")
public class TeamViewController {

    @GetMapping("/main")
    @ResponseBody
    public ModelAndView showMain() {
        return new ModelAndView("/teams/team_main");
    }

    @GetMapping("/details/{teamId}")
    @ResponseBody
    public ModelAndView showDetails() {
        return new ModelAndView("/teams/team_details");
    }
}
