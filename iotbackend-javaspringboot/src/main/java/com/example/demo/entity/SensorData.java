package com.example.demo.entity;


import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature")
    private Float temperature;

    @Column(name = "humidity")
    private Float humidity;

    @Column(name = "light")
    private Float light;
    
    @Column(name = "gas")
    private Float gas;
    


	public Float getGas() {
		return gas;
	}

	public void setGas(Float gas) {
		this.gas = gas;
	}

//	public void setGas(float dataValue) {
//		// TODO Auto-generated method stub
//		
//	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "measurement_time")
    private Date measurementTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public Float getHumidity() {
		return humidity;
	}

	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}

	public Float getLight() {
		return light;
	}

	public void setLight(Float light) {
		this.light = light;
	}

	public Date getMeasurementTime() {
		return measurementTime;
	}

	public void setMeasurementTime(Date measurementTime) {
		this.measurementTime = measurementTime;
	}


    
}
