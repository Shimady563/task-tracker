package com.shimady.notification.service.impl;

import com.shimady.notification.model.EmailMessage;
import com.shimady.notification.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements SenderService<EmailMessage> {

    private final MailSenderServiceImpl mailSenderService;

    @Override
    public void send(EmailMessage message) {
        mailSenderService.send(message.getEmail(), "Hello " + message.getUsername() + ",\n\nWelcome to the Task Tracker!",
                "Welcome to the Notification Service");
    }
}
