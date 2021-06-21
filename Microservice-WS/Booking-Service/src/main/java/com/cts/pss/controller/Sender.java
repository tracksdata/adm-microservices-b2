package com.cts.pss.controller;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Sender {

	@Autowired
	private RabbitTemplate rt;

	public void sendBookingDetails(Map<String, Object> bookingDetails) {
		System.out.println(">>>> BOOKING-SERVICE <<<<");
		System.out.println(">>>> Sending Booking Details to SEARCH-SERVICE <<<<<");
		rt.convertAndSend("InventoryQ", bookingDetails);
		System.out.println(">>>> Booking Details Sent to SEARCH-SERVICE <<<<");

	}

}
