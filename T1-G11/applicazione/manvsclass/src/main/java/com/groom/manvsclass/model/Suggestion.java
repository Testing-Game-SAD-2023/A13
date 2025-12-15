package com.groom.manvsclass.model;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.SuggestionLevel;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Column;

import jakarta.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@DiscriminatorValue("Suggestion")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Suggestion extends Guideline {

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SuggestionLevel level;

    @NotNull
	@ManyToOne
	@JoinColumn(name = "class_name", referencedColumnName = "name")
    private ClassUT classUT;	
}