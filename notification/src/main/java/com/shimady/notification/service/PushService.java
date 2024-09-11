package com.shimady.notification.service;

import com.shimady.notification.model.PushMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushService {

    private final SimpMessagingTemplate template;

    public void sendPush(PushMessage message) {
        log.info("Sending push notification to the browser, username {}", message.getUsername());

        String text = """
                  Welcome, %s,
                  You have %d tasks To Do.
                """.formatted(message.getUsername(), message.getTasksToDo());

        template.convertAndSend("/push/" + message.getUsername(), text);
    }
}