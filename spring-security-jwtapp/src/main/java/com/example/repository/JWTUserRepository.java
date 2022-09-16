package com.example.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.JWTUser;

public interface JWTUserRepository extends JpaRepository<JWTUser, Integer>{

	//Derived Query
	JWTUser findByUsername(String username);
	
}
