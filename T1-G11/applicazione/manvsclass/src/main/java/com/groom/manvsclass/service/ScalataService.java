package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.ClassUTScalata;
import com.groom.manvsclass.model.ClassUTScalataId;
import com.groom.manvsclass.dto.SingleScalataDTO;
import com.groom.manvsclass.dto.ScalataDTO;
import com.groom.manvsclass.repository.ScalataRepository;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.ClassUTRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class ScalataService {

    @Autowired
    private ScalataRepository scalataRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ClassUTRepository classUTRepository;

    @Transactional
    public void uploadScalata(ScalataDTO scalataDTO, String adminEmail) {

        Optional<Admin> adminOpt = adminRepository.findById(adminEmail);
        if (adminOpt.isEmpty()) {
            throw new NotFoundException("Admin " + adminEmail + " non trovato.");
        }

        Scalata newScalata = new Scalata();

        newScalata.setAdmin(adminOpt.get());
        newScalata.setName(scalataDTO.getScalataName());
        newScalata.setDescription(scalataDTO.getDescription());
        newScalata.setDate(LocalDate.now());

        List<ClassUTScalata> associations = new ArrayList<>();

        List<SingleScalataDTO> singleScalataDTOList = scalataDTO.getSingleScalataDTOList();
        int numLevels = 0;

        for (SingleScalataDTO singleScalataDTO : singleScalataDTOList) {

            ClassUTScalata newAssociation = new ClassUTScalata();

            Optional<ClassUT> classUTOpt = classUTRepository.findById(singleScalataDTO.getClassName());
            if (classUTOpt.isEmpty()) {
                throw new NotFoundException("Classe " +  singleScalataDTO.getClassName() + " non trovata.");
            }

            newAssociation.setClassUT(classUTOpt.get());
            newAssociation.setScalata(newScalata);
            newAssociation.setId(new ClassUTScalataId(classUTOpt.get().getName(), scalataDTO.getScalataName()));
            newAssociation.setLevel(singleScalataDTO.getLevel());
            newAssociation.setTimeLimit(singleScalataDTO.getTimeLimit());

            associations.add(newAssociation);
            numLevels += 1;
        }

        newScalata.setAssociations(associations);
        newScalata.setNumLevels(numLevels);

        scalataRepository.save(newScalata);
    }

    public void deleteScalataByName(String scalataName, String adminEmail) {

        Optional<Scalata> scalataOpt = scalataRepository.findById(scalataName);
        if(scalataOpt.isEmpty()) {

            throw new NotFoundException("Scalata con nome: " + scalataName + " non trovata");
        }

        Scalata scalataToDelete = scalataOpt.get();

        if(scalataToDelete.getAdmin() == null || !scalataToDelete.getAdmin().getEmail().equals(adminEmail)) {
            throw new ForbiddenException("L'admin " + adminEmail + " non ha i permessi per eliminare la scalata.");
        }

        scalataRepository.delete(scalataToDelete);
    }

    @Transactional
    public ScalataDTO findScalataByName(String scalataName) {

        Optional<Scalata> scalataOpt = scalataRepository.findById(scalataName);
        if (scalataOpt.isEmpty()) {
            throw new NotFoundException("Scalata con nome: " + scalataName + " non trovata.");
        }

        Scalata scalataToFind = scalataOpt.get();
        List<ClassUTScalata> associations = scalataToFind.getAssociations();

        List<SingleScalataDTO> singleScalataDTOList = new ArrayList<>();
        for (ClassUTScalata association : associations) {

            SingleScalataDTO singleScalataDTO = new SingleScalataDTO();
            singleScalataDTO.setClassName(association.getClassUT().getName());
            singleScalataDTO.setLevel(association.getLevel());
            singleScalataDTO.setTimeLimit(association.getTimeLimit());

            singleScalataDTOList.add(singleScalataDTO);
        }

        ScalataDTO scalataDTO = new ScalataDTO();
        scalataDTO.setScalataName(scalataName);
        scalataDTO.setDescription(scalataToFind.getDescription());
        scalataDTO.setSingleScalataDTOList(singleScalataDTOList);

        return scalataDTO;
    }

    public List<ScalataDTO> listScalate() {

        List<ScalataDTO> scalataDTOList = new ArrayList<>();

        List<Scalata> scalataList = scalataRepository.findAll();
        for (Scalata scalata : scalataList) {

            try {

                ScalataDTO scalataDTO = findScalataByName(scalata.getName());
                scalataDTOList.add(scalataDTO);

            } catch (NotFoundException e) {

            }
        }

        return scalataDTOList;
    }

}