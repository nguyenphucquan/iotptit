package com.example.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.MqttManager;
import com.example.demo.entity.Actions;
import com.example.demo.repository.ActionsRepository;
import com.example.demo.service.ActionsService;

@RestController
@CrossOrigin
@RequestMapping("/api/l")
public class LedFanController {
	
	private final MqttManager mqttManager;
	
	@Autowired
	private ActionsRepository actionsRepository;
	
	@Autowired
	private ActionsService actionsService;
	
	public LedFanController(MqttManager mqttManager) {
		this.mqttManager = mqttManager;
	}
	
	
	@PostMapping("/led")
	public String toggleLed(@RequestBody Actions actions) {
	    String name = actions.getName();
	    String status = actions.getAction();

	    if (status.equals("on")) {
	        mqttManager.publishMessage("esp8266/" + name, "on");
	    } else if (status.equals("off")) {
	        mqttManager.publishMessage("esp8266/" + name, "off");
	    } else {
	        return "Invalid status";
	    }
	    actions.setMeasurementTime(new Date()); 
	    actionsRepository.save(actions);

	    return "LED state updated";
	}
	@PostMapping("/fan")
	public String toggleFan(@RequestBody Actions actions) {
	    String name = actions.getName();
	    String status = actions.getAction();

	    if (status.equals("on")) {
	        mqttManager.publishMessage("esp8266/" + name, "on");
	    } else if (status.equals("off")) {
	        mqttManager.publishMessage("esp8266/" + name, "off");
	    } else {
	        return "Invalid status";
	    }
	    actions.setMeasurementTime(new Date()); 
	    actionsRepository.save(actions);

	    return "Fan state updated";
	}
	
	@PostMapping("/ac")
	public String toggleac(@RequestBody Actions actions) {
	    String name = actions.getName();
	    String status = actions.getAction();

	    if (status.equals("on")) {
	        mqttManager.publishMessage("esp8266/" + name, "on");
	    } else if (status.equals("off")) {
	        mqttManager.publishMessage("esp8266/" + name, "off");
	    } else {
	        return "Invalid status";
	    }
	    actions.setMeasurementTime(new Date()); 
	    actionsRepository.save(actions);

	    return "Fan state updated";
	}
	@GetMapping("/filter-and-page")
	public ResponseEntity<Page<Actions>> getFilteredAndPagedActions(
	    @RequestParam(value = "page", defaultValue = "0") int page,
	    @RequestParam(value = "size", defaultValue = "15") int size,
	    @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date startDate,
	    @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date endDate,
	    @RequestParam(name = "sort", required = false) String sort) {

	    Sort.Direction direction = Sort.Direction.ASC; // Mặc định sắp xếp tăng dần
	    Pageable pageable = PageRequest.of(page, size);

	    if (sort != null) {
	        String[] sortParams = sort.split(",");
	        if (sortParams.length == 2) {
	            String sortField = sortParams[0];
	            String sortOrder = sortParams[1];
	            if ("desc".equalsIgnoreCase(sortOrder)) {
	                direction = Sort.Direction.DESC;
	            }
	            pageable = PageRequest.of(page, size, direction, sortField);
	        }
	    }

	    Page<Actions> actionsDataPage;
	
		if (startDate != null && endDate != null) {
	        actionsDataPage = actionsService.findByMeasurementTimeBetween(startDate, endDate, pageable);
	    } else {
	        actionsDataPage = actionsService.findAll(pageable);
	    }

	    return ResponseEntity.ok(actionsDataPage);
	}
	
}
