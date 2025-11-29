package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Guideline;
import com.groom.manvsclass.repository.GuidelineRepository;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.mapper.GuidelineMapper;
import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.exception.DuplicatedTitlesException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class GuidelineService {

    @Autowired
    private GuidelineRepository guidelineRepository;

    @Autowired
    private GuidelineMapper guidelineMapper;

    @Transactional
    public void uploadGuidelines(List<GuidelineDTO> guidelinesDTO) {

        // effettua il mapping in ingresso DTO -> Model
        List<Guideline> guidelines = guidelineMapper.toEntityList(guidelinesDTO);

        List<Guideline> guidelinesToSave = new ArrayList<>();
        // List<String> duplicatedTitles = new ArrayList<>();

        for (Guideline guideline : guidelines) {

            String guidelineTitle = guideline.getTitle();
            boolean titleIsDuplicated = guidelineRepository.existsByTitle(guidelineTitle);
            if(titleIsDuplicated) {
                // duplicatedTitles.add(guidelineTitle);
                continue;
            }
            guidelinesToSave.add(guideline);
        }

        if (!guidelinesToSave.isEmpty()) {
            guidelineRepository.saveAll(guidelinesToSave);
        }
        /*if (!guidelinesToSave.isEmpty()) {
            String duplicatedMessage = "Linee guida duplicate trovate: " + String.join(", ", duplicatedTitles);
            throw new DuplicatedTitlesException(duplicatedMessage);
        }*/

    }

    @Transactional
    public List<GuidelineDTO> findGuidelines() {

        List<Guideline> guidelinesFound = guidelineRepository.findAllGuidelines();

        // effettua il mapping in uscita Model -> DTO
        return guidelineMapper.toDtoList(guidelinesFound);
    }

    public void deleteGuideline(String guidelineTitle) {

        Optional<Guideline> guidelineOpt = guidelineRepository.findByTitle(guidelineTitle);
        if(guidelineOpt.isEmpty()) {
            throw new NotFoundException("Linea guida " + guidelineTitle + " non trovata.");
        }

        Guideline guidelineToDelete = guidelineOpt.get();

        guidelineRepository.delete(guidelineToDelete);
    }

}