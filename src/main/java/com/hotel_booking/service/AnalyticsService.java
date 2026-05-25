package com.hotel_booking.service;

import com.hotel_booking.repository.BookingRepository;
import com.hotel_booking.repository.HotelRepository;
import com.hotel_booking.repository.RoomRepository;
import com.hotel_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private static final String[] MONTHS = {
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    };

    @Autowired private BookingRepository bookingRepo;
    @Autowired private HotelRepository   hotelRepo;
    @Autowired private RoomRepository    roomRepo;
    @Autowired private UserRepository    userRepo;

    public long getTotalUsers()    { return userRepo.count(); }
    public long getTotalHotels()   { return hotelRepo.count(); }
    public long getTotalRooms()    { return roomRepo.count(); }
    public long getTotalBookings() { return bookingRepo.count(); }

    public double getTotalRevenue() {
        Double rev = bookingRepo.getTotalRevenue();
        return rev != null ? rev : 0.0;
    }

    public long getTotalAvailableRooms() {
        Long avail = roomRepo.getTotalAvailableRooms();
        return avail != null ? avail : 0L;
    }

    // Returns 12-element array indexed by month (Jan=0 … Dec=11)
    public long[] getMonthlyBookings() {
        long[] data = new long[12];
        for (Object[] row : bookingRepo.getMonthlyBookingCounts()) {
            int month = ((Number) row[0]).intValue() - 1;
            data[month] = ((Number) row[1]).longValue();
        }
        return data;
    }

    public double[] getMonthlyRevenue() {
        double[] data = new double[12];
        for (Object[] row : bookingRepo.getMonthlyRevenue()) {
            int month = ((Number) row[0]).intValue() - 1;
            data[month] = ((Number) row[1]).doubleValue();
        }
        return data;
    }

    // Returns {hotelName -> bookingCount} ordered by count desc
    public Map<String, Long> getHotelBookingCounts() {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : bookingRepo.getBookingCountByHotel()) {
            map.put((String) row[0], ((Number) row[1]).longValue());
        }
        return map;
    }

    public String[] getMonthLabels() { return MONTHS; }
}
