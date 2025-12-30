package com.shimady.notification.service.impl;

import com.shimady.notification.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final MailSender mailSender;

    @Override
    public void send(String email, String subject, String text) {
        log.info("Sending email to: {}, with subject: {}", email, subject);
        log.debug("Message body: {}", text);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom(FROM_EMAIL);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }
}
