package com.hotel_booking.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hotel_booking.model.Hotel;
import com.hotel_booking.model.Room;
import com.hotel_booking.model.RoomImage;
import com.hotel_booking.model.User;
import com.hotel_booking.repository.RoomImageRepository;
import com.hotel_booking.service.HotelService;
import com.hotel_booking.service.RoomService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomImageRepository roomImageRepo;
    @GetMapping
    public String adminPage(HttpSession session, Model model){

        User user = (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/loginPage";
        }

        if(!"ADMIN".equals(user.getRole())){
            return "redirect:/hotelsPage";
        }

        model.addAttribute("hotels", hotelService.getAllHotels()); // 🔥 ADD THIS
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin";
    }
    @PostMapping("/addHotel")
    public String addHotel(Hotel hotel,
                           @RequestParam("imageFile") MultipartFile file){

        try {
            if (!file.isEmpty()) {

                String fileName = file.getOriginalFilename();

                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 🔥 Save path in DB
                hotel.setImageUrl("/uploads/" + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        hotelService.saveHotel(hotel);

        return "redirect:/admin";
    }
    @PostMapping("/addRoom")
    public String addRoom(Room room,
                          @RequestParam("imageFile") MultipartFile file) {

        try {
            if (!file.isEmpty()) {

                String fileName = file.getOriginalFilename();

                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 🔥 Save path in DB
                room.setImageUrl("/uploads/" + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        roomService.saveRoom(room);

        return "redirect:/admin";
    }
    @PostMapping("/updateHotelImage")
    public String updateHotelImage(@RequestParam int hotelId,
                                   @RequestParam("imageFile") MultipartFile file){

        try {
            if (!file.isEmpty()) {

                String fileName = file.getOriginalFilename();

                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 🔥 Fetch existing hotel
                Hotel hotel = hotelService.getHotelById(hotelId);

                if(hotel != null){
                    hotel.setImageUrl("/uploads/" + fileName);
                    hotelService.saveHotel(hotel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin";
    }
    @PostMapping("/updateRoom")
    public String updateRoom(@RequestParam int roomId,
                             @RequestParam("imageFile") MultipartFile file){

        try {
            if (!file.isEmpty()) {

                String fileName = file.getOriginalFilename();

                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                Room room = roomService.getRoomById(roomId);

                if(room != null){
                    room.setImageUrl("/uploads/" + fileName);
                    roomService.saveRoom(room);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin";
    }
    @PostMapping("/addRoomImages")
    public String addRoomImages(@RequestParam int roomId,
                               @RequestParam("files") MultipartFile[] files){

        Room room = roomService.getRoomById(roomId);

        for(MultipartFile file : files){

            if(!file.isEmpty()){
                try {
                    String fileName = file.getOriginalFilename();

                    Path path = Paths.get("uploads/" + fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    RoomImage img = new RoomImage();
                    img.setImageUrl("/uploads/" + fileName);
                    img.setRoom(room);

                    roomImageRepo.save(img);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/admin";
    }
}