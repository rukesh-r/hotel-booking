package com.hotel_booking.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String roomType;
    private double price;
    @Column(name = "available_rooms")
    private int availableRooms = 0;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomImage> images;
    public List<RoomImage> getImages() {
        return images;
    }

    public void setImages(List<RoomImage> images) {
        this.images = images;
    }
    public int getAvailableRooms() {
		return availableRooms;
	}

	public void setAvailableRooms(int availableRooms) {
		this.availableRooms = availableRooms;
	}

	@ManyToOne
    private Hotel hotel;
	@Column(name = "image_url")
	private String imageUrl;
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
    
    
}