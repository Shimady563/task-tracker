package com.shimady.notification.listener;

import com.shimady.notification.model.EmailMessage;
import com.shimady.notification.model.PushMessage;
import com.shimady.notification.model.ReminderMessage;
import com.shimady.notification.model.SMSMessage;
import com.shimady.notification.service.SenderService;
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

    private final SenderService<EmailMessage> emailService;
    private final SenderService<ReminderMessage> reminderService;
    private final SenderService<SMSMessage> smsService;
    private final SenderService<PushMessage> pushService;

    @KafkaListener(topics = "${kafka.topics.email.name}")
    public void listenEmailMessage(@Payload @Valid EmailMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received email message from partition {}, email: {}",
                metadata.partition(),
                message.getEmail());

        emailService.send(message);
    }

    @KafkaListener(topics = "${kafka.topics.sms.name}")
    public void listenSMSMessage(@Payload @Valid SMSMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received sms message from partition {}, username: {}",
                metadata.partition(),
                message.getUsername());

        smsService.send(message);
    }

    @KafkaListener(topics = "${kafka.topics.push.name}")
    public void onPushMessage(@Payload @Valid PushMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received push message from partition {}, username {}",
                metadata.partition(),
                message.getUsername());

        pushService.send(message);
    }

    @KafkaListener(topics = "${kafka.topics.reminder.name}")
    public void onReminderMessage(@Payload @Valid ReminderMessage message, ConsumerRecordMetadata metadata) {
        log.info("Received reminder message from partition {}, username {}",
                metadata.partition(),
                message.getUsername());

        reminderService.send(message);
    }
}
