-- ===== ADMIN ACCOUNT =====
-- Password is BCrypt hash of: Admin@123
INSERT IGNORE INTO user (name, email, password, role)
VALUES ('Admin', 'admin@stayease.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ADMIN');

-- ===== HOTELS =====
INSERT IGNORE INTO hotel (name, location, description, image_url) VALUES
('Taj Hotel & Convention Centre', 'Mumbai, Maharashtra', 'A luxurious 5-star hotel offering world-class amenities, fine dining, and stunning sea views in the heart of Mumbai.', '/uploads/hotel taj.jpg'),
('Hotel Paradise', 'Sikar, Rajasthan', 'A serene getaway nestled in the heart of Rajasthan, offering traditional hospitality with modern comforts.', '/uploads/hotel-paradise-khatushyamjika-sikar-hotels-1kqqh3azf0.avif'),
('Taj Tirupati', 'Tirupati, Andhra Pradesh', 'A premium hotel near the sacred Tirumala hills, offering peaceful stays with excellent pilgrimage facilities.', '/uploads/tajtirupati'),
('The Grand Residency', 'Delhi, NCR', 'An elegant business hotel in the capital city with spacious rooms, rooftop dining, and premium conference facilities.', '/uploads/ABC.webp'),
('Ocean Pearl Resort', 'Goa', 'A beachfront resort with stunning ocean views, water sports, infinity pool, and vibrant nightlife nearby.', '/uploads/download.jpeg'),
('Mountain View Inn', 'Manali, Himachal Pradesh', 'A cozy mountain retreat surrounded by snow-capped peaks, perfect for adventure lovers and nature enthusiasts.', '/uploads/download (1).jpeg');

-- ===== ROOMS =====
-- Taj Hotel & Convention Centre
INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Deluxe Room', 8500.00, 5, '/uploads/download (2).jpeg', id FROM hotel WHERE name = 'Taj Hotel & Convention Centre';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Suite', 18000.00, 3, '/uploads/download (3).jpeg', id FROM hotel WHERE name = 'Taj Hotel & Convention Centre';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Presidential Suite', 35000.00, 1, '/uploads/room1.jpeg', id FROM hotel WHERE name = 'Taj Hotel & Convention Centre';

-- Hotel Paradise
INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Standard Room', 2500.00, 8, '/uploads/download.jpeg', id FROM hotel WHERE name = 'Hotel Paradise';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Deluxe Room', 4200.00, 4, '/uploads/download (1).jpeg', id FROM hotel WHERE name = 'Hotel Paradise';

-- Taj Tirupati
INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Standard Room', 3500.00, 6, '/uploads/download (2).jpeg', id FROM hotel WHERE name = 'Taj Tirupati';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Deluxe Room', 6500.00, 3, '/uploads/download (3).jpeg', id FROM hotel WHERE name = 'Taj Tirupati';

-- The Grand Residency
INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Business Room', 5500.00, 7, '/uploads/room1.jpeg', id FROM hotel WHERE name = 'The Grand Residency';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Suite', 12000.00, 2, '/uploads/download.jpeg', id FROM hotel WHERE name = 'The Grand Residency';

-- Ocean Pearl Resort
INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Beach View Room', 7000.00, 5, '/uploads/download (1).jpeg', id FROM hotel WHERE name = 'Ocean Pearl Resort';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Pool Villa', 15000.00, 2, '/uploads/download (2).jpeg', id FROM hotel WHERE name = 'Ocean Pearl Resort';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Standard Room', 4500.00, 0, '/uploads/download (3).jpeg', id FROM hotel WHERE name = 'Ocean Pearl Resort';

-- Mountain View Inn
INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Cozy Cabin', 3200.00, 4, '/uploads/room1.jpeg', id FROM hotel WHERE name = 'Mountain View Inn';

INSERT IGNORE INTO room (room_type, price, available_rooms, image_url, hotel_id)
SELECT 'Deluxe Mountain Suite', 6800.00, 2, '/uploads/download.jpeg', id FROM hotel WHERE name = 'Mountain View Inn';
