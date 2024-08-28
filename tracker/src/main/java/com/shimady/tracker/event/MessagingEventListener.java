package com.shimady.tracker.event;

import com.shimady.tracker.exception.ResourceNotFoundException;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.dto.messaging.EmailMessage;
import com.shimady.tracker.model.dto.messaging.SMSMessage;
import com.shimady.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingEventListener {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    @TransactionalEventListener(UserCreationEvent.class)
    public void onUserCreationEvent(UserCreationEvent event) {
        log.info("Received authorization event, user id {}", event.getUserId());

        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + event.getUserId() + " not found"));

        EmailMessage emailMessage = new EmailMessage(user.getUsername(), user.getEmail());
        SMSMessage smsMessage = new SMSMessage(user.getUsername(), user.getPhoneNumber());

        kafkaTemplate.send("emailTopic", emailMessage);
        kafkaTemplate.send("smsTopic", smsMessage);
    }
}
