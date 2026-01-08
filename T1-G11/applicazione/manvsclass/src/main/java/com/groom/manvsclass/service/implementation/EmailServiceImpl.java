package com.groom.manvsclass.service.implementation;

import com.groom.manvsclass.model.entity.AssignmentEntity;
import com.groom.manvsclass.model.entity.TeamEntity;
import com.groom.manvsclass.service.EmailService;
import com.groom.manvsclass.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    private StudentService studentService;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // MAIL RESET PASSWORD
    @Override
    public void sendPasswordResetEmail(String email, String resetToken) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Richiesta di reset password");
        helper.setText("Per favore, copia il seguente token per resettare la password: " + resetToken);

        javaMailSender.send(message);
    }

    // MAIL DI INVITO
    @Override
    public void sendInvitationToken(String email, String invitationToken) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Invio token di invito");
        helper.setText("Per favore, copia il seguente token presso l'end-point \\login_with_invitation per registrarti come amministratore: " + invitationToken);

        javaMailSender.send(message);
    }


    //Modifica 12/12/2024
    //Mail aggiunta Team // MAIL DI NOTIFICA AGGIUNTA AL TEAM
    @Override
    @Async
    public void sendTeamAdditionNotificationToStudents(List<String> emails, String teamName) throws MessagingException {
        for (String email : emails) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Sei stato aggiunto a un nuovo Team!");
            helper.setText("Ciao,\n\n" +
                    "Sei stato aggiunto al team: \"" + teamName + "\n" +
                    "Accedi alla piattaforma per maggiori dettagli.\n\n");

            javaMailSender.send(message);
        }
    }

    @Override
    @Async
    public ResponseEntity<String> sendTeamNewAssignment(List<String> idsStudents, TeamEntity teamEntity, AssignmentEntity assignmentEntity, String jwt) {

        ResponseEntity<?> dettagliStudentiResponse = studentService.ottieniStudentiDettagli(idsStudents, jwt);
        if (!HttpStatus.OK.equals(dettagliStudentiResponse.getStatusCode())) {
            return ResponseEntity.status(dettagliStudentiResponse.getStatusCode())
                    .body("Errore nel recupero delle informazioni sugli studenti: " + dettagliStudentiResponse.getBody());
        }

        // 11. Recupera i dettagli degli studenti
        List<Map<String, Object>> studentiDettagli = (List<Map<String, Object>>) dettagliStudentiResponse.getBody();
        List<String> emails = studentiDettagli.stream()
                .map(student -> (String) student.get("email"))
                .collect(Collectors.toList());

        // 12. Invia email di notifica agli studenti aggiunti

        try {
            sendTeamNewAssignment(
                    emails,
                    teamEntity.getName(),
                    assignmentEntity.getDataScadenza(),
                    assignmentEntity.getTitolo(),
                    assignmentEntity.getDescrizione()
            );
        } catch (MessagingException e) {
            System.out.println("Errore durante l'invio della email.");
        }
        return null;
    }

    //Mail nuovo assignment agli studenti.
    @Override
    public void sendTeamNewAssignment(List<String> emails, String teamName, java.util.Date dataScadenza, String titoloAssignment, String descrizione) throws MessagingException {
        for (String email : emails) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Aggiungi destinatario
            helper.setTo(email);

            // Oggetto dell'email
            helper.setSubject("Nuovo assignment per il team: " + teamName);

            // Contenuto dell'email
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dataScadenza);
            String body = "Ciao,\n\n" +
                    "Hai un nuovo assignment nel team: \"" + teamName + "\".\n\n" +
                    "Titolo Assignment: " + titoloAssignment + "\n" +
                    "Descrizione: " + descrizione + "\n\n" +
                    "Data di scadenza: " + formattedDate + "\n\n" +
                    "Accedi alla piattaforma per maggiori dettagli e per completarlo.";

            helper.setText(body);

            // Invia l'email
            javaMailSender.send(message);

        }
    }

}
