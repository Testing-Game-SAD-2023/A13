package com.groom.manvsclass.service;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.mapper.SuggestionMapper;

import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.repository.SuggestionRepository;
import com.groom.manvsclass.service.ImageService;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.DuplicatedEntryException;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.function.Function;

@Service
public class SuggestionService {

    @Autowired
    private ClassUTRepository classUTRepository;
    @Autowired
    private SuggestionRepository suggestionRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private SuggestionMapper suggestionMapper;

    private void updateSuggestion(Suggestion oldSuggestion, Suggestion newSuggestion) {

        oldSuggestion.setHint(newSuggestion.getHint());
        oldSuggestion.setLevel(newSuggestion.getLevel());
        oldSuggestion.setDate(newSuggestion.getDate());
    }

    @Transactional
    public void uploadSuggestions(String className, List<SuggestionDTO> suggestionDTOs) {

        if (suggestionDTOs != null && !suggestionDTOs.isEmpty()) {

            List<Suggestion> suggestions = suggestionMapper.toEntityList(suggestionDTOs);

            Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
            if (classUTOpt.isEmpty()) {
                throw new NotFoundException("Classe " + className + " non trovata.");
            }
            ClassUT classUT = classUTOpt.get();

            List<Suggestion> existingSuggestions = suggestionRepository.findAllByClassUT_Name(className);

            Map<Integer, Suggestion> existingMap = existingSuggestions.stream()
                    .collect(Collectors.toMap(Suggestion::getOrder, Function.identity()));

            List<Suggestion> suggestionsToSave = new ArrayList<>();

            for (Suggestion suggestion : suggestions) {

                if (existingMap.containsKey(suggestion.getOrder())) {

                    Suggestion existingSuggestion = existingMap.get(suggestion.getOrder());
                    updateSuggestion(existingSuggestion, suggestion);

                    suggestionsToSave.add(existingSuggestion);
                } else {

                    suggestion.setClassUT(classUT);

                    suggestionsToSave.add(suggestion);
                }
            }

            suggestionRepository.saveAll(suggestionsToSave);
        }
    }

    @Transactional
    public void uploadSuggestionImage(String className, int order, MultipartFile image) {

        Optional<Suggestion> suggestionOpt = suggestionRepository.findByClassUT_NameAndOrder(className, order);
        if (suggestionOpt.isEmpty()) {
            throw new NotFoundException("Suggestion Not Found.");
        }

        Suggestion suggestionToSave = suggestionOpt.get();

        if (suggestionToSave.getImage() != null) {
            try {
                imageService.deleteImage(suggestionToSave.getImage());
            } catch (IOException e) {
                throw new RuntimeException("Errore nella cancellazione del file", e);
            }
        }

        String imageName = image.getOriginalFilename();
        if (imageName.contains(".")) {
            String imageExtension = imageName.substring(imageName.lastIndexOf("."));
            imageName = className + "_" + order + imageExtension;
        }

        try {
            imageService.storeImage(image, imageName);
        } catch(IOException e) {
            throw new RuntimeException("Errore nel salvataggio del file", e);
        }

        suggestionToSave.setImage(imageName);
        suggestionRepository.save(suggestionToSave);
    }

    @Transactional
    public List<SuggestionDTO> findSuggestions(String className) {

        boolean classExists = classUTRepository.existsById(className);
        if(!classExists) {
            throw new NotFoundException("Classe " + className + " non trovata.");
        }

        return suggestionMapper.toDtoList(suggestionRepository.findAllByClassUT_Name(className));
    }

    @Transactional
    public void deleteSuggestion(String className, int order) {

        Optional<Suggestion> suggestionOpt = suggestionRepository.findByClassUT_NameAndOrder(className, order);
        if(suggestionOpt.isEmpty()) {
            throw new NotFoundException("Suggerimento " + order + " non trovato per la classe " + className);
        }

        Suggestion suggestionToDelete = suggestionOpt.get();
        String imageName = suggestionToDelete.getImage();

        if (imageName != null) {
            try {
                imageService.deleteImage(imageName);
            } catch (IOException e) {
                throw new RuntimeException("Errore nella cancellazione del file", e);
            }
        }

        suggestionRepository.delete(suggestionToDelete);
    }

    @Transactional
    public void deleteSuggestionImage(String className, int order) {

        Optional<Suggestion> suggestionOpt = suggestionRepository.findByClassUT_NameAndOrder(className, order);
        if(suggestionOpt.isEmpty()) {
            throw new NotFoundException("Suggerimento " + order + " non trovato per la classe " + className);
        }

        Suggestion suggestionToSave = suggestionOpt.get();
        String imageName = suggestionToSave.getImage();

        if (imageName != null) {
            try {
                imageService.deleteImage(imageName);
            } catch (IOException e) {
                throw new RuntimeException("Errore nella cancellazione del file", e);
            }
        }

        suggestionToSave.setImage(null);
        suggestionRepository.save(suggestionToSave);
    }

}