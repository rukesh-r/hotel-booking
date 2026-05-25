package com.hotel_booking.repository;

import com.hotel_booking.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByHotelIdOrderByCreatedAtDesc(int hotelId);

    boolean existsByUserIdAndHotelId(int userId, int hotelId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.hotel.id = :hotelId")
    Optional<Double> findAverageRatingByHotelId(@Param("hotelId") int hotelId);

    long countByHotelId(int hotelId);
}
