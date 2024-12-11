package com.zam.security.services;

import com.zam.security.payload.response.MessageResponse;
import jakarta.mail.MessagingException;

import java.io.File;

public interface EmailService {

    public MessageResponse sendEmail(String toUser, String subject, String message);
    public MessageResponse sendEmailWithFile(String toUser, String subject, String message, File file) throws MessagingException;

}
