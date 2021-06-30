package com.cts.pss.service;

import java.time.LocalDate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.pss.entity.Flight;

@FeignClient(name = "search-service",url = "http://localhost:8082/api/pss/search")
public interface SearchServiceProxy {
	
	@GetMapping("/find/{origin}/{destination}/{flightDate}/{flightNumber}")
	Flight findFlight(@PathVariable String origin,@PathVariable String destination,@PathVariable LocalDate fightDate,@PathVariable String flightNumber);
	@GetMapping("/{flightId}")
	Flight getFlightById(@PathVariable int flightId);
	@PostMapping("/newFlight")
	Flight scheduleNewFlight(@RequestBody Flight flight);
}
