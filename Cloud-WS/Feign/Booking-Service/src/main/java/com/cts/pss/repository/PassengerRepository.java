package com.cts.pss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.pss.entity.BookingRecord;
import com.cts.pss.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Integer>{

}
