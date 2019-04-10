-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Apr 10, 2019 at 09:50 AM
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
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Book`
--

CREATE TABLE `Book` (
  `id_book` int(11) NOT NULL,
  `id_category` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `cover` varchar(255) DEFAULT NULL,
  `summary` text,
  `date_published` int(4) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `BookList`
--

CREATE TABLE `BookList` (
  `id_user` int(11) NOT NULL,
  `id_book_list_type` int(11) NOT NULL,
  `id_book` int(11) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `BookListType`
--

CREATE TABLE `BookListType` (
  `id_book_list_type` int(11) NOT NULL,
  `name_book_list_type` varchar(50) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Category`
--

CREATE TABLE `Category` (
  `id_category` int(11) NOT NULL,
  `name_category` varchar(50) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `City`
--

CREATE TABLE `City` (
  `id_city` int(11) NOT NULL,
  `name_city` varchar(50) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Country`
--

CREATE TABLE `Country` (
  `id_country` int(11) NOT NULL,
  `name_country` varchar(50) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Profile`
--

CREATE TABLE `Profile` (
  `id_profile` int(11) NOT NULL,
  `avatar` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Writer`
--

CREATE TABLE `Writer` (
  `id_author` int(11) NOT NULL,
  `id_book` int(11) NOT NULL,
  `last_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  ADD UNIQUE KEY `u_name_author` (`name_author`) USING BTREE,
  ADD KEY `i_last_update` (`last_update`),
  ADD KEY `i_name_author` (`name_author`) USING BTREE;

--
-- Indexes for table `Book`
--
ALTER TABLE `Book`
  ADD PRIMARY KEY (`id_book`),
  ADD KEY `i_title` (`title`) USING BTREE,
  ADD KEY `i_id_category` (`id_category`) USING BTREE;

--
-- Indexes for table `BookList`
--
ALTER TABLE `BookList`
  ADD PRIMARY KEY (`id_user`,`id_book_list_type`,`id_book`),
  ADD KEY `i_last_update` (`last_update`),
  ADD KEY `i_id_book_list_type` (`id_book_list_type`) USING BTREE,
  ADD KEY `i_id_book` (`id_book`) USING BTREE;

--
-- Indexes for table `BookListType`
--
ALTER TABLE `BookListType`
  ADD PRIMARY KEY (`id_book_list_type`),
  ADD UNIQUE KEY `i_name_book_list_type` (`name_book_list_type`) USING BTREE,
  ADD KEY `i_last_update` (`last_update`);

--
-- Indexes for table `Category`
--
ALTER TABLE `Category`
  ADD PRIMARY KEY (`id_category`),
  ADD UNIQUE KEY `u_name_category` (`name_category`) USING BTREE,
  ADD KEY `i_last_update` (`last_update`),
  ADD KEY `i_name_category` (`name_category`) USING BTREE;

--
-- Indexes for table `City`
--
ALTER TABLE `City`
  ADD PRIMARY KEY (`id_city`),
  ADD UNIQUE KEY `u_name_city` (`name_city`) USING BTREE,
  ADD KEY `i_name_city` (`name_city`) USING BTREE,
  ADD KEY `i_last_update` (`last_update`);

--
-- Indexes for table `Country`
--
ALTER TABLE `Country`
  ADD PRIMARY KEY (`id_country`),
  ADD UNIQUE KEY `u_name_country` (`name_country`) USING BTREE,
  ADD KEY `i_name_country` (`name_country`) USING BTREE,
  ADD KEY `i_last_update` (`last_update`) USING BTREE;

--
-- Indexes for table `Profile`
--
ALTER TABLE `Profile`
  ADD PRIMARY KEY (`id_profile`),
  ADD KEY `i_last_update` (`last_update`) USING BTREE;

--
-- Indexes for table `Quote`
--
ALTER TABLE `Quote`
  ADD PRIMARY KEY (`id_quote`),
  ADD KEY `i_last_update` (`last_update`) USING BTREE,
  ADD KEY `i_id_user` (`id_user`) USING BTREE,
  ADD KEY `i_id_book` (`id_book`) USING BTREE;

--
-- Indexes for table `Review`
--
ALTER TABLE `Review`
  ADD PRIMARY KEY (`id_book`,`id_user`),
  ADD KEY `i_last_update` (`last_update`) USING BTREE,
  ADD KEY `i_id_user` (`id_user`) USING BTREE,
  ADD KEY `i_id_book` (`id_book`) USING BTREE;

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `u_pseudo` (`pseudo`) USING BTREE,
  ADD UNIQUE KEY `u_id_profile` (`id_profile`) USING BTREE,
  ADD KEY `i_last_update` (`last_update`),
  ADD KEY `u_email` (`email`) USING BTREE,
  ADD KEY `i_pseudo` (`pseudo`) USING BTREE,
  ADD KEY `i_email` (`email`) USING BTREE,
  ADD KEY `i_id_country` (`id_country`) USING BTREE,
  ADD KEY `i_id_city` (`id_city`) USING BTREE;

--
-- Indexes for table `Writer`
--
ALTER TABLE `Writer`
  ADD PRIMARY KEY (`id_book`,`id_author`),
  ADD KEY `i_last_update` (`last_update`),
  ADD KEY `i_id_author` (`id_author`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Author`
--
ALTER TABLE `Author`
  MODIFY `id_author` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=241674;

--
-- AUTO_INCREMENT for table `Book`
--
ALTER TABLE `Book`
  MODIFY `id_book` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=174942;

--
-- AUTO_INCREMENT for table `BookListType`
--
ALTER TABLE `BookListType`
  MODIFY `id_book_list_type` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `Category`
--
ALTER TABLE `Category`
  MODIFY `id_category` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=174545;

--
-- AUTO_INCREMENT for table `City`
--
ALTER TABLE `City`
  MODIFY `id_city` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `Country`
--
ALTER TABLE `Country`
  MODIFY `id_country` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=247;

--
-- AUTO_INCREMENT for table `Profile`
--
ALTER TABLE `Profile`
  MODIFY `id_profile` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `Quote`
--
ALTER TABLE `Quote`
  MODIFY `id_quote` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Book`
--
ALTER TABLE `Book`
  ADD CONSTRAINT `Book_Category0_FK` FOREIGN KEY (`id_category`) REFERENCES `Category` (`id_category`);

--
-- Constraints for table `BookList`
--
ALTER TABLE `BookList`
  ADD CONSTRAINT `BookList_Book2_FK` FOREIGN KEY (`id_book`) REFERENCES `Book` (`id_book`),
  ADD CONSTRAINT `BookList_BookListType1_FK` FOREIGN KEY (`id_book_list_type`) REFERENCES `BookListType` (`id_book_list_type`),
  ADD CONSTRAINT `BookList_User0_FK` FOREIGN KEY (`id_user`) REFERENCES `User` (`id_user`);

--
-- Constraints for table `Quote`
--
ALTER TABLE `Quote`
  ADD CONSTRAINT `Quote_Book1_FK` FOREIGN KEY (`id_book`) REFERENCES `Book` (`id_book`),
  ADD CONSTRAINT `Quote_User0_FK` FOREIGN KEY (`id_user`) REFERENCES `User` (`id_user`);

--
-- Constraints for table `Review`
--
ALTER TABLE `Review`
  ADD CONSTRAINT `Review_Book0_FK` FOREIGN KEY (`id_book`) REFERENCES `Book` (`id_book`),
  ADD CONSTRAINT `Review_User1_FK` FOREIGN KEY (`id_user`) REFERENCES `User` (`id_user`);

--
-- Constraints for table `User`
--
ALTER TABLE `User`
  ADD CONSTRAINT `User_City2_FK` FOREIGN KEY (`id_city`) REFERENCES `City` (`id_city`),
  ADD CONSTRAINT `User_Country1_FK` FOREIGN KEY (`id_country`) REFERENCES `Country` (`id_country`),
  ADD CONSTRAINT `User_Profile0_FK` FOREIGN KEY (`id_profile`) REFERENCES `Profile` (`id_profile`);

--
-- Constraints for table `Writer`
--
ALTER TABLE `Writer`
  ADD CONSTRAINT `Writer_Author1_FK` FOREIGN KEY (`id_author`) REFERENCES `Author` (`id_author`),
  ADD CONSTRAINT `Writer_Book0_FK` FOREIGN KEY (`id_book`) REFERENCES `Book` (`id_book`);
