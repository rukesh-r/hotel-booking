package com.hotel_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel_booking.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findByLocation(String location);
}