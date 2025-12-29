package com.groom.manvsclass.service;

import com.groom.manvsclass.model.entity.AssignmentEntity;
import com.groom.manvsclass.model.entity.TeamEntity;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {

    void sendPasswordResetEmail(String email, String resetToken) throws MessagingException;

    void sendInvitationToken(String email, String invitationToken) throws MessagingException;

    void sendTeamAdditionNotificationToStudents(List<String> emails, String teamName) throws MessagingException;

    ResponseEntity<String> sendTeamNewAssignment(List<String> idsStudents, TeamEntity teamEntity, AssignmentEntity assignmentEntity, String jwt);

    void sendTeamNewAssignment(List<String> emails, String teamName, java.util.Date dataScadenza, String titoloAssignment, String descrizione) throws MessagingException;
}
