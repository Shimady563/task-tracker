package com.shimady.notification.service;

import com.shimady.notification.model.EmailMessage;
import com.shimady.notification.model.ReminderMessage;
import com.shimady.notification.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailSender mailSender;

    public void sendEmail(EmailMessage message) {
        log.info("Sending email to: {}", message.getEmail());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getEmail());
        mailMessage.setSubject("Welcome to the Notification Service");
        mailMessage.setText("Hello " + message.getUsername() + ",\n\nWelcome to the Task Tracker!");

        mailSender.send(new SimpleMailMessage());
    }

    public void sendReminderEmail(ReminderMessage message) {
        log.info("Sending reminder email to: {}", message.getEmail());

        StringBuilder text = new StringBuilder();
        text.append("""
                  Hello  %s,
                  "This is a reminder of your tasks for today.
                """.formatted(message.getUsername()));

        for (TaskStatus status : message.getTasksByStatus().keySet()) {
            text.append(message.getTasksByStatus().get(status))
                    .append(" ")
                    .append(status)
                    .append(" tasks, ");
        }

        text.deleteCharAt(text.length() - 1)
                .deleteCharAt(text.length() - 1)
                .append("due today.");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getEmail());
        mailMessage.setSubject("Reminder: Task is due soon");
        mailMessage.setText(text.toString());

        mailSender.send(new SimpleMailMessage());
    }
}
