package com.groom.manvsclass.controller.view;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.repository.ClassRepository;
import com.groom.manvsclass.service.OpponentService;
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
    private final ClassRepository classRepository;
    private final OpponentService opponentService;
    private final Logger logger = LoggerFactory.getLogger(OpponentViewController.class);


    public OpponentViewController(ClassRepository classRepository, OpponentService opponentService) {
        this.classRepository = classRepository;
        this.opponentService = opponentService;
    }

    @GetMapping("/main")
    public ModelAndView showClass(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "filterByDifficulty", required = false) String filterByDifficulty,
            @RequestParam(value = "search", required = false) String search
    ) {
        List<ClassUT> classUTList;

        if (filterByDifficulty != null && !filterByDifficulty.isBlank()) {
            classUTList = opponentService.filterByDifficulty(filterByDifficulty);
        } else if (sortBy != null && !sortBy.isBlank()) {
            classUTList = switch (sortBy) {
                case "Date" -> opponentService.orderByDate();
                case "Name" -> opponentService.orderByName();
                default -> classRepository.findAll();
            };
        } else {
            classUTList = classRepository.findAll();
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
        view.addObject("classUTs", classUTList);
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