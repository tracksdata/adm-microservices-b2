package com.cts.pss.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cts.pss.entity.CheckIn;

public interface CheckinRepository extends JpaRepository<CheckIn, Integer>{
	
	@Query("from CheckIn as c where c.bookingRecord.bookingId=:bookingId")
	CheckIn findByBookingId(int bookingId);
	
	CheckIn findBybookingRecord_bookingId(int bookingId);
	
	boolean existsBybookingRecord_bookingId(int bookingId);
  


}
