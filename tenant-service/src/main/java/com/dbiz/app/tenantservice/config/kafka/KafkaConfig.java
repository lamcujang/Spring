package com.dbiz.app.tenantservice.config.kafka;

import lombok.experimental.NonFinal;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.hostname}")
    private String hostName;

    @Value("${kafka.port}")
    private String port;

    @NonFinal
    @Value("${kafka.group-order-id}")
    String groupId;

    // Producer Factory Configuration
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hostName + ":" + port);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        // Optional Producer settings for enhanced reliability
        configMap.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensure all replicas acknowledge
        configMap.put(ProducerConfig.RETRIES_CONFIG, 3);  // Retry if sending fails
        configMap.put(ProducerConfig.LINGER_MS_CONFIG, 1); // Batch messages for performance
        configMap.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 2000); // Request timeout in milliseconds
        configMap.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 2001); // Delivery timeout in milliseconds
        configMap.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    // Producer Factory for AuditLogRequest
    @Bean
    public ProducerFactory<String, AuditLogRequest> auditLogProducerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hostName + ":" + port);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "org.common.dbiz.request.tenantRequest");
        configMap.put(ProducerConfig.ACKS_CONFIG, "all");
        configMap.put(ProducerConfig.RETRIES_CONFIG, 10);
        configMap.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configMap.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);
        configMap.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 5001);
        configMap.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, AuditLogRequest> auditLogKafkaTemplate() {
        return new KafkaTemplate<>(auditLogProducerFactory());
    }

    // Consumer Factory Configuration
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hostName + ":" + port);
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        // Optional Consumer settings for stability
        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // Start from beginning if no offset
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Handle commits manually
        configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10); // Limit records per poll to avoid long processing
        configMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000); // Heartbeat session timeout
        configMap.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5000); // Frequency of heartbeats
        configMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000); // Max interval for message processing

        return new DefaultKafkaConsumerFactory<>(configMap);
    }

    // Consumer Factory for AuditLogRequest
    @Bean
    public ConsumerFactory<String, AuditLogRequest> auditLogConsumerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hostName + ":" + port);
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "org.common.dbiz.request.tenantRequest");
        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        configMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        configMap.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5000);
        configMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        return new DefaultKafkaConsumerFactory<>(configMap);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3); // Number of threads for concurrent processing
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL); // For manual commits
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuditLogRequest> auditLogKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AuditLogRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(auditLogConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}

/////////////////////

//    @Value("${kafka.dlq.retry.max-attempts:3}")
//    private int maxRetryAttempts;
//
//    @Value("${kafka.dlq.retry.delay-ms:5000}")
//    private long retryDelayMs;

//    // Consumer Factory for AuditLogRequest DLQ
//    @Bean
//    public ConsumerFactory<String, AuditLogRequest> auditLogDlqConsumerFactory() {
//        Map<String, Object> configMap = new HashMap<>();
//        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hostName + ":" + port);
//        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + "-dlq");
//        configMap.put(JsonDeserializer.TRUSTED_PACKAGES, "org.common.dbiz.request.tenantRequest");
//        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        configMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
//        configMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
//        configMap.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5000);
//        configMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
//        return new DefaultKafkaConsumerFactory<>(configMap);
//    }

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, AuditLogRequest> auditLogDlqKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, AuditLogRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(auditLogDlqConsumerFactory());
//        factory.setConcurrency(1);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }

//    // Getter để sử dụng trong AuditLogConsumerImpl
//    public int getMaxRetryAttempts() {
//        return maxRetryAttempts;
//    }
//
//    public long getRetryDelayMs() {
//        return retryDelayMs;
//    }
