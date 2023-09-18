package com.example.demo.config;

import java.sql.Date;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.SensorData;
import com.example.demo.repository.SensorDataRepository;

@Component
public class MqttManager {

    private final IMqttClient mqttClient;
    private final SensorDataRepository sensorDataRepository;
    private SensorData currentSensorData;

    @Autowired
    public MqttManager(IMqttClient mqttClient, SensorDataRepository sensorDataRepository) {
        this.mqttClient = mqttClient;
        this.sensorDataRepository = sensorDataRepository;
        subscribeToTopics();
    }

    public void publishMessage(String topic, String message) {
        try {
            mqttClient.publish(topic, message.getBytes(), 0, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subscribeToTopics() {
        try {
            mqttClient.subscribe("dht/temperature", (topic, message) -> {
                handleSensorData("temperature", message);
            });

            mqttClient.subscribe("dht/humidity", (topic, message) -> {
                handleSensorData("humidity", message);
            });

            mqttClient.subscribe("light/lux", (topic, message) -> {
                handleSensorData("light", message);
            });
            mqttClient.subscribe("gas/sensor", (topic, message) -> {
                handleSensorData("gas", message);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSensorData(String dataType, MqttMessage message) {
        String payload = new String(message.getPayload());
        float dataValue = Float.parseFloat(payload);
       // System.out.println("Received " + dataType + ": " + dataValue);

        if (currentSensorData == null) {
            currentSensorData = new SensorData();
            currentSensorData.setMeasurementTime(new Date(System.currentTimeMillis()));
        }

        if ("temperature".equals(dataType)) {
            currentSensorData.setTemperature(dataValue);
        } else if ("humidity".equals(dataType)) {
            currentSensorData.setHumidity(dataValue);
        } else if ("light".equals(dataType)) {
            currentSensorData.setLight(dataValue);
        } 
        else if ("gas".equals(dataType)) {
            currentSensorData.setGas(dataValue);
        }

        if (shouldSaveData()) {
            saveSensorDataToDatabase(currentSensorData);
            currentSensorData = null; 
        }
    }

    private boolean shouldSaveData() {

        return currentSensorData.getTemperature() != null &&
               currentSensorData.getHumidity() != null &&
               currentSensorData.getLight() != null ;
        	//   currentSensorData.getDust() != null;
    }

    private void saveSensorDataToDatabase(SensorData sensorData) {

        sensorDataRepository.save(sensorData);
    }
}
