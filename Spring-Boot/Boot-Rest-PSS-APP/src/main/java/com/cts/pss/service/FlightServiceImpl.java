package com.cts.pss.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.pss.dao.FlightRepository;
import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;
import com.cts.pss.model.SearchQuery;

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightRepository flightDao;
	
	

	// Find Scheduled flights by Origin,Destination and FlightDate
	@Override
	public List<Flight> findFlights(SearchQuery query) {

		List<Flight> flights = flightDao.findFlightByOriginAndDestinationAndFlightDate(query.getOrigin(),
				query.getDestination(), query.getFlightDate());

		flights = flights.stream().filter(flight -> flight.getInventory().getCount() >= query.getTravellers())
				.collect(Collectors.toList());

		return flights;

	}

	@Override
	public List<Flight> listScheduledFlights(@PathVariable String origin, @PathVariable String destination,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate, @PathVariable int travellers) {
		return flightDao.findFlightByOriginAndDestinationAndFlightDate(origin, destination, flightDate).stream()
				.filter(flight -> flight.getInventory().getCount() >= travellers).collect(Collectors.toList());
	}

	// Find Flight By Flight Schedule ID
	@Override
	public Flight findFlightById(int id) {
		return flightDao.findById(id).orElse(null);
	}

	// find Flight By FlightNumber,origin,Destination and FLight Date
	@Override
	public Flight findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(SearchQuery query) {
		return flightDao.findByFlightNumberAndOriginAndDestinationAndFlightDate(query.getFlightNumber(),
				query.getOrigin(), query.getDestination(), query.getFlightDate());
	}

	// find Flight By FlightNumber,origin,Destination and FLight Date
	@Override
	public Flight findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(String flightNumber, String origin,
			String destination, LocalDate flightDate) {
		return flightDao.findByFlightNumberAndOriginAndDestinationAndFlightDate(flightNumber, origin, destination,
				flightDate);
	}

	// Find Fare of a given Scheduled Flight ID
	@Override
	public Fare findFareByFlightId(int id) {
		return flightDao.findById(id).orElse(null).getFare();
	}

	// Find Fare of a Flight by FlightNumber,Origin, Destination and FlightDate
	@Override
	public Fare getFareByQuery(SearchQuery query) {
		Flight flight = flightDao.findByFlightNumberAndOriginAndDestinationAndFlightDate(query.getFlightNumber(),
				query.getOrigin(), query.getDestination(), query.getFlightDate());
		return flight.getFare();
	}

	// Schedule new Flight
	@Override
	public Flight scheduleFlight(Flight flight) {
		return flightDao.save(flight);
	}

	// Remove Flight By ID
	@Override
	public void removeFlightById(int id) {
		flightDao.deleteById(id);
	}

	@Override
	// remove flight By flight number,origin,destination and flight date :: for POST
	// request
	public void removeFlightByFlightNumberAndOriginAndDestinationAndFlightDate(SearchQuery query) {
		 flightDao.deleteByFlightNumberAndOriginAndDestinationAndFlightDate(query.getFlightNumber(),
				query.getOrigin(), query.getDestination(), query.getFlightDate());
	}

	@Override
	// remove flight By flight number,origin,destination and flight date :: for get
	// Request
	public void removeFlightByFlightNumberAndOriginAndDestinationAndFlightDate(String flightNumber, String origin,
			String destination, LocalDate flightDate) {
		 flightDao.deleteByFlightNumberAndOriginAndDestinationAndFlightDate(flightNumber, origin, destination,
				flightDate);
	}

}
