package com.groom.manvsclass.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {

    ResponseEntity<?> ottieniStudentiDettagli(List<String> studentiIds, String jwt);
}
