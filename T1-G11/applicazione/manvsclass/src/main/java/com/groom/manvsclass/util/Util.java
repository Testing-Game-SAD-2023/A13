/*MODIFICA (5/11/2024) - Refactoring task T1
 * Util ora si occupa di implementare i servizi ritenuti di utilità generale.
 */
package com.groom.manvsclass.util;

import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.model.ClassUT;

import com.groom.manvsclass.repository.InteractionRepository;
import com.groom.manvsclass.repository.ClassUTRepository;

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

    // Metodo per generare un ID univoco (esempio con UUID)
    //Modifica 04/12/2024
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public List<Interaction> elencaInt() {
        return interactionRepository.findAll();
    }

    public List<Interaction> elencaReport() {

        return interactionRepository.findByType(0);
    }

    public long likes(String className) {
        return interactionRepository.countByClassUT_NameAndType(className, 1);
    }

    public Interaction uploadInteraction(Interaction interaction) {
        return interactionRepository.save(interaction);
    }

    public int API_id() {
        Random random = new Random();
        return random.nextInt(1000000 - 0 + 1) + 0;
    }

    public String API_email(int id_u) {
        return "prova." + id_u + "@email.com";
    }

    public String newLike(String className) {

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if(classUTOpt.isEmpty()) {
            return "Errore: Classe " + className + " non trovata";
        }

        ClassUT classUT = classUTOpt.get();

        Interaction newInteraction = new Interaction();
        newInteraction.setType(1);
        newInteraction.setDate(LocalDate.now());
        newInteraction.setClassUT(classUT);

        interactionRepository.save(newInteraction);

        return "Nuova interazione di tipo 'like' inserita per la classe: " + className;
    }

    public String newReport(String className, String commento) {

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if(classUTOpt.isEmpty()) {
            return "Errore: Classe " + className + " non trovata";
        }

        ClassUT classUT = classUTOpt.get();

        Interaction newInteraction = new Interaction();
        newInteraction.setType(0);
        newInteraction.setDescription(commento);
        newInteraction.setDate(LocalDate.now());
        newInteraction.setClassUT(classUT);

        interactionRepository.save(newInteraction);

        return "Nuova interazione di tipo 'report' inserita per la classe: " + className;
    }

    public Interaction eliminaInteraction(Long interactionId) {

        Optional<Interaction> interactionOpt = interactionRepository.findById(interactionId);
        if (interactionOpt.isEmpty()) {
            return null;
        }
        Interaction interactionToDelete = interactionOpt.get();
        interactionRepository.delete(interactionToDelete);
        return interactionToDelete;
    }
}