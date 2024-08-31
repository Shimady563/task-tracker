package com.shimady.tracker.event;

import com.shimady.tracker.exception.ResourceNotFoundException;
import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.messaging.EmailMessage;
import com.shimady.tracker.model.messaging.PushMessage;
import com.shimady.tracker.model.messaging.SMSMessage;
import com.shimady.tracker.repository.TaskRepository;
import com.shimady.tracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class MessagingEventListenerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private MessagingEventListener messagingEventListener;

    @Test
    public void shouldSendMessagesOnUserCreationEvent() {
        User user = new User();
        user.setId(1L);

        given(userRepository.findById(eq(user.getId()))).willReturn(Optional.of(user));

        messagingEventListener.onUserCreationEvent(new UserCreationEvent(user.getId()));

        then(kafkaTemplate).should().send(eq("emailTopic"), any(EmailMessage.class));
        then(kafkaTemplate).should().send(eq("smsTopic"), any(SMSMessage.class));
    }

    @Test
    public void shouldThrowExceptionOnUserCreationEventWhenNoUserWasFound() {
        Long id = 1L;

        given(userRepository.findById(eq(id))).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> messagingEventListener.onUserCreationEvent(new UserCreationEvent(id)));
    }

    @Test
    public void shouldSendPushMessageOnAuthenticationSuccessEvent() {
        Task task = new Task();

        User user = new User();
        user.setEmail("Email");
        user.setTasks(List.of(task));

        given(taskRepository.countByUserAndStatus(eq(user), eq(task.getStatus())))
                .willReturn(1L);

        messagingEventListener.onAuthenticationSuccessEvent(
                new AuthenticationSuccessEvent(
                        new UsernamePasswordAuthenticationToken(user, null)
                )
        );

        then(kafkaTemplate).should().send(eq("pushTopic"), any(PushMessage.class));
    }
}
