package com.groom.manvsclass.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@Controller
@RequestMapping("/hints")
public class HintViewController {

    @GetMapping("/main")
    public ModelAndView showHintsMain() {
        // Questa pagina avrà la logica per chiamare T1.getHints nel JS
        return new ModelAndView("hints/hints_main");
    }

    @GetMapping("/upload")
    public ModelAndView showUploadHints() {
        return new ModelAndView("hints/hints_upload");
    }

    @GetMapping("/edit")
    public ModelAndView showEditHint() {
        // Se necessario in futuro, per modificare un singolo suggerimento
        return new ModelAndView("hints/hints_edit");
    }
}