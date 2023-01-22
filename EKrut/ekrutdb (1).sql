-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ekrut
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `deliveries`
--

DROP TABLE IF EXISTS `deliveries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deliveries` (
  `order_id` int NOT NULL,
  `region` varchar(128) NOT NULL,
  `address` varchar(128) NOT NULL,
  `estimated_time` varchar(128) DEFAULT NULL,
  `deilvery_status` enum('pendingApproval','outForDelivery','done') NOT NULL DEFAULT 'pendingApproval',
  `customer_status` enum('APPROVED','NOT_APPROVED') NOT NULL DEFAULT 'NOT_APPROVED',
  PRIMARY KEY (`order_id`),
  KEY `region_idx` (`region`),
  CONSTRAINT `order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
INSERT INTO `deliveries` VALUES (1,'UAE','alalala 6 abu dabi','17/01/2023 23:50','outForDelivery','NOT_APPROVED'),(3,'UAE','lalala 2 dubai','17/01/2023 23:50','outForDelivery','NOT_APPROVED'),(4,'UAE','lalala 2 dubai','17/01/2023 23:50','outForDelivery','NOT_APPROVED'),(5,'North','Home 11 Karmiel',NULL,'pendingApproval','NOT_APPROVED'),(6,'UAE','lalala 5 abu dabi',NULL,'pendingApproval','NOT_APPROVED');
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `machines`
--

DROP TABLE IF EXISTS `machines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `machines` (
  `region_id` int NOT NULL,
  `region_name` varchar(45) NOT NULL,
  `machine_id` int NOT NULL,
  `machine_name` varchar(256) NOT NULL,
  `min_amount` int DEFAULT NULL,
  PRIMARY KEY (`machine_id`,`region_id`),
  KEY `region_id_idx` (`region_id`),
  KEY `region_name_idx` (`region_name`),
  CONSTRAINT `region_id` FOREIGN KEY (`region_id`) REFERENCES `regions` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `machines`
--

LOCK TABLES `machines` WRITE;
/*!40000 ALTER TABLE `machines` DISABLE KEYS */;
INSERT INTO `machines` VALUES (1,'North',1,'Braude Academic College',7),(1,'North',2,'Karmiel Train Station',3),(1,'North',3,'Technion',9),(1,'North',4,'Rambam Hospital',9),(1,'North',5,'Ofer Grand Canyon',15),(1,'North',6,'Haifa University',12),(2,'UAE',7,'Dubai Mall',5),(2,'UAE',8,'City Centre Mirdif',5),(2,'UAE',9,'Mall of the Emirates',5),(2,'UAE',10,'Dubai Hospital',5),(2,'UAE',11,'Burj Khalifa Tower',30),(2,'UAE',12,'Abu Dhabi',0),(3,'South',13,'Beer Sheva Rail Station',0),(3,'South',14,'Dimona Atomic Reactor',0),(3,'South',15,'Eilat Ice mall ',0);
/*!40000 ALTER TABLE `machines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `machine_id` int NOT NULL,
  `total_sum` int NOT NULL,
  `user_id` int NOT NULL,
  `buytime` varchar(45) NOT NULL,
  `products_amount` varchar(45) NOT NULL,
  `payment_status` varchar(45) DEFAULT NULL,
  `supply_method` enum('Delivery','Pickup','On-site') NOT NULL DEFAULT 'On-site',
  `cart_description` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,-1,61,25,'23/11/2022 21:00','76.0','later','Delivery','[1, 2], [8, 7], [2, 6], [5, 5], [3, 2]'),(2,4,50,20,'11/11/2022 00:00','50.0','paid','Pickup','[12, 2], [22, 5], [24, 0], [9, 7], [5, 3]'),(3,-1,850,25,'01/11/2022 01:00','850.0','later','Delivery','[8, 6], [6, 2], [4, 4], [2, 6], [1, 1]'),(4,-1,125,25,'10/11/2022 07:00','125.0','later','Delivery','[12, 2], [22, 5], [24, 0], [9, 7], [5, 3]'),(5,-1,21,20,'20/11/2022 05:00','21.0','paid','Delivery','[6, 2], [4, 7], [2, 3], [1, 5], [8, 6]'),(6,-1,107,25,'06/11/2022 06:00','107.0','later','Delivery','[12, 2], [22, 5], [24, 0], [9, 7], [5, 3]'),(7,7,123,25,'28/11/2022 12:00','123.0','later','Pickup','[25, 1], [18, 3], [16, 2], [7, 2], [19, 7]'),(8,4,77,23,'01/11/2022 11:00','96.0','later','On-site','[2, 5], [6, 4], [4, 2], [1, 6], [7, 1]'),(9,4,129,23,'12/11/2022 09:00','129.0','later','On-site','[2, 1], [5, 6], [7, 7], [6, 2], [1, 1]'),(10,4,118,23,'30/11/2022 10:00','118.0','later','On-site','[4, 6], [7, 5], [6, 7], [2, 3], [1, 5]');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pickups`
--

DROP TABLE IF EXISTS `pickups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pickups` (
  `order_id` int NOT NULL,
  `pickup_status` enum('done','inProgress') NOT NULL DEFAULT 'inProgress',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pickups`
--

LOCK TABLES `pickups` WRITE;
/*!40000 ALTER TABLE `pickups` DISABLE KEYS */;
INSERT INTO `pickups` VALUES (-1,'inProgress'),(1,'inProgress'),(2,'inProgress'),(3,'inProgress'),(4,'inProgress'),(5,'inProgress'),(6,'inProgress'),(7,'inProgress'),(8,'inProgress');
/*!40000 ALTER TABLE `pickups` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-23  0:59:05
