package com.shimady.notification.service;

public interface MailSenderService {
    String FROM_EMAIL = "noreply@test.local";

    void send(String email, String subject, String text);
}
