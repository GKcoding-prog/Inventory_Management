-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 26, 2025 at 01:04 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventory_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `appear`
--

CREATE TABLE `appear` (
  `PRODUCT_ID` bigint(4) NOT NULL,
  `REQ_ID` bigint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `CAT_ID` bigint(4) NOT NULL,
  `NAME_CAT` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `deliver`
--

CREATE TABLE `deliver` (
  `PRODUCT_ID` bigint(4) NOT NULL,
  `SUPPLIER_ID` bigint(4) NOT NULL,
  `DELIVERY_DATE` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `PRODUCT_ID` bigint(4) NOT NULL,
  `CAT_ID` bigint(4) NOT NULL,
  `PRODUCT_NAME` varchar(255) NOT NULL,
  `PRODUCT_PRICE` decimal(13,2) NOT NULL,
  `PRODUCT_QUANTITY` bigint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE `report` (
  `REPORT_ID` bigint(4) NOT NULL,
  `USER_ID` bigint(4) NOT NULL,
  `GENERATED_AT` date NOT NULL,
  `CONTENT` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `stockrequest`
--

CREATE TABLE `stockrequest` (
  `REQ_ID` bigint(4) NOT NULL,
  `SUPPLIER_ID` bigint(4) NOT NULL,
  `USER_ID` bigint(4) NOT NULL,
  `QUANTITY_REQ` bigint(4) NOT NULL,
  `PRODUCT_REQ` varchar(255) NOT NULL,
  `DATE_REQ` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `SUPPLIER_ID` bigint(4) NOT NULL,
  `SUPPLIER_FNAME` varchar(255) NOT NULL,
  `CONTACT_INFO` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `USER_ID` bigint(4) NOT NULL,
  `USER_ID_MANAGE` bigint(4) DEFAULT NULL,
  `USER_FNAME` varchar(255) NOT NULL,
  `USER_PHONE` varchar(255) NOT NULL,
  `USER_EMAIL` varchar(255) NOT NULL,
  `USER_PWD` varchar(255) NOT NULL,
  `USER_ROLE` varchar(255) NOT NULL,
  `USER_STATUS` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `appear`
--
ALTER TABLE `appear`
  ADD PRIMARY KEY (`PRODUCT_ID`,`REQ_ID`),
  ADD KEY `I_FK_APPEAR_PRODUCTS` (`PRODUCT_ID`),
  ADD KEY `I_FK_APPEAR_STOCKREQUEST` (`REQ_ID`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`CAT_ID`);

--
-- Indexes for table `deliver`
--
ALTER TABLE `deliver`
  ADD PRIMARY KEY (`PRODUCT_ID`,`SUPPLIER_ID`),
  ADD KEY `I_FK_DELIVER_PRODUCTS` (`PRODUCT_ID`),
  ADD KEY `I_FK_DELIVER_SUPPLIER` (`SUPPLIER_ID`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`PRODUCT_ID`),
  ADD KEY `I_FK_PRODUCTS_CATEGORY` (`CAT_ID`);

--
-- Indexes for table `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`REPORT_ID`),
  ADD KEY `I_FK_REPORT_USERS` (`USER_ID`);

--
-- Indexes for table `stockrequest`
--
ALTER TABLE `stockrequest`
  ADD PRIMARY KEY (`REQ_ID`),
  ADD KEY `I_FK_STOCKREQUEST_SUPPLIER` (`SUPPLIER_ID`),
  ADD KEY `I_FK_STOCKREQUEST_USERS` (`USER_ID`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`SUPPLIER_ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`USER_ID`),
  ADD KEY `I_FK_USERS_USERS` (`USER_ID_MANAGE`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `appear`
--
ALTER TABLE `appear`
  MODIFY `PRODUCT_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `CAT_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `deliver`
--
ALTER TABLE `deliver`
  MODIFY `PRODUCT_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `PRODUCT_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `report`
--
ALTER TABLE `report`
  MODIFY `REPORT_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `stockrequest`
--
ALTER TABLE `stockrequest`
  MODIFY `REQ_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `supplier`
--
ALTER TABLE `supplier`
  MODIFY `SUPPLIER_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `USER_ID` bigint(4) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `appear`
--
ALTER TABLE `appear`
  ADD CONSTRAINT `FK_APPEAR_PRODUCTS` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `products` (`PRODUCT_ID`),
  ADD CONSTRAINT `FK_APPEAR_STOCKREQUEST` FOREIGN KEY (`REQ_ID`) REFERENCES `stockrequest` (`REQ_ID`);

--
-- Constraints for table `deliver`
--
ALTER TABLE `deliver`
  ADD CONSTRAINT `FK_DELIVER_PRODUCTS` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `products` (`PRODUCT_ID`),
  ADD CONSTRAINT `FK_DELIVER_SUPPLIER` FOREIGN KEY (`SUPPLIER_ID`) REFERENCES `supplier` (`SUPPLIER_ID`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FK_PRODUCTS_CATEGORY` FOREIGN KEY (`CAT_ID`) REFERENCES `category` (`CAT_ID`);

--
-- Constraints for table `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `FK_REPORT_USERS` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`USER_ID`);

--
-- Constraints for table `stockrequest`
--
ALTER TABLE `stockrequest`
  ADD CONSTRAINT `FK_STOCKREQUEST_SUPPLIER` FOREIGN KEY (`SUPPLIER_ID`) REFERENCES `supplier` (`SUPPLIER_ID`),
  ADD CONSTRAINT `FK_STOCKREQUEST_USERS` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`USER_ID`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FK_USERS_USERS` FOREIGN KEY (`USER_ID_MANAGE`) REFERENCES `users` (`USER_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
