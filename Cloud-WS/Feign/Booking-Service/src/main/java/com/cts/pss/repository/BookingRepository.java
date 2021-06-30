package com.cts.pss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.pss.entity.BookingRecord;

public interface BookingRepository extends JpaRepository<BookingRecord, Integer>{

}
