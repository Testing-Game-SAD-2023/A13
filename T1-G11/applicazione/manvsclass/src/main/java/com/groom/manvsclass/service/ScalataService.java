package com.groom.manvsclass.service;

import com.groom.manvsclass.model.entity.ScalataEntity;
import org.springframework.http.ResponseEntity;

public interface ScalataService {

    ResponseEntity<?> uploadScalata(ScalataEntity scalataEntity, String jwt);

    ResponseEntity<?> listScalate();

    ResponseEntity<?> deleteScalataByName(String scalataName, String jwt);

    ResponseEntity<?> retrieveScalataByName(String scalataName);

}
