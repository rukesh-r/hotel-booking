package com.hotel_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "landing";
    }

    @GetMapping("/registerPage")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

//    @GetMapping("/hotelsPage")
//    public String hotelsPage() {
//        return "hotels";
//    }
//
//    @GetMapping("/roomsPage")
//    public String roomsPage() {
//        return "rooms";
//    }
//
//    @GetMapping("/bookingPage")
//    public String bookingPage() {
//        return "booking";
//    }

}