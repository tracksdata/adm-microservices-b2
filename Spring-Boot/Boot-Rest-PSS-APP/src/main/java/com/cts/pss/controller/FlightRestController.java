package com.cts.pss.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;
import com.cts.pss.model.SearchQuery;
import com.cts.pss.service.FlightService;

@RestController
@RequestMapping("/api/pss/search")
@CrossOrigin
public class FlightRestController {

	@Autowired
	private FlightService flightService;
	
	
	@GetMapping("/{id}")
	public Flight getFlightById(@PathVariable int id) {
		return flightService.findFlightById(id);
	}

	// get a single flight with POST request
	@PostMapping("/byFlightNumber")
	public Flight findFlightById(@RequestBody SearchQuery query) {
		return flightService.findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(query);
	}

	// get a single flight with GET request
	@GetMapping("/byFlightNumber/{flightNumber}/{origin}/{destination}/{flightDate}")
	public Flight listScheduledFlights(@PathVariable String flightNumber, @PathVariable String origin,
			@PathVariable String destination, @PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate) {
		return flightService.findFlightByFlightNumberAndOriginAndDestinationAndFlightDate(flightNumber, origin,
				destination, flightDate);

	}

	// Remove scheduled flight with parameters
	@DeleteMapping("/byFlightNumber/{flightNumber}/{origin}/{destination}/{flightDate}")
	public void removeFlight(@PathVariable String flightNumber, @PathVariable String origin,
			@PathVariable String destination, @PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate) {
		flightService.removeFlightByFlightNumberAndOriginAndDestinationAndFlightDate(flightNumber, origin, destination,
				flightDate);

	}

	// Remove scheduled flight Request Body
	@DeleteMapping()
	public void deleteFlight(@RequestBody SearchQuery query) {
		flightService.removeFlightByFlightNumberAndOriginAndDestinationAndFlightDate(query);
	}
	
	@DeleteMapping("/{id}")
	public void removeById(@PathVariable int id) {
		flightService.removeFlightById(id);
	}

	// list all scheduled flights with query filter with GET Mapping
	@GetMapping("/{origin}/{destination}/{flightDate}/{travellers}")
	public List<Flight> listScheduledFlights(@PathVariable String origin, @PathVariable String destination,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate, @PathVariable int travellers) {
		return flightService.listScheduledFlights(origin, destination, flightDate, travellers);

	}

	// list all scheduled flights with query filter with POST Mapping
	@PostMapping
	public List<Flight> filterFlighs(@RequestBody SearchQuery query) {

		return flightService.findFlights(query);
	}

	// Schedule new Flight
	@PostMapping("/addFlight")
	public Flight scheduleFlight(@RequestBody Flight flight) {
		System.out.println(flight);
		return flightService.scheduleFlight(flight);
	}

	// Update Flight
	@PutMapping()
	public Flight updateFlight(@RequestBody Flight flight) {
		System.out.println(flight);
		return flightService.scheduleFlight(flight);
	}

	// get Flight fare By ID
	@GetMapping("getFare/{flightId}")
	public Fare findFareByFlightId(@PathVariable int flightId) {
		return flightService.findFareByFlightId(flightId);
	}

	// get Fare by Flight Number,Origin,Destination and Flying Date
	@PostMapping("/fare")
	public Fare getFareByQuery(@RequestBody SearchQuery query) {
		return flightService.getFareByQuery(query);
	}

}
