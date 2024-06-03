package com.vision.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vision.entity.TblSubscription;
@Repository
public interface TblSubscriptionRepo extends JpaRepository<TblSubscription, Integer>{
	
	@Query(value="SELECT * FROM tbl_subscription WHERE ani=:ani and password=:password ",nativeQuery = true)
	TblSubscription findByAniAndPassword(@Param("ani") String ani,@Param("password") String password);
	
	TblSubscription findByAni(String ani);
	
	@Query(value="SELECT COUNT(ani) FROM tbl_subscription WHERE ani=:ani AND DATE(nextBilledDate)>=DATE(SUBDATE(NOW(),0)) ",nativeQuery = true)
	Integer checkNextBilledDate(@Param("ani") String ani);
	
	@Query(value="SELECT COUNT(ani) FROM tbl_subscription WHERE ani=:ani AND DATE(trialEndDate)>DATE(SUBDATE(NOW(),0)) ",nativeQuery = true)
	Integer checkTrialDate(@Param("ani") String ani);
	
	
	@Query(value="SELECT * FROM tbl_subscription WHERE EXISTS (SELECT evinaRequestId FROM web_info WHERE tbl_subscription.requestId = web_info.evinaRequestId) AND notify_status='0'",nativeQuery = true)
	List<TblSubscription> getPromotionData();
	
	
	
	
	
}
