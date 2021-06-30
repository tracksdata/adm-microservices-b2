package com.cts.pss.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Flight {
	@Id
	@GeneratedValue
	private int id;
	private String origin;
	private String destination;
	private LocalDate flightDate;
	private String flightNumber;
	private String duration;
	private LocalTime flightTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "inventoryId")
	private Inventory inventory;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "flightInfoid")
	private FlightInfo flightInfo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fareId")
	private Fare fare;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public LocalTime getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(LocalTime flightTime) {
		this.flightTime = flightTime;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public FlightInfo getFlightInfo() {
		return flightInfo;
	}

	public void setFlightInfo(FlightInfo flightInfo) {
		this.flightInfo = flightInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public LocalDate getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(LocalDate flightDate) {
		this.flightDate = flightDate;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Fare getFare() {
		return fare;
	}

	public void setFare(Fare fare) {
		this.fare = fare;
	}

}
