package com.dbiz.app.integrationservice.helper;

import com.dbiz.app.integrationservice.domain.KafkaSendEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendKafkaAfterCommit(KafkaSendEvent event) {
        kafkaTemplate.send(event.getTopic(), event.getData());
        log.info("🔥 Kafka message sent after transaction commit to topic ");
    }
}
