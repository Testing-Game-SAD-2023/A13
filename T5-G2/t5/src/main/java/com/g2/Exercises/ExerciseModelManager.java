package com.g2.Exercises;

import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;

public class ExerciseModelManager {
    
    public static void generateGoalsForExercise(Exercise exercise, GoalRepository goalRepository)
        throws JacksonException, IllegalArgumentException
    {
        Goal goal;
        for(String generatingString : exercise.getGoalTypes()){
            for(String student : exercise.getStudents()){
                goal = Goal.deserialize(generatingString);
                
                goal.setAssignmentId(exercise.id);
                goal.setPlayerId(student);
                goal.setCompletition(0);
                goalRepository.insert(goal);
            }
        }
    }

    public static void removeGoalsForExercise(Exercise exercise, GoalRepository goalRepository){
        goalRepository.deleteManyByAssignmentId(exercise.id);
    }

    /**
     * Questo metodo deve aggiornare la lista dei goal per un esercizio. Si suppone che
     * ad essere cambiata sia la lista degli studenti, ma il metodo funziona anche se a
     * cambire sia la lista dei GoalTypes.
     * 
     * Si compone di due operazioni principali:
     * 1) Verificare che per ogni studente e tipo di goal esista già un goal nel db, altrimenti bisogna crearlo;
     * 2) Verificare che non esistana nessun altro goal che non corrisponda ad una combinazione studente-tipo.
     * 
     * Quindi:
     * 1) si richiede una lista di goal per l'esercizio dal db;
     * 2) si scartano i goal man mano che si valutano i goal per studente-tipo se già esistono;
     * 3) si inseriscono nel db i goal nuovi;
     * 4) i goal non scartati dal db sono goal obsoleti di studenti non in lista o tipi rimossi.
     * 
     * Per garantire che le operazioni siano efficienti si usa l'hashSet, sulla base dell'{@link com.g2.Goals.Goal#hashCode() hashCode}.
     * Si intende utilizzare questa funzione in caso di richieste di update di missioni dal controller rest.
     * 
     * 
     * @param exercise
     * @throws JacksonException
     * @throws IllegalArgumentException
     */
    public static void updateGoalsForExercise(Exercise exercise, GoalRepository goalRepository)
        throws JacksonException, IllegalArgumentException
    {
        if(exercise.students == null){
            throw new IllegalArgumentException("Non c'è una lista di studenti!");
        }
        List<Goal> existingGoals = goalRepository.findAll(null, exercise.id, null,null);
        HashSet<Goal> hashedGoals = new HashSet<>(existingGoals);

        Goal goal;
        
        for(String generatingString : exercise.getGoalTypes()){
            for(String student : exercise.getStudents()){
                
                goal = Goal.deserialize(generatingString);
                goal.setAssignmentId(exercise.id);
                goal.setPlayerId(student);

                if(hashedGoals.contains(goal)){
                    hashedGoals.remove(goal);
                }else{
                    goal.setCompletition(0);
                    goalRepository.insert(goal);
                }
            }
        }
        
        for(Goal remainingGoal: hashedGoals){
            goalRepository.delete(remainingGoal);
        }
    }

    
}
