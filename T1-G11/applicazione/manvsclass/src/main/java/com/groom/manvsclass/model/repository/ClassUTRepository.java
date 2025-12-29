package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.ClassUTEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassUTRepository extends JpaRepository<ClassUTEntity, String> {

    @Query("SELECT COUNT(c) FROM class_ut c WHERE c.name IN :names")
    long countExistingClasses(@Param("names") List<String> classNames);

    @Query(value = "SELECT * FROM class_ut c " +
            "WHERE (:category IS NULL OR :category = '' OR jsonb_exists(c.category, :category)) " +
            "AND (:text IS NULL OR :text = '' OR " +
            "(c.name ILIKE CONCAT('%', :text, '%') OR c.description ILIKE CONCAT('%', :text, '%')))",
            nativeQuery = true)
    List<ClassUTEntity> searchAndFilter(@Param("text") String text, @Param("category") String category);

    @Query(value = "SELECT * FROM class_ut c WHERE jsonb_exists(c.category, :category)",
            nativeQuery = true)
    List<ClassUTEntity> findByCategory(@Param("category") String category);

    @Query(value = "SELECT * FROM class_ut WHERE name ILIKE CONCAT('%', :text, '%')",
            nativeQuery = true)
    List<ClassUTEntity> findByNameLike(@Param("text") String text);

    List<ClassUTEntity> findAllByOrderByDateAsc();

    List<ClassUTEntity> findAllByOrderByNameAsc();

    List<ClassUTEntity> findByDifficulty(String difficulty);

    @Query(value = "SELECT * FROM class_ut c " +
            "WHERE c.difficulty = :difficulty " +
            "AND (c.name ILIKE CONCAT('%', :text, '%') OR c.description ILIKE CONCAT('%', :text, '%'))",
            nativeQuery = true)
    List<ClassUTEntity> searchAndFilterByDifficulty(@Param("text") String text,
                                                    @Param("difficulty") String difficulty);
}
