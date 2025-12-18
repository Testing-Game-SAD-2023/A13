package com.groom.manvsclass.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "class_ut")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ClassUTEntity {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "uri")
    private String uri;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Type(type = "jsonb")
    @Column(name = "category", columnDefinition = "jsonb")
    private List<String> category; // Mappato come String per un semplice JSONB

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToMany(mappedBy = "selectedClasses", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ScalataEntity> scalate;
}