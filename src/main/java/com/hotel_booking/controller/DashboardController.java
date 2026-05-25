package com.hotel_booking.controller;

import com.hotel_booking.model.User;
import com.hotel_booking.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null)                    return "redirect:/loginPage";
        if (!"ADMIN".equals(user.getRole())) return "redirect:/hotelsPage";

        try {
            model.addAttribute("totalUsers",     analyticsService.getTotalUsers());
            model.addAttribute("totalHotels",    analyticsService.getTotalHotels());
            model.addAttribute("totalRooms",     analyticsService.getTotalRooms());
            model.addAttribute("totalBookings",  analyticsService.getTotalBookings());
            model.addAttribute("totalRevenue",   analyticsService.getTotalRevenue());
            model.addAttribute("availableRooms", analyticsService.getTotalAvailableRooms());

            model.addAttribute("monthLabels",     toJsonStringArray(analyticsService.getMonthLabels()));
            model.addAttribute("monthlyBookings", toJsonLongArray(analyticsService.getMonthlyBookings()));
            model.addAttribute("monthlyRevenue",  toJsonDoubleArray(analyticsService.getMonthlyRevenue()));

            Map<String, Long> hotelCounts = analyticsService.getHotelBookingCounts();
            model.addAttribute("hotelLabels",    toJsonStringArray(hotelCounts.keySet().toArray(new String[0])));
            model.addAttribute("hotelBookings",  toJsonLongArray(hotelCounts.values().stream().mapToLong(Long::longValue).toArray()));

        } catch (Exception e) {
            System.err.println("=== DASHBOARD ERROR ===");
            e.printStackTrace();
            throw e;
        }

        return "dashboard";
    }

    private String toJsonStringArray(String[] arr) {
        return "[" + Arrays.stream(arr)
                .map(s -> "\"" + s.replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(",")) + "]";
    }

    private String toJsonLongArray(long[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(arr[i]);
        }
        return sb.append("]").toString();
    }

    private String toJsonDoubleArray(double[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(String.format("%.2f", arr[i]));
        }
        return sb.append("]").toString();
    }
}
