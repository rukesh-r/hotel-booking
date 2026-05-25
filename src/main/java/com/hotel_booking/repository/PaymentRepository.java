package com.hotel_booking.repository;

import com.hotel_booking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByBooking_Id(int bookingId);
}
