package com.vision.service;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vision.entity.TblSubscription;
import com.vision.entity.WebInfo;
import com.vision.repository.TblSubscriptionRepo;

@Service
public class AuthenticationService {
	
	@Autowired
	private TblSubscriptionRepo subRepo;

	public String authenticatUser(WebInfo webInfo)
	{
		try {
			TblSubscription sub = subRepo.findByAniAndPassword(webInfo.getMobileNumber(), webInfo.getPassword());
			if(sub!=null)
			{
				if(sub.getNextBilledDate()!=null)
				{
					if(sub.getNextBilledDate().isAfter(LocalDate.now())
							|| sub.getNextBilledDate().equals(LocalDate.now()))
					{
						//User Can Access
						System.out.println("User Can Access");
						return "success";
					}
					System.out.println("User Can't Access");
					return "Billing Pending";
					
				}
				
				System.out.println("NextBilled Date is NULL");
				return "Billing Pending";
				
			}
			//Not sub
			return "Failed";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "Billing Pending";
		}
	}
	
	public String checkSubscriber(WebInfo webInfo)
	{
		try {
			TblSubscription sub = subRepo.findByAni(webInfo.getMobileNumber());
			if(sub!=null)
			{
				if(sub.getNextBilledDate()!=null)
				{
					if(sub.getNextBilledDate().isAfter(LocalDate.now())
							|| sub.getNextBilledDate().equals(LocalDate.now()))
					{
						//User Can Access
						System.out.println("User Can Access");
						return "success";
					}
					//User Can't Access
					System.out.println("User Can't Access");
					return "Billing Pending";
				}
				else
				{
					System.out.println("NextBilled Date is NULL");
					return "Billing Pending";
				}
			}else {
				//Not sub
				return "Failed";
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return "Billing Pending";
		}
	}
}
