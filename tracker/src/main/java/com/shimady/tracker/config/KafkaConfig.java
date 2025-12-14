package com.shimady.tracker.config;

import com.shimady.tracker.config.props.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final KafkaTopicProperties kafkaProperties;

    @Bean
    public NewTopic reminderTopic() {
        KafkaTopicProperties.TopicInfo reminder = kafkaProperties.getReminder();
        return TopicBuilder
                .name(reminder.getName())
                .partitions(reminder.getPartitions())
                .replicas(reminder.getReplicationFactor())
                .build();
    }
}
