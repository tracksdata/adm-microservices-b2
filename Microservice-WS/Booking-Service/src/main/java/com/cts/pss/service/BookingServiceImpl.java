package com.cts.pss.service;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;import org.aspectj.weaver.NewFieldTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.pss.controller.Sender;
import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.Fare;
import com.cts.pss.entity.Flight;
import com.cts.pss.model.CustomMessage;
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

	public Object bookFlight(SearchQuery query) {

		Fare fare = rt.getForObject(fareSericeUrl + "/" + query.getFlightNumber() + "/" + query.getOrigin() + "/"
				+ query.getDestination() + "/" + query.getFlightDate(), Fare.class);
		Flight flight = rt.getForObject(searchServiceUrl + "/find/" + query.getOrigin() + "/" + query.getDestination()
				+ "/" + query.getFlightDate() + "/" + query.getFlightNumber(), Flight.class);
		BookingRecord bookingRecord = null;
		// booking process

		if (flight.getInventory().getAvailableSeats() < query.getTravellers()) {
			System.out.println(">>>>>>>> No Seats Aviable For Booking <<<<<<<");
			
			return new CustomMessage("error", "No Seats Aviable For Booking");
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
				return new CustomMessage("error", "Invalid Passenger Count::: Bookiong is Not Done");
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
	
	
	// Reschedule flight
	
	public BookingRecord rescheduleBooking(int bookingId,int flightId) {
		
		BookingRecord bookingRecord = getBookingDetails(bookingId);
		
		Flight flight = rt.getForObject(searchServiceUrl + "/find/" + bookingRecord.getOrigin() + "/" + bookingRecord.getDestination()
		+ "/" + bookingRecord.getFlightDate() + "/" + bookingRecord.getFlightNumber(), Flight.class);
		
		
		
		// restore Inventory booked travellers
		//flight.getInventory().setAvailableSeats(flight.getInventory().getAvailableSeats()+bookingRecord.getTravellers());
		
		// get new selected flight information
	
		 Flight newFlight = rt.getForObject(searchServiceUrl+"/"+flightId, Flight.class);
		 
		 
		 // New Flight Booking Information
		 
		  bookingRecord.setBookingId(bookingId);
		  bookingRecord.setFlightNumber(newFlight.getFlightNumber());
		  bookingRecord.setOrigin(newFlight.getOrigin());
		  bookingRecord.setDestination(newFlight.getDestination());
		  bookingRecord.setFlightDate(newFlight.getFlightDate());
		  bookingRecord.setFlightTime(newFlight.getFlightTime());
		  bookingRecord.setFlightInfo(newFlight.getFlightInfo());
		  bookingRecord.setFare(newFlight.getFare().getTicketFare()*bookingRecord.getTravellers());
		  
		  bookingDao.save(bookingRecord);
		  
		  //Update new flight booking seats information in Inventory
		  //newFlight.getInventory().setAvailableSeats(newFlight.getInventory().getAvailableSeats()-bookingRecord.getTravellers());
		  
		   // Send Old Flight Details to Search-Service
			Map<String, Object> oldFlightDetails = new HashMap<>();
			oldFlightDetails.put("ID", flight.getId());
			oldFlightDetails.put("SEATS_UPDATED", bookingRecord.getTravellers());
			
			// Send New Flight Details to Search-Service
			Map<String, Object> newFlightDetails = new HashMap<>();
			newFlightDetails.put("ID", newFlight.getId());
			newFlightDetails.put("SEATS_BOOKED", bookingRecord.getTravellers());

			sender.rescheduleFlightInformation(oldFlightDetails); // adding
			sender.sendBookingDetails(newFlightDetails); // minus

		
		return bookingRecord;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
