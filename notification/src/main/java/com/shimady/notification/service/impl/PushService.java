package com.shimady.notification.service.impl;

import com.shimady.notification.model.PushMessage;
import com.shimady.notification.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushService implements SenderService<PushMessage> {

    private final SimpMessagingTemplate template;

    @Override
    public void send(PushMessage message) {
        log.info("Sending push notification to the browser, username: {}", message.getUsername());

        String text = """
                  Welcome, %s,
                  You have %d tasks To Do.
                """.formatted(message.getUsername(), message.getTasksToDo());

        template.convertAndSend("/push/" + message.getUsername(), text);
    }
}