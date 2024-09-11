package com.shimady.notification.service;

import com.shimady.notification.model.SMSMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSService {
    public void sendSMS(SMSMessage message) {
        log.info("Sending sms message to: {}", message.getPhoneNumber());

        // there should be the sms message sending,
        // but I didn't find a good API for sms sending
        // (I didn't like Twilio)
        // and it also requires sender number
        // (I don't have special number for that)
    }
}
