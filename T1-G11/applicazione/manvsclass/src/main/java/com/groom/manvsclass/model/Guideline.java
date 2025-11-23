package com.groom.manvsclass.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Lob;
import jakarta.persistence.Column;
import jakarta.persistence.Basic;
import jakarta.persistence.FetchType;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Base64;

@Entity
@Table(name = "guidelines")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Guideline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String hint;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    private LocalDate date;

    public String getBase64Image() {

        if (this.image == null || this.image.length == 0) {
            return null;
        }

        return Base64.getEncoder().encodeToString(this.image);
    }

}