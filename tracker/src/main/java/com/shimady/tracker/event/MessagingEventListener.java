package com.shimady.tracker.event;

import com.shimady.tracker.exception.ResourceNotFoundException;
import com.shimady.tracker.model.TaskStatus;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.messaging.EmailMessage;
import com.shimady.tracker.model.messaging.PushMessage;
import com.shimady.tracker.model.messaging.SMSMessage;
import com.shimady.tracker.repository.TaskRepository;
import com.shimady.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingEventListener {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    @TransactionalEventListener(value = UserCreationEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void onUserCreationEvent(UserCreationEvent event) {
        log.info("Received user creation event, user id {}", event.getUserId());

        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + event.getUserId() + " not found"));
        EmailMessage emailMessage = new EmailMessage(user.getUsername(), user.getEmail());
        SMSMessage smsMessage = new SMSMessage(user.getUsername(), user.getPhoneNumber());

        kafkaTemplate.send("emailTopic", emailMessage);
        kafkaTemplate.send("smsTopic", smsMessage);
    }

    @Async
    @EventListener(value = AuthenticationSuccessEvent.class)
    public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();

        log.info("Received authentication success event, email {}", user.getEmail());

        long tasksToDo = taskRepository.countByUserAndStatus(user, TaskStatus.TODO);
        PushMessage pushMessage = new PushMessage(user.getUsername(), tasksToDo);

        kafkaTemplate.send("pushTopic", pushMessage);
    }
}
