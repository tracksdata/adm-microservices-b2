package com.cts.pss.service;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.pss.controller.Sender;
import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;

import com.cts.pss.model.SearchQuery;
import com.cts.pss.repository.BookingRepository;

@Service
public class BookingServiceImpl {

	// Need Price of Flight
	// Need Flight Details which is going to be booked

	@Autowired
	private RestTemplate rt;

	@Autowired
	private BookingRepository bookingDao;

	@Autowired
	private Sender sender;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private String fareSericeUrl = "http://localhost:8081/api/pss/fare";
	private String searchServiceUrl = "http://localhost:8082/api/pss/search";

	public BookingRecord bookFlight(SearchQuery query) {

		Fare fare = rt.getForObject(fareSericeUrl + "/" + query.getFlightNumber() + "/" + query.getOrigin() + "/"
				+ query.getDestination() + "/" + query.getFlightDate(), Fare.class);
		Flight flight = rt.getForObject(searchServiceUrl + "/find/" + query.getOrigin() + "/" + query.getDestination()
				+ "/" + query.getFlightDate() + "/" + query.getFlightNumber(), Flight.class);
		BookingRecord bookingRecord = null;
		// booking process

		if (flight.getInventory().getAvailableSeats() < query.getTravellers()) {
			System.out.println(">>>>>>>> No Seats Aviable For Booking <<<<<<<");
			return null;
		}

		if (flight != null) {

			bookingRecord = new BookingRecord(LocalDateTime.now(), flight.getFlightNumber(), flight.getOrigin(),
					flight.getDestination(), flight.getFlightDate(), flight.getFlightTime(), "CONFIRMED",
					query.getTravellers(), flight.getFlightInfo(), query.getPassengers());

			// Calculate Fare based on number of Travellers

			bookingRecord.setFare(flight.getFare().getTicketFare() * query.getTravellers());

			if (query.getPassengers().getCo_Passengers().size() == query.getTravellers() - 1) {
				bookingDao.save(bookingRecord);
				System.out.println(">>>>> BOOKING-SERVICE:::: Booking is Completed...");

			} else {
				System.out.println("Invalid Passenger Count::: Bookiong is Not Done");
				return null;
			}

		}
		
		// Send Booking Details to Search-Service
		Map<String, Object> bookingDetails = new HashMap<>();
		bookingDetails.put("ID", flight.getId());
		bookingDetails.put("SEATS_BOOKED", query.getTravellers());

		sender.sendBookingDetails(bookingDetails);

		return bookingRecord;
	}
	
	
	// get Booking Data by ID
	
	public BookingRecord getBookingDetails(int bookingId) {
		return bookingDao.findById(bookingId).orElse(null);
	}

}
