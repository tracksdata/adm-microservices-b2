package com.cts.pss.entity;

import java.time.LocalDate;
import java.util.List;

import com.cts.pss.model.SearchQuery;

public interface FlightSearchService {

	List<Flight> findFlights(SearchQuery query);

	Flight findFlight(SearchQuery query);

	Flight findFlightById(int id);

	List<Flight> findFlights(String origin, String destination, LocalDate flightDate, int travellers);

}