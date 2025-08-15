package com.dbiz.app.integrationservice.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class KafkaSendEvent extends ApplicationEvent {
    private final String topic;
    private final Object data;

    public KafkaSendEvent(Object source, String topic, Object data) {
        super(source);
        this.topic = topic;
        this.data = data;
    }
}
