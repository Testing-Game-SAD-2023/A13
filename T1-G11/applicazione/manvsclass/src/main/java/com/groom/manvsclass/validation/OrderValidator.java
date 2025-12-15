package com.groom.manvsclass.validation;

import com.groom.manvsclass.dto.GuidelineDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class OrderValidator implements ConstraintValidator<ValidOrder, List<? extends GuidelineDTO>> {

    @Override
    public boolean isValid(List<? extends GuidelineDTO> suggestionDTOs, ConstraintValidatorContext valContext) {

        // interpretiamo una lista null o vuota come valida dal punto di vista
        // dell'ordinamento (in alternativa utilizzare anche @NotNull e/o @NotEmpty)
        if (suggestionDTOs == null || suggestionDTOs.isEmpty()) {
            return true;
        }

        Integer previous = null;

        for (GuidelineDTO suggestionDTO : suggestionDTOs) {

            int current = suggestionDTO.getOrder();

            // CHECK PRIMO ELEMENTO UGUALE A 1
            if (previous == null) {
                if (current != 1) {
                    valContext.disableDefaultConstraintViolation();
                    valContext.buildConstraintViolationWithTemplate(
                            "La lista deve iniziare con order = 1. Trovato: " + current).addConstraintViolation();
                    return false;
                }
            } else {

                // CHECK SEQUENZA PROGRESSIVA
                if (current != previous + 1) {
                    valContext.disableDefaultConstraintViolation();
                    valContext.buildConstraintViolationWithTemplate(
                            "Interruzione sequenza: atteso " + (previous + 1) + ", trovato " + current)
                            .addConstraintViolation();
                    return false;
                }
            }

            previous = current;
        }

        return true;
    }
}