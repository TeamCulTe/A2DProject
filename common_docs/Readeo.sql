-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Nov 22, 2018 at 12:00 PM
-- Server version: 5.6.38
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `Readeo`
--

-- --------------------------------------------------------

--
-- Table structure for table `Author`
--

CREATE TABLE `Author` (
  `id_author` int(11) NOT NULL,
  `name_author` varchar(50) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Book`
--

CREATE TABLE `Book` (
  `id_book` int(11) NOT NULL,
  `id_category` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `cover` varchar(50) NOT NULL,
  `summary` text NOT NULL,
  `date_published` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `BookList`
--

CREATE TABLE `BookList` (
  `id_book` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `type` varchar(50) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Category`
--

CREATE TABLE `Category` (
  `id_category` int(11) NOT NULL,
  `name_category` varchar(50) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `City`
--

CREATE TABLE `City` (
  `id_city` int(11) NOT NULL,
  `name_city` varchar(50) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Country`
--

CREATE TABLE `Country` (
  `id_country` int(11) NOT NULL,
  `name_country` varchar(50) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Profile`
--

CREATE TABLE `Profile` (
  `id_profile` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `avatar` varchar(50) NOT NULL,
  `description` varchar(50) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Quote`
--

CREATE TABLE `Quote` (
  `id_quote` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_book` int(11) NOT NULL,
  `quote` text NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Review`
--

CREATE TABLE `Review` (
  `id_user` int(11) NOT NULL,
  `id_book` int(11) NOT NULL,
  `review` text NOT NULL,
  `shared` tinyint(1) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `id_user` int(11) NOT NULL,
  `pseudo` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `id_profile` int(11) NOT NULL,
  `id_city` int(11) NOT NULL,
  `id_country` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Writer`
--

CREATE TABLE `Writer` (
  `id_author` int(11) NOT NULL,
  `id_book` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Author`
--
ALTER TABLE `Author`
  ADD PRIMARY KEY (`id_author`),
  ADD UNIQUE KEY `name` (`name_author`);

--
-- Indexes for table `Book`
--
ALTER TABLE `Book`
  ADD PRIMARY KEY (`id_book`),
  ADD UNIQUE KEY `title` (`title`),
  ADD KEY `Book_Category0_FK` (`id_category`);

--
-- Indexes for table `BookList`
--
ALTER TABLE `BookList`
  ADD PRIMARY KEY (`id_book`,`id_user`),
  ADD KEY `BookList_User1_FK` (`id_user`);

--
-- Indexes for table `Category`
--
ALTER TABLE `Category`
  ADD PRIMARY KEY (`id_category`),
  ADD UNIQUE KEY `Category_AK` (`name_category`);

--
-- Indexes for table `City`
--
ALTER TABLE `City`
  ADD PRIMARY KEY (`id_city`);

--
-- Indexes for table `Country`
--
ALTER TABLE `Country`
  ADD PRIMARY KEY (`id_country`),
  ADD UNIQUE KEY `Country_AK` (`name_country`);

--
-- Indexes for table `Profile`
--
ALTER TABLE `Profile`
  ADD PRIMARY KEY (`id_profile`),
  ADD UNIQUE KEY `Profile_User0_AK` (`id_user`);

--
-- Indexes for table `Quote`
--
ALTER TABLE `Quote`
  ADD PRIMARY KEY (`id_quote`),
  ADD KEY `Quote_User0_FK` (`id_user`),
  ADD KEY `Quote_Book1_FK` (`id_book`);

--
-- Indexes for table `Review`
--
ALTER TABLE `Review`
  ADD PRIMARY KEY (`id_book`,`id_user`),
  ADD KEY `Review_User1_FK` (`id_user`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `User_AK0` (`pseudo`),
  ADD UNIQUE KEY `User_Profile0_AK` (`id_profile`),
  ADD KEY `User_Country1_FK` (`id_country`),
  ADD KEY `User_City2_FK` (`id_city`);

--
-- Indexes for table `Writer`
--
ALTER TABLE `Writer`
  ADD PRIMARY KEY (`id_book`,`id_author`),
  ADD KEY `Writer_Author1_FK` (`id_author`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Author`
--
ALTER TABLE `Author`
  MODIFY `id_author` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Book`
--
ALTER TABLE `Book`
  MODIFY `id_book` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Category`
--
ALTER TABLE `Category`
  MODIFY `id_category` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `City`
--
ALTER TABLE `City`
  MODIFY `id_city` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Country`
--
ALTER TABLE `Country`
  MODIFY `id_country` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Profile`
--
ALTER TABLE `Profile`
  MODIFY `id_profile` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Quote`
--
ALTER TABLE `Quote`
  MODIFY `id_quote` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Book`
--
ALTER TABLE `Book`
  ADD CONSTRAINT `Book_Category0_FK` FOREIGN KEY (`id_category`) REFERENCES `read`.`Category` (`id_category`);

--
-- Constraints for table `BookList`
--
ALTER TABLE `BookList`
  ADD CONSTRAINT `BookList_Book0_FK` FOREIGN KEY (`id_book`) REFERENCES `book` (`id_book`),
  ADD CONSTRAINT `BookList_User1_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

--
-- Constraints for table `Profile`
--
ALTER TABLE `Profile`
  ADD CONSTRAINT `Profile_User0_FK` FOREIGN KEY (`id_user`) REFERENCES `read`.`User` (`id_user`);

--
-- Constraints for table `Quote`
--
ALTER TABLE `Quote`
  ADD CONSTRAINT `Quote_Book1_FK` FOREIGN KEY (`id_book`) REFERENCES `read`.`Book` (`id_book`),
  ADD CONSTRAINT `Quote_User0_FK` FOREIGN KEY (`id_user`) REFERENCES `read`.`User` (`id_user`);

--
-- Constraints for table `Review`
--
ALTER TABLE `Review`
  ADD CONSTRAINT `Review_Book0_FK` FOREIGN KEY (`id_book`) REFERENCES `read`.`Book` (`id_book`),
  ADD CONSTRAINT `Review_User1_FK` FOREIGN KEY (`id_user`) REFERENCES `read`.`User` (`id_user`);

--
-- Constraints for table `User`
--
ALTER TABLE `User`
  ADD CONSTRAINT `User_City2_FK` FOREIGN KEY (`id_city`) REFERENCES `read`.`City` (`id_city`),
  ADD CONSTRAINT `User_Country1_FK` FOREIGN KEY (`id_country`) REFERENCES `read`.`Country` (`id_country`),
  ADD CONSTRAINT `User_Profile0_FK` FOREIGN KEY (`id_profile`) REFERENCES `read`.`Profile` (`id_profile`);

--
-- Constraints for table `Writer`
--
ALTER TABLE `Writer`
  ADD CONSTRAINT `Writer_Author1_FK` FOREIGN KEY (`id_author`) REFERENCES `Author` (`id_author`),
  ADD CONSTRAINT `Writer_Book0_FK` FOREIGN KEY (`id_book`) REFERENCES `Book` (`id_book`);
