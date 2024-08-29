package com.shimady.tracker.schedule;

import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.TaskStatus;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.messaging.ReminderMessage;
import com.shimady.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingScheduler {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(cron = "${scheduling.messaging.cron}")
    public void scheduleReminderMessages() {
        log.info("Sending scheduled reminder messages to all users");

        List<User> users = userRepository.findAllFetchTasks();

        for (User user : users) {
            Map<TaskStatus, Integer> tasksByStatus = user.getTasks().stream()
                    .collect(Collectors.groupingBy(Task::getStatus, Collectors.summingInt(task -> 1)));

            log.info(String.valueOf(tasksByStatus));

            ReminderMessage reminderMessage = new ReminderMessage(user.getUsername(), user.getEmail(), tasksByStatus);

            kafkaTemplate.send("reminderTopic", reminderMessage);
        }
    }
}
