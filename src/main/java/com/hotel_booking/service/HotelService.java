package com.hotel_booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel_booking.model.Hotel;
import com.hotel_booking.repository.HotelRepository;

@Service
public class HotelService {

    @Autowired
    private HotelRepository repo;

    public List<Hotel> getAllHotels(){
        return repo.findAll();
    }

    public List<Hotel> searchByLocation(String location){
        return repo.findByLocation(location);
    }

    public Hotel saveHotel(Hotel hotel){
        return repo.save(hotel);
    }
    public Hotel getHotelById(int id){
        return repo.findById(id).orElse(null);
    }
}