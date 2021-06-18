package com.cts.pss.controller;

import java.util.Map;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
public class Receiver {
	
	
	@Bean
	public Queue myQ1() {
		return new Queue("InventoryQ",false);
	}
	
	@RabbitListener(queues = "InventoryQ")
	public void updateInventory(Map<String, Object> bookingDetails) {
		
		System.out.println(">>>>> Booking Details <<<<<");
		System.out.println(bookingDetails);
		
		
	}
	
	
	
	

}
