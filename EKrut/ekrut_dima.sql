-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: ekrut
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
-- Table structure for table `clients_report`
--

DROP TABLE IF EXISTS `clients_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(500) NOT NULL,
  `supplymethods` varchar(45) NOT NULL,
  `totalorders` varchar(45) NOT NULL,
  `user_status` varchar(45) NOT NULL,
  `year` varchar(45) NOT NULL,
  `month` varchar(45) NOT NULL,
  `region` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients_report`
--

LOCK TABLES `clients_report` WRITE;
/*!40000 ALTER TABLE `clients_report` DISABLE KEYS */;
INSERT INTO `clients_report` VALUES (1,'0-2,40,3-6,65,6-12,98,13-17,15,18-25,9','100,27','227','117,110','2021','01','North'),(12,'0-1,2','1,0','2','2,0','2022','12','UAE'),(13,'0-1,14,1-2,3,2-3,2','11,10','24','24,0','2022','12','North');
/*!40000 ALTER TABLE `clients_report` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
INSERT INTO `deliveries` VALUES (5,'North','abcd 3/5 ','08/01/2023 13:16','outForDelivery','NOT_APPROVED'),(6,'North','aaa 2/3 ','28/12/2022 19:57','done','APPROVED'),(14,'North','cc 2','','pendingApproval','NOT_APPROVED'),(15,'UAE','aaaa 44/5 karmiel',NULL,'pendingApproval','NOT_APPROVED'),(24,'North','aaa 2/3 ','08/01/2023 13:16','outForDelivery','APPROVED'),(25,'South','aaa 2/3 ',NULL,'pendingApproval','NOT_APPROVED');
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_in_machine`
--

DROP TABLE IF EXISTS `item_in_machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_in_machine` (
  `machine_id` int NOT NULL,
  `item_id` int NOT NULL,
  `current_amount` int NOT NULL,
  `call_status` enum('NotOpened','Processed','Complete') NOT NULL DEFAULT 'NotOpened',
  `times_under_min` int NOT NULL DEFAULT '0',
  `worker_id` int DEFAULT NULL,
  PRIMARY KEY (`machine_id`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_in_machine`
--

LOCK TABLES `item_in_machine` WRITE;
/*!40000 ALTER TABLE `item_in_machine` DISABLE KEYS */;
INSERT INTO `item_in_machine` VALUES (1,1,10,'NotOpened',0,0),(1,2,10,'NotOpened',0,0),(1,3,10,'NotOpened',0,0),(1,4,10,'NotOpened',0,0),(1,5,10,'NotOpened',0,0),(1,6,10,'NotOpened',0,0),(1,7,10,'NotOpened',0,0),(1,8,10,'NotOpened',0,0),(1,9,10,'NotOpened',0,0),(1,10,10,'NotOpened',0,0),(1,11,10,'NotOpened',0,0),(1,12,10,'NotOpened',0,0),(1,13,10,'NotOpened',0,0),(1,14,10,'NotOpened',0,0),(1,15,10,'NotOpened',0,0),(1,16,10,'NotOpened',0,0),(1,17,10,'NotOpened',0,0),(1,18,10,'NotOpened',0,0),(1,19,10,'NotOpened',0,0),(1,20,10,'NotOpened',0,0),(1,21,10,'NotOpened',0,0),(1,22,10,'NotOpened',0,0),(1,23,10,'NotOpened',0,0),(1,24,10,'NotOpened',0,0),(1,25,10,'NotOpened',0,0),(1,26,10,'NotOpened',0,0),(2,1,10,'NotOpened',0,0),(2,2,10,'NotOpened',0,0),(2,3,8,'NotOpened',0,0),(2,4,7,'NotOpened',0,0),(2,5,2,'NotOpened',0,0),(2,6,10,'NotOpened',0,0),(2,7,10,'NotOpened',0,0),(2,8,9,'NotOpened',0,0),(2,9,10,'NotOpened',0,0),(2,10,10,'NotOpened',0,0),(2,11,10,'NotOpened',0,0),(2,12,10,'NotOpened',0,0),(2,13,10,'NotOpened',0,0),(2,14,10,'NotOpened',0,0),(2,15,10,'NotOpened',0,0),(2,16,10,'NotOpened',0,0),(2,17,10,'NotOpened',0,0),(2,18,10,'NotOpened',0,0),(2,19,10,'NotOpened',0,0),(2,20,10,'NotOpened',0,0),(2,21,10,'NotOpened',0,0),(2,22,10,'NotOpened',0,0),(2,23,10,'NotOpened',0,0),(2,24,10,'NotOpened',0,0),(2,25,10,'NotOpened',0,0),(2,26,10,'NotOpened',0,0),(3,1,10,'NotOpened',0,0),(3,2,9,'NotOpened',0,0),(3,3,6,'Processed',0,12),(3,4,10,'NotOpened',0,0),(3,5,10,'NotOpened',0,0),(3,6,4,'NotOpened',0,0),(3,7,10,'NotOpened',0,0),(3,8,6,'NotOpened',0,0),(3,9,10,'NotOpened',0,0),(3,10,10,'NotOpened',0,0),(3,11,10,'NotOpened',0,0),(3,12,10,'NotOpened',0,0),(3,13,10,'NotOpened',0,0),(3,14,10,'NotOpened',0,0),(3,15,10,'NotOpened',0,0),(3,16,10,'NotOpened',0,0),(3,17,10,'NotOpened',0,0),(3,18,10,'NotOpened',0,0),(3,19,10,'NotOpened',0,0),(3,20,10,'NotOpened',0,0),(3,21,10,'NotOpened',0,0),(3,22,6,'NotOpened',0,0),(3,23,10,'NotOpened',0,0),(3,24,10,'NotOpened',0,0),(3,25,10,'NotOpened',0,0),(3,26,10,'NotOpened',0,0),(4,1,10,'NotOpened',0,0),(4,2,10,'NotOpened',0,0),(4,3,8,'NotOpened',0,0),(4,4,9,'NotOpened',0,0),(4,5,8,'NotOpened',0,0),(4,6,10,'NotOpened',0,0),(4,7,10,'NotOpened',0,0),(4,8,10,'NotOpened',0,0),(4,9,10,'NotOpened',0,0),(4,10,10,'NotOpened',0,0),(4,11,10,'NotOpened',0,0),(4,12,10,'NotOpened',0,0),(4,13,10,'NotOpened',0,0),(4,14,10,'NotOpened',0,0),(4,15,9,'NotOpened',0,0),(4,16,10,'NotOpened',0,0),(4,17,10,'NotOpened',0,0),(4,18,10,'NotOpened',0,0),(4,19,10,'NotOpened',0,0),(4,20,10,'NotOpened',0,0),(4,21,9,'NotOpened',0,0),(4,22,10,'NotOpened',0,0),(4,23,10,'NotOpened',0,0),(4,24,10,'NotOpened',0,0),(4,25,10,'NotOpened',0,0),(4,26,10,'NotOpened',0,0),(5,1,10,'Processed',0,32),(5,2,10,'NotOpened',0,0),(5,3,10,'Processed',0,34),(5,4,10,'NotOpened',0,0),(5,5,10,'NotOpened',0,0),(5,6,10,'NotOpened',0,0),(5,7,10,'NotOpened',0,0),(5,8,10,'NotOpened',0,0),(5,9,10,'NotOpened',0,0),(5,10,10,'NotOpened',0,0),(5,11,10,'NotOpened',0,0),(5,12,10,'NotOpened',0,0),(5,13,10,'NotOpened',0,0),(5,14,10,'NotOpened',0,0),(5,15,10,'NotOpened',0,0),(5,16,10,'NotOpened',0,0),(5,17,10,'NotOpened',0,0),(5,18,10,'NotOpened',0,0),(5,19,10,'NotOpened',0,0),(5,20,10,'NotOpened',0,0),(5,21,10,'NotOpened',0,0),(5,22,10,'NotOpened',0,0),(5,23,10,'NotOpened',0,0),(5,24,10,'NotOpened',0,0),(5,25,10,'NotOpened',0,0),(5,26,10,'NotOpened',0,0),(6,1,10,'NotOpened',0,0),(6,2,10,'NotOpened',0,0),(6,3,10,'NotOpened',0,0),(6,4,10,'NotOpened',0,0),(6,5,10,'NotOpened',0,0),(6,6,10,'NotOpened',0,0),(6,7,10,'NotOpened',0,0),(6,8,10,'NotOpened',0,0),(6,9,10,'NotOpened',0,0),(6,10,10,'NotOpened',0,0),(6,11,10,'NotOpened',0,0),(6,12,10,'NotOpened',0,0),(6,13,10,'NotOpened',0,0),(6,14,10,'NotOpened',0,0),(6,15,10,'NotOpened',0,0),(6,16,10,'NotOpened',0,0),(6,17,10,'NotOpened',0,0),(6,18,10,'NotOpened',0,0),(6,19,10,'NotOpened',0,0),(6,20,10,'NotOpened',0,0),(6,21,10,'NotOpened',0,0),(6,22,10,'NotOpened',0,0),(6,23,10,'NotOpened',0,0),(6,24,10,'NotOpened',0,0),(6,25,10,'NotOpened',0,0),(6,26,10,'NotOpened',0,0),(7,1,10,'NotOpened',0,0),(7,2,10,'NotOpened',0,0),(7,3,10,'NotOpened',0,0),(7,4,10,'NotOpened',0,0),(7,5,10,'NotOpened',0,0),(7,6,10,'NotOpened',0,0),(7,7,10,'NotOpened',0,0),(7,8,10,'NotOpened',0,0),(7,9,10,'NotOpened',0,0),(7,10,10,'NotOpened',0,0),(7,11,10,'NotOpened',0,0),(7,12,0,'NotOpened',0,0),(7,13,10,'NotOpened',0,0),(7,14,10,'NotOpened',0,0),(7,15,10,'NotOpened',0,0),(7,16,10,'NotOpened',0,0),(7,17,10,'NotOpened',0,0),(7,18,10,'NotOpened',0,0),(7,19,10,'NotOpened',0,0),(7,20,10,'NotOpened',0,0),(7,21,10,'NotOpened',0,0),(7,22,10,'NotOpened',0,0),(7,23,10,'NotOpened',0,0),(7,24,10,'NotOpened',0,0),(7,25,10,'NotOpened',0,0),(7,26,10,'NotOpened',0,0),(8,1,10,'NotOpened',0,0),(8,2,10,'NotOpened',0,0),(8,3,10,'NotOpened',0,0),(8,4,10,'NotOpened',0,0),(8,5,10,'NotOpened',0,0),(8,6,10,'NotOpened',0,0),(8,7,10,'NotOpened',0,0),(8,8,10,'NotOpened',0,0),(8,9,10,'NotOpened',0,0),(8,10,10,'NotOpened',0,0),(8,11,10,'NotOpened',0,0),(8,12,10,'NotOpened',0,0),(8,13,10,'NotOpened',0,0),(8,14,10,'NotOpened',0,0),(8,15,10,'NotOpened',0,0),(8,16,10,'NotOpened',0,0),(8,17,10,'NotOpened',0,0),(8,18,10,'NotOpened',0,0),(8,19,10,'NotOpened',0,0),(8,20,10,'NotOpened',0,0),(8,21,10,'NotOpened',0,0),(8,22,10,'NotOpened',0,0),(8,23,10,'NotOpened',0,0),(8,24,10,'NotOpened',0,0),(8,25,10,'NotOpened',0,0),(8,26,10,'NotOpened',0,0),(9,1,10,'NotOpened',0,0),(9,2,10,'NotOpened',0,0),(9,3,10,'NotOpened',0,0),(9,4,10,'NotOpened',0,0),(9,5,10,'NotOpened',0,0),(9,6,10,'NotOpened',0,0),(9,7,10,'NotOpened',0,0),(9,8,10,'NotOpened',0,0),(9,9,10,'NotOpened',0,0),(9,10,10,'NotOpened',0,0),(9,11,10,'NotOpened',0,0),(9,12,10,'NotOpened',0,0),(9,13,10,'NotOpened',0,0),(9,14,10,'NotOpened',0,0),(9,15,10,'NotOpened',0,0),(9,16,10,'NotOpened',0,0),(9,17,10,'NotOpened',0,0),(9,18,10,'NotOpened',0,0),(9,19,10,'NotOpened',0,0),(9,20,10,'NotOpened',0,0),(9,21,10,'NotOpened',0,0),(9,22,10,'NotOpened',0,0),(9,23,10,'NotOpened',0,0),(9,24,10,'NotOpened',0,0),(9,25,10,'NotOpened',0,0),(9,26,10,'NotOpened',0,0),(10,1,10,'NotOpened',0,0),(10,2,10,'NotOpened',0,0),(10,3,10,'NotOpened',0,0),(10,4,10,'NotOpened',0,0),(10,5,10,'NotOpened',0,0),(10,6,10,'NotOpened',0,0),(10,7,10,'NotOpened',0,0),(10,8,10,'NotOpened',0,0),(10,9,10,'NotOpened',0,0),(10,10,10,'NotOpened',0,0),(10,11,10,'NotOpened',0,0),(10,12,10,'NotOpened',0,0),(10,13,10,'NotOpened',0,0),(10,14,10,'NotOpened',0,0),(10,15,10,'NotOpened',0,0),(10,16,10,'NotOpened',0,0),(10,17,10,'NotOpened',0,0),(10,18,10,'NotOpened',0,0),(10,19,10,'NotOpened',0,0),(10,20,10,'NotOpened',0,0),(10,21,10,'NotOpened',0,0),(10,22,10,'NotOpened',0,0),(10,23,10,'NotOpened',0,0),(10,24,10,'NotOpened',0,0),(10,25,10,'NotOpened',0,0),(10,26,10,'NotOpened',0,0),(11,1,10,'NotOpened',0,0),(11,2,10,'NotOpened',0,0),(11,3,10,'NotOpened',0,0),(11,4,10,'NotOpened',0,0),(11,5,10,'NotOpened',0,0),(11,6,10,'NotOpened',0,0),(11,7,10,'NotOpened',0,0),(11,8,10,'NotOpened',0,0),(11,9,10,'NotOpened',0,0),(11,10,10,'NotOpened',0,0),(11,11,10,'NotOpened',0,0),(11,12,10,'NotOpened',0,0),(11,13,10,'NotOpened',0,0),(11,14,10,'NotOpened',0,0),(11,15,10,'NotOpened',0,0),(11,16,10,'NotOpened',0,0),(11,17,10,'NotOpened',0,0),(11,18,10,'NotOpened',0,0),(11,19,10,'NotOpened',0,0),(11,20,10,'NotOpened',0,0),(11,21,10,'NotOpened',0,0),(11,22,10,'NotOpened',0,0),(11,23,10,'NotOpened',0,0),(11,24,10,'NotOpened',0,0),(11,25,10,'NotOpened',0,0),(11,26,10,'NotOpened',0,0),(12,1,10,'NotOpened',0,0),(12,2,10,'NotOpened',0,0),(12,3,10,'NotOpened',0,0),(12,4,10,'NotOpened',0,0),(12,5,10,'NotOpened',0,0),(12,6,10,'NotOpened',0,0),(12,7,10,'NotOpened',0,0),(12,8,10,'NotOpened',0,0),(12,9,10,'NotOpened',0,0),(12,10,10,'NotOpened',0,0),(12,11,10,'NotOpened',0,0),(12,12,10,'NotOpened',0,0),(12,13,10,'NotOpened',0,0),(12,14,10,'NotOpened',0,0),(12,15,10,'NotOpened',0,0),(12,16,10,'NotOpened',0,0),(12,17,10,'NotOpened',0,0),(12,18,10,'NotOpened',0,0),(12,19,10,'NotOpened',0,0),(12,20,10,'NotOpened',0,0),(12,21,10,'NotOpened',0,0),(12,22,10,'NotOpened',0,0),(12,23,10,'NotOpened',0,0),(12,24,10,'NotOpened',0,0),(12,25,10,'NotOpened',0,0),(12,26,10,'NotOpened',0,0),(13,1,10,'NotOpened',0,0),(13,2,10,'NotOpened',0,0),(13,3,10,'NotOpened',0,0),(13,4,10,'NotOpened',0,0),(13,5,10,'NotOpened',0,0),(13,6,10,'NotOpened',0,0),(13,7,10,'NotOpened',0,0),(13,8,10,'NotOpened',0,0),(13,9,10,'NotOpened',0,0),(13,10,10,'NotOpened',0,0),(13,11,10,'NotOpened',0,0),(13,12,10,'NotOpened',0,0),(13,13,10,'NotOpened',0,0),(13,14,10,'NotOpened',0,0),(13,15,10,'NotOpened',0,0),(13,16,10,'NotOpened',0,0),(13,17,10,'NotOpened',0,0),(13,18,10,'NotOpened',0,0),(13,19,10,'NotOpened',0,0),(13,20,10,'NotOpened',0,0),(13,21,10,'NotOpened',0,0),(13,22,10,'NotOpened',0,0),(13,23,10,'NotOpened',0,0),(13,24,10,'NotOpened',0,0),(13,25,10,'NotOpened',0,0),(13,26,10,'NotOpened',0,0),(14,1,10,'NotOpened',0,0),(14,2,10,'NotOpened',0,0),(14,3,10,'NotOpened',0,0),(14,4,10,'NotOpened',0,0),(14,5,10,'NotOpened',0,0),(14,6,10,'NotOpened',0,0),(14,7,10,'NotOpened',0,0),(14,8,10,'NotOpened',0,0),(14,9,10,'NotOpened',0,0),(14,10,10,'NotOpened',0,0),(14,11,10,'NotOpened',0,0),(14,12,10,'NotOpened',0,0),(14,13,10,'NotOpened',0,0),(14,14,10,'NotOpened',0,0),(14,15,10,'NotOpened',0,0),(14,16,10,'NotOpened',0,0),(14,17,10,'NotOpened',0,0),(14,18,10,'NotOpened',0,0),(14,19,10,'NotOpened',0,0),(14,20,10,'NotOpened',0,0),(14,21,10,'NotOpened',0,0),(14,22,10,'NotOpened',0,0),(14,23,10,'NotOpened',0,0),(14,24,10,'NotOpened',0,0),(14,25,10,'NotOpened',0,0),(14,26,10,'NotOpened',0,0);
/*!40000 ALTER TABLE `item_in_machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price` double NOT NULL,
  `item_img_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `itemName_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,'Bamba',12,'Bamba.png'),(2,'Bamba Nougat',10,'Bamba_Nugat.png'),(3,'Apple Flavored Water',9,'Apple flavored water.png'),(4,'Klik Biscuit',8,'Click_Biscuit.png'),(5,'Klik Conrflakes',8,'Click_Cornflakes.png'),(6,'M&M Peanut',8,'m&m peanuts.png'),(7,'Prigat Orange Juice',6,'Orange juice Prigat 500 ml.png'),(8,'Pack of Oreo\'s',10,'Oreo_pack.png'),(9,'Bissli BBQ',8,'Beasley BBQ.png'),(10,'Coca-Cola Zero Bottle',7,'bottle of Coca-Cola Zero 500 ml.png'),(11,'Coca Cola Can',5,'Coca Cola can.png'),(12,'Coca Cola Zero Can',5,'Coca Cola Zero can.png'),(13,'Coca Cola Bottle',7,'Coke bottle 500 ml.png'),(14,'Mexican Doritos ',8,'Dorietos mexican flavor.png'),(15,'Spicey & Sour Doritos ',8,'Dorietos spicey sour.png'),(16,'Elite Kranz Milk Chocolate',6,'Elite Karanz milk chocolate.png'),(17,'Fanta',5,'Fanta.png'),(18,'Grapes Flavored Water',6,'Grapes flavored water.png'),(19,'Kif Kef',5,'Kief Kef.png'),(20,'Kinder Bueno White Chocolate',6,'Kinder Bueno White Chocolate.png'),(21,'Kinder Chocolate Sticks',6,'Kinder chocolate 4 sticks.png'),(22,'Orbit Mint Gum',5,'Orbit mint gum.png'),(23,'Peach Flavored Protein Yogurt',5,'Peach flavored protein yogurt.png'),(24,'Peach Flavored Water',6,'Peach flavored water.png'),(25,'Snickers Bar',6,'Snickers.png'),(26,'Twix Bar',6,'Twix.png');
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `machines`
--

DROP TABLE IF EXISTS `machines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `machines` (
  `region_id` int DEFAULT NULL,
  `region_name` varchar(45) NOT NULL,
  `machine_id` int NOT NULL,
  `machine_name` varchar(256) NOT NULL,
  `min_amount` int DEFAULT NULL,
  PRIMARY KEY (`machine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `machines`
--

LOCK TABLES `machines` WRITE;
/*!40000 ALTER TABLE `machines` DISABLE KEYS */;
INSERT INTO `machines` VALUES (1,'North',1,'Braude Academic College',7),(1,'North',2,'Karmiel Train Station',3),(1,'North',3,'Technion',9),(1,'North',4,'Rambam Hospital',9),(1,'North',5,'Ofer Grand Canyon',15),(1,'North',6,'Haifa University',12),(2,'UAE',7,'Dubai Mall',5),(2,'UAE',8,'City Centre Mirdif',5),(2,'UAE',9,'Mall of the Emirates',5),(2,'UAE',10,'Dubai Hospital',5),(2,'UAE',11,'Burj Khalifa Tower',30),(2,'UAE',12,'Abu Dhabi',0),(3,'South',13,'Beer Sheva Rail Station',0),(3,'South',14,'Dimona Atomic Reactor',0);
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,112,1,'22/12/2022 14:43','3',NULL,'On-site'),(2,1,142,2,'22/12/2022 14:43','3',NULL,'On-site'),(3,2,155,1,'22/12/2022 14:43','13',NULL,'On-site'),(4,2,41,4,'22/12/2022 14:43','3',NULL,'On-site'),(5,3,77,3,'22/12/2022 14:43','3',NULL,'Delivery'),(6,3,17,5,'22/12/2022 14:43','3',NULL,'Delivery'),(7,1,12,1,'22/12/2022 14:43','3',NULL,'On-site'),(8,2,115,2,'22/12/2022 14:43','3',NULL,'On-site'),(9,1,11,3,'22/12/2022 14:43','3',NULL,'On-site'),(10,3,110,1,'22/12/2022 14:43','3',NULL,'On-site'),(11,2,142,4,'22/12/2022 14:43','3',NULL,'On-site'),(12,6,155,5,'22/12/2022 14:43','13',NULL,'Pickup'),(13,2,41,2,'22/12/2022 14:43','3',NULL,'Pickup'),(14,7,77,1,'22/12/2022 14:43','3',NULL,'Delivery'),(15,3,17,3,'22/12/2022 14:43','3',NULL,'Delivery'),(16,6,12,2,'22/12/2022 14:43','3',NULL,'Pickup'),(17,4,115,1,'22/12/2022 14:43','3',NULL,'Pickup'),(18,5,11,3,'22/12/2022 14:43','3',NULL,'Pickup'),(19,6,110,4,'22/12/2022 14:43','3',NULL,'Pickup'),(20,4,112,3,'11/12/2022 14:43','3',NULL,'Pickup'),(21,2,142,2,'20/12/2022 14:43','3',NULL,'Pickup'),(22,1,155,1,'26/12/2022 14:43','13',NULL,'Pickup'),(23,2,41,5,'13/12/2022 14:43','3',NULL,'Pickup'),(24,2,77,129,'17/12/2022 14:43','3',NULL,'Delivery'),(25,24,17,129,'16/12/2022 14:43','3',NULL,'Delivery'),(26,6,12,129,'05/12/2022 14:43','3',NULL,'Pickup'),(27,21,115,129,'11/12/2022 14:43','3',NULL,'Pickup'),(28,23,11,129,'12/12/2022 14:43','3',NULL,'Pickup'),(29,6,110,7,'25/12/2022 14:43','3',NULL,'Pickup'),(30,11,37,7,'22/12/2022 14:43','1',NULL,'Pickup'),(31,2,-1,132,'08/01/2023 14:14','-1',NULL,'Pickup'),(32,3,-1,132,'08/01/2023 14:26','-1',NULL,'Pickup'),(33,2,-1,132,'08/01/2023 14:30','-1',NULL,'Pickup'),(34,4,-1,132,'08/01/2023 14:36','-1',NULL,'Pickup'),(35,4,-1,132,'08/01/2023 14:37','-1',NULL,'Pickup'),(36,4,-1,132,'08/01/2023 14:38','-1',NULL,'Pickup'),(37,4,-1,132,'08/01/2023 14:38','-1',NULL,'Pickup'),(38,4,-1,132,'08/01/2023 14:38','-1',NULL,'Pickup'),(39,4,-1,132,'08/01/2023 14:39','-1',NULL,'Pickup'),(40,-1,-1,132,'08/01/2023 16:01','-1',NULL,'Delivery'),(41,-1,-1,132,'08/01/2023 16:45','-1',NULL,'Delivery'),(42,-1,-1,132,'08/01/2023 16:46','-1',NULL,'Delivery'),(43,4,-1,132,'08/01/2023 23:20','-1',NULL,'Pickup'),(44,2,-1,132,'09/01/2023 12:44','-1',NULL,'Pickup'),(45,1,112,2,'22/12/2022 14:43','3',NULL,'On-site');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders_report`
--

DROP TABLE IF EXISTS `orders_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(300) DEFAULT NULL,
  `month` varchar(45) DEFAULT NULL,
  `year` varchar(45) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders_report`
--

LOCK TABLES `orders_report` WRITE;
/*!40000 ALTER TABLE `orders_report` DISABLE KEYS */;
INSERT INTO `orders_report` VALUES (65,'Burj Khalifa Tower,1,37','12','2022','UAE'),(66,'Braude Academic College,5,389,Karmiel Train Station,7,677,Technion,1,110,Haifa University,5,399,Rambam Hospital,2,227,Ofer Grand Canyon,1,11','12','2022','North');
/*!40000 ALTER TABLE `orders_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_messages`
--

DROP TABLE IF EXISTS `personal_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personal_messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `date` varchar(64) NOT NULL,
  `type` varchar(64) NOT NULL,
  `message` varchar(2048) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_messages`
--

LOCK TABLES `personal_messages` WRITE;
/*!40000 ALTER TABLE `personal_messages` DISABLE KEYS */;
INSERT INTO `personal_messages` VALUES (5,0,'26/12/2022','message title','This message sent via Email to Email lidi@gmail.com:\nThe whole message is here nigga'),(6,32,'26/12/2022','message title','This message sent via Email to Email lidi@gmail.com:\nThe whole message is here nigga'),(7,83,'28/12/2022','message title','This message sent via Email to dudy@gmail.com:\nThe whole message is here nigga'),(8,0,'28/12/2022','Need your action','New user has been signed up\nplease go to \'Users Approval\' to review and approve the request'),(9,83,'28/12/2022','message title','This message sent via Email to dudy@gmail.com:\nThe whole message is here nigga'),(10,83,'28/12/2022','message title','This message sent via Email to dudy@gmail.com:\nThe whole message is here nigga'),(11,0,'28/12/2022','Need your action','New user has been signed up\nplease go to \'Users Approval\' to review and approve the request'),(12,0,'28/12/2022','Need your action','New user has been signed up\nplease go to \'Users Approval\' to review and approve the request'),(13,83,'28/12/2022','Need your action','New user has been signed up\nplease go to \'Users Approval\' to review and approve the request'),(14,83,'28/12/2022','message title','This message sent via Email to dudy@gmail.com:\nThe whole message is here nigga'),(15,83,'28/12/2022','title','Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here Long here '),(16,110,'08/01/2023','Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKlik Conrflakes, Apple Flavored Water'),(17,110,'08/01/2023','Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKlik Conrflakes, Apple Flavored Water'),(18,110,'09/01/2023','Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #2:\nKlik Conrflakes'),(19,110,'09/01/2023','Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #2:\nKlik Conrflakes'),(20,20,'11/01/2023','Simulation: Request Approved','This message sent via SMS to 0538503033:\nCongratulations your request has been approved!\nSee Mail for full details'),(21,21,'11/01/2023','Simulation: Request Approved','This message sent via SMS to 0538503033:\nCongratulations your request has been approved!\nSee Mail for full details'),(22,22,'11/01/2023','Simulation: Request Approved','This message sent via SMS to 0538503033:\nCongratulations your request has been approved!\nSee Mail for full details'),(23,23,'11/01/2023','Simulation: Request Approved','This message sent via SMS to 0538503033:\nCongratulations your request has been approved!\nSee Mail for full details'),(24,24,'11/01/2023','Simulation: Request Approved','This message sent via SMS to 0538503033:\nCongratulations your request has been approved!\nSee Mail for full details'),(25,25,'11/01/2023','Simulation: Request Approved','This message sent via SMS to 0538503033:\nCongratulations your request has been approved!\nSee Mail for full details'),(26,24,'11/01/2023','Simulation: Request Approved','This message sent via SMS to 0538503033:\nCongratulations your request has been approved!\nSee Mail for full details'),(27,24,'11/01/2023','Simulation: Request Approved','This message sent via Email to itay@gmail.com:\nCongratulations your request has been approved!\nYour login info:\nUsername: mbrS\nPassword: 123456\nAs a member you get the following benefits:\n*From time to time there are special sales on some regions.\n*Discount of 20% on your first purchase\n*A quick login using EKT app. Here is a link for download: www.ekt.ekrut.com'),(28,1,'15/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #3:\nPack of Oreo\'s'),(29,1,'15/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #3:\nPack of Oreo\'s'),(30,1,'15/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #3:\nM&M Peanut, Orbit Mint Gum'),(31,1,'15/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #3:\nM&M Peanut, Orbit Mint Gum'),(32,1,'15/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #3:\nM&M Peanut, Orbit Mint Gum'),(33,1,'15/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #3:\nM&M Peanut, Orbit Mint Gum');
/*!40000 ALTER TABLE `personal_messages` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pickups`
--

LOCK TABLES `pickups` WRITE;
/*!40000 ALTER TABLE `pickups` DISABLE KEYS */;
INSERT INTO `pickups` VALUES (12,'inProgress'),(13,'inProgress'),(16,'inProgress'),(17,'inProgress'),(18,'inProgress'),(19,'inProgress'),(20,'inProgress'),(21,'inProgress'),(22,'inProgress'),(23,'inProgress'),(26,'inProgress'),(27,'done'),(28,'inProgress'),(29,'inProgress'),(30,'inProgress'),(33,'inProgress'),(34,'inProgress'),(43,'inProgress'),(44,'inProgress'),(45,'done'),(46,'inProgress'),(47,'inProgress');
/*!40000 ALTER TABLE `pickups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regions`
--

DROP TABLE IF EXISTS `regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `regions` (
  `region_id` int NOT NULL,
  `region_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regions`
--

LOCK TABLES `regions` WRITE;
/*!40000 ALTER TABLE `regions` DISABLE KEYS */;
INSERT INTO `regions` VALUES (1,'North'),(2,'UAE'),(3,'South');
/*!40000 ALTER TABLE `regions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `region` varchar(128) NOT NULL,
  `sale_type` varchar(45) NOT NULL,
  `days` varchar(128) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `sale_status` enum('Active','NotActive') NOT NULL DEFAULT 'NotActive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,'North','1+1','Sunday','08:00:00','23:59:59','Active'),(2,'North','30%','Sunday,Monday','14:00:00','23:59:59','Active'),(8,'UAE','10%','Tuesday, Wednesday, Thursday, Monday, Sunday','00:00:00','01:00:00','NotActive'),(9,'UAE','30%','Monday, Tuesday','05:00:00','20:00:00','NotActive'),(10,'North','10%','Monday','08:00:00','20:00:00','NotActive'),(11,'UAE','10%','Monday','08:00:00','20:00:00','Active'),(12,'South','10%','Monday','08:00:00','20:00:00','Active'),(13,'North','1+1','Tuesday, Monday, Wednesday','07:00:00','08:00:00','Active'),(14,'North','1+1','Tuesday, Monday, Wednesday','07:00:00','08:00:00','NotActive'),(56,'North','1+1','Sunday','08:00:00','10:00:00','Active'),(57,'North','1+1','Sunday','08:00:00','10:00:00','NotActive');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supply_report`
--

DROP TABLE IF EXISTS `supply_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supply_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `machine_id` int NOT NULL,
  `machine_min_amount` int NOT NULL,
  `item_id` varchar(500) NOT NULL,
  `times_under_min` varchar(500) NOT NULL,
  `end_stock` varchar(500) NOT NULL,
  `month` varchar(45) NOT NULL,
  `year` varchar(45) NOT NULL,
  `region` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supply_report`
--

LOCK TABLES `supply_report` WRITE;
/*!40000 ALTER TABLE `supply_report` DISABLE KEYS */;
INSERT INTO `supply_report` VALUES (62,1,7,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','01','2023','1'),(63,1,7,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','1'),(64,2,3,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,8,7,2,10,10,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','1'),(65,3,9,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,2,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0','10,9,6,10,10,4,10,6,10,10,10,10,10,10,10,10,10,10,10,10,10,6,10,10,10,10','12','2022','1'),(66,4,9,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,8,9,8,10,10,10,10,10,10,10,10,10,9,10,10,10,10,10,9,10,10,10,10,10','12','2022','1'),(67,5,15,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','1'),(68,6,12,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','1'),(69,7,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,0,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(70,8,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(71,9,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(72,10,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(73,11,30,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(74,12,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(75,13,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','3'),(76,14,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','3');
/*!40000 ALTER TABLE `supply_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_number` varchar(45) DEFAULT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `first_name` varchar(128) NOT NULL,
  `last_name` varchar(128) NOT NULL,
  `email` varchar(128) NOT NULL,
  `phone_number` varchar(128) NOT NULL,
  `cc_number` varchar(45) DEFAULT NULL,
  `region` varchar(128) DEFAULT NULL,
  `role_type` varchar(128) NOT NULL,
  `logged_in` tinyint(1) NOT NULL,
  `is_not_approved` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `userName_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'100878859','rmNorth','123456','Lidor','Ankava','lidor@EKrut.com','0538503033',NULL,'North','regionManager',0,0),(2,'101868859','doNorth','123456','Eyal','Greenberg','eyal@EKrut.com','0538503033',NULL,'North','deliveryOperator',0,0),(3,'106828759','mwNorth','123456','Ofer','Gold','ofer@EKrut.com','0538503033',NULL,'North','marketingWorker',0,0),(4,'102858859','rmSouth','123456','Neta','The fork','neta@EKrut.com','0538503033',NULL,'South','regionManager',0,0),(5,'103848859','doSouth','123456','Yossef','Ben porat','yossef@EKrut.com','0538503033',NULL,'South','deliveryOperator',0,0),(6,'106828858','mwSouth','123456','Bigi','Amzalag','ofer@EKrut.com','0538503033',NULL,'South','marketingWorker',0,0),(7,'104838859','rmUAE','123456','Dima','Chdnsky','dima@EKrut.com','0538503033',NULL,'UAE','regionManager',0,0),(8,'105828859','doUAE','123456','Hamudi','Kabudi','hamudi@EKrut.com','0538503033',NULL,'UAE','deliveryOperator',0,0),(9,'106828957','mwUAE','123456','Guy','Amzalag','ofer@EKrut.com','0538503033',NULL,'UAE','marketingWorker',0,0),(10,'106818859','csworker','123456','Ravit','Gamliel','ravit@EKrut.com','0538503033',NULL,'null','customerServiceWorker',0,0),(11,'106838659','mktmanager','123456','Vital','Marciano','vital@EKrut.com','0538503033',NULL,'null','marketingManager',0,0),(12,'106848559','spw1','123456','David','Asulin','david@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(13,'106858459','spw2','123456','Yair','Asulin','yair@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(14,'106868359','spw3','123456','Saray','Asulin','saray@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(15,'106878259','spw4','123456','Rivka','Asulin','rivka@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(16,'106888159','ceo','123456','Adam','Mizrahi','adam@EKrut.com','0538503033',NULL,'null','CEO',0,0),(17,'007818859','usrN','123456','Anna','Zak','anna@gmail.com','0538503033',NULL,'North','user',0,1),(18,'205818859','usrS','123456','Omer','Adam','omer@gmail.com','0538503033',NULL,'South','user',0,1),(19,'304818859','usrU','123456','Eden','Ben Zaken','kapara@gmail.com','0538503033',NULL,'UAE','user',0,1),(20,'403818859','rgsN','123456','Ravid','Plotnik','nechi@gmail.com','0538503033','1234123412341234','North','registered',0,0),(21,'502818859','rgsS','123456','Keren','Peles','keren@gmail.com','0538503033','1234123412341234','South','registered',0,0),(22,'601818859','rgsU','123456','Berry','Sakharof','berry@gmail.com','0538503033','1234123412341234','UAE','registered',0,0),(23,'700818859','mbrN','123456','Idan','Haviv','idan@gmail.com','0538503033','1234123412341234','North','member',0,0),(24,'800808859','mbrS','123456','Itay','Levi','itay@gmail.com','0538503033','1234123412341234','South','member',0,0),(25,'900808759','mbrU','123456','Daniel','Salomon','daniel@gmail.com','0538503033','1234123412341234','UAE','member',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-16 13:43:19
