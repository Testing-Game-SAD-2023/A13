/*MODIFICA (5/11/2024) - Refactoring task T1
 * Util ora si occupa di implementare i servizi ritenuti di utilità generale.
 */
package com.groom.manvsclass.util;

import com.groom.manvsclass.repository.InteractionRepository;
import com.groom.manvsclass.repository.ClassUTRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public int API_id() {
        Random random = new Random();
        return random.nextInt(1000000 - 0 + 1) + 0;
    }

    public String API_email(int id_u) {
        return "prova." + id_u + "@email.com";
    }

}