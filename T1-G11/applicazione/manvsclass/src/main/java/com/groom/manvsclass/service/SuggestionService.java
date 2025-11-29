package com.groom.manvsclass.service;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Suggestion;

import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.repository.SuggestionRepository;
import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.mapper.SuggestionMapper;

import com.groom.manvsclass.exception.DuplicatedTitlesException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class SuggestionService {

    @Autowired
    private ClassUTRepository classUTRepository;
    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private SuggestionMapper suggestionMapper;

    @Transactional
    public void uploadSuggestions(String className, List<SuggestionDTO> suggestionsDTO) {

        // effettua il mapping in ingresso DTO -> Model
        List<Suggestion> suggestions = suggestionMapper.toEntityList(suggestionsDTO);

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if (classUTOpt.isEmpty()) {
            throw new NotFoundException("Classe " + className + " non trovata.");
        }

        List<Suggestion> suggestionsToSave = new ArrayList<>();
        // List<String> duplicatedTitles = new ArrayList<>();

        for (Suggestion suggestion : suggestions) {

            boolean titleIsDuplicated = suggestionRepository.existsByClassUT_NameAndTitle(className, suggestion.getTitle());

            if (titleIsDuplicated) {
                // duplicatedTitles.add(singleSuggestionDTO.getTitle());
                continue;
            }

            suggestion.setClassUT(classUTOpt.get());
            suggestionsToSave.add(suggestion);
        }

        if (!suggestionsToSave.isEmpty()) {
            suggestionRepository.saveAll(suggestionsToSave);
        }

        /*if (!duplicatedTitles.isEmpty()) {
            String duplicatedMessage = "Suggerimenti duplicati trovati: " + String.join(", ", duplicatedTitles);

            // Se lancio questa eccezione il db torna allo stato iniziale a causa di @Transactional
            // throw new DuplicatedTitlesException(duplicatedMessage);
        }*/
    }

    @Transactional
    public List<SuggestionDTO> findSuggestions(String className) {

        List<Suggestion> suggestionsFound = suggestionRepository.findAllByClassUT_Name(className);
        
        // effettua il mapping in uscita Model -> DTO
        return suggestionMapper.toDtoList(suggestionsFound);
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

        suggestionRepository.delete(suggestionOpt.get());
    }

}