package com.banking.bankingapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name("payments-topic").build();
    }

    @Bean
    public NewTopic transferTopic() {
        return TopicBuilder.name("transfer-topic").build();
    }

    @Bean
    public NewTopic debitTopic() {
        return TopicBuilder.name("debit-topic").build();
    }

    @Bean
    public NewTopic creditTopic() {
        return TopicBuilder.name("credit-topic").build();
    }
}
