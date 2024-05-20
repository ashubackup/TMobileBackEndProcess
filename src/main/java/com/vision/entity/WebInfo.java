package com.vision.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="web_info")
public class WebInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String mobileNumber;
	private String language;
	@CreationTimestamp
	private LocalDateTime dateTime;
	private String password;
	private String evinaRequestId;

}
