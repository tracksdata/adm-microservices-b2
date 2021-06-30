package com.cts.pss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.Passenger;
import com.cts.pss.model.SearchQuery;
import com.cts.pss.service.BookingService;

@RestController
@RequestMapping("api/pss/booking")
@CrossOrigin
@RefreshScope
public class BookingRestController {

	@Autowired
	private BookingService bookingService;
	
	@Value("${coupon.code}")
	private String couponCode;


	// Reschedule flight

	@GetMapping("/{bookingId}/{flightId}")
	public BookingRecord rescheduleFight(@PathVariable int bookingId, @PathVariable int flightId) {

		if (findBookingById(bookingId) == null) {
			return null;
		}

		System.out.println(">>>> Booking Coupon Applied::: "+couponCode);
		return bookingService.rescheduleBooking(bookingId, flightId);
	}
	
	@PostMapping("/{flightId}/{travellers}")
	public ResponseEntity<?> book(@PathVariable int flightId,@PathVariable int travellers, @RequestBody Passenger passenger) {

		System.out.println(">>>> Booking Coupon Applied::: "+couponCode);
		// ????
		Object br = bookingService.bookFlightV1(flightId,travellers,passenger);

		if (!(br instanceof BookingRecord)) {
			return new ResponseEntity<>(br, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(br, HttpStatus.OK);

	}

	@PostMapping
	public ResponseEntity<?> f1(@RequestBody SearchQuery qrery) {
		System.out.println(">>>> Booking Coupon Applied::: "+couponCode);


		// ????
		Object br = bookingService.bookFlight(qrery);

		if (!(br instanceof BookingRecord)) {
			return new ResponseEntity<>(br, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(br, HttpStatus.OK);

	}
	
	
	@DeleteMapping
	public BookingRecord deleteCustomPassengers(@RequestBody SearchQuery query) {
		
		
		System.out.println(">>>>> Booking ID:::: "+query.getBookingId());
		System.out.println(">>>>> CoPassengers SIze:: "+query.getCoPassengers().size());
		return bookingService.customCancelBooking(query.getBookingId(), query.getCoPassengers());
	}

	// delete booking by booking ID

	@DeleteMapping("/{bookingId}")
	public String deleteBookingByBookingId(@PathVariable int bookingId) {
		boolean status = bookingService.deleteBookingById(bookingId);
		if (status) {
			return "Booking ID " + bookingId + " is Deleted ";
		} else {
			return "Booking ID " + bookingId + " Not Found";
		}
	}

	@GetMapping("/{bookingId}")
	public BookingRecord findBookingById(@PathVariable int bookingId) {
		return bookingService.getBookingDetails(bookingId);
	}

}
