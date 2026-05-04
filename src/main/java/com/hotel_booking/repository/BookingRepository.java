package com.hotel_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking,Integer>{

	List<Booking> findByUser_Id(int userId);
    
    

}