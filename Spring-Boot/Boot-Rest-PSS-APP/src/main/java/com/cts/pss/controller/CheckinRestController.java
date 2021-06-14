package com.cts.pss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pss.entity.CheckIn;
import com.cts.pss.service.CheckinService;

@RestController
@RequestMapping("/api/pss/checkin")
@CrossOrigin
public class CheckinRestController {
	
	@Autowired
	private CheckinService checkinService;
	
	
		//Post a new check-in
		@PostMapping("/{bookingId}")
		public ResponseEntity<?> checkin(@PathVariable int bookingId) {
			if(checkinService.existsBybookingRecord_bookingId(bookingId)) {
				
				return new ResponseEntity<>("Booking Id "+bookingId+" is already checked-in",HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<CheckIn>(checkinService.checkin(bookingId), HttpStatus.OK);
		}
	
	
	/**
	//Post a new check-in
	@PostMapping("/{bookingId}")
	public CheckIn checkin(@PathVariable int bookingId) {
		if(checkinService.existsBybookingRecord_bookingId(bookingId)) {
			return null;
		}
		return checkinService.checkin(bookingId);
	}
	*/

	//get Check-In information by CheckinId
	@GetMapping("/{checkinId}")
	public CheckIn getCheckinInfo(@PathVariable int checkinId) {
		return checkinService.findById(checkinId);
	}
	
	//get Check-In information by bookingID
	@GetMapping("/info/{bookingId}")
	public CheckIn findBybookingRecord_bookingId(@PathVariable int bookingId) {
		return checkinService.findBybookingRecord_bookingId(bookingId);
	}
	
	//returns true if booking id available in check-in table
	@GetMapping("/info/status/{bookingId}")
	public boolean checkinStatus(@PathVariable int bookingId) {
		return checkinService.existsBybookingRecord_bookingId(bookingId);
	}
}
