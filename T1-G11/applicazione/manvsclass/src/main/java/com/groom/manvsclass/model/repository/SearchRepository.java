package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.ClassUT;

import java.util.List;

public interface SearchRepository {

    List<ClassUT> findByText(String text);
}
