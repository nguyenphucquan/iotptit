package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Actions;

public interface ActionsRepository extends JpaRepository<Actions, Long>{

	Page<Actions> findByMeasurementTimeBetween(Date startDate, Date endDate, Pageable pageable);
	
	List<Actions> findByMeasurementTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<Actions> findByMeasurementTimeBetween(Date startDate, Date endDate);
	

}
