package com.groom.manvsclass.model;

import java.io.Serializable;
import jakarta.persistence.Embeddable;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class ClassUTScalataId implements Serializable {

    private String className;
    private String scalataName;

}