package com.cts.pss.controller;

import java.util.Map;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import com.cts.pss.service.FlightSearchService;

@Controller
public class Receiver {
	
	@Autowired
	private FlightSearchService searchService;
	
	
	@Bean
	public Queue myQ1() {
		return new Queue("InventoryQ",false);
	}
	
	@RabbitListener(queues = "InventoryQ")
	public void updateInventory(Map<String, Object> bookingDetails) {
		System.out.println(">>>>>> SEARCH-SERVICE RECEIVER <<<<<<");
		System.out.println(">>>>> Booking Details <<<<<");
		System.out.println(bookingDetails);
		System.out.println("------------------------------");
		searchService.updateInventory((int)bookingDetails.get("ID"),(int) bookingDetails.get("SEATS_BOOKED"));
		
		
		
	}
	
	
	
	

}
