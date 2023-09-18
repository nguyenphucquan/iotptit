package com.example.demo.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Bean
    public IMqttClient mqttClient(
            @Value("${mqtt.broker.host}") String brokerHost,
            @Value("${mqtt.broker.port}") int brokerPort,
            @Value("${mqtt.client.id}") String clientId) {
        try {
            String brokerUrl = "tcp://" + brokerHost + ":" + brokerPort;
            IMqttClient mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            mqttClient.connect(options);

            if (mqttClient.isConnected()) {
                System.out.println("Connected to MQTT broker: " + brokerUrl);
            } else {
                System.out.println("Failed to connect to MQTT broker.");
            }

            return mqttClient;
        } catch (Exception e) {
            throw new RuntimeException("Error creating MQTT client", e);
        }
    }
}

