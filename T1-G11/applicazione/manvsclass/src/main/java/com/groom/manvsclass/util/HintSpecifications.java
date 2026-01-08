package com.groom.manvsclass.util;

import com.groom.manvsclass.model.entity.HintEntity;
import com.groom.manvsclass.model.enums.HintTypeEnum;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HintSpecifications {

    public static Specification<HintEntity> withDynamicQuery(Map<String, String> queryParams) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (CollectionUtils.isEmpty(queryParams)) {
                // Nessun parametro, ritorna tutto
            } else {

                // 1. FILTRO TYPE
                String typeParam = queryParams.get("type");
                if (StringUtils.hasText(typeParam)) {
                    try {
                        HintTypeEnum type = HintTypeEnum.valueOf(typeParam.toUpperCase());
                        predicates.add(criteriaBuilder.equal(root.get("type"), type));
                    } catch (IllegalArgumentException e) {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Tipo non valido");
                    }
                }

                // 2. FILTRO CLASS UT NAME (Specifico)
                String classUtNameParam = queryParams.get("classUTName");
                if (StringUtils.hasText(classUtNameParam)) {
                    // Qui usiamo path navigation diretta perché se cerchiamo per classe, la classe deve esistere
                    predicates.add(criteriaBuilder.equal(root.get("classUt").get("name"), classUtNameParam));
                }

                // 3. FILTRO ORDER
                String orderParam = queryParams.get("order");
                if (StringUtils.hasText(orderParam)) {
                    try {
                        Integer order = Integer.parseInt(orderParam);
                        predicates.add(criteriaBuilder.equal(root.get("order"), order));
                    } catch (NumberFormatException e) {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Ordine non valido");
                    }
                }

                // 4. NUOVO FILTRO SEARCH (Globale su Name, Content, ClassName)
                String searchParam = queryParams.get("search");
                if (StringUtils.hasText(searchParam)) {
                    String pattern = "%" + searchParam.toLowerCase() + "%";

                    // Usiamo una Left Join per includere anche i suggerimenti che non hanno una classe (GENERIC)
                    // altrimenti la query escluderebbe i record con class_ut_id = null
                    Join<Object, Object> classUtJoin = root.join("classUt", JoinType.LEFT);

                    Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
                    Predicate contentLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), pattern);
                    Predicate classNameLike = criteriaBuilder.like(criteriaBuilder.lower(classUtJoin.get("name")), pattern);

                    // Aggiungiamo (A OR B OR C) alla lista dei predicati (che sono in AND tra loro)
                    predicates.add(criteriaBuilder.or(nameLike, contentLike, classNameLike));
                }
            }

            query.orderBy(criteriaBuilder.asc(root.get("order")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}