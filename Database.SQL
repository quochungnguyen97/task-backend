-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 17, 2022 at 09:26 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tasks`
--

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `uuid` varchar(256) COLLATE utf8_vietnamese_ci NOT NULL,
  `title` text COLLATE utf8_vietnamese_ci NOT NULL,
  `completed` tinyint(1) NOT NULL,
  `status` int(11) NOT NULL,
  `deadline` bigint(11) NOT NULL,
  `created_time` bigint(11) NOT NULL,
  `modified_time` bigint(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `username` varchar(256) COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`uuid`, `title`, `completed`, `status`, `deadline`, `created_time`, `modified_time`, `deleted`, `username`) VALUES
('929fe24d-68ab-4557-bb7b-0de1232f921e', 'Cook dinner', 1, 1, -1, 1658041661743, 1658042688627, 1, 'quochung'),
('asdfasdf', 'Go shopping', 1, 1, 123, 123, 123, 0, 'quochung'),
('asss', 'Go shopping', 1, 1, 123, 123, 123, 0, 'quocssshung');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(256) COLLATE utf8_vietnamese_ci NOT NULL,
  `password` varchar(256) COLLATE utf8_vietnamese_ci NOT NULL,
  `display_name` text COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`, `display_name`) VALUES
('quochung', 'hung1234', 'Nguyen Quoc Hung');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`uuid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
