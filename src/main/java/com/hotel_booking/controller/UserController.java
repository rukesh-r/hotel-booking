package com.hotel_booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel_booking.model.User;
import com.hotel_booking.service.UserService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public String register(User user) {

        service.register(user);

        return "redirect:/loginPage";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = service.login(email, password);

        if(user != null) {
            session.setAttribute("user", user);
            return "redirect:/hotelsPage";
        } else {
            model.addAttribute("error", "Invalid Email or Password");
            return "login";
        }
    }
}