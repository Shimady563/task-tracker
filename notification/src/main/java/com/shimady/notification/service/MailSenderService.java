package com.shimady.notification.service;

public interface MailSenderService {

    void send(String email, String subject, String text);
}
