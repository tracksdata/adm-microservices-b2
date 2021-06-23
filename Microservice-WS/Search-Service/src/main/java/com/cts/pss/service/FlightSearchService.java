package com.cts.pss.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.pss.entity.Flight;
import com.cts.pss.model.SearchQuery;

public interface FlightSearchService {

	List<Flight> findFlights(SearchQuery query);

	Flight findFlight(SearchQuery query);

	Flight findFlightById(int id);

	List<Flight> findFlights(String origin, String destination, LocalDate flightDate, int travellers);
	public void updateInventory(int id, int seats_booked);

	Flight findByOriginAndDestinationAndFlightDateAndFlightNumber(String origin, String destination,
			LocalDate flightDate, String flightNumber);

	void updateRescheeduledFlightInventory(int flightId, int updated_seats);

	Flight scheduleNewFlight(Flight flight);

}