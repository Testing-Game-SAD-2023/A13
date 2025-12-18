package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.HintEntity;
import com.groom.manvsclass.model.enums.HintTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HintRepository extends JpaRepository<HintEntity, Long>, JpaSpecificationExecutor<HintEntity> {

    @Query("SELECT h FROM HintEntity h WHERE h.content = :content AND h.type = :type AND " +
            "((h.type = 'CLASS' AND h.classUt.name = :classUtName) OR (h.type = 'GENERIC' AND h.classUt IS NULL))")
    Optional<HintEntity> findUniqueHint(@Param("content") String content,
                                        @Param("type") HintTypeEnum type,
                                        @Param("classUtName") String classUtName);

    Optional<HintEntity> findFirstByClassUtNameAndTypeOrderByOrderDesc(String classUtName, HintTypeEnum type);

    HintEntity findByContentAndTypeAndClassUtName(String content,  HintTypeEnum type, String classUtName);

    List<HintEntity> findByClassUtName(String classUtName);

    HintEntity findByClassUtNameAndOrder(String classUtName, Integer order);

    List<HintEntity> findByType(HintTypeEnum type);

    HintEntity findByTypeAndOrder(HintTypeEnum type, Integer order);

    void deleteByType(HintTypeEnum type);

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM HintEntity h " +
            "WHERE h.type = :type AND h.order = :order AND " +
            "( (:classUtName IS NULL AND h.classUt IS NULL) OR (h.classUt.name = :classUtName) )")
    boolean existsByClassUtNameAndTypeAndOrder(
            @Param("classUtName") String classUtName,
            @Param("type") HintTypeEnum type,
            @Param("order") Integer order
    );

    Optional<HintEntity> findByClassUtNameAndTypeAndOrder(String classUtName, HintTypeEnum type, Integer order);

    void deleteByClassUt_Name(String name);
}
