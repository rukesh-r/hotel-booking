package com.hotel_booking.controller;

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
public class BookingPageController {

	@Autowired
	private RoomService roomService;
	@GetMapping("/bookingPage")
	public String bookingPage(@RequestParam int roomId,
	                          HttpSession session,
	                          Model model) {

	    User user = (User) session.getAttribute("user");

	    if(user == null){
	        return "redirect:/loginPage";
	    }

	    Room room = roomService.getRoomById(roomId); // 🔥 add this

	    model.addAttribute("roomId", roomId);
	    model.addAttribute("userId", user.getId());
	    model.addAttribute("price", room.getPrice()); // 🔥 pass price

	    return "booking";
	}
}