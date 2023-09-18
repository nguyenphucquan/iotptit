package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@ Entity
public class Actions {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "action")
	    private String action;

	    @Column(name = "name")
	    private String name;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "measurement_time")
	    private Date measurementTime;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}
		public Date getMeasurementTime() {
			return measurementTime;
		}

		public void setMeasurementTime(Date measurementTime) {
			this.measurementTime = measurementTime;
		}

} 
