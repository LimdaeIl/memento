package com.natural.memento.user.infrastructure.email;


import jakarta.mail.MessagingException;

public interface EmailSender {
    void sendCode(String to, String subject, String content);
}