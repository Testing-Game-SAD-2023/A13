package com.groom.manvsclass.repository;

import java.util.List;

import com.groom.manvsclass.model.ClassUT;

public interface SearchRepository {

	List<ClassUT> findByText(String text);
}
