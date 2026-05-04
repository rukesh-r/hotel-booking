package com.hotel_booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel_booking.model.Room;
import com.hotel_booking.model.User;
import com.hotel_booking.service.RoomService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RoomPageController {

    @Autowired
    private RoomService service;

    @GetMapping("/roomsPage")
    public String roomsPage(@RequestParam int hotelId,
                            @RequestParam(required = false) Boolean availableOnly,
                            HttpSession session,
                            Model model){

        User user = (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/loginPage";
        }

        List<Room> rooms;

        if(Boolean.TRUE.equals(availableOnly)){
            rooms = service.getAvailableRoomsByHotel(hotelId);
        } else {
            rooms = service.getRoomsByHotel(hotelId);
        }

        model.addAttribute("rooms", rooms);
        model.addAttribute("hotelId", hotelId);
        model.addAttribute("availableOnly", availableOnly);

        return "rooms";
    }
    

}