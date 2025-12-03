package com.groom.manvsclass.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "suggestions")
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