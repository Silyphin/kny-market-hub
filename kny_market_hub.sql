-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 03, 2025 at 05:05 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kny_market_hub`
--

-- --------------------------------------------------------

--
-- Table structure for table `markets`
--

CREATE TABLE `markets` (
  `id` bigint(20) NOT NULL,
  `google_place_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `address` text NOT NULL,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `opening_time` time DEFAULT NULL,
  `closing_time` time DEFAULT NULL,
  `description` text DEFAULT NULL,
  `specialties` text DEFAULT NULL,
  `highlights` text DEFAULT NULL,
  `is_covered` tinyint(1) DEFAULT 0,
  `crowd_level_morning` enum('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
  `crowd_level_afternoon` enum('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
  `crowd_level_evening` enum('LOW','MEDIUM','HIGH') DEFAULT 'LOW',
  `data_source` enum('LOCAL','GOOGLE','HYBRID') DEFAULT 'HYBRID',
  `last_google_sync` timestamp NULL DEFAULT NULL,
  `google_rating` decimal(2,1) DEFAULT NULL,
  `google_total_ratings` int(11) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `website` varchar(500) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `markets`
--

INSERT INTO `markets` (`id`, `google_place_id`, `name`, `address`, `latitude`, `longitude`, `opening_time`, `closing_time`, `description`, `specialties`, `highlights`, `is_covered`, `crowd_level_morning`, `crowd_level_afternoon`, `crowd_level_evening`, `data_source`, `last_google_sync`, `google_rating`, `google_total_ratings`, `phone_number`, `website`, `created_at`, `updated_at`) VALUES
(1, 'ChIJrfDZImDDSjARFs51Q2biJqQ', 'Pulau Tikus Market', 'Jalan Pasar, Pulau Tikus, 10350 George Town', 5.43130000, 100.31140000, '06:00:00', '12:00:00', 'Traditional wet market with fresh produce and local delicacies', 'Fresh seafood, tropical fruits, local vegetables, morning dim sum', 'Popular local market, variety of fresh produce, good prices', 0, 'HIGH', 'MEDIUM', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-06-28 03:31:48', '2025-07-03 02:57:26'),
(2, 'ChIJDUY6ZMjCSjARltdklMUx924', 'Tanjung Bungah Market', 'Jalan Tanjung Bungah, 11200 Tanjung Bungah', 5.45860000, 100.28970000, '06:30:00', '11:30:00', 'Seaside market with great sea breeze', 'Fresh fish, morning dim sum, local snacks, seasonal fruits', 'Seaside market with sea breeze, fresh catch, peaceful environment', 0, 'MEDIUM', 'LOW', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-06-28 03:31:48', '2025-07-03 02:57:27'),
(3, 'ChIJH2nWHZfDSjARnmlmTw6Zi5k', 'Chowrasta Market', 'Jalan Penang, George Town, 10100 Penang', 5.41420000, 100.33140000, '06:30:00', '13:00:00', 'Historic market operating since late 1800s in UNESCO World Heritage Site', 'Pickled nutmegs, tau sar pneah, fresh produce, local snacks, dried goods', 'One of Malaysia oldest markets, UNESCO heritage area, traditional preserved foods', 1, 'HIGH', 'MEDIUM', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:25'),
(4, 'ChIJO7EDsn_DSjARNZkAuySZmPo', 'Cecil Street Market', 'Lebuh Cecil, George Town, 10300 Penang', 5.41380000, 100.33280000, '06:00:00', '12:00:00', 'Popular local market known for authentic Penang street food', 'Hokkien Mee, Curry Mee, Char Kway Teow, fresh seafood', 'Authentic local food experience, famous noodle dishes', 0, 'HIGH', 'MEDIUM', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:25'),
(5, 'ChIJE9_NRVDDSjARi_ixJSZ7Xk8', 'Air Itam Market', 'Jalan Pasar, 11500 Air Itam, Penang', 5.39880000, 100.27160000, '06:00:00', '12:00:00', 'Traditional wet market near Kek Lok Si Temple', 'Fresh vegetables, fruits, meat, local delicacies, duck kway teow', 'Near Kek Lok Si Temple, famous duck noodle soup, local produce', 0, 'HIGH', 'MEDIUM', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:22'),
(6, 'ChIJjZPDCZHDSjARXXHQCWTBlb4', 'Campbell Street Market', 'Campbell Street, George Town, 10100 Penang', 5.41590000, 100.33410000, '06:30:00', '14:00:00', 'Historic city center market with diverse offerings', 'Fresh produce, textiles, household items, local snacks', 'City center location, diverse shopping options', 1, 'HIGH', 'MEDIUM', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:24'),
(7, 'ChIJyeGxpWvASjARqVysjAvtpUo', 'Bayan Lepas Market', 'Jalan Mahsuri, Bayan Lepas, 11900 Penang', 5.29760000, 100.25890000, '06:00:00', '11:00:00', 'Modern suburban wet market serving local community', 'Fresh seafood, vegetables, fruits, local breakfast', 'Modern facilities, ample parking, local community hub', 1, 'MEDIUM', 'LOW', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:24'),
(8, 'ChIJbwkhPdbDSjARBhrRX-uyee0', 'Jelutong Market', 'Jalan Jelutong, 11600 Jelutong, Penang', 5.39420000, 100.31850000, '06:00:00', '12:00:00', 'Local neighborhood market with fresh produce', 'Fresh fish, vegetables, fruits, breakfast stalls', 'Local neighborhood market, good value for money', 0, 'MEDIUM', 'LOW', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:26'),
(9, 'ChIJ7f1P1CbCSjARKBNVwGhOux4', 'Farlim Market', 'Jalan Farlim, Air Itam, 11500 Penang', 5.40120000, 100.28350000, '06:00:00', '11:30:00', 'Suburban market serving Air Itam area', 'Fresh produce, local breakfast, household items', 'Suburban location, good parking, local community market', 0, 'MEDIUM', 'LOW', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:26'),
(10, 'ChIJVzE2qGHqSjARqsovzTej6sk', 'Balik Pulau Market', 'Jalan Besar Balik Pulau, 11000 Balik Pulau, Penang', 5.34780000, 100.24890000, '06:00:00', '12:00:00', 'Rural market famous for local fruits and produce', 'Nutmeg, durian, local fruits, vegetables, traditional snacks', 'Famous for nutmeg and local fruits, rural authentic experience', 0, 'HIGH', 'MEDIUM', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:23'),
(11, 'ChIJrzR0T_znSjAR6Z2yyjre7c4', 'Batu Ferringhi Market', 'Jalan Batu Ferringhi, 11100 Batu Ferringhi, Penang', 5.47120000, 100.24680000, '06:30:00', '11:00:00', 'Coastal market serving tourist and local community', 'Fresh seafood, tropical fruits, vegetables', 'Beach area market, fresh seafood, tourist-friendly', 0, 'MEDIUM', 'LOW', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:23'),
(12, 'ChIJl7eQ9xnASjARG226Ug4ou1s', 'Sungai Ara Market', 'Jalan Sungai Ara, 11900 Bayan Lepas, Penang', 5.31450000, 100.27890000, '06:00:00', '11:30:00', 'Local community market with good variety', 'Fresh produce, seafood, breakfast stalls, household goods', 'Local community market, good variety and prices', 0, 'MEDIUM', 'LOW', 'LOW', 'LOCAL', NULL, NULL, NULL, NULL, NULL, '2025-07-01 06:19:12', '2025-07-03 02:57:27');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `provider` varchar(20) DEFAULT NULL,
  `provider_id` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `is_active` bit(1) DEFAULT NULL,
  `oauth_id` varchar(255) DEFAULT NULL,
  `oauth_provider` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `profile_picture` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `name`, `provider`, `provider_id`, `created_at`, `updated_at`, `is_active`, `oauth_id`, `oauth_provider`, `phone_number`, `profile_picture`) VALUES
(1, 'thambridget@gmail.com', NULL, 'Bridget Tham', 'facebook', '30410963648518576', '2025-07-02 09:02:52', '2025-07-02 15:32:21', b'1', '30410963648518576', 'facebook', NULL, NULL),
(2, 'ddd@gmail.com', '$2a$10$cQEFdQ17kyVL6dG4OyBOluzJjLYmwGXIJJyips75FzfGtlwoxjYQO', 'Bridget Tham', 'email', NULL, '2025-07-02 14:28:11', '2025-07-02 14:28:11', b'1', NULL, NULL, NULL, NULL),
(3, 'abc@gmail.com', '$2a$10$yzYY5qpHvqT61Qpbhe3gBuUHZVHDt/xLrTT/HqRijQE2RyzsP3TU6', 'ABC ABC', 'email', NULL, '2025-07-02 15:12:25', '2025-07-02 15:12:25', b'1', NULL, NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `markets`
--
ALTER TABLE `markets`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `google_place_id` (`google_place_id`),
  ADD KEY `idx_google_place_id` (`google_place_id`),
  ADD KEY `idx_data_source` (`data_source`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `markets`
--
ALTER TABLE `markets`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
