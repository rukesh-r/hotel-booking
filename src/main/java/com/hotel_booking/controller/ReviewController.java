package com.hotel_booking.controller;

import com.hotel_booking.model.Hotel;
import com.hotel_booking.model.User;
import com.hotel_booking.repository.HotelRepository;
import com.hotel_booking.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping("/hotelDetail")
    public String hotelDetail(@RequestParam int hotelId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) return "redirect:/hotelsPage";

        model.addAttribute("hotel", hotel);
        model.addAttribute("reviews", reviewService.getReviewsByHotel(hotelId));
        model.addAttribute("avgRating", reviewService.getAverageRating(hotelId));
        model.addAttribute("reviewCount", reviewService.getReviewCount(hotelId));
        model.addAttribute("alreadyReviewed", reviewService.hasUserReviewed(user.getId(), hotelId));
        return "hotel-detail";
    }

    @PostMapping("/review/submit")
    public String submitReview(@RequestParam int hotelId,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        String error = reviewService.addReview(user, hotelId, rating, comment);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
        } else {
            redirectAttributes.addFlashAttribute("success", "Review submitted successfully!");
        }
        return "redirect:/hotelDetail?hotelId=" + hotelId;
    }
}
