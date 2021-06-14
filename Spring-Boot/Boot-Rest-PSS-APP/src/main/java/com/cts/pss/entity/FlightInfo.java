package com.cts.pss.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
@Entity
public class FlightInfo {
	
	@Id
	@GeneratedValue
	private int flightInfoid;
	private String flightNumber;
	private String flightType;
	private int seatCapacity;
	
	@OneToOne
	@JoinColumn(name="airlineId")
	private AirlineInfo airlineInfo;

	public int getFlightInfoid() {
		return flightInfoid;
	}

	public void setFlightInfoid(int flightInfoid) {
		this.flightInfoid = flightInfoid;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getFlightType() {
		return flightType;
	}

	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}

	public int getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(int seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	public AirlineInfo getAirlineInfo() {
		return airlineInfo;
	}

	public void setAirlineInfo(AirlineInfo airlineInfo) {
		this.airlineInfo = airlineInfo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
