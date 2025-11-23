package com.groom.manvsclass.model;

import com.groom.manvsclass.model.Admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "achievements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String name;
    private String description;
    private String statistic;
    private float progressRequired;
	
	@ManyToOne
	@JoinColumn(name = "admin_email", referencedColumnName = "email")
	private Admin admin;
}
