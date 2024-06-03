package com.vision.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="web_info")
public class WebInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String mobileNumber;
	@CreationTimestamp
	private LocalDateTime dateTime;
	private String password;
	private String evinaRequestId;
	private String language;
	private String status;
	private LocalDateTime promotion_date;
	private String request;
	private String response;

}
