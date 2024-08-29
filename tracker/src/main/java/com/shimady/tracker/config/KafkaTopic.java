package com.shimady.tracker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    @Bean
    public NewTopic emailTopic() {
        return TopicBuilder.name("emailTopic")
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic smsTopic() {
        return TopicBuilder.name("smsTopic")
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pushTopic() {
        return TopicBuilder.name("pushTopic")
                .partitions(4)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic reminderTopic() {
        return TopicBuilder.name("reminderTopic")
                .partitions(4)
                .replicas(1)
                .build();
    }
}
