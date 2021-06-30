package com.cts.pss.model;

import java.time.LocalDate;
import java.util.List;

import com.cts.pss.entity.CoPassenger;
import com.cts.pss.entity.Passenger;

public class SearchQuery {

	private String flightNumber;
	private String origin;
	private String destination;
	private LocalDate flightDate;
	private int travellers;
	
	private int bookingId;
	
	private List<CoPassenger> coPassengers;
	
	

	private Passenger passengers;
	
	

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public List<CoPassenger> getCoPassengers() {
		return coPassengers;
	}

	public void setCoPassengers(List<CoPassenger> coPassengers) {
		this.coPassengers = coPassengers;
	}

	public Passenger getPassengers() {
		return passengers;
	}

	public void setPassengers(Passenger passengers) {
		this.passengers = passengers;
	}

	public int getTravellers() {
		return travellers;
	}

	public void setTravellers(int travellers) {
		this.travellers = travellers;
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

}
