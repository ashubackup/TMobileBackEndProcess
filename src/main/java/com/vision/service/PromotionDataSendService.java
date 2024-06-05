package com.vision.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vision.entity.TblSubscription;
import com.vision.entity.WebInfo;
import com.vision.repository.TblSubscriptionRepo;
import com.vision.repository.WebInfoRepo;

@Service
public class PromotionDataSendService {
	@Autowired
	private TblSubscriptionRepo subRepo;
	@Autowired
	private WebInfoRepo webInfoRepo;
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${promotion.url}")
	private String promotionUrl;
	
	public void sendPromotionData()
	{
		
		List<TblSubscription> promotionData = subRepo.getPromotionData();	
		if(!promotionData.isEmpty())
		{
			for (TblSubscription tblSubscription : promotionData) {
				sendData(tblSubscription.getAni(), tblSubscription.getTransaction_Id());
				tblSubscription.setNotify_status("1");		
				subRepo.save(tblSubscription);
				
			}
		}
	}
	
	public void sendData(String ani, String transactionId)
	{
		try {
			promotionUrl = promotionUrl.replace("<ANI>", ani).replace("<EXT_REF>", transactionId);
			System.out.println("Request is--" + promotionUrl);
	        String response = restTemplate.getForObject(promotionUrl, String.class);
	        
	        webInfoRepo.save(WebInfo.builder()
	            	.mobileNumber(ani)
	            	.request(promotionUrl)
	            	.response(response)
	            	.status("Send")
	            	.promotion_date(LocalDateTime.now())
	            	.build());
			
		}catch(Exception e) {
			 webInfoRepo.save(WebInfo.builder()
		            	.mobileNumber(ani)
		            	.request(promotionUrl)
		            	.response(e.getMessage())
		            	.status("Send")
		            	.promotion_date(LocalDateTime.now())
		            	.build());
			
		}
		
        
		
	}
	
	

}
			