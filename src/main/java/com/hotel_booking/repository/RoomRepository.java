package com.hotel_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_booking.model.Room;

public interface RoomRepository extends JpaRepository<Room,Integer>{

	List<Room> findByHotel_Id(int hotelId);
	
	List<Room> findByHotel_IdAndAvailableRoomsGreaterThan(int hotelId, int count);

}