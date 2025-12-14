package com.shimady.auth.config;

import com.shimady.auth.config.props.KafkaTopicProperties;
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
    public NewTopic emailTopic() {
        KafkaTopicProperties.TopicInfo email = kafkaProperties.getEmail();
        return TopicBuilder
                .name(email.getName())
                .partitions(email.getPartitions())
                .replicas(email.getReplicationFactor())
                .build();
    }

    @Bean
    public NewTopic smsTopic() {
        KafkaTopicProperties.TopicInfo sms = kafkaProperties.getSms();
        return TopicBuilder
                .name(sms.getName())
                .partitions(sms.getPartitions())
                .replicas(sms.getReplicationFactor())
                .build();
    }

    @Bean
    public NewTopic pushTopic() {
        KafkaTopicProperties.TopicInfo push = kafkaProperties.getPush();
        return TopicBuilder
                .name(push.getName())
                .partitions(push.getPartitions())
                .replicas(push.getReplicationFactor())
                .build();
    }
}
