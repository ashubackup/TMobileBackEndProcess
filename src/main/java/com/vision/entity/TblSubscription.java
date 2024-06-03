package com.vision.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_subscription")
public class TblSubscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;
	private String ani;
	private LocalDateTime subDateTime;
	private LocalDateTime lastBilledDate;
	private LocalDate nextBilledDate;
	private String shortCode;
	private String operatorId;
	private String price;
	private String pack;
	private String mAct;
	private LocalDateTime processDatetime;
	private String serviceName;
	private String status;
	private String password;
	private String requestId;
	private String notify_status;

}
