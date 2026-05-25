package com.hotel_booking.controller;

import com.hotel_booking.model.User;
import com.hotel_booking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public String register(User user, Model model) {
        try {
            service.register(user);
            return "redirect:/loginPage?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Email already exists. Please use a different email.");
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = service.login(email, password);

        if (user != null) {
            session.setAttribute("user", user);
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/hotelsPage";
        }

        model.addAttribute("error", "Invalid email or password.");
        return "login";
    }
}
