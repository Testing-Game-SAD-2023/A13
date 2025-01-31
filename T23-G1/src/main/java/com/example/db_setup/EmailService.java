package com.example.db_setup;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  // MAIL RESET PASSWORD
  public void sendPasswordResetEmail(String email, String resetToken) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("Password reset request");
    helper.setText("Please copy the following token to reset your password: " + resetToken);

    javaMailSender.send(message);

  }

  // MAIL REGISTER ID
  public void sendMailRegister(String email, Integer id) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("Registration completed successfully");
    helper.setText("Welcome to GamesApp! This is your ID: " + id);

    javaMailSender.send(message);
  }

  // MAIL NOTIFICA AGGIUNTA STUDENTE A TEAM
  public void sendStudentAddedToTeamEmail(String email, String teamName) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("You have been added to a team!");
    helper.setText("You have been added to the team: " + teamName + ". Start collaborating now!");

    javaMailSender.send(message);
  }

  // MAIL NOTIFICA RIMOZIONE STUDENTE DA TEAM
  public void sendStudentRemovedFromTeamEmail(String email, String teamName) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("You have been removed from a team");
    helper.setText("You have been removed from the team: " + teamName + ". Contact your administrator if you think this is a mistake.");

    javaMailSender.send(message);
  }

  // MAIL NOTIFICA CANCELLAZIONE TEAM
  public void sendTeamDeletedEmail(String email, String teamName) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("Your team has been deleted");
    helper.setText("The team " + teamName + " has been deleted. Contact your administrator if you have any questions.");

    javaMailSender.send(message);
  }
}