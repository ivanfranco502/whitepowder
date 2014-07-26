CREATE DATABASE  IF NOT EXISTS `whitepowder` /*!40100 DEFAULT CHARACTER SET latin1 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `general_information`
--

LOCK TABLES `general_information` WRITE;
/*!40000 ALTER TABLE `general_information` DISABLE KEYS */;
INSERT INTO `general_information` VALUES (1,'Bar, Pileta Cerrada',4500,2000,'Junio','Agosto');
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
  `mess_user_id` int(11) NOT NULL,
  PRIMARY KEY (`mess_id`),
  KEY `FK_message_users_idx` (`mess_user_id`),
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
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `noti_id` int(11) NOT NULL,
  `noti_message_id` int(11) NOT NULL,
  `noti_description` varchar(100) NOT NULL,
  `noti_notification_type_id` int(11) NOT NULL,
  PRIMARY KEY (`noti_id`),
  KEY `FK_notification_message_idx` (`noti_message_id`),
  KEY `FK_notification_notification_type_idx` (`noti_notification_type_id`),
  CONSTRAINT `FK_notification_notification_type` FOREIGN KEY (`noti_notification_type_id`) REFERENCES `notification_type` (`noty_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
-- Table structure for table `notification_type`
--

DROP TABLE IF EXISTS `notification_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_type` (
  `noty_id` int(11) NOT NULL,
  `noty_description` varchar(45) NOT NULL,
  PRIMARY KEY (`noty_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_type`
--

LOCK TABLES `notification_type` WRITE;
/*!40000 ALTER TABLE `notification_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_type` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slope`
--

LOCK TABLES `slope` WRITE;
/*!40000 ALTER TABLE `slope` DISABLE KEYS */;
INSERT INTO `slope` VALUES (1,'Mondragón',450,3,1),(2,'Mecano',550,1,1),(3,'Tamarindo',120,2,1),(5,'Tamarindo2',240,2,1);
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
  `sldi_id` int(11) NOT NULL AUTO_INCREMENT,
  `sldi_description` varchar(50) NOT NULL,
  `sldi_color` varchar(45) NOT NULL,
  PRIMARY KEY (`sldi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slope_dificulty`
--

LOCK TABLES `slope_dificulty` WRITE;
/*!40000 ALTER TABLE `slope_dificulty` DISABLE KEYS */;
INSERT INTO `slope_dificulty` VALUES (1,'Muy fácil, pendiente 10-15%','Verde'),(2,'Fáciles e Intermedias, pendiente 15-25%','Azul'),(3,'Difíciles, pendientes de 25-40%','Rojo'),(4,'Muy difíciles, pendientes 40-50%','Negro');
/*!40000 ALTER TABLE `slope_dificulty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `token_id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(45) DEFAULT NULL,
  `token_user_id` int(11) NOT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `token_UNIQUE` (`token`),
  KEY `fk_user_id_idx` (`token_user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`token_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,'e7a60fb332517a9538874e03cf0c208e75b18c35',14),(3,'390198e4cc565f9987b45ab750cb1c8b770e7bf3',29),(4,'05f89ca00ad9b66dbf006c01a3e65bd11904014c',33),(5,'8f121df37e581a6c6b4d887dee79076538691e55',31),(6,'10931d0c01c2182e455a0b2b7ddc7ffbca971b64',35),(7,'ae5b3cd947588447b76e7913d92632957ef00d56',36),(8,'238c1b43dff9a6bd0ece4782ac40f3a9ea95e936',37),(9,'4ebcb8aef7dc69b4610d23fb644a4ce0f55e145f',39);
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (14,'joaquin','joaquin','ibar.joaquin@gmail.com','ibar.joaquin@gmail.com',1,'bpedzrxdwmgookwggs0kkw0wsowog8k','gFWteIaOfxcWXo2lQd2nz1JLaKRWCDK/XpEakBMCPRzDbtLnfXZQXzOgmyqfcbeiZuzKfG7hbCdXLMJ1h/FNMw==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(29,'lucas','lucas','lucas.sagaria@gmail.com','lucas.sagaria@gmail.com',1,'jivourhmi3sowss0o8ksgwwk04c8oco','fyQsPJD79LifX8PIpZdpOPw5TXnZi2mxVf8EjauRElzpZ9lvxAJJAK5N+YHVhBpepyuGmb8AVqJy1TYwEB6YmQ==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(31,'ignaciojabad','ignaciojabad','ignaciojabad@gmail.com','ignaciojabad@gmail.com',1,'nbpwk319bio80oocwwwscssc8w48w0g','y0hB/SsyFzTILqdnfJ3CkIBtqWP11FIRVYE66ZOH6bpxs6nIBJteB9oZJ1CHDtx19BHPc9xka8K6FJFCm55f/Q==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(32,'admin','admin','ibar.joaquin+tavros@gmail.com','ibar.joaquin+tavros@gmail.com',1,'7zrqw95gddkw4oskws48s44o0ck8ko8','oLr7gbBIp44/SGETxiWIlvgsU14dHcZA9z27Zd6vYvAn6igQ/1/1J2MyiSoxylU80hsC4mE8QQvLTWv7eQvEGQ==','2014-07-26 20:04:29',0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_ADMIN\";}',0,NULL),(33,'gonzalo','gonzalo','ggarciacordoba@gmail.com','ggarciacordoba@gmail.com',1,'f9nw5eiyfdskwg8wosg8gs0co044scc','jv59xB/oYhLp4KVuesKnWytgmeX1QSs7uJpT0W3Vvyq+wdSfbZEB0hMej4GF61PETLi6sCel8iJyzC6YTRnvBw==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(34,'newUser','newuser','mail@mail.com','mail@mail.com',1,'771l35zssxwkos8o8cc8go8ocg04cw0','5HjYvylcke3KPpfSX+E+WmhOHijBK4HDC3U7Jo/+dCiOtsJXc/XkoCwdQlfX0S5Win2CjIBaGyS3nce1YtYPRQ==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(35,'lsagaria','lsagaria','lucassagaria@hotmail.com','lucassagaria@hotmail.com',1,'lna75sqwug0g8koc04o0wc040o40g88','NwkFPJ4dpD/zZIGWjbzJ/249iolegb/46kq+EaX96YzCSYjXr8hjQodsBu605E0tMby98y3+z8yctspbxc5BCg==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(36,'nachito','nachito','nachito_xeneize@hotmail.com','nachito_xeneize@hotmail.com',1,'1g6qtqze736s44wo000ocww4sw0k8gw','1oMIN+zF/+Er9msswN9VqfUA3uLodLzy1yrVhqltqDZG7jZ76ltDkfl9jIouS0KTysYmxq/dzTExW18qi6GFnA==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(37,'recon','recon','lucas.sagaria+recon@gmail.com','lucas.sagaria+recon@gmail.com',1,'kxmi6tamryo8c4gwco4kcw884ck4ssc','Pw+Io4NwPC4ZkrcJsJHqNy2SWTbazkj6Itnk+alTjn0suGSsuYzppLaSTNV817LQl2jzHyptf9IcA3dfzJ0VxQ==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_RECON\";}',0,NULL),(38,'prueba','prueba','prueba@prueba.prueba','prueba@prueba.prueba',1,'3w8tumg14zacskg400cs40g8w8s8k04','HcrQXhDcGlux7ClLy3ldQ/X/N++L94k3UFI9iQGRHGTFwM5Z5ekb/bihLX5gsBXaUvO/8VNtWb0vUcAsQNdeHQ==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL),(39,'ivan.franco','ivan.franco','ivanfranco502@gmail.com','ivanfranco502@gmail.com',1,'cismq1ckz14c8444kwogsko8gk80008','Kbaai08VvdkPO21PrLAiwdGrVT0Lj+tU1RsgPwDX21eFVIDPfRPa94iJsC7PnP739nlrztLRnzw77woBM7nM5g==',NULL,0,0,NULL,NULL,NULL,'a:1:{i:0;s:10:\"ROLE_SKIER\";}',0,NULL);
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

-- Dump completed on 2014-07-26 19:26:31
