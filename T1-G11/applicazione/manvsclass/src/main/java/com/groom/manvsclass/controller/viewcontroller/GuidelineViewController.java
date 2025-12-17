package com.groom.manvsclass.controller.viewcontroller;

import com.groom.manvsclass.service.GuidelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@Controller
@RequestMapping("/opponents")
public class GuidelineViewController {
    private final GuidelineService guidelineService;
    private final Logger logger = LoggerFactory.getLogger(GuidelineViewController.class);


    public GuidelineViewController(GuidelineService guidelineService) {
        this.guidelineService = guidelineService;
    }

    @GetMapping("/guidelines/main")
    public ModelAndView showGuidelinesPage() {

        ModelAndView view = new ModelAndView("opponents/guidelines_main");
        view.addObject("guidelines", guidelineService.findGuidelines());

        return view;
    }
}