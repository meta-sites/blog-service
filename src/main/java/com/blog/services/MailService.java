package com.blog.services;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    void sendEmailWithTemplate(String to, String subject, String templateName, Context context) throws MessagingException, UnsupportedEncodingException;

    String getTemplate(String templateName, Context context);
}