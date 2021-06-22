package com.cts.pss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pss.entity.BookingRecord;
import com.cts.pss.model.SearchQuery;
import com.cts.pss.service.BookingServiceImpl;

@RestController
@RequestMapping("api/pss/booking")
@CrossOrigin
public class BookingRestController {

	@Autowired
	private BookingServiceImpl bookingService;
	
	
	
	
	
	// Reschedule flight
	
	@GetMapping("/{bookingId}/{flightId}")
	public BookingRecord rescheduleFight(@PathVariable int bookingId,@PathVariable int flightId) {
		
		if(findBookingById(bookingId)==null) {
			return null;
		}
		
		return bookingService.rescheduleBooking(bookingId, flightId);
		
	}

	@PostMapping
	public ResponseEntity<?> f1(@RequestBody SearchQuery qrery) {

		// ????

		Object br = bookingService.bookFlight(qrery);
		
		if(!(br instanceof BookingRecord)) {
			return new ResponseEntity<>(br, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(br, HttpStatus.OK);

	}

	@GetMapping("/{bookingId}")
	public BookingRecord findBookingById(@PathVariable int bookingId) {
		return bookingService.getBookingDetails(bookingId);
	}

}
