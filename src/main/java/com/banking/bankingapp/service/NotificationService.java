package com.banking.bankingapp.service;

import com.banking.bankingapp.model.dto.request.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final KafkaTemplate<String, Notification> kafkaTemplate;

    @Autowired
    public NotificationService(KafkaTemplate<String, Notification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(final String topic, final Notification notification) {
        this.kafkaTemplate.send(topic, notification);
    }
}
