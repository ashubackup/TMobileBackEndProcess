package com.vision.service;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vision.entity.AllCallBack;
import com.vision.repository.AllCallbackRepo;

@Service
public class AllCallBackService
{
	@Autowired
	private AllCallbackRepo callbackRepo;
	
	
	public String saveCallback(String callback)
	{
		try {
			
			JSONObject callbackObject = new JSONObject(callback);
            String subType = callbackObject.getJSONObject("transaction").getJSONObject("data").getJSONObject("action").getString("subType");
            System.out.println("type---" + subType);
            
            
			AllCallBack allCallBack = new AllCallBack();
	    	allCallBack.setCallback(callback);
	    	allCallBack.setType(subType);
	    	allCallBack.setStatus("0");
	    	
			callbackRepo.save(allCallBack);
			return "success";
			
		}catch(JSONException e)
		{
			AllCallBack allCallBack = new AllCallBack();
	    	allCallBack.setCallback(callback);
	    	allCallBack.setDateTime(LocalDateTime.now());
	    	allCallBack.setStatus("0");
	    	allCallBack.setType("JsonException");
			callbackRepo.save(allCallBack);
			return "success";
			
		}
		catch(Exception e)
		{
			e.getMessage();
			return "Failed";
		}
	}

}
