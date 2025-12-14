package com.shimady.auth.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicProperties {
    private TopicInfo email = new TopicInfo();
    private TopicInfo sms = new TopicInfo();
    private TopicInfo push = new TopicInfo();

    @Setter
    @Getter
    public static class TopicInfo {
        private String name = "default-topic";
        private int partitions = 1;
        private short replicationFactor = 1;
    }
}
