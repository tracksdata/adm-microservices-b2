package com.cts.pss.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cts.pss.entity.Co_Passenger;
@Transactional
public interface CoPassengerRepository extends JpaRepository<Co_Passenger, Integer> {

	@Modifying
	@Query(value = "delete from copassengers where copassenger_id= :copassenger_id",nativeQuery = true)
	public void deleteCopassengersById(int copassenger_id);
	
	@Modifying
	@Query(value = "delete from co_passenger where copassenger_id= :copassenger_id",nativeQuery = true)
	public void deleteCopassenger(int copassenger_id);
	
	
	
	
}
