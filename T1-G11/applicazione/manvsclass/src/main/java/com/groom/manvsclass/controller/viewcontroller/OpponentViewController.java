package com.groom.manvsclass.controller.viewcontroller;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.service.OpponentService;
import com.groom.manvsclass.service.ClassUTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/opponents")
public class OpponentViewController {
    private final OpponentService opponentService;
    private final ClassUTService classUTService;
    private final Logger logger = LoggerFactory.getLogger(OpponentViewController.class);


    public OpponentViewController(OpponentService opponentService, ClassUTService classUTService) {
        this.opponentService = opponentService;
        this.classUTService = classUTService;
    }

    @GetMapping("/main")
    public ModelAndView showClass(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "filterByDifficulty", required = false) String filterByDifficulty,
            @RequestParam(value = "search", required = false) String search
    ) {
        List<ClassUT> classUTList;

        if (filterByDifficulty != null && !filterByDifficulty.isBlank()) {
            classUTList = classUTService.filterByDifficulty(filterByDifficulty);
        } else if (sortBy != null && !sortBy.isBlank()) {
            classUTList = switch (sortBy) {
                case "Date" -> classUTService.orderByDate();
                case "Name" -> classUTService.orderByName();
                default -> classUTService.getClassUTs();
            };
        } else {
            classUTList = classUTService.getClassUTs();
        }

        // Applichiamo la ricerca testuale
        if (search != null && !search.isBlank()) {
            String loweredSearch = search.toLowerCase();
            classUTList = classUTList.stream()
                    .filter(c -> c.getName().toLowerCase().equals(loweredSearch))
                    .toList();
        }

        logger.info("[opponents/opponents_main] classUTs found: {}", classUTList);
        ModelAndView view = new ModelAndView("opponents/opponents_main");
        view.addObject("classes", classUTList);
        return view;
    }

    @GetMapping("/upload")
    public ModelAndView showUploadRobots() {
        return new ModelAndView("opponents/opponents_upload");
    }

    @GetMapping("/edit")
    public ModelAndView showModificaClasse() {
        return new ModelAndView("opponents/opponents_edit");
    }
}