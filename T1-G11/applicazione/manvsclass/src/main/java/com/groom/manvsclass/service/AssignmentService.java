package com.groom.manvsclass.service;

import com.groom.manvsclass.dto.AssignmentDTO;
import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.repository.AssignmentRepository;
import com.groom.manvsclass.mapper.AssignmentMapper;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.TeamRepository;
import com.groom.manvsclass.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private AssignmentMapper assignmentMapper;

    @Transactional
    public void createAssignment(String teamName, AssignmentDTO assignmentDTO, String adminEmail) {

        Assignment assignment = assignmentMapper.toEntity(assignmentDTO);

        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException("Il team " + teamName + " non è stato trovato.");
        }

        Team assignmentTeam = teamOpt.get();

        if (assignmentTeam.getAdmin() == null || !assignmentTeam.getAdmin().getEmail().equals(adminEmail)) {
            throw new ForbiddenException("L'admin " + adminEmail + " non ha i permessi per creare un Assignment per questo Team.");
        }

        assignment.setTeam(assignmentTeam);

        assignmentRepository.save(assignment);

        // 9. Invia notifica agli utenti del team
        List<String> studentIds = assignmentTeam.getStudentIds();
        List<Integer> integerList = studentIds.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        String Title = "Assignment";
        String Message = "Nuovo Assignment: " + assignment.getTitle();
        notificationService.sendNotificationsToUsers(integerList, Title, Message, "Team");

        //10. Invio email agli utenti del team
        //emailService.sendTeamNewAssignment(idsStudentiTeam, existingTeam, assignment, jwt);
    }

    public List<AssignmentDTO> findAdminSingleTeamAssignments(String teamName, String adminEmail) {

            boolean teamExists = teamRepository.existsByName(teamName);
            if (!teamExists) {
                throw new NotFoundException("Team " + teamName + " non trovato.");
            }

            List<Assignment> assignments = assignmentRepository.findAllByTeam_Name(teamName);
            if (assignments.isEmpty()) {
                throw new NotFoundException("Nessun assignment trovato per il Team con nome " + teamName);
            }

            List<AssignmentDTO> assignmentDTOs = assignmentMapper.toDtoList(assignments);

            return assignmentDTOs;
    }

    public List<AssignmentDTO> findAdminTeamsAssignments(String adminEmail) {

            List<Team> adminTeams = teamRepository.findByAdmin_Email(adminEmail);
            if (adminTeams.isEmpty()) {
                throw new NotFoundException("Admin " + adminEmail + " non è associato ad alcun team.");
            }

            List<Long> teamIds = adminTeams.stream()
                    .map(Team::getId)
                    .collect(Collectors.toList());

            List<Assignment> assignments = assignmentRepository.findAllByTeam_IdIn(teamIds);
            if (assignments.isEmpty()) {
                throw new NotFoundException("Non sono stati trovati assignment per i team dell'admin: " + adminEmail);
            }

            List<AssignmentDTO> assignmentDTOs = assignmentMapper.toDtoList(assignments);

            return (assignmentDTOs);
    }

    @Transactional
    public void deleteAssignment(String assignmentTitle, String adminEmail) {

        Optional<Assignment> assignmentOpt = assignmentRepository.findByTitle(assignmentTitle);
        if (assignmentOpt.isEmpty()) {
            throw new NotFoundException("Assignment con titolo " + assignmentTitle + " non trovato.");
        }

        Assignment assignmentToDelete = assignmentOpt.get();

        Long teamId = assignmentToDelete.getTeam().getId();

        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException("Team con ID " + teamId + " non trovato.");
        }

        Team existingTeam = teamOpt.get();

        if (!existingTeam.getAdmin().getEmail().equals(adminEmail)) {
            throw new ForbiddenException("Non hai i permessi per rimuovere gli assignment di questo team.");
        }

        assignmentRepository.delete(assignmentToDelete);

    }

}

    