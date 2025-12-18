/*MODIFICA (5/11/2024) - Refactoring task T1
 * Util ora si occupa di implementare i servizi ritenuti di utilità generale.
 */
package com.groom.manvsclass.util;

import com.groom.manvsclass.model.entity.ClassUTEntity;
import com.groom.manvsclass.model.entity.InteractionEntity;
import com.groom.manvsclass.model.repository.ClassUTRepository;
import com.groom.manvsclass.model.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    public List<InteractionEntity> elencaInt() {
        return interactionRepository.findAll();
    }

    public List<InteractionEntity> elencaReport() {
        return interactionRepository.findByInteractionType(0);
    }

    public long likes(String name) {
        return interactionRepository.countLikesByClassName(name);
    }

    public InteractionEntity uploadInteraction(InteractionEntity interazione) {
        return interactionRepository.save(interazione);
    }

    public Long API_id() {
        Random random = new Random();
        return random.nextLong(1000000 - 0 + 1) + 0;
    }

    public String API_email(Long id_u) {
        return "prova." + id_u + "@email.com";
    }

    public String newLike(String name) {
        InteractionEntity newInteractionEntity = new InteractionEntity();
        Long id_u = API_id();
        String email_u = API_email(id_u);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data = currentDate.format(formatter);

        newInteractionEntity.setId(0);
        newInteractionEntity.setUserId(id_u);
        newInteractionEntity.setUserEmail(email_u);

        ClassUTEntity classUTEntity = classUTRepository.findById(name).orElse(null);

        if (classUTEntity == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Class not found");
        }

        newInteractionEntity.setClassUt(classUTEntity);
        newInteractionEntity.setInteractionType(1);
        newInteractionEntity.setCreatedAt(LocalDateTime.now());
        interactionRepository.save(newInteractionEntity);

        return "Nuova interazione di tipo 'like' inserita per la classe: " + name;
    }

    public String newReport(String name, String commento) {
        InteractionEntity newInteractionEntity = new InteractionEntity();
        Long id_u = API_id();
        String email_u = API_email(id_u);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data = currentDate.format(formatter);

        newInteractionEntity.setId(0);
        newInteractionEntity.setUserId(id_u);
        newInteractionEntity.setUserEmail(email_u);

        ClassUTEntity classUTEntity = classUTRepository.findById(name).orElse(null);
        if (classUTEntity == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Class not found");
        }

        newInteractionEntity.setClassUt(classUTEntity);
        newInteractionEntity.setInteractionType(0);
        newInteractionEntity.setCreatedAt(LocalDateTime.now());
        newInteractionEntity.setDescription(commento);
        interactionRepository.save(newInteractionEntity);

        return "Nuova interazione di tipo 'report' inserita per la classe: " + name;
    }

    public InteractionEntity eliminaInteraction(int id_i) {
        InteractionEntity interactionToDelete = interactionRepository.findById(id_i)
                .orElse(null);
        if (interactionToDelete != null) {
            interactionRepository.delete(interactionToDelete);
        }
        return interactionToDelete;
    }
}