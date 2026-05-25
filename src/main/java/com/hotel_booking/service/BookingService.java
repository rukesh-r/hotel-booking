package com.hotel_booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel_booking.model.Booking;
import com.hotel_booking.model.Payment;
import com.hotel_booking.model.Room;
import com.hotel_booking.repository.BookingRepository;
import com.hotel_booking.repository.PaymentRepository;
import com.hotel_booking.repository.RoomRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository repo;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    public Booking saveBooking(Booking booking) {

        if (booking == null) return null;

        Room dbRoom = roomRepository.findById(booking.getRoom().getId()).orElse(null);
        if (dbRoom == null) return null;

        if (dbRoom.getAvailableRooms() <= 0) return null;

        dbRoom.setAvailableRooms(dbRoom.getAvailableRooms() - 1);
        roomRepository.save(dbRoom);

        booking.setRoom(dbRoom);
        // Email is sent by PaymentService after successful payment
        return repo.save(booking);
    }

    public void cancelBooking(int bookingId) {

        Booking booking = repo.findById(bookingId).orElse(null);
        if (booking == null) return;

        // Capture details before deletion for the email
        Booking snapshot = copyForEmail(booking);

        Room dbRoom = roomRepository.findById(booking.getRoom().getId()).orElse(null);
        if (dbRoom != null) {
            dbRoom.setAvailableRooms(dbRoom.getAvailableRooms() + 1);
            roomRepository.save(dbRoom);
        }

        // Delete linked payment first to avoid FK constraint violation
        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);
        if (payment != null) {
            paymentRepository.delete(payment);
        }

        repo.deleteById(bookingId);

        // Send cancellation email asynchronously
        emailService.sendCancellationNotice(snapshot);
    }

    public List<Booking> getUserBookings(int userId) {
        return repo.findByUser_Id(userId);
    }

    // Detached copy so the email has all data after the entity is deleted
    private Booking copyForEmail(Booking b) {
        Booking copy = new Booking();
        copy.setId(b.getId());
        copy.setUser(b.getUser());
        copy.setRoom(b.getRoom());
        copy.setCheckIn(b.getCheckIn());
        copy.setCheckOut(b.getCheckOut());
        copy.setTotalPrice(b.getTotalPrice());
        copy.setStatus(b.getStatus());
        copy.setBookingDate(b.getBookingDate());
        return copy;
    }
}
