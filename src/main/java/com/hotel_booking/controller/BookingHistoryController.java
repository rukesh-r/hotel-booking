package com.hotel_booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hotel_booking.model.Booking;
import com.hotel_booking.model.User;
import com.hotel_booking.service.BookingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingHistoryController {

    @Autowired
    private BookingService service;

    @GetMapping("/bookingHistory")
    public String bookingHistory(HttpSession session, Model model){

        User user = (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/loginPage";
        }

        System.out.println("Logged user: " + user.getId()); // DEBUG

        List<Booking> bookings = service.getUserBookings(user.getId());

        System.out.println("Bookings: " + bookings); // DEBUG

        model.addAttribute("bookings", bookings);

        return "bookingHistory";
        
    }
    
}