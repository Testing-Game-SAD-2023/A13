package com.groom.manvsclass.service;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.repository.ClassUTRepository;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

@Service
public class ClassUTService {

    @Autowired
    private ClassUTRepository classUTRepository;

    public List<ClassUT> getClassUTs() {

        return classUTRepository.findAll();
    }

    public List<String> getClassUTNames() {

        List<ClassUT> classUTList = getClassUTs();
        List<String> classUTNames = new ArrayList<>();

        for (ClassUT classUT : classUTList) {

            classUTNames.add(classUT.getName());
        }

        return classUTNames;
    }

    public List<ClassUT> filterByDifficulty(String difficulty) {

        OpponentDifficulty difficultyEnum = OpponentDifficulty.valueOf(difficulty.toUpperCase());

        return classUTRepository.filterByDifficulty(difficultyEnum);
    }

    public List<ClassUT> orderByDate() {

        return classUTRepository.orderByDate();
    }

    public List<ClassUT> orderByName() {

        return classUTRepository.orderByName();
    }

}