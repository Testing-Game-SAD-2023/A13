package com.groom.manvsclass.model.entity;

import com.groom.manvsclass.model.enums.HintTypeEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "hint")
public class HintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_email", referencedColumnName = "email")
    private AdminEntity admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_ut_name", referencedColumnName = "name")
    private ClassUTEntity classUt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "image_uri")
    private String imageUri;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private HintTypeEnum type;

    @Column(name = "\"order\"", nullable = false)
    private Integer order;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}