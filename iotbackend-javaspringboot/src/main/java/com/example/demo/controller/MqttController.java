package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.MqttManager;
import com.example.demo.entity.SensorData;
import com.example.demo.repository.SensorDataRepository;
import com.example.demo.service.SensorDataService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class MqttController {

	@Autowired
	private SensorDataRepository sensorDataRepository;

	@Autowired
	private SensorDataService sensorDataService;

	@Autowired
	public MqttController(MqttManager mqttManager) {
	}

//	@PostMapping("/led/{status}")
//	public String toggleLed(@PathVariable String status) {
//		if (status.equals("on")) {
//			mqttManager.publishMessage("esp8266/led", "on");
//			return "LED turned on";
//		} else if (status.equals("off")) {
//			mqttManager.publishMessage("esp8266/led", "off");
//			return "LED turned off";
//		} else {
//			return "Invalid status";
//		}
//	}

    @GetMapping("/sensordata")
	public ResponseEntity<List<SensorData>> getSensorData() {
		List<SensorData> sensorDataList = sensorDataRepository.findAll();
		if (!sensorDataList.isEmpty()) {
			return new ResponseEntity<>(sensorDataList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/filter-and-page")
	public ResponseEntity<Page<SensorData>> getFilteredAndPagedSensorData(
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

	    Page<SensorData> sensorDataPage;
	    if (startDate != null && endDate != null) {
	        sensorDataPage = sensorDataService.findByMeasurementTimeBetween(startDate, endDate, pageable);
	    } else {
	        sensorDataPage = sensorDataService.findAll(pageable);
	    }

	    return ResponseEntity.ok(sensorDataPage);
	}
}
