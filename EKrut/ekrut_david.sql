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
  `description` varchar(500) DEFAULT NULL,
  `supplymethods` varchar(45) DEFAULT NULL,
  `totalorders` varchar(45) DEFAULT NULL,
  `year` varchar(45) DEFAULT NULL,
  `month` varchar(45) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients_report`
--

LOCK TABLES `clients_report` WRITE;
/*!40000 ALTER TABLE `clients_report` DISABLE KEYS */;
INSERT INTO `clients_report` VALUES (1,'0-2,40,3-6,65,6-12,98,13-17,15,18-25,9','100,27','227','2021','01','North');
/*!40000 ALTER TABLE `clients_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `creditCardNumber` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `expireMonth` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `expireYear` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `cvv` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `subscriberID` int DEFAULT NULL,
  `firstPurchase` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `subscriberID_UNIQUE` (`subscriberID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
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
  `customer_id` varchar(45) NOT NULL,
  `address` varchar(128) NOT NULL,
  `estimated_time` varchar(128) DEFAULT NULL,
  `deilvery_status` enum('pendingApproval','outForDelivery','done') NOT NULL DEFAULT 'pendingApproval',
  `customer_status` enum('APPROVED','NOT_APPROVED') NOT NULL DEFAULT 'NOT_APPROVED',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveries`
--

LOCK TABLES `deliveries` WRITE;
/*!40000 ALTER TABLE `deliveries` DISABLE KEYS */;
INSERT INTO `deliveries` VALUES (1,'North','007818859','abcd 3/5 ','25/12/2022 16:50','pendingApproval','NOT_APPROVED'),(2,'North','007818859','aaa 2/3 ','28/12/2022 19:57','outForDelivery','APPROVED'),(3,'North','2','cc 2',NULL,'pendingApproval','NOT_APPROVED'),(4,'Center','6','aaaa 44/5 karmiel',NULL,'pendingApproval','NOT_APPROVED');
/*!40000 ALTER TABLE `deliveries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_address`
--

DROP TABLE IF EXISTS `delivery_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_address` (
  `city` varchar(45) NOT NULL,
  `street` varchar(45) DEFAULT NULL,
  `zipcode` varchar(45) DEFAULT NULL,
  `house_num` int DEFAULT NULL,
  `floor` int DEFAULT NULL,
  `apparetment_num` int DEFAULT NULL,
  PRIMARY KEY (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_address`
--

LOCK TABLES `delivery_address` WRITE;
/*!40000 ALTER TABLE `delivery_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `delivery_address` ENABLE KEYS */;
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
  `current_amount` int DEFAULT NULL,
  `minimum_amount` int DEFAULT NULL,
  `call_status` varchar(45) DEFAULT NULL,
  `times_under_min` int DEFAULT NULL,
  `calls_amount` int DEFAULT NULL,
  PRIMARY KEY (`machine_id`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_in_machine`
--

LOCK TABLES `item_in_machine` WRITE;
/*!40000 ALTER TABLE `item_in_machine` DISABLE KEYS */;
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
INSERT INTO `items` VALUES (1,'Bamba',12,'Bamba.png'),(2,'Bamba nugat',10,'Bamba_Nugat.png'),(3,'Apple flavored water',9,'Apple flavored water.png'),(4,'Click biscuit',8,'Click_Biscuit.png'),(5,'Click cornflakes',8,'Click_Cornflakes.png'),(6,'m&m peanut',8,'m&m peanuts.png'),(7,'orange juice prigat',6,'Orange juice Prigat 500 ml.png'),(8,'oreo pack',10,'Oreo_pack.png'),(9,'Beasley BBQ',8,'Beasley BBQ.png'),(10,'bottle of Coca-Cola Zero 500 ml',7,'bottle of Coca-Cola Zero 500 ml.png'),(11,'Coca Cola can',5,'Coca Cola can.png'),(12,'Coca Cola Zero can',5,'Coca Cola Zero can.png'),(13,'Coke bottle 500 ml',7,'Coke bottle 500 ml.png'),(14,'Dorietos mexican flavor',8,'Dorietos mexican flavor.png'),(15,'Dorietos spicey sour',8,'Dorietos spicey sour.png'),(16,'Elite Karanz milk chocolate',6,'Elite Karanz milk chocolate.png'),(17,'Fanta',5,'Fanta.png'),(18,'Grapes flavored water',6,'Grapes flavored water.png'),(19,'Kief Kef',5,'Kief Kef.png'),(20,'Kinder Bueno White Chocolate',6,'Kinder Bueno White Chocolate.png'),(21,'Kinder chocolate 4 sticks',6,'Kinder chocolate 4 sticks.png'),(22,'Orbit mint gum',5,'Orbit mint gum.png'),(23,'Peach flavored protein yogurt',5,'Peach flavored protein yogurt.png'),(24,'Peach flavored water',6,'Peach flavored water.png'),(25,'Snickers',6,'Snickers.png'),(26,'Twix',6,'Twix.png');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `machines`
--

LOCK TABLES `machines` WRITE;
/*!40000 ALTER TABLE `machines` DISABLE KEYS */;
INSERT INTO `machines` VALUES (1,'North',1,'Ort Braude Academic Collage',NULL),(1,'North',2,'Big Karmiel',NULL),(1,'North',3,'City hall',NULL),(1,'North',4,'Ort Pasgot',NULL),(1,'North',5,'Karmiel Train Station',NULL),(1,'North',6,'Lev Karmiel Mall',NULL),(1,'North',7,'Thecnion',NULL),(1,'North',8,'Rambam hospital',NULL),(1,'North',9,'Ofer Grand Kenyon',NULL),(1,'North',10,'Beni Zion Hospital',NULL),(1,'North',11,'Haifa University',NULL),(1,'North',12,'Academic Gordon Collage',NULL),(1,'North',13,'Galil Maarvi Academic Collage',NULL),(2,'Center',19,'Dizingoffe Center',NULL),(2,'Center',20,'TLV fashion mall',NULL),(2,'Center',21,'Savidor center Train sation',NULL),(2,'Center',22,'Hashalom Train station',NULL),(2,'Center',23,'Tel Aviv university',NULL),(2,'Center',24,'Tel Aviv Yafo acadimic Collage',NULL),(3,'South',25,'Beer Sheva rail station',NULL),(3,'South',26,'Dimona Atomic reactor',NULL);
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
  `products_description` varchar(45) DEFAULT NULL,
  `supply_method` enum('Delivery','Pickup','On-site') NOT NULL DEFAULT 'On-site',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (0,1,112,2,'22/12/2022 14:43','3',NULL,'On-site'),(1,1,112,1,'22/12/2022 14:43','3',NULL,'On-site'),(2,1,142,2,'22/12/2022 14:43','3',NULL,'On-site'),(3,2,155,1,'22/12/2022 14:43','13',NULL,'On-site'),(4,2,41,4,'22/12/2022 14:43','3',NULL,'On-site'),(5,3,77,3,'22/12/2022 14:43','3',NULL,'Delivery'),(6,3,17,5,'22/12/2022 14:43','3',NULL,'Delivery'),(7,1,12,1,'22/12/2022 14:43','3',NULL,'On-site'),(8,2,115,2,'22/12/2022 14:43','3',NULL,'On-site'),(9,1,11,3,'22/12/2022 14:43','3',NULL,'On-site'),(10,3,110,1,'22/12/2022 14:43','3',NULL,'On-site'),(11,2,142,4,'22/12/2022 14:43','3',NULL,'On-site'),(12,6,155,5,'22/12/2022 14:43','13',NULL,'Pickup'),(13,2,41,2,'22/12/2022 14:43','3',NULL,'Pickup'),(14,7,77,1,'22/12/2022 14:43','3',NULL,'Delivery'),(15,3,17,3,'22/12/2022 14:43','3',NULL,'Delivery'),(16,6,12,2,'22/12/2022 14:43','3',NULL,'Pickup'),(17,4,115,1,'22/12/2022 14:43','3',NULL,'Pickup'),(18,5,11,3,'22/12/2022 14:43','3',NULL,'Pickup'),(19,6,110,4,'22/12/2022 14:43','3',NULL,'Pickup'),(20,4,112,3,'11/12/2022 14:43','3',NULL,'Pickup'),(21,2,142,2,'20/12/2022 14:43','3',NULL,'Pickup'),(22,26,155,1,'26/12/2022 14:43','13',NULL,'Pickup'),(23,2,41,5,'13/12/2022 14:43','3',NULL,'Pickup'),(24,2,77,6,'17/12/2022 14:43','3',NULL,'Delivery'),(25,24,17,7,'16/12/2022 14:43','3',NULL,'Delivery'),(26,6,12,7,'05/12/2022 14:43','3',NULL,'Pickup'),(27,21,115,7,'11/12/2022 14:43','3',NULL,'Pickup'),(28,23,11,7,'12/12/2022 14:43','3',NULL,'Pickup'),(29,6,110,7,'25/12/2022 14:43','3',NULL,'Pickup'),(30,11,37,7,'22/12/2022 14:43','1',NULL,'Pickup');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders_online`
--

DROP TABLE IF EXISTS `orders_online`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders_online` (
  `shipment_method` varchar(45) NOT NULL,
  PRIMARY KEY (`shipment_method`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders_online`
--

LOCK TABLES `orders_online` WRITE;
/*!40000 ALTER TABLE `orders_online` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders_online` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders_report`
--

LOCK TABLES `orders_report` WRITE;
/*!40000 ALTER TABLE `orders_report` DISABLE KEYS */;
INSERT INTO `orders_report` VALUES (40,'Ort Braude Academic Collage,5,389,Big Karmiel,7,677,City hall,1,110,Lev Karmiel Mall,6,554,Ort Pasgot,3,342,Karmiel Train Station,2,22,Haifa University,1,37','12','2022','North'),(41,'Savidor center Train sation,1,115,Tel Aviv university,1,11','12','2022','Center'),(42,'Dimona Atomic reactor,1,155','12','2022','South'),(43,'Ort Braude Academic Collage,5,389,Big Karmiel,7,677,City hall,1,110,Lev Karmiel Mall,5,399,Ort Pasgot,2,227,Karmiel Train Station,1,11,Haifa University,1,37','12','2022','North');
/*!40000 ALTER TABLE `orders_report` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regions`
--

LOCK TABLES `regions` WRITE;
/*!40000 ALTER TABLE `regions` DISABLE KEYS */;
INSERT INTO `regions` VALUES (1,'North'),(2,'Center'),(3,'South');
/*!40000 ALTER TABLE `regions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `region` varchar(45) NOT NULL,
  `city` varchar(45) DEFAULT NULL,
  `machine_id` varchar(45) DEFAULT NULL,
  `time` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` int NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `days` enum('Sundy','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday') DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `start_time` varchar(45) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `end_time` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `hours` enum('06-12','12-18','18-00','00-06') DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
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
  `item_id` varchar(500) DEFAULT NULL,
  `item_name` varchar(500) DEFAULT NULL,
  `min_stock` varchar(500) DEFAULT NULL,
  `start_stock` varchar(500) DEFAULT NULL,
  `cur_stock` varchar(500) DEFAULT NULL,
  `year` varchar(45) DEFAULT NULL,
  `month` varchar(45) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  `missing_severiity` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supply_report`
--

LOCK TABLES `supply_report` WRITE;
/*!40000 ALTER TABLE `supply_report` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'regionm','123456','david','asulin','dudyas6@gmmgm.com','500535030',NULL,'North','regionManager',0,0),(2,NULL,'ceom','123456','ceo','ceo','ceo@ceo.ceo','12319024',NULL,'','CEO',0,0),(3,NULL,'customer','123456','customer','customer','customer@customer','123123',NULL,'','registered',0,0),(5,'205905050','customer2','123456','customer1','customer1','customer1@customer1','123123123','205905050','','registered',0,1),(6,'205905050','customer3','123456','customer1','customer1','customer1@customer1','123123123','205905050','','registered',0,0),(7,'205905050','customer4','123456','customer1','customer1','customer1@customer1','123123123','205905050','','registered',0,0),(31,NULL,'regionm2','123456','david','asulin','dudyas6@gmmgm.com','500535030',NULL,'South','regionManager',0,0),(33,'007818859','customer1','123456','Anna','Zak','anna@gmail.com','0538503033',NULL,'North','registered',1,0);
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

-- Dump completed on 2023-01-04 22:56:04
