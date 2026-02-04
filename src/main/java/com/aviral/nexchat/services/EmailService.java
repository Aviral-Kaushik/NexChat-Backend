package com.aviral.nexchat.services;

public interface EmailService {
    void sendHtmlMail(String to, String subject, String htmlBody);
}
