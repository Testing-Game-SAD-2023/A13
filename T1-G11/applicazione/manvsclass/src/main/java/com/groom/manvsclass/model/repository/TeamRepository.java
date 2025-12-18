package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, String> {

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM team t WHERE jsonb_exists(t.id_studenti, :idStudente)",
            nativeQuery = true)
    TeamEntity findByIdStudenti(@Param("idStudente") String idStudente);
}
