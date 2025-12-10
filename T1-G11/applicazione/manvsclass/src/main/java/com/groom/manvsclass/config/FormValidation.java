/*
* Questa classe contiene costanti pubbliche per la validazione del form per il caricamento delle classi.
*/
package com.groom.manvsclass.config;

import java.lang.String;
import java.util.Objects;
import com.groom.manvsclass.model.ClassUT;

public class FormValidation {
	public static final int CLASS_NAME_MAX_LENGTH = 30;
	public static final int CLASS_CATEGORY_MAX_LENGTH = 30;
	public static final int CLASS_DESCRIPTION_MAX_LENGTH = 150;
	public static final String[] DIFFICULTY_LEVELS = {"Beginner", "Intermediate", "Advanced"};
	
	/*
	* Questo metodo restituisce true se i dati contenuti nell'oggetto ClassUT
	* rispettano la validazione.
	*/
	public static boolean validateClassUT(ClassUT classe) throws NullPointerException {
		Objects.requireNonNull(classe);
		Objects.requireNonNull(classe.getName());
		Objects.requireNonNull(classe.getDifficulty());

		//Il nome della classe deve essere presente.
		if(classe.getName().length() == 0 || classe.getName().length() > CLASS_NAME_MAX_LENGTH) {
			return false;
		}
		
		//La stringa che rappresenta la difficoltà deve essere una delle 3 possibili.
		boolean foundDifficulty = false;
		
		for(String difficulty : DIFFICULTY_LEVELS) {
			if(difficulty.equals(classe.getDifficulty())) {
				foundDifficulty = true;
				break;
			}
		}
		
		if(foundDifficulty == false) {
			return false;
		}
		
		if(classe.getCategory() != null) {
			if(classe.getCategory().size() > 3) {
				return false;
			}
			else {
				for(String category : classe.getCategory()) {
					if(category.length() > CLASS_CATEGORY_MAX_LENGTH) {
						return false;
					}
				}
			}
		}
		
		if(classe.getDescription() != null) {
			if(classe.getDescription().length() > CLASS_DESCRIPTION_MAX_LENGTH) {
				return false;
			}
		}
		
		return true;
	}	
}
