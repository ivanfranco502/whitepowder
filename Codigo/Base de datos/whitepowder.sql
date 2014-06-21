CREATE DATABASE  IF NOT EXISTS `whitepowder` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `whitepowder`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: whitepowder
-- ------------------------------------------------------
-- Server version	5.6.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alert` (
  `aler_id` int(11) NOT NULL,
  `aler_x_position` double NOT NULL,
  `aler_y_position` varchar(45) NOT NULL,
  `aler_message_id` int(11) NOT NULL,
  PRIMARY KEY (`aler_id`),
  KEY `FK_alert_message_idx` (`aler_message_id`),
  CONSTRAINT `FK_alert_message` FOREIGN KEY (`aler_message_id`) REFERENCES `message` (`mess_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert`
--

LOCK TABLES `alert` WRITE;
/*!40000 ALTER TABLE `alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coordinate`
--

DROP TABLE IF EXISTS `coordinate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coordinate` (
  `coor_id` int(11) NOT NULL,
  `coor_created_date` datetime NOT NULL,
  `coor_x` double NOT NULL,
  `coor_y` double NOT NULL,
  PRIMARY KEY (`coor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coordinate`
--

LOCK TABLES `coordinate` WRITE;
/*!40000 ALTER TABLE `coordinate` DISABLE KEYS */;
/*!40000 ALTER TABLE `coordinate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `general_information`
--

DROP TABLE IF EXISTS `general_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `general_information` (
  `gein_id` int(11) NOT NULL AUTO_INCREMENT,
  `gein_amenities` text,
  `gein_maximum_height` double DEFAULT NULL,
  `gein_minimum_height` double DEFAULT NULL,
  `gein_season_since` varchar(10) DEFAULT NULL,
  `gein_season_til` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`gein_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_information`
--

LOCK TABLES `general_information` WRITE;
/*!40000 ALTER TABLE `general_information` DISABLE KEYS */;
/*!40000 ALTER TABLE `general_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hour_day`
--

DROP TABLE IF EXISTS `hour_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hour_day` (
  `hoda_id` int(11) NOT NULL AUTO_INCREMENT,
  `hoda_day` varchar(9) NOT NULL,
  `hoda_start_hour` varchar(5) NOT NULL,
  `hoda_end_hour` varchar(5) NOT NULL,
  `hoda_close` tinyint(1) DEFAULT NULL,
  `hoda_general_information_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`hoda_id`),
  KEY `FK_hour_day_general_information_idx` (`hoda_general_information_id`),
  CONSTRAINT `FK_hour_day_general_information` FOREIGN KEY (`hoda_general_information_id`) REFERENCES `general_information` (`gein_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hour_day`
--

LOCK TABLES `hour_day` WRITE;
/*!40000 ALTER TABLE `hour_day` DISABLE KEYS */;
/*!40000 ALTER TABLE `hour_day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `mess_id` int(11) NOT NULL,
  `mess_createdDate` datetime NOT NULL,
  `mess_message_type_id` int(11) NOT NULL,
  `mess_user_id` int(11) NOT NULL,
  PRIMARY KEY (`mess_id`),
  KEY `FK_message_message_type_idx` (`mess_message_type_id`),
  KEY `FK_message_users_idx` (`mess_user_id`),
  CONSTRAINT `FK_message_message_type` FOREIGN KEY (`mess_message_type_id`) REFERENCES `message_type` (`mety_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_message_users` FOREIGN KEY (`mess_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_type`
--

DROP TABLE IF EXISTS `message_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_type` (
  `mety_id` int(11) NOT NULL,
  `mety_description` varchar(45) NOT NULL,
  PRIMARY KEY (`mety_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_type`
--

LOCK TABLES `message_type` WRITE;
/*!40000 ALTER TABLE `message_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `noti_id` int(11) NOT NULL,
  `noti_message_id` int(11) NOT NULL,
  `noti_description` varchar(100) NOT NULL,
  PRIMARY KEY (`noti_id`),
  KEY `FK_notification_message_idx` (`noti_message_id`),
  CONSTRAINT `FK_notification_message` FOREIGN KEY (`noti_message_id`) REFERENCES `message` (`mess_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_user`
--

DROP TABLE IF EXISTS `notification_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_user` (
  `nous_notification_id` int(11) NOT NULL,
  `nous_user_id` int(11) NOT NULL,
  PRIMARY KEY (`nous_notification_id`,`nous_user_id`),
  KEY `FK_notification_user_user_idx` (`nous_user_id`),
  CONSTRAINT `FK_notification_user_notifitation` FOREIGN KEY (`nous_notification_id`) REFERENCES `notification` (`noti_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_notification_user_users` FOREIGN KEY (`nous_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_user`
--

LOCK TABLES `notification_user` WRITE;
/*!40000 ALTER TABLE `notification_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile` (
  `prof_id` int(11) NOT NULL,
  `prof_maximum_speed` double DEFAULT NULL,
  `prof_average_speed` double DEFAULT NULL,
  `prof_speed_acumulator` double DEFAULT NULL,
  `prof_speed_times` int(11) DEFAULT NULL,
  `prof_maximum_height` double DEFAULT NULL,
  `prof_total_length` double DEFAULT NULL,
  `prof_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`prof_id`),
  KEY `FK_profile_users_idx` (`prof_user_id`),
  CONSTRAINT `FK_profile_users` FOREIGN KEY (`prof_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile`
--

LOCK TABLES `profile` WRITE;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slope`
--

DROP TABLE IF EXISTS `slope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slope` (
  `slop_id` int(11) NOT NULL AUTO_INCREMENT,
  `slop_description` varchar(50) NOT NULL,
  `slop_length` int(11) DEFAULT NULL,
  `slop_dificulty_id` int(11) NOT NULL,
  `slop_general_information_id` int(11) NOT NULL,
  PRIMARY KEY (`slop_id`),
  KEY `FK_slope_slope_dificulty_idx` (`slop_dificulty_id`),
  KEY `FK_slope_general_information_idx` (`slop_general_information_id`),
  CONSTRAINT `FK_slope_general_information` FOREIGN KEY (`slop_general_information_id`) REFERENCES `general_information` (`gein_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_slope_slope_dificulty` FOREIGN KEY (`slop_dificulty_id`) REFERENCES `slope_dificulty` (`sldi_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slope`
--

LOCK TABLES `slope` WRITE;
/*!40000 ALTER TABLE `slope` DISABLE KEYS */;
/*!40000 ALTER TABLE `slope` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slope_coordinate`
--

DROP TABLE IF EXISTS `slope_coordinate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slope_coordinate` (
  `slco_id` int(11) NOT NULL AUTO_INCREMENT,
  `slco_coordinate_id` int(11) NOT NULL,
  `slco_slope_id` int(11) NOT NULL,
  `slco_user_id` int(11) NOT NULL,
  `slco_update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`slco_id`),
  KEY `FK_slope_coordinate_slope_idx` (`slco_slope_id`),
  KEY `FK_slope_coordinate_users_idx` (`slco_user_id`),
  KEY `FK_slope_coordinate_coordinate_idx` (`slco_coordinate_id`),
  CONSTRAINT `FK_slope_coordinate_coordinate` FOREIGN KEY (`slco_coordinate_id`) REFERENCES `coordinate` (`coor_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_slope_coordinate_slope` FOREIGN KEY (`slco_slope_id`) REFERENCES `slope` (`slop_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_slope_coordinate_users` FOREIGN KEY (`slco_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slope_coordinate`
--

LOCK TABLES `slope_coordinate` WRITE;
/*!40000 ALTER TABLE `slope_coordinate` DISABLE KEYS */;
/*!40000 ALTER TABLE `slope_coordinate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slope_dificulty`
--

DROP TABLE IF EXISTS `slope_dificulty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slope_dificulty` (
  `sldi_id` int(11) NOT NULL,
  `sldi_description` varchar(50) NOT NULL,
  `sldi_color` varchar(45) NOT NULL,
  PRIMARY KEY (`sldi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slope_dificulty`
--

LOCK TABLES `slope_dificulty` WRITE;
/*!40000 ALTER TABLE `slope_dificulty` DISABLE KEYS */;
/*!40000 ALTER TABLE `slope_dificulty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_coordinate`
--

DROP TABLE IF EXISTS `user_coordinate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_coordinate` (
  `usco_id` int(11) NOT NULL AUTO_INCREMENT,
  `usco_coordinate_id` int(11) NOT NULL,
  `usco_user_id` int(11) NOT NULL,
  `usco_update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`usco_id`),
  KEY `FK_user_coordinate_user_idx` (`usco_user_id`),
  KEY `FK_user_coordinate_coordinate_idx` (`usco_coordinate_id`),
  CONSTRAINT `FK_user_coordinate_coordinate` FOREIGN KEY (`usco_coordinate_id`) REFERENCES `coordinate` (`coor_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_user_coordinate_user` FOREIGN KEY (`usco_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_coordinate`
--

LOCK TABLES `user_coordinate` WRITE;
/*!40000 ALTER TABLE `user_coordinate` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_coordinate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `username_canonical` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email_canonical` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `salt` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `locked` tinyint(1) NOT NULL,
  `expired` tinyint(1) NOT NULL,
  `expires_at` datetime DEFAULT NULL,
  `confirmation_token` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password_requested_at` datetime DEFAULT NULL,
  `roles` longtext COLLATE utf8_unicode_ci NOT NULL COMMENT '(DC2Type:array)',
  `credentials_expired` tinyint(1) NOT NULL,
  `credentials_expire_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_1483A5E992FC23A8` (`username_canonical`),
  UNIQUE KEY `UNIQ_1483A5E9A0D96FBF` (`email_canonical`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
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

-- Dump completed on 2014-06-20 21:23:40
