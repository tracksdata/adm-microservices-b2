package com.cts.pss.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;


@Controller
public class Receiver {

	@RabbitListener(queues = {"TestQ1"})
	public void f1(String obj){
		System.out.println(">>>>>>>>>> Receiving Data from TestQ1 <<<<<<<");
		System.out.println(obj);
		System.out.println("----------------------------------------");
		
	}
	

	
	
	

}
