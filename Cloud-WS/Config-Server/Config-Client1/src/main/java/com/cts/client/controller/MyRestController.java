package com.cts.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RefreshScope
public class MyRestController {
	
	@Value("${database}")
	private String databaseName;
	

	@GetMapping()
	public String mysqlConnect() {
		return "You are now connected with "+databaseName;
	}

}
