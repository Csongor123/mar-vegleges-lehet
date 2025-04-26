-- Sample events
INSERT INTO event (name, location, date, category) VALUES
-- Running
('Running - Annual Marathon', 'City Park', '2025-06-15', 'Running'),
('Running - Charity Run', 'River Park', '2026-03-01', 'Running'),

-- Swimming
('Swimming - Championship', 'Olympic Pool', '2025-07-10', 'Swimming'),
('Swimming - Old Swim Meet', 'Local Pool', '2023-05-22', 'Swimming'),

-- Basketball
('Basketball - Tournament', 'Sports Arena', '2025-08-05', 'Basketball'),

-- Football
('Football - Cup', 'National Stadium', '2025-09-01', 'Football'),
('Football - League Match', 'Debrecen Arena', '2025-09-15', 'Football'),

-- Tennis
('Tennis - Open', 'Grand Court', '2025-10-05', 'Tennis'),

-- Röplabda
('Volleyball - National Championship', 'Beach Arena', '2025-08-20', 'Röplabda'),

-- Kézilabda
('Handball - League Final', 'City Hall', '2025-11-12', 'Kézilabda'),

-- Jégkorong
('Ice Hockey - Winter Cup', 'Ice Rink Stadium', '2025-12-01', 'Jégkorong'),

-- Kerékpározás
('Cycling - City Tour', 'City Streets', '2025-05-18', 'Kerékpározás'),

-- Vívás
('Fencing - Summer Tournament', 'Grand Hall', '2025-06-05', 'Vívás'),

-- Asztalitenisz
('Table Tennis - Open Cup', 'Community Center', '2025-07-01', 'Asztalitenisz');



-- Sample participants
INSERT INTO participant (name, age, email, activity_date, event_id) VALUES
-- Running
('John Doe', 35, 'john@example.com', '2025-04-25', 1),
('Jane Smith', 28, 'jane@example.com', '2025-04-25', 1),
('New Runner', 19, 'newrunner@example.com', '2025-04-25', 2),

-- Swimming
('Michael Johnson', 30, 'michael@example.com', '2025-04-25', 3),
('Emma Waters', 26, 'emma@example.com', '2025-04-25', 4),

-- Basketball
('James Hooper', 22, 'james@example.com', '2025-04-25', 5),

-- Football
('David Beckham', 45, 'david@example.com', '2025-04-25', 6),
('Cristiano Ronaldo', 39, 'ronaldo@example.com', '2025-04-25', 7),

-- Tennis
('Serena Williams', 42, 'serena@example.com', '2025-04-25', 8),

-- Röplabda
('Lili Horváth', 24, 'lili@example.com', '2025-04-25', 9),
('Ádám Nagy', 27, 'adam@example.com', '2025-04-25', 9),

-- Kézilabda
('Péter Kovács', 29, 'peter@example.com', '2025-04-25', 10),
('Judit Tóth', 31, 'judit@example.com', '2025-04-25', 10),

-- Jégkorong
('Martin Kiss', 32, 'martin@example.com', '2025-04-25', 11),
('Dániel Major', 34, 'daniel@example.com', '2025-04-25', 11),

-- Kerékpározás
('Norbert Varga', 36, 'norbert@example.com', '2025-04-25', 12),
('Krisztina Szabó', 25, 'krisztina@example.com', '2025-04-25', 12),

-- Vívás
('Gergő Farkas', 21, 'gergo@example.com', '2025-04-25', 13),

-- Asztalitenisz
('Tamás Németh', 23, 'tamas@example.com', '2025-04-25', 14);
