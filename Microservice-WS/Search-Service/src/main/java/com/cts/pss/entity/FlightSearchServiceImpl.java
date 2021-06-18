package com.cts.pss.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.pss.model.SearchQuery;
import com.cts.pss.repository.FlightRepository;

@Service
public class FlightSearchServiceImpl implements FlightSearchService {

	@Autowired
	private FlightRepository flightDao;
	
	
	@Override
	public List<Flight> findFlights(String origin, String destination, LocalDate flightDate, int travellers) {
		// TODO Auto-generated method stub
		return flightDao.findByOriginAndDestinationAndFlightDate(origin, destination, flightDate);
	}

	@Override
	public List<Flight> findFlights(SearchQuery query) {
		
		List<Flight> flights=flightDao.findByOriginAndDestinationAndFlightDate(query.getOrigin(), query.getDestination(),
				query.getFlightDate());
		

		return flights.stream().filter(flight->flight.getInventory().getAvailableSeats()>=query.getTravellers()).collect(Collectors.toList());

	}

	@Override
	public Flight findFlight(SearchQuery query) {
		return flightDao.findByOriginAndDestinationAndFlightDateAndFlightNumber(query.getOrigin(),
				query.getDestination(), query.getFlightDate(), query.getFlightNumber());

	}

	@Override
	public Flight findFlightById(int id) {
		return flightDao.findById(id).orElse(null);
	}

}
