package com.cts.pss.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

	@PostMapping
	public BookingRecord f1(@RequestBody SearchQuery qrery) {
		
		return bookingService.bookFlight(qrery);
		
	}
	
	@GetMapping("/{bookingId}")
	public BookingRecord findBookingById(@PathVariable int bookingId) {
		return bookingService.getBookingDetails(bookingId);
	}
	
}
