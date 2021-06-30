package com.cts.pss.service;

import java.util.List;

import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.CoPassenger;
import com.cts.pss.entity.Passenger;
import com.cts.pss.model.SearchQuery;

public interface BookingService {

	Object bookFlight(SearchQuery query);

	Object bookFlightV1(int flightId, int travellers, Passenger passenger);

	boolean deleteBookingById(int bookingId);

	//cancel booking for selected passengers only
	BookingRecord customCancelBooking(int booingId, List<CoPassenger> coPassengers);

	BookingRecord getBookingDetails(int bookingId);

	BookingRecord rescheduleBooking(int bookingId, int flightId);

}