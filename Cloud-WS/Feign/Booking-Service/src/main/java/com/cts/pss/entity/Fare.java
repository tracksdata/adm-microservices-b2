package com.cts.pss.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Fare {

	@Id
	@GeneratedValue
	private int fareId;
	private String currency;
	private double ticketFare;

	public int getFareId() {
		return fareId;
	}

	public void setFareId(int fareId) {
		this.fareId = fareId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getTicketFare() {
		return ticketFare;
	}

	public void setTicketFare(double ticketFare) {
		this.ticketFare = ticketFare;
	}

	@Override
	public String toString() {
		return "Fare [fareId=" + fareId + ", currency=" + currency + ", ticketFare=" + ticketFare + "]";
	}

}
