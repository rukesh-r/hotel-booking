package com.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel_booking.model.RoomImage;

public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {
}