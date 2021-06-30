package com.cts.pss.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;

public interface FareService {

	Fare getFare(String flightNumber, String origin, String destionation, LocalDate flightDate);

	Fare getFareByFlightId(int id);
	


}