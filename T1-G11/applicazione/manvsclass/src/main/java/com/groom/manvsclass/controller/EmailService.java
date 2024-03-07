package com.groom.manvsclass.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Autowired
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  // MAIL RESET PASSWORD
  public void sendPasswordResetEmail(String email, String resetToken) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("Richiesta di reset password");
    helper.setText("Per favore, copia il seguente token per resettare la password: " + resetToken);

    javaMailSender.send(message);
  }
    // MAIL DI INVITO
  public void sendInvitationToken(String email, String invitationToken) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("Invio token di invito");
    helper.setText("Per favore, copia il seguente token presso l'end-point \\login_with_invitation per registrarti come amministratore: " + invitationToken);

    javaMailSender.send(message);
  }
  
}