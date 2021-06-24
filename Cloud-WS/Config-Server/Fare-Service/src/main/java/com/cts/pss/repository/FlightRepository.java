package com.cts.pss.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.pss.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Integer>{

	Flight findByFlightNumberAndOriginAndDestinationAndFlightDate(String flightNumber,String origin,String destionation,LocalDate flightDate);
	
}
