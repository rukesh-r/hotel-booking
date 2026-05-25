package com.hotel_booking.service;

import com.hotel_booking.model.Hotel;
import com.hotel_booking.model.Review;
import com.hotel_booking.model.User;
import com.hotel_booking.repository.HotelRepository;
import com.hotel_booking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public List<Review> getReviewsByHotel(int hotelId) {
        return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId);
    }

    public double getAverageRating(int hotelId) {
        return reviewRepository.findAverageRatingByHotelId(hotelId)
                .map(avg -> Math.round(avg * 10.0) / 10.0)
                .orElse(0.0);
    }

    public long getReviewCount(int hotelId) {
        return reviewRepository.countByHotelId(hotelId);
    }

    public boolean hasUserReviewed(int userId, int hotelId) {
        return reviewRepository.existsByUserIdAndHotelId(userId, hotelId);
    }

    // Returns error message or null on success
    public String addReview(User user, int hotelId, int rating, String comment) {
        if (comment == null || comment.trim().isEmpty()) {
            return "Review comment cannot be empty.";
        }
        if (rating < 1 || rating > 5) {
            return "Rating must be between 1 and 5.";
        }
        if (reviewRepository.existsByUserIdAndHotelId(user.getId(), hotelId)) {
            return "You have already reviewed this hotel.";
        }
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) {
            return "Hotel not found.";
        }
        Review review = new Review();
        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(rating);
        review.setComment(comment.trim());
        reviewRepository.save(review);
        return null;
    }
}
