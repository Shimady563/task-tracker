package com.shimady.notification.service.impl;

import com.shimady.notification.model.ReminderMessage;
import com.shimady.notification.model.TaskStatus;
import com.shimady.notification.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderService implements SenderService<ReminderMessage> {

    private final MailSenderServiceImpl mailSenderService;

    @Override
    public void send(ReminderMessage message) {
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

        mailSenderService.send(message.getEmail(), text.toString(), "Reminder: Task is due soon");
    }
}
