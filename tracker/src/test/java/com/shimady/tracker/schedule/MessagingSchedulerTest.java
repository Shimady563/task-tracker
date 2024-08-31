package com.shimady.tracker.schedule;

import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.messaging.ReminderMessage;
import com.shimady.tracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MessagingSchedulerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private MessagingScheduler messagingScheduler;

    @Test
    public void shouldScheduleReminderMessages() {
        Task task1 = new Task();
        Task task2 = new Task();
        User user1 = new User();
        user1.setTasks(List.of(task1));
        User user2 = new User();
        user2.setTasks(List.of(task2));

        given(userRepository.findAllFetchTasks()).willReturn(List.of(user1, user2));

        messagingScheduler.scheduleReminderMessages();

        then(kafkaTemplate)
                .should(times(2))
                .send(eq("reminderTopic"), any(ReminderMessage.class));
    }
}
