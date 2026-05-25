package com.hotel_booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotel_booking.model.Booking;
import com.hotel_booking.model.Room;
import com.hotel_booking.model.User;
import com.hotel_booking.service.BookingService;
import com.hotel_booking.service.RoomService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired private RoomService roomService;
    @Autowired private BookingService service;

    @PostMapping
    public String bookRoom(Booking booking, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        booking.setUser(user);
        Booking result = service.saveBooking(booking);

        if (result == null) {
            redirectAttributes.addFlashAttribute("error", "No rooms available!");
            return "redirect:/roomsPage";
        }

        // Redirect to payment page instead of confirming immediately
        return "redirect:/payment/" + result.getId();
    }

    @GetMapping("/user/{id}")
    public List<Booking> getBookings(@PathVariable int id) {
        return service.getUserBookings(id);
    }

    @GetMapping("/cancelBooking")
    public String cancelBooking(@RequestParam int id, RedirectAttributes redirectAttributes) {
        service.cancelBooking(id);
        redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
        return "redirect:/bookingHistory";
    }

    @GetMapping("/paymentPage")
    public String paymentPage(@RequestParam int roomId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        return "booking-form";
    }
}
