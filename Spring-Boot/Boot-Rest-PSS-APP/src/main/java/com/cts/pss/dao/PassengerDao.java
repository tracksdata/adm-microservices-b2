package com.cts.pss.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.cts.pss.entity.Passenger;
@Transactional
public interface PassengerDao extends JpaRepository<Passenger, Integer>{
	
	
}
