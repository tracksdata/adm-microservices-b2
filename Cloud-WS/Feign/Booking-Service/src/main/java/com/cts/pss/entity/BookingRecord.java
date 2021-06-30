package com.cts.pss.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class BookingRecord {

	@Id
	@GeneratedValue
	private int bookingId;
	private LocalDateTime bookingDate;
	private String flightNumber;
	private String origin;
	private String destination;
	private LocalDate flightDate;
	private LocalTime flightTime;
	private double fare;
	private String status;
	private int travellers;

	@OneToOne
	@JoinColumn(name = "flightInfoid")
	private FlightInfo flightInfo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "passengerId")
	private Passenger passenger;
	
	
	public BookingRecord() {
		// TODO Auto-generated constructor stub
	}
	
	public BookingRecord(LocalDateTime bookingDate, String flightNumber, String origin, String destination,
			LocalDate flightDate, LocalTime flightTime, String status, int travellers, FlightInfo flightInfo,
			Passenger passenger) {
		super();
		this.bookingDate = bookingDate;
		this.flightNumber = flightNumber;
		this.origin = origin;
		this.destination = destination;
		this.flightDate = flightDate;
		this.flightTime = flightTime;
		this.status = status;
		this.travellers = travellers;
		this.flightInfo = flightInfo;
		this.passenger = passenger;
	}



	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDateTime getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
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

	public LocalTime getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(LocalTime flightTime) {
		this.flightTime = flightTime;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTravellers() {
		return travellers;
	}

	public void setTravellers(int travellers) {
		this.travellers = travellers;
	}

	public FlightInfo getFlightInfo() {
		return flightInfo;
	}

	public void setFlightInfo(FlightInfo flightInfo) {
		this.flightInfo = flightInfo;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

}
