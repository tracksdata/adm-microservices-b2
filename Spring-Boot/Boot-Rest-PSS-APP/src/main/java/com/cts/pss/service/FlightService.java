package com.cts.pss.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;
import com.cts.pss.model.SearchQuery;

public interface FlightService {

	Flight findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(String flightNumber, String origin,
			String destination, LocalDate flightDate);

	// Find Scheduled flights by Origin,Destination and FlightDate
	List<Flight> findFlights(SearchQuery query);

	List<Flight> listScheduledFlights(String origin, String destination, LocalDate flightDate, int travellers);

	// Find Flight By Flight Schedule ID
	Flight findFlightById(int id);

	// find Flight By FlightNumber,origin,Destination and FLight Date
	Flight findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(SearchQuery query);

	// Find Fare of a given Scheduled Flight ID
	Fare findFareByFlightId(int id);

	// Find Fare of a Flight by FlightNumber,Origin, Destination and FlightDate
	Fare getFareByQuery(SearchQuery query);

	// Schedule new Flight
	Flight scheduleFlight(Flight flight);

	public void removeFlightByFlightNumberAndOriginAndDestinationAndFlightDate(String flightNumber, String origin,
			String destination, LocalDate flightDate);

	public void removeFlightByFlightNumberAndOriginAndDestinationAndFlightDate(SearchQuery query);

	public void removeFlightById(int id);
}