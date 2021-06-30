package com.cts.pss.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.pss.entity.Fare;
import com.cts.pss.repository.FlightRepository;

@Service
public class FareServiceImpl implements FareService {

	@Autowired
	private FlightRepository flightDao;

	@Override
	public Fare getFare(String flightNumber, String origin, String destionation, LocalDate flightDate) {
		return flightDao
				.findByFlightNumberAndOriginAndDestinationAndFlightDate(flightNumber, origin, destionation, flightDate)
				.getFare();
	}

	@Override
	public Fare getFareByFlightId(int id) {
		return flightDao.findById(id).orElse(null).getFare();
	}

}
