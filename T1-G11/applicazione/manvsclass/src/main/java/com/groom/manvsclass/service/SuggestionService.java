package com.groom.manvsclass.service;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Guideline;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.SuggestionLevel;
import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.dto.SingleGuidelineDTO;
import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.dto.SingleSuggestionDTO;
import com.groom.manvsclass.dto.SuggestionResponseDTO;
import com.groom.manvsclass.dto.GuidelineResponseDTO;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.repository.GuidelineRepository;
import com.groom.manvsclass.repository.SuggestionRepository;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.DuplicatedTitlesException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Base64;
import java.time.LocalDate;

@Service
public class SuggestionService {

    @Autowired
    private ClassUTRepository classUTRepository;
    @Autowired
    private GuidelineRepository guidelineRepository;
    @Autowired
    private SuggestionRepository suggestionRepository;

    @Transactional
    public void uploadSuggestions(SuggestionDTO suggestionDTO) {

        String className = suggestionDTO.getClassName();

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if (classUTOpt.isEmpty()) {
            throw new NotFoundException("Classe " + className + " non trovata.");

        }

        LocalDate localDate = LocalDate.now();
        List<SingleSuggestionDTO> singleSuggestionDTOs = suggestionDTO.getSuggestions();

        List<Suggestion> suggestionsToSave = new ArrayList<>();
        List<String> duplicatedTitles = new ArrayList<>();

        for (SingleSuggestionDTO singleSuggestionDTO : singleSuggestionDTOs) {

            boolean titleIsDuplicated = suggestionRepository.existsByClassUT_NameAndTitle(className, singleSuggestionDTO.getTitle());

            if (titleIsDuplicated) {
                duplicatedTitles.add(singleSuggestionDTO.getTitle());
                continue;
            }

            Suggestion newSuggestion = new Suggestion();
            newSuggestion.setClassUT(classUTOpt.get());
            newSuggestion.setTitle(singleSuggestionDTO.getTitle());
            newSuggestion.setHint(singleSuggestionDTO.getHint());
            newSuggestion.setLevel(singleSuggestionDTO.getLevel());
            newSuggestion.setDate(localDate);

            String base64Image = singleSuggestionDTO.getImage();

            if (base64Image != null && !base64Image.trim().isEmpty()) {
                    // Rimuovo intestazione 'data:image/png;base64,'
                if (base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                }

                byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
                newSuggestion.setImage(decodedBytes);

            } else {

                newSuggestion.setImage(null);
            }

            suggestionsToSave.add(newSuggestion);
        }

        if (!suggestionsToSave.isEmpty()) {
            suggestionRepository.saveAll(suggestionsToSave);
        }

        if (!duplicatedTitles.isEmpty()) {
            String duplicatedMessage = "Suggerimenti duplicati trovati: " + String.join(", ", duplicatedTitles);

            // Se lancio questa eccezione il db torna allo stato iniziale a causa di @Transactional
            // throw new DuplicatedTitlesException(duplicatedMessage);
        }
    }

    @Transactional
    public List<SuggestionResponseDTO> findSuggestions(String className) {

        List<Suggestion> suggestions =  suggestionRepository.findAllByClassUT_Name(className);

        List<SuggestionResponseDTO>  suggestionResponses = new ArrayList<>();

        for(Suggestion suggestion : suggestions) {

            // uso il DTO per evitare loop infinito suggestion -> classUT -> suggestion -> ...
            SuggestionResponseDTO suggestionResponseDTO = new SuggestionResponseDTO();
            suggestionResponseDTO.setId(suggestion.getId());
            suggestionResponseDTO.setTitle(suggestion.getTitle());
            suggestionResponseDTO.setHint(suggestion.getHint());
            suggestionResponseDTO.setImage(suggestion.getImage());
            suggestionResponseDTO.setLevel(suggestion.getLevel());
            suggestionResponseDTO.setDate(suggestion.getDate());

            suggestionResponses.add(suggestionResponseDTO);

        }

        return  suggestionResponses;
    }

    public void deleteSuggestion(String className, String suggestionTitle) {

        Optional<ClassUT> classOpt = classUTRepository.findById(className);
        if(classOpt.isEmpty()) {
            throw new NotFoundException("Classe " + className + " non trovata.");
        }

        Optional<Suggestion> suggestionOpt = suggestionRepository.findByClassUT_NameAndTitle(className, suggestionTitle);
        if(suggestionOpt.isEmpty()) {
            throw new NotFoundException("Suggerimento " + suggestionTitle + " non trovato.");
        }

        Suggestion suggestionToDelete = suggestionOpt.get();
        suggestionRepository.delete(suggestionToDelete);
    }

    @Transactional
    public void uploadGuidelines(GuidelineDTO guidelineDTOs) {

        LocalDate localDate = LocalDate.now();
        List<Guideline> guidelinesToSave = new ArrayList<>();
        // List<String> duplicatedTitles = new ArrayList<>();

        for (SingleGuidelineDTO guidelineDTO : guidelineDTOs.getGuidelines()) {

            String guidelineTitle = guidelineDTO.getTitle();
            boolean titleIsDuplicated = guidelineRepository.existsByTitle(guidelineTitle);
            if(titleIsDuplicated) {
                // duplicatedTitles.add(guidelineTitle);
                continue;
            }

            Guideline newGuideline = new Guideline();
            newGuideline.setTitle(guidelineTitle);
            newGuideline.setHint(guidelineDTO.getHint());
            newGuideline.setDate(localDate);

            String base64Image = guidelineDTO.getImage();
            if(base64Image != null && !base64Image.trim().isEmpty()) {
                if (base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                }
                byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
                newGuideline.setImage(decodedBytes);
            }
            else {
                newGuideline.setImage(null);
            }

            guidelinesToSave.add(newGuideline);
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
    public List<GuidelineResponseDTO> findGuidelines() {

        List<Guideline> guidelines =  guidelineRepository.findAllGuidelines();

        List<GuidelineResponseDTO>  guidelineResponses = new ArrayList<>();

        for(Guideline guideline : guidelines) {

            // uso il DTO per evitare loop infinito suggestion -> classUT -> suggestion -> ...
            // presente anche in Guideline perchè memorizzati nella stessa tabella
            GuidelineResponseDTO guidelineResponseDTO = new GuidelineResponseDTO();
            guidelineResponseDTO.setId(guideline.getId());
            guidelineResponseDTO.setTitle(guideline.getTitle());
            guidelineResponseDTO.setHint(guideline.getHint());
            guidelineResponseDTO.setImage(guideline.getImage());
            guidelineResponseDTO.setDate(guideline.getDate());

            guidelineResponses.add(guidelineResponseDTO);

        }

        return  guidelineResponses;
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