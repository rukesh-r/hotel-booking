package com.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_booking.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    User findByEmail(String email);
    
    User findByEmailAndPassword(String email,String password);
}