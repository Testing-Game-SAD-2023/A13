/*MODIFICA (5/11/2024) - Refactoring task T1
 * Util ora si occupa di implementare i servizi ritenuti di utilità generale.
 */
package com.groom.manvsclass.util;

import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.model.InteractionType;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.dto.InteractionDTO;
import com.groom.manvsclass.mapper.InteractionMapper;

import com.groom.manvsclass.repository.InteractionRepository;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Optional;

@Component
public class Util {

    @Autowired
    private InteractionRepository interactionRepository;
    @Autowired
    private ClassUTRepository classUTRepository;

    @Autowired
    private InteractionMapper interactionMapper;

    // Metodo per generare un ID univoco (esempio con UUID)
    //Modifica 04/12/2024
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public List<InteractionDTO> elencaInt() {

        List<Interaction> allInteractions = interactionRepository.findAll();

        // effettua il mapping in uscita Model -> DTO
        return interactionMapper.toDtoList(allInteractions);
    }

    public List<InteractionDTO> elencaReport() {

        List<Interaction> reportInteractions = interactionRepository.findByType(InteractionType.REPORT);

        // effettua il mapping in uscita Model -> DTO
        return interactionMapper.toDtoList(reportInteractions);
    }

    public long getClassLikes(String className) {

        boolean classExists = classUTRepository.existsById(className);
        if (!classExists) {
            throw new NotFoundException("Classe " + className + " non trovata.");
        }

        return interactionRepository.countByClassUT_NameAndType(className, InteractionType.LIKE);
    }

    public int API_id() {
        Random random = new Random();
        return random.nextInt(1000000 - 0 + 1) + 0;
    }

    public String API_email(int id_u) {
        return "prova." + id_u + "@email.com";
    }

    public void uploadInteraction(InteractionDTO interactionDTO) {

        // effettua il mapping in ingresso DTO -> Model
        String className = interactionDTO.getClassName();
        Interaction interaction = interactionMapper.toEntity(interactionDTO);

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