package com.hotel_booking.controller;

import com.hotel_booking.model.Booking;
import com.hotel_booking.model.Payment;
import com.hotel_booking.model.Payment.PaymentMethod;
import com.hotel_booking.model.Payment.PaymentStatus;
import com.hotel_booking.model.User;
import com.hotel_booking.repository.BookingRepository;
import com.hotel_booking.repository.PaymentRepository;
import com.hotel_booking.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PaymentRepository paymentRepository;

    @GetMapping("/{bookingId}")
    public String paymentPage(@PathVariable int bookingId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) return "redirect:/bookingHistory";

        model.addAttribute("booking", booking);
        model.addAttribute("methods", PaymentMethod.values());
        return "payment";
    }

    @PostMapping("/process")
    public String processPayment(@RequestParam int bookingId,
                                 @RequestParam PaymentMethod paymentMethod,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        Payment payment = paymentService.processPayment(bookingId, paymentMethod);
        if (payment == null) return "redirect:/bookingHistory";

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            return "redirect:/payment/success/" + payment.getId();
        } else {
            return "redirect:/payment/failure/" + payment.getId();
        }
    }

    @GetMapping("/success/{paymentId}")
    public String successPage(@PathVariable int paymentId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) return "redirect:/bookingHistory";

        model.addAttribute("payment", payment);
        return "payment-success";
    }

    @GetMapping("/failure/{paymentId}")
    public String failurePage(@PathVariable int paymentId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginPage";

        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) return "redirect:/bookingHistory";

        model.addAttribute("payment", payment);
        return "payment-failure";
    }
}
