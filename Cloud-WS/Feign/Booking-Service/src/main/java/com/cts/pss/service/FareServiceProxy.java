package com.cts.pss.service;

import java.time.LocalDate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.pss.entity.Fare;

@FeignClient(name = "fare-service",url = "http://localhost:8081/api/pss/fare")
public interface FareServiceProxy {
	
	@GetMapping("/{flightNumber}/{origin}/{destination}/{flightDate}")
	Fare getFare(@PathVariable String flightNumber,@PathVariable String origin, @PathVariable String destination,@PathVariable LocalDate flightDate);
	@GetMapping("/{flightId}")
	Fare getFareByFlightId(@PathVariable int flightId);

}
