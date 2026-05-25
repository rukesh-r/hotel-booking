package com.hotel_booking.repository;

import com.hotel_booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByHotel_Id(int hotelId);

    List<Room> findByHotel_IdAndAvailableRoomsGreaterThan(int hotelId, int count);

    @Query("SELECT SUM(r.availableRooms) FROM Room r")
    Long getTotalAvailableRooms();
}
