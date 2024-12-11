package com.zam.security.services.impl;

import com.zam.security.payload.response.MessageResponse;
import com.zam.security.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${email.sender}")
    private String userEmail;

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public MessageResponse sendEmail(String toUser, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(userEmail);
        simpleMailMessage.setTo(toUser);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
        return new MessageResponse("Mail sent successfully");
    }

    @Override
    public MessageResponse sendEmailWithFile(String toUser, String subject, String message, File file) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setFrom(userEmail);
        mimeMessageHelper.setTo(toUser);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message);

        String fileName = file.getName();
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";  // Agrega la extensión si no está presente
        }
        mimeMessageHelper.addAttachment(fileName, file);
        mailSender.send(mimeMessage);
        return new MessageResponse("Mail sent successfully");
    }

}
