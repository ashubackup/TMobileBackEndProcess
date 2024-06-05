package com.vision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.vision.service.PromotionDataSendService;

//@RestController
public class PromotionDataController {
	@Autowired
	private PromotionDataSendService service;
	
	//Sending to chandra
	//@Scheduled(fixedDelay = 2000L)
	public void processPromotionData()
	{
		service.sendPromotionData();
		
	}

}
