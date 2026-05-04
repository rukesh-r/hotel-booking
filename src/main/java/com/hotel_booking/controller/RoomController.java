package com.hotel_booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel_booking.model.Room;
import com.hotel_booking.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService service;

    @GetMapping("/{hotelId}")
    public List<Room> getRooms(@PathVariable int hotelId){
        return service.getRoomsByHotel(hotelId);
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room){
        return service.saveRoom(room);
    }
}