-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 04, 2017 at 03:17 AM
-- Server version: 10.1.24-MariaDB
-- PHP Version: 7.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id3237782_medicine`
--

-- --------------------------------------------------------

--
-- Table structure for table `Medicines`
--

CREATE TABLE `Medicines` (
  `OfficialName` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `Uses` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `Warnings` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `Ingredients` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `24hOverdose` int(32) NOT NULL DEFAULT '0',
  `oneOverdose` int(32) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Messages`
--

CREATE TABLE `Messages` (
  `MsgID` bigint(15) NOT NULL,
  `MsgTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Subject` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `MsgText` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `Sender` text COLLATE utf8_unicode_ci NOT NULL,
  `MsgStatus` enum('Read','Deleted','Unread','Trash') COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `OffbrandName`
--

CREATE TABLE `OffbrandName` (
  `Official` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `Alias` varchar(32) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Schedule`
--

CREATE TABLE `Schedule` (
  `UserN` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `MedNa` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `Day` int(2) NOT NULL,
  `Hour` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `Min` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `intentID` int(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `UserMeds`
--

CREATE TABLE `UserMeds` (
  `User` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `MedName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `PerBottle` int(5) NOT NULL,
  `Dosage` int(5) NOT NULL,
  `AmtLeft` int(5) NOT NULL,
  `AlarmCount` int(2) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `Username` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `Email` text COLLATE utf8_unicode_ci NOT NULL,
  `Status` enum('Active','Banned','Admin','Deleted') COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Medicines`
--
ALTER TABLE `Medicines`
  ADD PRIMARY KEY (`OfficialName`);

--
-- Indexes for table `Messages`
--
ALTER TABLE `Messages`
  ADD PRIMARY KEY (`MsgID`);

--
-- Indexes for table `OffbrandName`
--
ALTER TABLE `OffbrandName`
  ADD PRIMARY KEY (`Official`,`Alias`);

--
-- Indexes for table `Schedule`
--
ALTER TABLE `Schedule`
  ADD PRIMARY KEY (`UserN`,`MedNa`,`intentID`),
  ADD KEY `MedNa` (`MedNa`);

--
-- Indexes for table `UserMeds`
--
ALTER TABLE `UserMeds`
  ADD PRIMARY KEY (`User`,`MedName`),
  ADD KEY `MedName` (`MedName`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`Username`),
  ADD UNIQUE KEY `Username` (`Username`),
  ADD KEY `Username_2` (`Username`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `OffbrandName`
--
ALTER TABLE `OffbrandName`
  ADD CONSTRAINT `OffbrandName_ibfk_1` FOREIGN KEY (`Official`) REFERENCES `Medicines` (`OfficialName`) ON DELETE CASCADE;

--
-- Constraints for table `Schedule`
--
ALTER TABLE `Schedule`
  ADD CONSTRAINT `Schedule_ibfk_1` FOREIGN KEY (`UserN`) REFERENCES `Users` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Schedule_ibfk_2` FOREIGN KEY (`MedNa`) REFERENCES `UserMeds` (`MedName`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `UserMeds`
--
ALTER TABLE `UserMeds`
  ADD CONSTRAINT `UserMeds_ibfk_1` FOREIGN KEY (`User`) REFERENCES `Users` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
