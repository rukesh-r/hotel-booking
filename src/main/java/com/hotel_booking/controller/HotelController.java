package com.hotel_booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel_booking.model.Hotel;
import com.hotel_booking.service.HotelService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService service;

    @GetMapping("/api")
    public List<Hotel> getHotels(){
        return service.getAllHotels();
    }

    @GetMapping("/search")
    public String search(@RequestParam String location, Model model){

        List<Hotel> hotels = service.searchByLocation(location);

        model.addAttribute("hotels", hotels);

        return "hotels"; // 🔥 VERY IMPORTANT
    }

    @PostMapping
    public Hotel addHotel(@RequestBody Hotel hotel){
        return service.saveHotel(hotel);
    }

    @GetMapping("/hotelsPage")
    public String hotelsPage(Model model){

        model.addAttribute("hotels", service.getAllHotels());

        return "hotels";
    }
}