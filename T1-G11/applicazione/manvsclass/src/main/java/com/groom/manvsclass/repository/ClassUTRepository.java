package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.ClassUT;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassUTRepository extends JpaRepository<ClassUT, String> {

        // Sostituisce findByText (SearchRepositoryImpl)
        // Cerca sia nel nome che nella descrizione
        List<ClassUT> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

        // Sostituisce filterByDifficulty (da SearchRepositoryImpl)
        @Query("SELECT DISTINCT c FROM ClassUT c JOIN c.opponents o WHERE o.opponentDifficulty = :difficulty")
        List<ClassUT> filterByDifficulty(OpponentDifficulty difficulty);

        // Sostituisce orderByDate (da SearchRepositoryImpl)
        List<ClassUT> findAllByOrderByDateAsc();

        // Sostituisce orderByName (da SearchRepositoryImpl)
        List<ClassUT> findAllByOrderByNameAsc();

        // Sostituisce searchAndDFilter (da SearchRepositoryImpl)
        List<ClassUT> findByNameContainingIgnoreCaseAndDifficulty(String name, OpponentDifficulty difficulty);

        // Sostituisce filterByCategory (da SearchRepositoryImpl)
        // Usa una JOIN sulla tabella delle categorie
        @Query("SELECT c FROM ClassUT c JOIN c.categories cat WHERE cat.name = :categoryName")
        List<ClassUT> findAllByCategoryName(@Param("categoryName") String categoryName);

        @Query("SELECT c FROM ClassUT c JOIN c.categories cat WHERE (c.name LIKE %:text% OR c.description LIKE %:text%) AND cat.name = :categoryName")
        List<ClassUT> searchAndFilterByCategory(@Param("text") String text, @Param("categoryName") String categoryName);

        @Query("SELECT c FROM ClassUT c ORDER BY c.date")
        List<ClassUT> orderByDate();

        @Query("SELECT c FROM ClassUT c ORDER BY c.name")
        List<ClassUT> orderByName();

}
