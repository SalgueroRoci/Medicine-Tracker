-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 04, 2017 at 03:21 AM
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

--
-- Dumping data for table `Medicines`
--

INSERT INTO `Medicines` (`OfficialName`, `Uses`, `Warnings`, `Ingredients`, `24hOverdose`, `oneOverdose`) VALUES
('Meloxicam', 'Meloxicam is used to treat arthritis. It reduces pain, swelling, and stiffness of the joints. Meloxicam is known as a nonsteroidal anti-inflammatory drug (NSAID).', 'Stomach upset, nausea, dizziness, or diarrhea may occur. \r\nSymptoms of overdose: slow/shallow breathing, extreme drowsiness, severe stomach pain, vomit that looks like coffee grounds. The maximum amount of meloxicam for adults is 15 (mg) per day.', 'Mobic (Meloxicam)', 15, 15),
('Tylenol', 'Temporarily relieves minor aches and pains \r\nFor headaches and fevers', 'Liver warning. \r\nAllergy alert: Do not use with any other drug containing acetaminophen', 'Acetaminophen 325 mg', 4000, 1000),
('Viagra', 'Viagra is used to treat erectile dysfunction (impotence) in men. \r\nRevatio, which is used to treat pulmonary arterial hypertension and improve exercise capacity in men and women.', 'Side effects: dyspepsia, headache, visual disturbance, and flushing, nasal congestion. \r\nDon\'t take Viagra if you\'re taking nitrate drugs for chest pain or heart problems. \r\n\r\nOverdose Symptoms:  Chest pain, Nausea, Irregular heartbeat, or fainting', 'Sildenafil citrate ', 0, 0);

--
-- Dumping data for table `Messages`
--

INSERT INTO `Messages` (`MsgID`, `MsgTime`, `Subject`, `MsgText`, `Sender`, `MsgStatus`) VALUES
(173, '2017-12-03 19:15:41', 'y', 'k', 'teamobilecorp@gmail.com', 'Trash'),
(283, '2017-12-03 19:15:37', 't', 'hh', 'teamobilecorp@gmail.com', 'Trash'),
(803, '2017-12-03 19:16:08', 'Application Doesn\'t Open ', 'The App will not run on Android 4.3', 'test@gmail.com', 'Read');

--
-- Dumping data for table `Schedule`
--

INSERT INTO `Schedule` (`UserN`, `MedNa`, `Day`, `Hour`, `Min`, `intentID`) VALUES
('Austin', 'newest', 4, '21', '58', 279351326),
('Austin', 'potato', 4, '21', '39', 278181718),
('Austin', 'test123', 4, '21', '35', 277952311),
('ro', 'Tylenol', 5, '19', '33', 357080648),
('user1', 'med1', 0, '14', '06', 157491064),
('user1', 'med1', 1, '14', '06', 157491769),
('user1', 'med1', 2, '14', '06', 157492474),
('user1', 'med1', 6, '14', '06', 157493179),
('vmdelfin', 'Mucinex', 1, '13', '10', 436232841),
('vmdelfin', 'Mucinex', 2, '13', '10', 436233546),
('vmdelfin', 'Mucinex', 3, '13', '10', 436234251),
('vmdelfin', 'Tylenol', 0, '17', '41', 436744875),
('vmdelfin', 'Tylenol', 6, '17', '41', 436745580),
('vmdelfin', 'Zyrtec', 1, '12', '00', 435958304),
('vmdelfin', 'Zyrtec', 3, '12', '00', 435959009),
('vmdelfin', 'Zyrtec', 5, '12', '00', 435959714);

--
-- Dumping data for table `UserMeds`
--

INSERT INTO `UserMeds` (`User`, `MedName`, `PerBottle`, `Dosage`, `AmtLeft`, `AlarmCount`) VALUES
('Austin', 'newest', 36, 25, 36, 1),
('Austin', 'potato', 25, 14, 25, 1),
('Austin', 'test123', 36, 25, 36, 1),
('ro', 'Tylenol', 50, 5, 49, 1),
('user1', 'med1', 100, 1, 100, 1),
('vmdelfin', 'Mucinex', 16, 325, 16, 1),
('vmdelfin', 'Tylenol', 24, 500, 24, 1),
('vmdelfin', 'Zyrtec', 30, 10, 30, 1);

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`Username`, `Password`, `Email`, `Status`) VALUES
('Admin', '073817c4395b7f718f8e356e97073bd7', 'teamobilecorp@gmail.com', 'Admin'),
('Austin', '305e4f55ce823e111a46a9d500bcb86c', 'mytest@aol.com', 'Active'),
('ro', 'e10adc3949ba59abbe56e057f20f883e', 'test@gmail.com', 'Active'),
('user', '22d7fe8c185003c98f97e5d6ced420c7', '123@hnail.com', 'Active'),
('user1', '22d7fe8c185003c98f97e5d6ced420c7', 'qwe@qq.com', 'Active'),
('vmdelfin', '28d203a563a3dcfab9dd76540bf1f84f', 'vmdelfin@csu.fullerton.edu', 'Active');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
