package com.groom.manvsclass.service;

import com.groom.manvsclass.dto.InteractionDTO;
import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.mapper.InteractionMapper;
import com.groom.manvsclass.model.InteractionType;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.repository.InteractionRepository;
import com.groom.manvsclass.repository.ClassUTRepository;

import com.groom.manvsclass.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InteractionService {

    @Autowired
    private InteractionRepository interactionRepository;
    @Autowired
    private ClassUTRepository classUTRepository;
    @Autowired
    private InteractionMapper interactionMapper;

    public List<InteractionDTO> findInteractions() {


        List<Interaction> interactions = interactionRepository.findAll();

        return interactionMapper.toDtoList(interactions);
    }

    public List<InteractionDTO> findReports() {

        List<Interaction> interactions = interactionRepository.findByType(InteractionType.REPORT);

        return interactionMapper.toDtoList(interactions);
    }

    public long countLikes(String className) {

        boolean classExists = classUTRepository.existsById(className);
        if (!classExists) {
            throw new NotFoundException("Classe " + className + " non trovata.");
        }

        return interactionRepository.countByClassUT_NameAndType(className, InteractionType.LIKE);
    }

    public void uploadInteraction(String className, InteractionDTO interactionDTO) {

        Interaction interaction= interactionMapper.toEntity(interactionDTO);

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if(classUTOpt.isEmpty()) {
            throw new NotFoundException("Classe " + className + " non trovata");
        }

        interaction.setClassUT(classUTOpt.get());

        interactionRepository.save(interaction);
    }

    public void eliminaInteraction(Long interactionId) {

        Optional<Interaction> interactionOpt = interactionRepository.findById(interactionId);
        if (interactionOpt.isEmpty()) {
            throw new NotFoundException("Interazione " + interactionId + " non trovata.");
        }

        interactionRepository.delete(interactionOpt.get());
    }
}