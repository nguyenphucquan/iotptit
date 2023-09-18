package com.example.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Actions;
import com.example.demo.repository.ActionsRepository;

@Service
public class ActionsService {
		
	@Autowired
	private ActionsRepository actionsRepository;
	   public Page<Actions> findAll(Pageable pageable) {
	        return actionsRepository.findAll(pageable);
	    }

	    public Page<Actions> findByMeasurementTimeBetween(Date startDate, Date endDate, Pageable pageable) {
	        return actionsRepository.findByMeasurementTimeBetween(startDate, endDate, pageable);
	    }
}
