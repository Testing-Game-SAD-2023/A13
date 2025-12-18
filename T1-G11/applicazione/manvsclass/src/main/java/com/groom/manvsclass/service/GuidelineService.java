package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Guideline;
import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.mapper.GuidelineMapper;
import com.groom.manvsclass.repository.GuidelineRepository;
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
public class GuidelineService {

    @Autowired
    private GuidelineRepository guidelineRepository;
    @Autowired
    private GuidelineMapper guidelineMapper;
    @Autowired
    private ImageService imageService;

    private void updateGuideline(Guideline oldGuideline, Guideline newGuideline) {

        oldGuideline.setHint(newGuideline.getHint());
        oldGuideline.setDate(newGuideline.getDate());
    }

    @Transactional
    public void uploadGuidelineImage(int order, MultipartFile image) {

        Optional<Guideline> guidelineOpt = guidelineRepository.findByOrder(order);
        if (guidelineOpt.isEmpty()) {
            throw new NotFoundException("Guideline Not Found.");
        }

        Guideline guidelineToSave = guidelineOpt.get();

        if (guidelineToSave.getImage() != null) {
            try {
                imageService.deleteImage(guidelineToSave.getImage());
            } catch (IOException e) {
                throw new RuntimeException("Errore nella cancellazione del file", e);
            }
        }

        String imageName = image.getOriginalFilename();
        if (imageName.contains(".")) {
            String imageExtension = imageName.substring(imageName.lastIndexOf("."));
            imageName = order + imageExtension;
        }

        try {
            imageService.storeImage(image, imageName);
        } catch(IOException e) {
            throw new RuntimeException("Errore nel salvataggio del file", e);
        }

        guidelineToSave.setImage(imageName);
        guidelineRepository.save(guidelineToSave);
    }

    @Transactional
    public void uploadGuidelines(List<GuidelineDTO> guidelineDTOs) {

        if (guidelineDTOs != null && !guidelineDTOs.isEmpty()) {

            List<Guideline> guidelines = guidelineMapper.toEntityList(guidelineDTOs);

            List<Guideline> existingGuidelines = guidelineRepository.findAllGuidelines();

            Map<Integer, Guideline> existingMap = existingGuidelines.stream()
                    .collect(Collectors.toMap(Guideline::getOrder, Function.identity()));

            List<Guideline> guidelinesToSave = new ArrayList<>();

            for (Guideline guideline : guidelines) {

                if (existingMap.containsKey(guideline.getOrder())) {

                    Guideline existingGuideline = existingMap.get(guideline.getOrder());
                    updateGuideline(existingGuideline, guideline);

                    guidelinesToSave.add(existingGuideline);
                }

                else {

                    guidelinesToSave.add(guideline);
                }
            }

            guidelineRepository.saveAll(guidelinesToSave);
        }
    }

    @Transactional
    public List<GuidelineDTO> findGuidelines() {

        return  guidelineMapper.toDtoList(guidelineRepository.findAllGuidelines());
    }

    @Transactional
    public void deleteGuideline(int order) {

        Optional<Guideline> guidelineOpt = guidelineRepository.findByOrder(order);
        if(guidelineOpt.isEmpty()) {
            throw new NotFoundException("Linea guida " + order + " non trovata.");
        }

        Guideline guidelineToDelete = guidelineOpt.get();
        String imageName = guidelineToDelete.getImage();

        if (imageName != null) {
            try {
                imageService.deleteImage(imageName);
            } catch (IOException e) {
                throw new RuntimeException("Errore nella cancellazione del file", e);
            }
        }

        guidelineRepository.delete(guidelineToDelete);
    }

    @Transactional
    public void deleteGuidelineImage(int order) {

        Optional<Guideline> guidelineOpt = guidelineRepository.findByOrder(order);
        if(guidelineOpt.isEmpty()) {
            throw new NotFoundException("Linea Guida " + order + " non trovato.");
        }

        Guideline guidelineToSave = guidelineOpt.get();
        String imageName = guidelineToSave.getImage();

        if (imageName != null) {
            try {
                imageService.deleteImage(imageName);
            } catch (IOException e) {
                throw new RuntimeException("Errore nella cancellazione del file", e);
            }
        }

        guidelineToSave.setImage(null);
        guidelineRepository.save(guidelineToSave);
    }

}