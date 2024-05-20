package com.vision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vision.service.AllCallBackService;

@RestController
public class CallBackController 
{
	@Autowired
	private AllCallBackService service;
	
	@PostMapping("/callback")
    public String callbackStoreData(@RequestBody(required = false) String callbackBody) 
	{
        try {
            if (callbackBody != null) {
            	return service.saveCallback(callbackBody);
            }
        } catch (Exception e){
            e.printStackTrace();
            return "Failed";
        }
        return "Failed";
    }
}
