package com.cts.pss.controller;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Sender {
	
	@Autowired
	private RabbitTemplate rt;
	
	
	@Bean
	public Queue myQ1() {
		System.out.println(">>>>>> Creating TestQ1 <<<<<<");
		return new Queue("TestQ1",false);
	}
	
	@Bean
	public Queue myQ2() {
		System.out.println(">>>>>> Creating TestQ2 <<<<<<");
		return new Queue("TestQ2",false);
	}
	
	

	
	@GetMapping("/")
	public String f1() {
		
		rt.convertAndSend("TestQ1","My Message 3");
		rt.convertAndSend("TestQ2","My Second JMS Application...");

		
		return "Welcome";
	}
	
	
	
	
}
