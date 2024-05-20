package com.vision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vision.entity.AllCallBack;

@Repository
public interface AllCallbackRepo extends JpaRepository<AllCallBack, Integer>{

}
