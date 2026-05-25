package com.hotel_booking.service;

import com.hotel_booking.model.Booking;
import com.hotel_booking.model.Booking.BookingStatus;
import com.hotel_booking.model.Payment;
import com.hotel_booking.model.Payment.PaymentMethod;
import com.hotel_booking.model.Payment.PaymentStatus;
import com.hotel_booking.repository.BookingRepository;
import com.hotel_booking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    /** Simulate payment — Cash on Arrival always succeeds; others succeed 90% of the time. */
    public Payment processPayment(int bookingId, PaymentMethod method) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) return null;

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(method);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());

        boolean success = (method == PaymentMethod.CASH_ON_ARRIVAL) || (Math.random() > 0.1);

        if (success) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            emailService.sendBookingConfirmation(booking);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
        }

        return paymentRepository.save(payment);
    }

    public Payment getByBookingId(int bookingId) {
        return paymentRepository.findByBooking_Id(bookingId).orElse(null);
    }
}
