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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients_report`
--

LOCK TABLES `clients_report` WRITE;
/*!40000 ALTER TABLE `clients_report` DISABLE KEYS */;
INSERT INTO `clients_report` VALUES (3,'0-1,5,1-3,2,3-4,2','0,19','19','10,9','2022','12','UAE'),(4,'0-1,6,1-2,1,2-4,1,4-11,1','0,23','23','16,7','2022','12','North'),(5,'0-1,2,1-2,4,2-5,2,5-7,1,7-8,1','10,25','35','18,17','2022','11','UAE'),(6,'0-1,3,1-2,4,2-4,1,4-8,1,8-22,1','10,35','45','31,14','2022','11','North');
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
  PRIMARY KEY (`machine_id`,`item_id`),
  KEY `item_id_idx` (`item_id`),
  CONSTRAINT `item_id` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`),
  CONSTRAINT `machine_id` FOREIGN KEY (`machine_id`) REFERENCES `machines` (`machine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_in_machine`
--

LOCK TABLES `item_in_machine` WRITE;
/*!40000 ALTER TABLE `item_in_machine` DISABLE KEYS */;
INSERT INTO `item_in_machine` VALUES (1,1,10,'NotOpened',0,0),(1,2,10,'NotOpened',0,0),(1,3,2,'NotOpened',0,0),(1,4,7,'NotOpened',0,0),(1,5,6,'NotOpened',0,0),(1,6,6,'NotOpened',0,0),(1,7,10,'NotOpened',0,0),(1,8,5,'NotOpened',0,0),(1,9,2,'NotOpened',0,0),(1,10,10,'NotOpened',0,0),(1,11,10,'NotOpened',0,0),(1,12,5,'NotOpened',0,0),(1,13,10,'NotOpened',0,0),(1,14,10,'NotOpened',0,0),(1,15,10,'NotOpened',0,0),(1,16,10,'NotOpened',0,0),(1,17,6,'NotOpened',0,0),(1,18,10,'NotOpened',0,0),(1,19,10,'NotOpened',0,0),(1,20,10,'NotOpened',0,0),(1,21,10,'NotOpened',0,0),(1,22,10,'NotOpened',0,0),(1,23,10,'NotOpened',0,0),(1,24,10,'NotOpened',0,0),(1,25,10,'NotOpened',0,0),(1,26,10,'NotOpened',0,0),(2,1,10,'NotOpened',0,0),(2,2,10,'NotOpened',0,0),(2,3,8,'NotOpened',0,0),(2,4,6,'NotOpened',0,0),(2,5,2,'NotOpened',0,0),(2,6,10,'NotOpened',0,0),(2,7,9,'NotOpened',0,0),(2,8,9,'NotOpened',0,0),(2,9,10,'NotOpened',0,0),(2,10,10,'NotOpened',0,0),(2,11,10,'NotOpened',0,0),(2,12,10,'NotOpened',0,0),(2,13,10,'NotOpened',0,0),(2,14,10,'NotOpened',0,0),(2,15,10,'NotOpened',0,0),(2,16,10,'NotOpened',0,0),(2,17,10,'NotOpened',0,0),(2,18,6,'NotOpened',0,0),(2,19,6,'NotOpened',0,0),(2,20,10,'NotOpened',0,0),(2,21,10,'NotOpened',0,0),(2,22,10,'NotOpened',0,0),(2,23,9,'NotOpened',0,0),(2,24,10,'NotOpened',0,0),(2,25,10,'NotOpened',0,0),(2,26,10,'NotOpened',0,0),(3,1,10,'NotOpened',0,0),(3,2,9,'NotOpened',0,0),(3,3,6,'Processed',0,12),(3,4,10,'NotOpened',0,0),(3,5,9,'NotOpened',0,0),(3,6,4,'NotOpened',0,0),(3,7,9,'NotOpened',0,0),(3,8,5,'NotOpened',0,0),(3,9,10,'NotOpened',0,0),(3,10,10,'NotOpened',0,0),(3,11,10,'NotOpened',0,0),(3,12,9,'NotOpened',0,0),(3,13,10,'NotOpened',0,0),(3,14,10,'NotOpened',0,0),(3,15,10,'NotOpened',0,0),(3,16,10,'NotOpened',0,0),(3,17,10,'NotOpened',0,0),(3,18,10,'NotOpened',0,0),(3,19,10,'NotOpened',0,0),(3,20,10,'NotOpened',0,0),(3,21,10,'NotOpened',0,0),(3,22,6,'NotOpened',0,0),(3,23,10,'NotOpened',0,0),(3,24,10,'NotOpened',0,0),(3,25,10,'NotOpened',0,0),(3,26,10,'NotOpened',0,0),(4,1,10,'NotOpened',0,0),(4,2,0,'Complete',0,12),(4,3,0,'Complete',0,12),(4,4,0,'Complete',0,12),(4,5,0,'Complete',0,12),(4,6,0,'NotOpened',0,0),(4,7,0,'NotOpened',0,0),(4,8,0,'NotOpened',0,0),(4,9,0,'Processed',0,12),(4,10,0,'Processed',0,12),(4,11,0,'Processed',0,12),(4,12,0,'Processed',0,12),(4,13,0,'NotOpened',0,0),(4,14,0,'NotOpened',0,0),(4,15,0,'NotOpened',0,0),(4,16,0,'Processed',0,12),(4,17,0,'NotOpened',0,0),(4,18,0,'NotOpened',0,0),(4,19,0,'NotOpened',0,0),(4,20,0,'NotOpened',0,0),(4,21,0,'NotOpened',0,0),(4,22,0,'NotOpened',0,0),(4,23,0,'Complete',0,12),(4,24,0,'NotOpened',0,0),(4,25,0,'NotOpened',0,0),(4,26,0,'Processed',0,12),(5,1,10,'Processed',0,32),(5,2,8,'NotOpened',0,0),(5,3,10,'Processed',0,34),(5,4,9,'NotOpened',0,0),(5,5,10,'NotOpened',0,0),(5,6,10,'NotOpened',0,0),(5,7,10,'NotOpened',0,0),(5,8,9,'NotOpened',0,0),(5,9,10,'NotOpened',0,0),(5,10,10,'NotOpened',0,0),(5,11,10,'NotOpened',0,0),(5,12,9,'NotOpened',0,0),(5,13,9,'NotOpened',0,0),(5,14,10,'NotOpened',0,0),(5,15,10,'NotOpened',0,0),(5,16,9,'NotOpened',0,0),(5,17,10,'NotOpened',0,0),(5,18,10,'NotOpened',0,0),(5,19,10,'NotOpened',0,0),(5,20,6,'NotOpened',0,0),(5,21,10,'NotOpened',0,0),(5,22,6,'NotOpened',0,0),(5,23,10,'NotOpened',0,0),(5,24,9,'NotOpened',0,0),(5,25,10,'NotOpened',0,0),(5,26,10,'NotOpened',0,0),(6,1,10,'NotOpened',0,0),(6,2,9,'NotOpened',0,0),(6,3,9,'NotOpened',0,0),(6,4,9,'NotOpened',0,0),(6,5,10,'NotOpened',0,0),(6,6,9,'NotOpened',0,0),(6,7,8,'NotOpened',0,0),(6,8,8,'NotOpened',0,0),(6,9,9,'NotOpened',0,0),(6,10,9,'NotOpened',0,0),(6,11,10,'NotOpened',0,0),(6,12,10,'NotOpened',0,0),(6,13,10,'NotOpened',0,0),(6,14,9,'NotOpened',0,0),(6,15,9,'NotOpened',0,0),(6,16,9,'NotOpened',0,0),(6,17,8,'NotOpened',0,0),(6,18,10,'NotOpened',0,0),(6,19,10,'NotOpened',0,0),(6,20,10,'NotOpened',0,0),(6,21,9,'NotOpened',0,0),(6,22,10,'NotOpened',0,0),(6,23,9,'NotOpened',0,0),(6,24,9,'NotOpened',0,0),(6,25,9,'NotOpened',0,0),(6,26,10,'NotOpened',0,0),(7,1,10,'NotOpened',0,0),(7,2,6,'NotOpened',0,0),(7,3,7,'NotOpened',0,0),(7,4,9,'NotOpened',0,0),(7,5,10,'NotOpened',0,0),(7,6,8,'NotOpened',0,0),(7,7,9,'NotOpened',0,0),(7,8,9,'NotOpened',0,0),(7,9,7,'NotOpened',0,0),(7,10,10,'NotOpened',0,0),(7,11,9,'NotOpened',0,0),(7,12,0,'NotOpened',0,0),(7,13,10,'NotOpened',0,0),(7,14,10,'NotOpened',0,0),(7,15,10,'NotOpened',0,0),(7,16,10,'NotOpened',0,0),(7,17,10,'NotOpened',0,0),(7,18,10,'NotOpened',0,0),(7,19,10,'NotOpened',0,0),(7,20,7,'NotOpened',0,0),(7,21,9,'NotOpened',0,0),(7,22,10,'NotOpened',0,0),(7,23,10,'NotOpened',0,0),(7,24,9,'NotOpened',0,0),(7,25,7,'NotOpened',0,0),(7,26,10,'NotOpened',0,0),(8,1,10,'NotOpened',0,0),(8,2,9,'NotOpened',0,0),(8,3,9,'NotOpened',0,0),(8,4,9,'NotOpened',0,0),(8,5,9,'NotOpened',0,0),(8,6,9,'NotOpened',0,0),(8,7,10,'NotOpened',0,0),(8,8,10,'NotOpened',0,0),(8,9,9,'NotOpened',0,0),(8,10,10,'NotOpened',0,0),(8,11,10,'NotOpened',0,0),(8,12,10,'NotOpened',0,0),(8,13,10,'NotOpened',0,0),(8,14,10,'NotOpened',0,0),(8,15,10,'NotOpened',0,0),(8,16,10,'NotOpened',0,0),(8,17,10,'NotOpened',0,0),(8,18,10,'NotOpened',0,0),(8,19,10,'NotOpened',0,0),(8,20,10,'NotOpened',0,0),(8,21,10,'NotOpened',0,0),(8,22,10,'NotOpened',0,0),(8,23,10,'NotOpened',0,0),(8,24,10,'NotOpened',0,0),(8,25,10,'NotOpened',0,0),(8,26,10,'NotOpened',0,0),(9,1,10,'NotOpened',0,0),(9,2,10,'NotOpened',0,0),(9,3,7,'NotOpened',0,0),(9,4,10,'NotOpened',0,0),(9,5,10,'NotOpened',0,0),(9,6,10,'NotOpened',0,0),(9,7,7,'NotOpened',0,0),(9,8,9,'NotOpened',0,0),(9,9,10,'NotOpened',0,0),(9,10,10,'NotOpened',0,0),(9,11,10,'NotOpened',0,0),(9,12,10,'NotOpened',0,0),(9,13,10,'NotOpened',0,0),(9,14,10,'NotOpened',0,0),(9,15,10,'NotOpened',0,0),(9,16,10,'NotOpened',0,0),(9,17,10,'NotOpened',0,0),(9,18,10,'NotOpened',0,0),(9,19,10,'NotOpened',0,0),(9,20,10,'NotOpened',0,0),(9,21,10,'NotOpened',0,0),(9,22,10,'NotOpened',0,0),(9,23,10,'NotOpened',0,0),(9,24,10,'NotOpened',0,0),(9,25,10,'NotOpened',0,0),(9,26,10,'NotOpened',0,0),(10,1,10,'NotOpened',0,0),(10,2,1,'NotOpened',0,0),(10,3,5,'NotOpened',0,0),(10,4,0,'NotOpened',0,0),(10,5,8,'NotOpened',0,0),(10,6,4,'NotOpened',0,0),(10,7,7,'NotOpened',0,0),(10,8,8,'NotOpened',0,0),(10,9,7,'NotOpened',0,0),(10,10,9,'NotOpened',0,0),(10,11,10,'NotOpened',0,0),(10,12,10,'NotOpened',0,0),(10,13,9,'NotOpened',0,0),(10,14,9,'NotOpened',0,0),(10,15,0,'NotOpened',0,0),(10,16,9,'NotOpened',0,0),(10,17,9,'NotOpened',0,0),(10,18,6,'NotOpened',0,0),(10,19,8,'NotOpened',0,0),(10,20,9,'NotOpened',0,0),(10,21,0,'NotOpened',0,0),(10,22,0,'NotOpened',0,0),(10,23,3,'NotOpened',0,0),(10,24,9,'NotOpened',0,0),(10,25,0,'NotOpened',0,0),(10,26,6,'NotOpened',0,0),(11,1,10,'NotOpened',0,0),(11,2,10,'NotOpened',0,0),(11,3,10,'NotOpened',0,0),(11,4,9,'NotOpened',0,0),(11,5,10,'NotOpened',0,0),(11,6,8,'NotOpened',0,0),(11,7,10,'NotOpened',0,0),(11,8,9,'NotOpened',0,0),(11,9,10,'NotOpened',0,0),(11,10,9,'NotOpened',0,0),(11,11,9,'NotOpened',0,0),(11,12,9,'NotOpened',0,0),(11,13,10,'NotOpened',0,0),(11,14,7,'NotOpened',0,0),(11,15,7,'NotOpened',0,0),(11,16,10,'NotOpened',0,0),(11,17,10,'NotOpened',0,0),(11,18,10,'NotOpened',0,0),(11,19,10,'NotOpened',0,0),(11,20,9,'NotOpened',0,0),(11,21,9,'NotOpened',0,0),(11,22,9,'NotOpened',0,0),(11,23,9,'NotOpened',0,0),(11,24,10,'NotOpened',0,0),(11,25,10,'NotOpened',0,0),(11,26,10,'NotOpened',0,0),(12,1,10,'NotOpened',0,0),(12,2,3,'NotOpened',0,0),(12,3,10,'NotOpened',0,0),(12,4,5,'NotOpened',0,0),(12,5,10,'NotOpened',0,0),(12,6,0,'NotOpened',0,0),(12,7,10,'NotOpened',0,0),(12,8,3,'NotOpened',0,0),(12,9,7,'NotOpened',0,0),(12,10,10,'NotOpened',0,0),(12,11,0,'NotOpened',0,0),(12,12,0,'NotOpened',0,0),(12,13,7,'NotOpened',0,0),(12,14,7,'NotOpened',0,0),(12,15,10,'NotOpened',0,0),(12,16,3,'NotOpened',0,0),(12,17,9,'NotOpened',0,0),(12,18,10,'NotOpened',0,0),(12,19,9,'NotOpened',0,0),(12,20,4,'NotOpened',0,0),(12,21,6,'NotOpened',0,0),(12,22,5,'NotOpened',0,0),(12,23,10,'NotOpened',0,0),(12,24,10,'NotOpened',0,0),(12,25,9,'NotOpened',0,0),(12,26,10,'NotOpened',0,0),(13,1,10,'NotOpened',0,0),(13,2,10,'NotOpened',0,0),(13,3,10,'NotOpened',0,0),(13,4,10,'NotOpened',0,0),(13,5,10,'NotOpened',0,0),(13,6,10,'NotOpened',0,0),(13,7,10,'NotOpened',0,0),(13,8,10,'NotOpened',0,0),(13,9,10,'NotOpened',0,0),(13,10,10,'NotOpened',0,0),(13,11,10,'NotOpened',0,0),(13,12,10,'NotOpened',0,0),(13,13,10,'NotOpened',0,0),(13,14,10,'NotOpened',0,0),(13,15,10,'NotOpened',0,0),(13,16,10,'NotOpened',0,0),(13,17,10,'NotOpened',0,0),(13,18,10,'NotOpened',0,0),(13,19,10,'NotOpened',0,0),(13,20,10,'NotOpened',0,0),(13,21,10,'NotOpened',0,0),(13,22,10,'NotOpened',0,0),(13,23,10,'NotOpened',0,0),(13,24,10,'NotOpened',0,0),(13,25,10,'NotOpened',0,0),(13,26,10,'NotOpened',0,0),(14,1,10,'NotOpened',0,0),(14,2,10,'NotOpened',0,0),(14,3,10,'NotOpened',0,0),(14,4,10,'NotOpened',0,0),(14,5,10,'NotOpened',0,0),(14,6,10,'NotOpened',0,0),(14,7,10,'NotOpened',0,0),(14,8,10,'NotOpened',0,0),(14,9,10,'NotOpened',0,0),(14,10,10,'NotOpened',0,0),(14,11,10,'NotOpened',0,0),(14,12,10,'NotOpened',0,0),(14,13,10,'NotOpened',0,0),(14,14,10,'NotOpened',0,0),(14,15,10,'NotOpened',0,0),(14,16,10,'NotOpened',0,0),(14,17,10,'NotOpened',0,0),(14,18,10,'NotOpened',0,0),(14,19,10,'NotOpened',0,0),(14,20,10,'NotOpened',0,0),(14,21,10,'NotOpened',0,0),(14,22,10,'NotOpened',0,0),(14,23,10,'NotOpened',0,0),(14,24,10,'NotOpened',0,0),(14,25,10,'NotOpened',0,0),(14,26,10,'NotOpened',0,0);
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
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` double NOT NULL,
  `item_img_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,-1,61,25,'23/11/2022 21:00','76.0','later','Delivery'),(2,4,50,20,'11/11/2022 00:00','50.0','paid','Pickup'),(3,-1,850,25,'01/11/2022 01:00','850.0','later','Delivery'),(4,-1,125,25,'10/11/2022 07:00','125.0','later','Delivery'),(5,-1,21,20,'20/11/2022 05:00','21.0','paid','Delivery'),(6,-1,107,25,'06/11/2022 06:00','107.0','later','Delivery'),(7,7,123,25,'28/11/2022 12:00','123.0','later','Pickup'),(8,4,77,23,'01/11/2022 11:00','96.0','later','On-site'),(9,4,129,23,'12/11/2022 09:00','129.0','later','On-site'),(10,4,118,23,'30/11/2022 10:00','118.0','later','On-site'),(11,11,84,25,'14/11/2022 19:00','84.0','later','Pickup'),(12,10,80,22,'23/11/2022 06:00','80.0','paid','On-site'),(13,4,63,23,'02/11/2022 12:00','63.0','later','On-site'),(14,12,76,25,'15/11/2022 17:00','76.0','later','Pickup'),(15,4,198,23,'10/11/2022 07:00','198.0','later','On-site'),(16,12,123,25,'21/11/2022 07:00','123.0','later','Pickup'),(17,10,118,22,'20/11/2022 04:00','118.0','paid','On-site'),(18,4,113,23,'02/11/2022 17:00','113.0','later','On-site'),(19,4,317,23,'17/11/2022 10:00','317.0','later','On-site'),(20,4,168,20,'20/11/2022 19:00','168.0','paid','Pickup'),(21,4,468,23,'05/11/2022 06:00','468.0','later','On-site'),(22,4,35,20,'29/11/2022 19:00','35.0','paid','Pickup'),(23,4,110,23,'07/11/2022 17:00','110.0','later','On-site'),(24,10,26,22,'29/11/2022 10:00','26.0','paid','On-site'),(25,10,127,22,'11/11/2022 11:00','127.0','paid','On-site'),(26,4,48,23,'14/11/2022 17:00','60.0','later','On-site'),(27,4,16,23,'07/11/2022 23:00','20.0','later','On-site'),(28,10,68,25,'09/11/2022 10:00','68.0','later','On-site'),(29,4,330,20,'12/11/2022 11:00','330.0','paid','Pickup'),(30,10,182,25,'12/11/2022 09:00','182.0','later','On-site'),(31,2,39,23,'25/11/2022 20:00','49.0','later','Pickup'),(32,5,38,23,'26/11/2022 14:00','47.0','later','Pickup'),(33,6,41,23,'18/11/2022 22:00','51.0','later','Pickup'),(34,6,24,20,'04/11/2022 16:00','30.0','paid','Pickup'),(35,5,12,20,'03/11/2022 07:00','15.0','paid','Pickup'),(36,6,23,20,'13/11/2022 01:00','29.0','paid','Pickup'),(37,8,36,22,'04/11/2022 10:00','51.0','paid','Pickup'),(38,7,11,22,'24/11/2022 12:00','16.0','paid','Pickup'),(39,11,13,22,'10/11/2022 22:00','18.0','paid','Pickup'),(40,9,39,22,'28/11/2022 12:00','55.0','paid','Pickup'),(41,12,11,25,'01/11/2022 10:00','16.0','later','Pickup'),(42,11,13,25,'07/11/2022 15:00','19.0','later','Pickup'),(43,6,61,25,'01/12/2022 08:47','76','later','On-site'),(44,4,50,20,'01/12/2022 09:48','50','paid','On-site'),(45,4,850,25,'01/12/2022 10:49','850','later','On-site'),(46,11,125,25,'02/12/2022 10:49','125','later','On-site'),(47,10,21,20,'03/12/2022 11:49','21','paid','On-site'),(48,12,107,25,'04/12/2022 12:49','107','later','On-site'),(49,10,123,25,'05/12/2022 13:49','123','later','On-site'),(50,4,77,23,'06/12/2022 11:49','96','later','On-site'),(51,4,129,23,'07/12/2022 10:49','129','later','On-site'),(52,4,118,23,'08/12/2022 10:49','118','later','On-site'),(53,11,84,25,'09/12/2022 10:49','84','later','On-site'),(54,10,80,22,'10/12/2022 10:49','80','paid','On-site'),(55,4,63,23,'11/12/2022 10:49','63','later','On-site'),(56,12,76,25,'12/12/2022 10:49','76','later','On-site'),(57,4,198,23,'13/12/2022 10:49','198','later','On-site'),(58,12,123,25,'14/12/2022 10:49','123','later','On-site'),(59,10,118,22,'15/12/2022 10:49','118','paid','On-site'),(60,4,113,23,'16/12/2022 10:49','113','later','On-site'),(61,4,317,23,'17/12/2022 10:49','317','later','On-site'),(62,4,168,20,'18/12/2022 10:49','168','paid','On-site'),(63,4,468,23,'19/12/2022 10:49','468','later','On-site'),(64,4,35,20,'20/12/2022 10:49','35','paid','On-site'),(65,4,110,23,'21/12/2022 10:49','110','later','On-site'),(66,10,26,22,'22/12/2022 10:49','26','paid','On-site'),(67,10,127,22,'23/12/2022 19:49','127','paid','On-site'),(68,4,48,23,'24/12/2022 19:49','60','later','On-site'),(69,4,16,23,'17/12/2022 19:56','20','later','On-site'),(70,10,68,25,'16/12/2022 19:57','68','later','On-site'),(71,4,330,20,'17/12/2022 19:58','330','paid','On-site'),(72,10,182,25,'17/12/2022 19:58','182','later','On-site'),(73,2,39,23,'16/12/2022 22:21','49','later','On-site'),(74,5,38,23,'17/12/2022 22:21','47','later','On-site'),(75,6,41,23,'17/12/2022 22:22','51','later','On-site'),(76,6,24,20,'18/12/2022 22:23','30','paid','On-site'),(77,5,12,20,'17/12/2022 22:24','15','paid','On-site'),(78,6,23,20,'22/12/2022 22:24','29','paid','On-site'),(79,8,36,22,'20/12/2022 22:25','51','paid','On-site'),(80,7,11,22,'25/12/2022 22:25','16','paid','On-site'),(81,11,13,22,'26/12/2022 22:26','18','paid','On-site'),(82,9,39,22,'30/12/2022 22:26','55','paid','On-site'),(83,12,11,25,'30/12/2022 22:29','16','later','On-site'),(84,11,13,25,'31/12/2022 22:29','19','later','On-site'),(85,4,52,12,'11/11/2022 13:35','12','later','On-site');
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
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders_report`
--

LOCK TABLES `orders_report` WRITE;
/*!40000 ALTER TABLE `orders_report` DISABLE KEYS */;
INSERT INTO `orders_report` VALUES (69,'Burj Khalifa Tower,4,235,Dubai Hospital,8,745,Abu Dhabi,4,317,City Centre Mirdif,1,36,Dubai Mall,1,11,Mall of the Emirates,1,39','12','2022','UAE'),(70,'Haifa University,4,149,Rambam Hospital,16,3090,Karmiel Train Station,1,39,Ofer Grand Canyon,2,50','12','2022','North'),(71,'Dubai Mall,2,134,Burj Khalifa Tower,3,110,Dubai Hospital,6,601,Abu Dhabi,3,210,City Centre Mirdif,1,36,Mall of the Emirates,1,39','11','2022','UAE'),(72,'Rambam Hospital,16,2292,Karmiel Train Station,1,39,Ofer Grand Canyon,2,50,Haifa University,3,88','11','2022','North');
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
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_messages`
--

LOCK TABLES `personal_messages` WRITE;
/*!40000 ALTER TABLE `personal_messages` DISABLE KEYS */;
INSERT INTO `personal_messages` VALUES (1,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nPeach Flavored Protein Yogurt'),(2,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nPeach Flavored Protein Yogurt'),(3,25,'17/01/2023','Simulation: Delivery','This message sent via SMS to 0538503033:\nHi!\nOrder number 1 is on the way\nThe estimated arrivel time is 17/01/2023 23:50'),(4,25,'17/01/2023','Simulation: Delivery','This message sent via SMS to 0538503033:\nHi!\nOrder number 3 is on the way\nThe estimated arrivel time is 17/01/2023 23:50'),(5,25,'17/01/2023','Simulation: Delivery','This message sent via SMS to 0538503033:\nHi!\nOrder number 4 is on the way\nThe estimated arrivel time is 17/01/2023 23:50'),(6,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nBamba Nougat, Twix Bar, Bissli BBQ'),(7,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nBamba Nougat, Twix Bar, Bissli BBQ'),(8,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nCoca Cola Zero Can, Coca Cola Can, Bissli BBQ, Coca-Cola Zero Bottle, Klik Conrflakes'),(9,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nCoca Cola Zero Can, Coca Cola Can, Bissli BBQ, Coca-Cola Zero Bottle, Klik Conrflakes'),(10,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nCoca-Cola Zero Bottle, Pack of Oreo\'s, Coca Cola Zero Can, Peach Flavored Water, Spicey & Sour Doritos '),(11,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #11:\nKlik Biscuit, M&M Peanut, Coca Cola Zero Can, Mexican Doritos , Spicey & Sour Doritos , Pack of Oreo\'s, Coca Cola Can'),(12,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nCoca-Cola Zero Bottle, Pack of Oreo\'s, Coca Cola Zero Can, Peach Flavored Water, Spicey & Sour Doritos '),(13,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #11:\nKlik Biscuit, M&M Peanut, Coca Cola Zero Can, Mexican Doritos , Spicey & Sour Doritos , Pack of Oreo\'s, Coca Cola Can'),(14,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nKinder Chocolate Sticks'),(15,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nKinder Chocolate Sticks'),(16,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nApple Flavored Water'),(17,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nApple Flavored Water'),(18,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKlik Biscuit, Klik Conrflakes, Coca Cola Bottle, Bissli BBQ'),(19,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKlik Biscuit, Klik Conrflakes, Coca Cola Bottle, Bissli BBQ'),(20,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nSnickers Bar'),(21,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nMexican Doritos , Kif Kef, Coca Cola Zero Can, Coca Cola Can'),(22,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nMexican Doritos , Kif Kef, Coca Cola Zero Can, Coca Cola Can'),(23,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nSnickers Bar'),(24,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nGrapes Flavored Water, Fanta, Bamba Nougat'),(25,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nGrapes Flavored Water, Fanta, Bamba Nougat'),(26,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nApple Flavored Water, Elite Kranz Milk Chocolate, Twix Bar'),(27,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nApple Flavored Water, Elite Kranz Milk Chocolate, Twix Bar'),(28,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nPack of Oreo\'s, Coca Cola Zero Can, Apple Flavored Water, Prigat Orange Juice, Klik Conrflakes, Elite Kranz Milk Chocolate, Grapes Flavored Water, Klik Biscuit'),(29,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nPack of Oreo\'s, Coca Cola Zero Can, Apple Flavored Water, Prigat Orange Juice, Klik Conrflakes, Elite Kranz Milk Chocolate, Grapes Flavored Water, Klik Biscuit'),(30,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKinder Chocolate Sticks'),(31,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKinder Chocolate Sticks'),(32,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nPeach Flavored Protein Yogurt'),(33,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nPeach Flavored Protein Yogurt'),(34,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nBamba Nougat'),(35,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nBamba Nougat'),(36,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nSpicey & Sour Doritos , Bamba Nougat, Orbit Mint Gum'),(37,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nSpicey & Sour Doritos , Bamba Nougat, Orbit Mint Gum'),(38,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKinder Bueno White Chocolate'),(39,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKinder Bueno White Chocolate'),(40,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKif Kef'),(41,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKif Kef'),(42,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nPeach Flavored Protein Yogurt'),(43,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nPeach Flavored Protein Yogurt'),(44,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKinder Chocolate Sticks, Snickers Bar, M&M Peanut, Peach Flavored Water, Spicey & Sour Doritos , Orbit Mint Gum, Fanta, Mexican Doritos '),(45,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #4:\nKinder Chocolate Sticks, Snickers Bar, M&M Peanut, Peach Flavored Water, Spicey & Sour Doritos , Orbit Mint Gum, Fanta, Mexican Doritos '),(46,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nKlik Biscuit, Peach Flavored Protein Yogurt, Bamba Nougat, M&M Peanut'),(47,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #10:\nKlik Biscuit, Peach Flavored Protein Yogurt, Bamba Nougat, M&M Peanut'),(48,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #5:\nPack of Oreo\'s, Coca Cola Bottle, Elite Kranz Milk Chocolate, Bamba Nougat, Peach Flavored Water, Klik Biscuit'),(49,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #5:\nPack of Oreo\'s, Coca Cola Bottle, Elite Kranz Milk Chocolate, Bamba Nougat, Peach Flavored Water, Klik Biscuit'),(50,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #6:\nKlik Biscuit, Fanta, Bissli BBQ, Bamba Nougat, Spicey & Sour Doritos , Prigat Orange Juice'),(51,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #6:\nKlik Biscuit, Fanta, Bissli BBQ, Bamba Nougat, Spicey & Sour Doritos , Prigat Orange Juice'),(52,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #6:\nCoca-Cola Zero Bottle, Apple Flavored Water, M&M Peanut, Peach Flavored Water'),(53,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #6:\nCoca-Cola Zero Bottle, Apple Flavored Water, M&M Peanut, Peach Flavored Water'),(54,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #5:\nBamba Nougat, Coca Cola Zero Can'),(55,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #5:\nBamba Nougat, Coca Cola Zero Can'),(56,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #6:\nPack of Oreo\'s, Mexican Doritos , Fanta, Snickers Bar'),(57,1,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to lidor@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #6:\nPack of Oreo\'s, Mexican Doritos , Fanta, Snickers Bar'),(58,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #11:\nOrbit Mint Gum, Coca-Cola Zero Bottle, Kinder Bueno White Chocolate'),(59,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #11:\nOrbit Mint Gum, Coca-Cola Zero Bottle, Kinder Bueno White Chocolate'),(60,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via SMS to 0538503033:\nAlert!\nThere are some items under the minimum amount of Machine #11:\nM&M Peanut, Peach Flavored Protein Yogurt, Kinder Chocolate Sticks'),(61,7,'17/01/2023','Simulation: Minimum amount Alert','This message sent via Email to dima@EKrut.com:\nAlert!\nThere are some items under the minimum amount of Machine #11:\nM&M Peanut, Peach Flavored Protein Yogurt, Kinder Chocolate Sticks');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pickups`
--

LOCK TABLES `pickups` WRITE;
/*!40000 ALTER TABLE `pickups` DISABLE KEYS */;
INSERT INTO `pickups` VALUES (1,'inProgress'),(2,'inProgress'),(3,'inProgress'),(4,'inProgress'),(5,'inProgress'),(6,'inProgress'),(7,'inProgress'),(8,'inProgress'),(11,'inProgress'),(14,'inProgress'),(16,'inProgress'),(19,'inProgress'),(20,'inProgress'),(21,'inProgress'),(22,'inProgress'),(23,'inProgress'),(24,'inProgress'),(27,'inProgress'),(29,'inProgress'),(31,'inProgress'),(32,'inProgress'),(33,'inProgress'),(34,'inProgress'),(35,'inProgress'),(36,'inProgress'),(37,'inProgress'),(38,'inProgress'),(39,'inProgress'),(40,'inProgress'),(41,'inProgress'),(42,'inProgress');
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
  `region_name` varchar(100) NOT NULL,
  PRIMARY KEY (`region_id`),
  UNIQUE KEY `region_name_UNIQUE` (`region_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regions`
--

LOCK TABLES `regions` WRITE;
/*!40000 ALTER TABLE `regions` DISABLE KEYS */;
INSERT INTO `regions` VALUES (1,'North'),(3,'South'),(2,'UAE');
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
  PRIMARY KEY (`id`),
  KEY `region_idx` (`region`),
  CONSTRAINT `region` FOREIGN KEY (`region`) REFERENCES `regions` (`region_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,'UAE','30%','Tuesday, Friday, Wednesday','07:00:00','23:00:00','Active'),(2,'North','20%','Tuesday','07:00:00','23:00:00','Active'),(3,'North','20%','Monday','05:00:00','13:00:00','NotActive'),(4,'South','1+1','Monday, Wednesday','05:00:00','13:00:00','NotActive'),(5,'North','20%','Monday, Wednesday','05:00:00','22:00:00','NotActive');
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
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supply_report`
--

LOCK TABLES `supply_report` WRITE;
/*!40000 ALTER TABLE `supply_report` DISABLE KEYS */;
INSERT INTO `supply_report` VALUES (15,1,7,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,2,7,6,6,10,5,2,10,10,5,10,10,10,10,6,10,10,10,10,10,10,10,10,10','11','2022','1'),(16,2,3,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,8,6,2,10,9,9,10,10,10,10,10,10,10,10,10,6,6,10,10,10,9,10,10,10','11','2022','1'),(17,3,9,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,9,6,10,9,4,9,5,10,10,10,9,10,10,10,10,10,10,10,10,10,6,10,10,10,10','11','2022','1'),(18,4,9,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,2,3,2,3,1,1,2,3,2,2,4,1,2,2,2,2,2,2,1,2,1,2,2,1,2','10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','11','2022','1'),(19,5,15,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,2,0,1,0,0,0,1,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,1,0,0','10,8,10,9,10,10,10,9,10,10,10,9,9,10,10,9,10,10,10,6,10,6,10,9,10,10','11','2022','1'),(20,6,12,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,1,1,1,0,1,1,1,1,1,0,0,0,1,1,0,2,0,0,0,0,0,0,1,1,0','10,9,9,9,10,9,8,8,9,9,10,10,10,9,9,9,8,10,10,10,9,10,9,9,9,10','11','2022','1'),(21,7,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,6,7,9,10,8,9,9,7,10,9,0,10,10,10,10,10,10,10,7,9,10,10,9,7,10','11','2022','2'),(22,8,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,9,9,9,9,9,10,10,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','11','2022','2'),(23,9,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,7,10,10,10,7,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','11','2022','2'),(24,10,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,3,0,1,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,2,0,1,0','10,1,5,0,8,4,7,8,7,9,10,10,9,9,0,9,9,6,8,9,0,0,3,9,0,6','11','2022','2'),(25,11,30,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,1,0,2,0,1,0,1,1,1,0,1,1,0,0,0,0,1,1,1,1,0,0,0','10,10,10,9,10,8,10,9,10,9,9,9,10,7,7,10,10,10,10,9,9,9,9,10,10,10','11','2022','2'),(26,12,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,3,10,5,10,0,10,3,7,10,0,0,7,7,10,3,9,10,9,4,6,5,10,10,9,10','11','2022','2'),(27,13,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','11','2022','3'),(28,14,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','11','2022','3'),(29,1,7,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,2,7,6,6,10,5,2,10,10,5,10,10,10,10,6,10,10,10,10,10,10,10,10,10','12','2022','1'),(30,2,3,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,8,6,2,10,9,9,10,10,10,10,10,10,10,10,10,6,6,10,10,10,9,10,10,10','12','2022','1'),(31,3,9,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,9,6,10,9,4,9,5,10,10,10,9,10,10,10,10,10,10,10,10,10,6,10,10,10,10','12','2022','1'),(32,4,9,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','12','2022','1'),(33,5,15,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,8,10,9,10,10,10,9,10,10,10,9,9,10,10,9,10,10,10,6,10,6,10,9,10,10','12','2022','1'),(34,6,12,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,9,9,9,10,9,8,8,9,9,10,10,10,9,9,9,8,10,10,10,9,10,9,9,9,10','12','2022','1'),(35,7,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,6,7,9,10,8,9,9,7,10,9,0,10,10,10,10,10,10,10,7,9,10,10,9,7,10','12','2022','2'),(36,8,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,9,9,9,9,9,10,10,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(37,9,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,7,10,10,10,7,9,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','2'),(38,10,5,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,1,5,0,8,4,7,8,7,9,10,10,9,9,0,9,9,6,8,9,0,0,3,9,0,6','12','2022','2'),(39,11,30,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,9,10,8,10,9,10,9,9,9,10,7,7,10,10,10,10,9,9,9,9,10,10,10','12','2022','2'),(40,12,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,3,10,5,10,0,10,3,7,10,0,0,7,7,10,3,9,10,9,4,6,5,10,10,9,10','12','2022','2'),(41,13,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','3'),(42,14,0,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0','10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10','12','2022','3');
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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'100878859','rmNorth','123456','Lidor','Ankava','lidor@EKrut.com','0538503033',NULL,'North','regionManager',0,0),(2,'101868859','doNorth','123456','Eyal','Greenberg','eyal@EKrut.com','0538503033',NULL,'North','deliveryOperator',0,0),(3,'106828759','mwNorth','123456','Ofer','Gold','ofer@EKrut.com','0538503033',NULL,'North','marketingWorker',0,0),(4,'102858859','rmSouth','123456','Neta','The fork','neta@EKrut.com','0538503033',NULL,'South','regionManager',0,0),(5,'103848859','doSouth','123456','Yossef','Ben porat','yossef@EKrut.com','0538503033',NULL,'South','deliveryOperator',0,0),(6,'106828858','mwSouth','123456','Bigi','Amzalag','ofer@EKrut.com','0538503033',NULL,'South','marketingWorker',0,0),(7,'104838859','rmUAE','123456','Dima','Chdnsky','dima@EKrut.com','0538503033',NULL,'UAE','regionManager',0,0),(8,'105828859','doUAE','123456','Hamudi','Kabudi','hamudi@EKrut.com','0538503033',NULL,'UAE','deliveryOperator',0,0),(9,'106828957','mwUAE','123456','Guy','Amzalag','ofer@EKrut.com','0538503033',NULL,'UAE','marketingWorker',1,0),(10,'106818859','csworker','123456','Ravit','Gamliel','ravit@EKrut.com','0538503033',NULL,'null','customerServiceWorker',0,0),(11,'106838659','mktmanager','123456','Vital','Marciano','vital@EKrut.com','0538503033',NULL,'null','marketingManager',0,0),(12,'106848559','spw1','123456','David','Asulin','david@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(13,'106858459','spw2','123456','Yair','Asulin','yair@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(14,'106868359','spw3','123456','Saray','Asulin','saray@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(15,'106878259','spw4','123456','Rivka','Asulin','rivka@EKrut.com','0538503033',NULL,'null','supplyWorker',0,0),(16,'106888159','ceo','123456','Adam','Mizrahi','adam@EKrut.com','0538503033',NULL,'null','CEO',0,0),(17,'007818859','usrN','123456','Anna','Zak','anna@gmail.com','0538503033',NULL,'North','user',0,1),(18,'205818859','usrS','123456','Omer','Adam','omer@gmail.com','0538503033',NULL,'South','user',0,1),(19,'304818859','usrU','123456','Eden','Ben Zaken','kapara@gmail.com','0538503033',NULL,'UAE','user',0,1),(20,'403818859','rgsN','123456','Ravid','Plotnik','nechi@gmail.com','0538503033','1234123412341234','North','registered',0,0),(21,'502818859','rgsS','123456','Keren','Peles','keren@gmail.com','0538503033','1234123412341234','South','registered',0,0),(22,'601818859','rgsU','123456','Berry','Sakharof','berry@gmail.com','0538503033','1234123412341234','UAE','registered',0,0),(23,'700818859','mbrN','123456','Idan','Haviv','idan@gmail.com','0538503033','1234123412341234','North','member',0,0),(24,'800808859','mbrS','123456','Itay','Levi','itay@gmail.com','0538503033','1234123412341234','South','member',0,0),(25,'900808759','mbrU','123456','Daniel','Salomon','daniel@gmail.com','0538503033','1234123412341234','UAE','member',0,0);
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

-- Dump completed on 2022-12-17 23:52:07
