package com.cts.pss.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.pss.dao.BookingRepository;
import com.cts.pss.dao.CheckinRepository;
import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.CheckIn;

@Service
public class CheckinServiceImpl implements CheckinService{

	@Autowired
	private CheckinRepository checkinDao;

	@Autowired
	private BookingRepository bookingDao;

	
	@Override
	public CheckIn checkin(int bookingId) {

		BookingRecord bookingRecord = bookingDao.findById(bookingId).orElse(null);

		if (bookingRecord != null) {
			CheckIn checkin = new CheckIn();
			checkin.setCheckinTime(LocalDateTime.now());
			checkin.setSeatNumber("A2");
			bookingRecord.setStatus("CHECKED-IN");
			checkin.setBookingRecord(bookingRecord);

			checkinDao.save(checkin);
			return checkin;
		}
	
		return null;
	}
	
	
	
	@Override
	public boolean existsBybookingRecord_bookingId(int bookingId){
		return checkinDao.existsBybookingRecord_bookingId(bookingId);
	}

	@Override
	public CheckIn findById(int checkinId) {
		// TODO Auto-generated method stub
		return checkinDao.findById(checkinId).orElse(null);
	}
	
	@Override
	public CheckIn findByBookingId(int bookingId) {
		return checkinDao.findByBookingId(bookingId);
	}
	
	@Override
	public CheckIn findBybookingRecord_bookingId(int bookingId) {
		return checkinDao.findBybookingRecord_bookingId(bookingId);
	}


}
