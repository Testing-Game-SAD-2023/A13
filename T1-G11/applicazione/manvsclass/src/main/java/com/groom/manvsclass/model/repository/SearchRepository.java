package com.groom.manvsclass.model.repository;

import java.util.List;


import com.groom.manvsclass.model.ClassUT;

public interface SearchRepository {

	List<ClassUT> findByText(String text);
}
