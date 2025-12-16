package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.ClassUTScalata;
import com.groom.manvsclass.model.ClassUTScalataId;
import com.groom.manvsclass.dto.ClassUTScalataDTO;
import com.groom.manvsclass.dto.ScalataDTO;
import com.groom.manvsclass.repository.ScalataRepository;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.mapper.ScalataMapper;

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
    @Autowired
    private ScalataMapper scalataMapper;

    @Transactional
    public void uploadScalata(ScalataDTO scalataDTO, String adminEmail) {

        Scalata newScalata = scalataMapper.toEntity(scalataDTO);

        Optional<Admin> adminOpt = adminRepository.findById(adminEmail);
        if (adminOpt.isEmpty()) {
            throw new NotFoundException("Admin " + adminEmail + " non trovato.");
        }
        newScalata.setAdmin(adminOpt.get());

        List<ClassUTScalataDTO> classUTScalataDTOs = scalataDTO.getClassUTScalataDTOs();

        List<ClassUTScalata> associations = new ArrayList<>();

        for (ClassUTScalataDTO classUTScalataDTO : classUTScalataDTOs) {

            Optional<ClassUT> classUTOpt = classUTRepository.findById(classUTScalataDTO.getClassName());
            if (classUTOpt.isEmpty()) {
                throw new NotFoundException("Classe " +  classUTScalataDTO.getClassName() + " non trovata.");
            }

            ClassUTScalata association = scalataMapper.toAssociation(classUTScalataDTO, newScalata, classUTOpt.get());

            associations.add(association);
        }

        newScalata.setAssociations(associations);
        newScalata.setNumLevels(associations.size());

        scalataRepository.save(newScalata);
    }

    public void deleteScalataByName(String scalataName, String adminEmail) {

        Optional<Scalata> scalataOpt = scalataRepository.findById(scalataName);
        if(scalataOpt.isEmpty()) {
            throw new NotFoundException("Scalata con nome: " + scalataName + " non trovata");
        }

        Scalata scalataToDelete = scalataOpt.get();

        if(!scalataToDelete.getAdmin().getEmail().equals(adminEmail)) {
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

        List<ClassUTScalataDTO> classUTScalataDTOList = new ArrayList<>();
        for (ClassUTScalata association : associations) {

            ClassUTScalataDTO classUTScalataDTO = new ClassUTScalataDTO();
            classUTScalataDTO.setClassName(association.getClassUT().getName());
            classUTScalataDTO.setLevel(association.getLevel());
            classUTScalataDTO.setTimeLimit(association.getTimeLimit());

            classUTScalataDTOList.add(classUTScalataDTO);
        }

        ScalataDTO scalataDTO = new ScalataDTO();
        scalataDTO.setScalataName(scalataName);
        scalataDTO.setDescription(scalataToFind.getDescription());
        scalataDTO.setClassUTScalataDTOs(classUTScalataDTOList);

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