package com.cts.pss.service;

import java.util.List;

import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.CoPassenger;
import com.cts.pss.entity.Passenger;
import com.cts.pss.model.SearchQuery;

public interface BookingService {
	
	BookingRecord bookFlight(SearchQuery query);
	BookingRecord getBookingData(int id);
	public BookingRecord rescheduleBooking(int bookingId, int flightId);
	public BookingRecord customCancelBooking(int bookingId,List<CoPassenger> coPassengers);
	public void cancelBooking(int bookingId);


	

}