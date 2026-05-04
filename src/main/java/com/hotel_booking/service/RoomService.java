package com.hotel_booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel_booking.model.Room;
import com.hotel_booking.repository.RoomRepository;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    @Autowired
    private RoomRepository roomRepository;
    public List<Room> getRoomsByHotel(int hotelId){
        return repo.findByHotel_Id(hotelId);
    }

    public Room saveRoom(Room room){
        return repo.save(room);
    }
    public Room getRoomById(int id){
        return repo.findById(id).orElse(null);
    }
    public List<Room> getAvailableRoomsByHotel(int hotelId){
        return repo.findByHotel_IdAndAvailableRoomsGreaterThan(hotelId, 0);
    }
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }
}