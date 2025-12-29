package com.groom.manvsclass.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "interactions")
public class InteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_name", referencedColumnName = "name", nullable = false)
    @ToString.Exclude
    private ClassUTEntity classUt;

    @Column(name = "interaction_type", nullable = false)
    private Integer interactionType; // 1 = Like, 0 = Report

    //commento
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public boolean isLike() {
        return this.interactionType != null && this.interactionType == 1;
    }

    public boolean isReport() {
        return this.interactionType != null && this.interactionType == 0;
    }

}
