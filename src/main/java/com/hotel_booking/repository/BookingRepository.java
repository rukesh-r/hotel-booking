package com.hotel_booking.repository;

import com.hotel_booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser_Id(int userId);

    @Query("SELECT SUM(b.totalPrice) FROM Booking b")
    Double getTotalRevenue();

    // Returns [month(1-12), bookingCount]
    @Query("SELECT MONTH(b.checkIn), COUNT(b) FROM Booking b GROUP BY MONTH(b.checkIn) ORDER BY MONTH(b.checkIn)")
    List<Object[]> getMonthlyBookingCounts();

    // Returns [month(1-12), totalRevenue]
    @Query("SELECT MONTH(b.checkIn), SUM(b.totalPrice) FROM Booking b GROUP BY MONTH(b.checkIn) ORDER BY MONTH(b.checkIn)")
    List<Object[]> getMonthlyRevenue();

    // Returns [hotelName, bookingCount]
    @Query("SELECT b.room.hotel.name, COUNT(b) FROM Booking b GROUP BY b.room.hotel.name ORDER BY COUNT(b) DESC")
    List<Object[]> getBookingCountByHotel();
}
