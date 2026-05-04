package com.hotel_booking.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hotel_booking.model.User;
import com.hotel_booking.service.HotelService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HotelPageController {

    @Autowired
    private HotelService service;

    @GetMapping("/hotelsPage")
    public String hotelsPage(HttpSession session, Model model){

        User user = (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/loginPage";
        }

        model.addAttribute("hotels", service.getAllHotels());

        return "hotels";
    }
}