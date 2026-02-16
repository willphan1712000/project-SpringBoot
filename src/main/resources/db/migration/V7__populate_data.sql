-- 1. Insert 10 Records into 'categories'
-- We explicitly set IDs to ensure they map correctly to the products later.
INSERT INTO categories (id, name) VALUES 
(1, 'Smartphones'),
(2, 'Laptops'),
(3, 'Headphones'),
(4, 'Televisions'),
(5, 'Cameras'),
(6, 'Smart Watches'),
(7, 'Gaming Consoles'),
(8, 'Home Audio'),
(9, 'Monitors'),
(10, 'Accessories');

-- 2. Insert 20 Records into 'products'
-- Note: category_id corresponds to the IDs above (1-10)
INSERT INTO products (name, price, `description`, category_id) VALUES 
('Galaxy S24 Ultra', 1199.99, 'Latest flagship smartphone with AI features and titanium frame.', 1),
('iPhone 15 Pro', 999.00, 'Titanium design, A17 Pro chip, and advanced camera system.', 1),
('MacBook Air M3', 1099.00, 'Supercharged by M3, incredibly thin and light laptop.', 2),
('Dell XPS 15', 1499.50, 'High-performance laptop with InfinityEdge display for creators.', 2),
('Sony WH-1000XM5', 348.00, 'Industry-leading noise canceling wireless headphones.', 3),
('AirPods Pro (2nd Gen)', 249.00, 'Wireless earbuds with active noise cancellation and transparency mode.', 3),
('LG OLED C3 65"', 1699.99, 'Premium OLED TV with advanced evo panel and brightness booster.', 4),
('Samsung QN90C', 1499.99, 'Neo QLED 4K TV with Mini LED technology.', 4),
('Sony Alpha a7 IV', 2498.00, 'Full-frame mirrorless camera with 33MP sensor and 4K video.', 5),
('Canon EOS R6 Mark II', 2299.00, 'High-speed mirrorless camera perfect for action photography.', 5),
('Apple Watch Series 9', 399.00, 'Advanced health sensors and double tap gesture control.', 6),
('Garmin Fenix 7', 699.99, 'Rugged multisport GPS watch with solar charging capabilities.', 6),
('PlayStation 5', 499.99, 'Next-gen console with haptic feedback and 3D audio.', 7),
('Nintendo Switch OLED', 349.99, 'Versatile gaming console with 7-inch OLED screen.', 7),
('Sonos Arc Soundbar', 899.00, 'Premium smart soundbar for TV, movies, music, and gaming.', 8),
('Bose SoundLink Flex', 149.00, 'Portable Bluetooth speaker with waterproof design.', 8),
('Dell UltraSharp 27"', 539.99, '4K USB-C Hub Monitor with wide color coverage.', 9),
('Samsung Odyssey G9', 1299.99, '49-inch curved gaming monitor with 240Hz refresh rate.', 9),
('Logitech MX Master 3S', 99.99, 'Performance wireless mouse with ultra-fast scrolling.', 10),
('Anker 737 Power Bank', 149.99, '24,000mAh portable charger with 140W fast charging.', 10);