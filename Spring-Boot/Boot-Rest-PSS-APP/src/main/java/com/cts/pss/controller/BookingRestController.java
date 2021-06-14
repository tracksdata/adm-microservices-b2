package com.cts.pss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pss.entity.BookingRecord;
import com.cts.pss.model.SearchQuery;
import com.cts.pss.service.BookingService;

@RestController
@RequestMapping("/api/pss/booking")
@CrossOrigin
public class BookingRestController {

	@Autowired
	private BookingService bookingService;

	@PostMapping
	public BookingRecord book(@RequestBody SearchQuery query) {
		return bookingService.bookFlight(query);
	}

	@GetMapping("/{id}")
	public BookingRecord getBooking(@PathVariable int id) {

		return bookingService.getBookingData(id);
	}

	// Reschedule flight
	@GetMapping("/{bookingId}/{flightId}")
	public BookingRecord reschedule(@PathVariable int bookingId, @PathVariable int flightId) {
		return bookingService.rescheduleBooking(bookingId, flightId);
	}

	// Delete CoPassengerrs by Selection
	@DeleteMapping()
	public BookingRecord customCancelBooking(@RequestBody SearchQuery query) {
		return bookingService.customCancelBooking(query.getBookingId(), query.getCoPassengers());
	}

	// Delete All passengers by Booking ID
	@DeleteMapping("/{bookingId}")
	public void cancelBooking(@PathVariable int bookingId) {
		bookingService.cancelBooking(bookingId);
	}

}
