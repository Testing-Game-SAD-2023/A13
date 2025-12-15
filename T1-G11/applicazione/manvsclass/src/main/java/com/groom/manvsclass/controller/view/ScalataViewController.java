package com.groom.manvsclass.controller.view;

import com.groom.manvsclass.model.dto.ScalataDTO;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.service.ScalataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
@RequestMapping("/scalata")
public class ScalataViewController {

    private final JwtService jwtService;
    private final ScalataService scalataService;

    public ScalataViewController(JwtService jwtService,  ScalataService scalataService) {
        this.jwtService = jwtService;
        this.scalataService = scalataService;
    }

    @GetMapping("/main")
    public ModelAndView mainScalata(
            @CookieValue(name = "jwt", required = false) String jwt,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortBy", required = false) String author,
            @RequestParam(value = "search", required = false) String search) {

        // Recupera la lista di scalate
        List<ScalataDTO> scalate = scalataService.getAll();

        // FILTRAGGIO (se il parametro 'search' è presente)
        if (search != null && !search.trim().isEmpty()) {
            scalate = scalate.stream()
                    // Filtra le scalate il cui nome (o altro campo rilevante) contiene il termine di ricerca
                    .filter(s -> s.getName().toLowerCase().startsWith(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // ORDINAMENTO (se il parametro 'sortBy' è presente)
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            switch (sortBy.toLowerCase()) {
                // Ordina per nome in ordine alfabetico
                case "name" -> scalate.sort(Comparator.comparing(ScalataDTO::getName, String.CASE_INSENSITIVE_ORDER));
                // Ordina per data
                case "date" -> scalate.sort(Comparator.comparing(ScalataDTO::getDate).reversed());
            }
        }

        // Popola e restituisci la ModelAndView
        ModelAndView mv = new ModelAndView("scalata/scalata_main");
        mv.addObject("scalate", scalate);

        return mv;
    }

    @GetMapping("/create")
    public ModelAndView createScalata(@CookieValue(name = "jwt", required = false) String jwt) {
        ModelAndView mv = new ModelAndView("scalata/scalata_edit");
        return mv;
    }
}
