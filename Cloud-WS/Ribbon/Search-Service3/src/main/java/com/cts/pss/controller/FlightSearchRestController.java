package com.cts.pss.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pss.entity.Flight;
import com.cts.pss.model.SearchQuery;
import com.cts.pss.service.FlightSearchService;

@RestController
@RequestMapping("api/pss/search")
@CrossOrigin
@RefreshScope
public class FlightSearchRestController {

	@Autowired
	private FlightSearchService searchService;

	@Value("${originairports.shutdown}")
	private String originAirportShutdownList;

	@GetMapping("/{origin}/{destination}/{flightDate}/{travellers}")
	public List<Flight> findAllFlightsV1(@PathVariable String origin, @PathVariable String destination,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate, @PathVariable int travellers) {

		System.out.println("=======================================");
		System.out.println(">>>>>>> SEARCH-SERVICE-3 <<<<<<");
		System.out.println("=======================================");
		System.out.println(">>>>>>> Shoutdownlist::: " + originAirportShutdownList);

		if (Arrays.asList(originAirportShutdownList.split(",")).contains(origin)) {

			System.out.println(">>>>>> The origin airport is in shutdown state <<<<");
			return new ArrayList<Flight>();
		}

		return searchService.findFlights(origin, destination, flightDate, travellers);
	}

	@GetMapping("/find/{origin}/{destination}/{flightDate}/{flightNumber}")
	public Flight findAllFlightsV2(@PathVariable String origin, @PathVariable String destination,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate, @PathVariable String flightNumber) {

		System.out.println("=======================================");
		System.out.println(">>>>>>> SEARCH-SERVICE-3 <<<<<<");
		System.out.println("=======================================");
		System.out.println(">>>>>>> Shoutdownlist::: " + originAirportShutdownList);

		if (Arrays.asList(originAirportShutdownList.split(",")).contains(origin)) {
			System.out.println(">>>> The origin airport is in shutdown state <<<<< ");
			return null;
		}

		return searchService.findByOriginAndDestinationAndFlightDateAndFlightNumber(origin, destination, flightDate,
				flightNumber);
	}

	// schedule new Flight
	@PostMapping("/newFlight")
	public Flight scheduleNewFlight(@RequestBody Flight newFlight) {

		return searchService.scheduleNewFlight(newFlight);
	}

	@PostMapping
	public List<Flight> findAllFlights(@RequestBody SearchQuery query) {

		System.out.println("=======================================");
		System.out.println(">>>>>>> SEARCH-SERVICE-3 <<<<<<");
		System.out.println("=======================================");
		System.out.println(">>>>>>> Shoutdownlist::: " + originAirportShutdownList);

		if (Arrays.asList(originAirportShutdownList.split(",")).contains(query.getOrigin())) {
			System.out.println(">>>>>> The origin airport is in shutdown state <<<<< ");
			return new ArrayList<Flight>();
		}

		return searchService.findFlights(query);
	}

	@PostMapping("/byId")
	public Flight findFlight(@RequestBody SearchQuery query) {
		System.out.println(">>>>>>> Shoutdownlist::: " + originAirportShutdownList);

		if (Arrays.asList(originAirportShutdownList.split(",")).contains(query.getOrigin())) {
			System.out.println("The origin airport is in shutdown state");
			return null;
		}

		return searchService.findFlight(query);
	}

	@GetMapping("/{id}")
	public Flight findFlightById(@PathVariable int id) {

		System.out.println("=======================================");
		System.out.println(">>>>>>> SEARCH-SERVICE-3 <<<<<<");
		System.out.println("=======================================");
		return searchService.findFlightById(id);
	}

}
