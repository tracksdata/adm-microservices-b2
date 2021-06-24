package com.cts.pss.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.pss.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Integer>{
	
	List<Flight> findByOriginAndDestinationAndFlightDate(String origin,String destination,LocalDate flightDate);
	Flight findByOriginAndDestinationAndFlightDateAndFlightNumber(String origin,String destination,LocalDate flightDate,String flightNumber);
	
}
