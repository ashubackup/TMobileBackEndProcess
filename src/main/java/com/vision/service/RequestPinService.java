package com.vision.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vision.Utility.UtilityService2;
import com.vision.entity.ServiceInfo;
import com.vision.entity.WebInfo;
import com.vision.repository.ServiceInfoRepo;
import com.vision.repository.WebInfoRepo;

@Service
public class RequestPinService 
{
	@Autowired
	private ServiceInfoRepo infoRepo;
	@Autowired
	private UtilityService2 utilityService2;

	@Autowired
	private WebInfoRepo webRepo;
	
	public String requestRedirectUrl(WebInfo webInfo, String host, String status)
	{
		ServiceInfo serviceInfo = infoRepo.findByStatus(status);
		System.out.println(serviceInfo);
		try {
			
			if(serviceInfo != null ) 
			{
				String dateTime = LocalDateTime.now().toString();
				String dencryptedSignature = utilityService2.buildSignature(serviceInfo.getApiKey(), serviceInfo.getApiSecret(), serviceInfo.getApplicationId(), serviceInfo.getCountryId(),
						serviceInfo.getOperatorId(), serviceInfo.getCpId(), dateTime, webInfo.getLanguage().toUpperCase(), 
						serviceInfo.getShortcode(), "RedirectToCG");
				
				System.out.println("Decrypted Signature ---" +  dencryptedSignature);
				saveUserData(webInfo);
				return utilityService2.getCGUrlFromApi(serviceInfo, webInfo, host, dencryptedSignature, dateTime);
				
				
			}else {
				System.out.println("ServiceInfo data is null");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return "Failed";
		}
		
		return "Failed";
		
	}
	
	 public void saveUserData(WebInfo webInfo)
	 {
		 try {
			 webRepo.save(webInfo);
		 }catch(Exception e) {
			 e.printStackTrace();	
		 }
	 }
}
