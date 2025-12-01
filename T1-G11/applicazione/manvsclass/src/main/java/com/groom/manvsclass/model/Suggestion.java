package com.groom.manvsclass.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
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