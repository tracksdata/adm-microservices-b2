package com.cts.pss.service;

import com.cts.pss.entity.CheckIn;

public interface CheckinService {

	CheckIn checkin(int bookingId);

	CheckIn findById(int checkinId);

	CheckIn findBybookingRecord_bookingId(int bookingId);
	CheckIn findByBookingId(int bookingId);

	boolean existsBybookingRecord_bookingId(int bookingId);

}