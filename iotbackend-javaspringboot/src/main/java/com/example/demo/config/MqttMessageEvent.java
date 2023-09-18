package com.example.demo.config;

import org.springframework.context.ApplicationEvent;

public class MqttMessageEvent extends ApplicationEvent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String topic;
    private String message;

    public MqttMessageEvent(Object source, String topic, String message) {
        super(source);
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }
}
