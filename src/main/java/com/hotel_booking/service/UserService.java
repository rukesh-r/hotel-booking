package com.hotel_booking.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel_booking.model.User;
import com.hotel_booking.repository.UserRepository;
@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    public User register(User user){

        user.setRole("USER"); // default role

        return repo.save(user);
    }
    public User login(String email,String password){
        return repo.findByEmailAndPassword(email,password);
    }
    
}