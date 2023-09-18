package com.example.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.SensorData;
import com.example.demo.repository.SensorDataRepository;

@Service
public class SensorDataService {
    
    @Autowired
    private SensorDataRepository sensorDataRepository;

    public Page<SensorData> findAll(Pageable pageable) {
        return sensorDataRepository.findAll(pageable);
    }

    public Page<SensorData> findByMeasurementTimeBetween(Date startDate, Date endDate, Pageable pageable) {
        return sensorDataRepository.findByMeasurementTimeBetween(startDate, endDate, pageable);
    }


//    public SensorData saveSensorData(SensorData sensorData) {
//        return sensorDataRepository.save(sensorData);
//    }
}
