package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.SensorData;

public interface SensorDataRepository extends JpaRepository<SensorData, Long>{

	List<SensorData> findByMeasurementTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<SensorData> findByMeasurementTimeBetween(Date startDate, Date endDate);
	
	Page<SensorData> findByMeasurementTimeBetween(Date startDate, Date endDate, Pageable pageable);

}
