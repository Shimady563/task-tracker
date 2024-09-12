package com.shimady.notification.listener;

import com.shimady.notification.model.EmailMessage;
import com.shimady.notification.model.PushMessage;
import com.shimady.notification.model.ReminderMessage;
import com.shimady.notification.model.SMSMessage;
import com.shimady.notification.service.EmailService;
import com.shimady.notification.service.PushService;
import com.shimady.notification.service.SMSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final EmailService emailService;
    private final SMSService smsService;
    private final PushService pushService;

    @KafkaListener(topics = "emailTopic", groupId = "notification")
    public void listenEmailMessage(@Payload @Valid EmailMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received email message from partition {}, email: {}",
                metadata.partition(),
                message.getEmail());

        emailService.sendEmail(message);
    }

    @KafkaListener(topics = "smsTopic", groupId = "notification")
    public void listenSMSMessage(@Payload @Valid SMSMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received sms message from partition {}, username: {}",
                metadata.partition(),
                message.getUsername());

        smsService.sendSMS(message);
    }

    @KafkaListener(topics = "pushTopic", groupId = "notification")
    public void onPushMessage(@Payload @Valid PushMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received push message from partition {}, username {}",
                metadata.partition(),
                message.getUsername());

        pushService.sendPush(message);
    }

    @KafkaListener(topics = "reminderTopic", groupId = "notification")
    public void onReminderMessage(@Payload @Valid ReminderMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received reminder message from partition {}, username {}",
                metadata.partition(),
                message.getUsername());

        emailService.sendReminderEmail(message);
    }
}
