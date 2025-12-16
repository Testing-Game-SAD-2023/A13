package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.ClassUT;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassUTRepository extends JpaRepository<ClassUT, String> {

        List<ClassUT> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

        @Query("SELECT c FROM ClassUT c WHERE c.difficulty = :difficulty")
        List<ClassUT> filterByDifficulty(OpponentDifficulty difficulty);

        List<ClassUT> findAllByOrderByDateAsc();

        List<ClassUT> findAllByOrderByNameAsc();

        List<ClassUT> findByNameContainingIgnoreCaseAndDifficulty(String name, OpponentDifficulty difficulty);

        @Query("SELECT c FROM ClassUT c JOIN c.categories cat WHERE cat.name = :categoryName")
        List<ClassUT> findAllByCategoryName(@Param("categoryName") String categoryName);

        @Query("SELECT c FROM ClassUT c JOIN c.categories cat WHERE (c.name LIKE %:text% OR c.description LIKE %:text%) AND cat.name = :categoryName")
        List<ClassUT> searchAndFilterByCategory(@Param("text") String text, @Param("categoryName") String categoryName);

        @Query("SELECT c FROM ClassUT c ORDER BY c.date")
        List<ClassUT> orderByDate();

        @Query("SELECT c FROM ClassUT c ORDER BY c.name")
        List<ClassUT> orderByName();

}
