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
INSERT INTO `clients_report` VALUES (1,'0-2,40,3-6,65,6-12,98,13-17,15,18-25,9','100,100,27','227','2021','01','Karmiel');
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
  `customer_id` int NOT NULL,
  `address` varchar(128) NOT NULL,
  `city` varchar(128) NOT NULL,
  `estimated_time` varchar(128) DEFAULT NULL,
  `actual_time` varchar(128) DEFAULT NULL,
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
INSERT INTO `deliveries` VALUES (1,1,'abcd 3/5 ','Karmiel','25/12/2022 16:50',NULL,'done','APPROVED'),(2,2,'aaa 2/3 ','Karmiel',NULL,NULL,'pendingApproval','NOT_APPROVED'),(3,2,'cc 2','haifa',NULL,NULL,'pendingApproval','NOT_APPROVED');
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
  `current_amount` int NOT NULL,
  `call_status` enum('NotOpened','Processed','Complete') NOT NULL DEFAULT 'NotOpened',
  `times_under_min` int NOT NULL DEFAULT '0',
  `worker_id` int DEFAULT NULL,
  PRIMARY KEY (`machine_id`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_in_machine`
--

LOCK TABLES `item_in_machine` WRITE;
/*!40000 ALTER TABLE `item_in_machine` DISABLE KEYS */;
INSERT INTO `item_in_machine` VALUES (0,0,0,'NotOpened',0,NULL),(0,2,0,'NotOpened',0,NULL),(1,1,6,'NotOpened',0,NULL),(1,2,6,'NotOpened',0,NULL),(1,3,6,'NotOpened',0,NULL),(2,1,6,'NotOpened',0,NULL),(2,2,6,'NotOpened',0,NULL),(2,3,6,'NotOpened',0,NULL),(3,1,5,'NotOpened',0,NULL),(3,2,5,'NotOpened',0,0),(3,3,5,'NotOpened',0,0),(4,1,10,'NotOpened',0,NULL),(4,2,10,'NotOpened',0,NULL),(4,3,10,'NotOpened',0,NULL),(5,1,6,'NotOpened',0,NULL),(5,2,6,'NotOpened',0,NULL),(5,3,6,'NotOpened',0,NULL),(6,1,5,'NotOpened',0,NULL),(6,2,5,'NotOpened',0,NULL),(6,3,5,'NotOpened',0,NULL),(7,1,8,'NotOpened',0,NULL),(7,2,8,'NotOpened',0,NULL),(7,3,8,'NotOpened',0,NULL),(8,1,5,'NotOpened',0,NULL),(8,2,5,'NotOpened',0,NULL),(8,3,5,'NotOpened',0,NULL),(9,1,5,'NotOpened',0,NULL),(9,2,5,'NotOpened',0,NULL),(9,3,5,'NotOpened',0,NULL),(10,1,5,'NotOpened',0,NULL),(10,2,5,'NotOpened',0,NULL),(10,3,5,'NotOpened',0,NULL),(11,1,5,'NotOpened',0,NULL),(11,2,5,'NotOpened',0,NULL),(11,3,5,'NotOpened',0,NULL),(12,1,5,'NotOpened',0,NULL),(12,2,5,'NotOpened',0,NULL),(12,3,5,'NotOpened',0,NULL);
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
  `manufacturer` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
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
INSERT INTO `items` VALUES (1,'Bamba',12,'Osem','penuts snack','Bamba.png'),(2,'Bamba nugat',10,'Osem','penuts snack fiiled with nugat ','Bamba_Nugat.png'),(3,'Apple flavored water',9,'neviyot','Apple flavored water neviyot 500 ml','Apple flavored water.png'),(4,'Click biscuit',8,'Uniliver','Milk chocolate covered biscuit','Click_Biscuit.png'),(5,'Click cornflakes',8,'Uniliver','Cornflakes covered in milk chocolate','Click_Cornflakes.png'),(6,'m&m peanut',8,'MARS MULTISALES ISRAEL LTD','Peanut Chocolate Candies Made of whole peanuts covered with milk chocolate, inside colorful sugar shells','m&m peanuts.png'),(7,'orange juice prigat',6,'Prigat','Prigat Orange juice has a delicious blend of natural flavors and contains 15% fruit juice','Orange juice Prigat 500 ml.png'),(8,'oreo pack',10,' Nabisco','4 packs of 4','Oreo_pack.png'),(9,'Beasley BBQ',8,'osem','0','Beasley BBQ.png'),(10,'bottle of Coca-Cola Zero 500 ml',7,'Coca cola','0','bottle of Coca-Cola Zero 500 ml.png'),(11,'Coca Cola can',5,'Coca cola','330 ml','Coca Cola can.png'),(12,'Coca Cola Zero can',5,'Coca cola','330 ml','Coca Cola Zero can.png'),(13,'Coke bottle 500 ml',7,'Coca cola','0','Coke bottle 500 ml.png'),(14,'Dorietos mexican flavor',8,'Elite','0','Dorietos mexican flavor.png'),(15,'Dorietos spicey sour',8,'Elite','0','Dorietos spicey sour.png'),(16,'Elite Karanz milk chocolate',6,'Elite','0','Elite Karanz milk chocolate.png'),(17,'Fanta',5,'Coca cola','330 ml can','Fanta.png'),(18,'Grapes flavored water',6,'neviyot','0','Grapes flavored water.png'),(19,'Kief Kef',5,'Elite','0','Kief Kef.png'),(20,'Kinder Bueno White Chocolate',6,'Kinder','0','Kinder Bueno White Chocolate.png'),(21,'Kinder chocolate 4 sticks',6,'Kinder','0','Kinder chocolate 4 sticks.png'),(22,'Orbit mint gum',5,'Orbit','0','Orbit mint gum.png'),(23,'Peach flavored protein yogurt',5,'Yople','0','Peach flavored protein yogurt.png'),(24,'Peach flavored water',6,'neviyot','0','Peach flavored water.png'),(25,'Snickers',6,'MARS MULTISALES ISRAEL LTD','0','Snickers.png'),(26,'Twix',6,'MARS MULTISALES ISRAEL LTD','0','Twix.png');
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
INSERT INTO `machines` VALUES (1,'North',1,'Ort Braude Academic Collage',7),(1,'North',2,'Karmiel Train Station',3),(1,'North',3,'Thecnion',5),(1,'North',4,'Rambam hospital',9),(1,'North',5,'Ofer Grand Kenyon',6),(1,'North',6,'Haifa University',6),(2,'Center',7,'Dizingoffe Center',5),(2,'Center',8,'TLV fashion mall',5),(2,'Center',9,'Savidor center Train sation',5),(2,'Center',10,'Hashalom Train station',5),(2,'Center',11,'Tel Aviv university',0),(2,'Center',12,'Tel Aviv Yafo acadimic Collage',0),(3,'South',13,'Beer Sheva rail station',0),(3,'South',14,'Dimona Atomic reactor',0);
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
  `machine_id` int DEFAULT NULL,
  `total_sum` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `buytime` varchar(45) DEFAULT NULL,
  `products_amount` varchar(45) DEFAULT NULL,
  `products_description` varchar(45) DEFAULT NULL,
  `is_delivery` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (0,1,112,3,'22/12/2022 14:43','3',NULL,0),(1,1,112,3,'22/12/2022 14:43','3',NULL,0),(2,1,142,3,'22/12/2022 14:43','3',NULL,0),(3,2,155,3,'22/12/2022 14:43','13',NULL,0),(4,2,41,3,'22/12/2022 14:43','3',NULL,0),(5,3,77,3,'22/12/2022 14:43','3',NULL,1),(6,3,17,3,'22/12/2022 14:43','3',NULL,1),(7,1,12,3,'22/12/2022 14:43','3',NULL,0),(8,2,115,3,'22/12/2022 14:43','3',NULL,0),(9,1,11,3,'22/12/2022 14:43','3',NULL,0),(10,3,110,3,'22/12/2022 14:43','3',NULL,0),(11,2,142,3,'22/12/2022 14:43','3',NULL,0),(12,6,155,3,'22/12/2022 14:43','13',NULL,0),(13,2,41,3,'22/12/2022 14:43','3',NULL,0),(14,7,77,3,'22/12/2022 14:43','3',NULL,1),(15,3,17,3,'22/12/2022 14:43','3',NULL,1),(16,6,12,3,'22/12/2022 14:43','3',NULL,0),(17,4,115,3,'22/12/2022 14:43','3',NULL,0),(18,5,11,3,'22/12/2022 14:43','3',NULL,0),(19,6,110,3,'22/12/2022 14:43','3',NULL,0),(20,4,112,3,'11/12/2022 14:43','3',NULL,0),(21,2,142,3,'20/12/2022 14:43','3',NULL,0),(22,26,155,3,'26/12/2022 14:43','13',NULL,0),(23,2,41,3,'13/12/2022 14:43','3',NULL,0),(24,2,77,3,'17/12/2022 14:43','3',NULL,1),(25,24,17,3,'16/12/2022 14:43','3',NULL,1),(26,6,12,3,'05/12/2022 14:43','3',NULL,0),(27,21,115,3,'11/12/2022 14:43','3',NULL,0),(28,23,11,3,'12/12/2022 14:43','3',NULL,0),(29,6,110,3,'25/12/2022 14:43','3',NULL,0),(30,11,37,1,'22/12/2022 14:43','1',NULL,0);
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
-- Table structure for table `sales_type`
--

DROP TABLE IF EXISTS `sales_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_type` (
  `type_id` int NOT NULL,
  `type_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_type`
--

LOCK TABLES `sales_type` WRITE;
/*!40000 ALTER TABLE `sales_type` DISABLE KEYS */;
INSERT INTO `sales_type` VALUES (1,'\'1+1\''),(2,'\'10%\''),(3,'\'20%\''),(4,'\'30%\'');
/*!40000 ALTER TABLE `sales_type` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'regionm','123456','david','asulin','dudyas6@gmmgm.com','500535030',NULL,'North','regionManager',0,0),(2,NULL,'ceom','123456','ceo','ceo','ceo@ceo.ceo','12319024',NULL,'','CEO',0,0),(3,NULL,'customer','123456','customer','customer','customer@customer','123123',NULL,'','registered',0,0),(4,'205905050','customer1','123456','customer1','customer1','customer1@customer1','123123123','205905050','','registered',0,0),(5,'205905050','customer2','123456','customer1','customer1','customer1@customer1','123123123','205905050','','registered',0,1),(6,'205905050','customer3','123456','customer1','customer1','customer1@customer1','123123123','205905050','','registered',0,0),(7,'205905050','customer4','123456','customer1','customer1','customer1@customer1','123123123','205905050','','registered',0,0),(8,'205905050','marketingm','123456','yossi','levi','a@a.com','123123123',NULL,NULL,'marketingManager',0,0),(31,NULL,'regionm2','123456','david','asulin','dudyas6@gmmgm.com','500535030',NULL,'Center','regionManager',0,0),(32,'123456789','supplyw1','123456','neta','neta','neta6@gmmgm.com','123123123',NULL,NULL,'supplyWorker',0,0),(33,'123456789','supplyw2','123456','vital','vital','vital6@gmmgm.com','123123123',NULL,NULL,'supplyWorker',0,0),(34,'123456789','supplyw3','123456','lidor','lidor','lidor6@gmmgm.com','123123123',NULL,NULL,'supplyWorker',0,0),(35,'123456789','supplyw4','123456','dima','dima','dima6@gmmgm.com','123123123',NULL,NULL,'supplyWorker',0,0);
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

-- Dump completed on 2022-12-28 22:01:40
