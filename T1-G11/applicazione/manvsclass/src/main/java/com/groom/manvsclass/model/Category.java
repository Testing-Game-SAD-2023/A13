package com.groom.manvsclass.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String name;
	
	@ManyToMany(mappedBy = "categories")
	private List<ClassUT> classes;
}