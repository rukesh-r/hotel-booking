package com.hotel_booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel_booking.model.Booking;
import com.hotel_booking.model.Room;
import com.hotel_booking.repository.BookingRepository;
import com.hotel_booking.repository.RoomRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository repo;
    @Autowired
    private RoomRepository roomRepository;

    public Booking saveBooking(Booking booking){

        if(booking == null) return null;

        // 🔥 Fetch room from DB
        Room dbRoom = roomRepository.findById(booking.getRoom().getId()).orElse(null);

        if(dbRoom == null) return null;

        // 🔥 Check availability
        if(dbRoom.getAvailableRooms() <= 0){
            return null;
        }

        // 🔥 Reduce room count
        dbRoom.setAvailableRooms(dbRoom.getAvailableRooms() - 1);
        roomRepository.save(dbRoom);

        // 🔥 IMPORTANT: attach full room object
        booking.setRoom(dbRoom);

        return repo.save(booking);
    }
    public void cancelBooking(int bookingId){

        Booking booking = repo.findById(bookingId).orElse(null);

        if(booking == null){
            return;
        }

        Room room = booking.getRoom();

        if(room != null){
            Room dbRoom = roomRepository.findById(room.getId()).orElse(null);

            if(dbRoom != null){
                dbRoom.setAvailableRooms(dbRoom.getAvailableRooms() + 1);
                roomRepository.save(dbRoom);
            }
        }

        repo.deleteById(bookingId);
    }

    public List<Booking> getUserBookings(int userId){
        return repo.findByUser_Id(userId);
    }
    
}