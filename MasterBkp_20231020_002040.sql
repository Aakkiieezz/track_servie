-- MariaDB dump 10.19-11.1.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: track_servie_stg
-- ------------------------------------------------------
-- Server version	11.1.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `catalog`
--

DROP TABLE IF EXISTS `catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk3mprwb52pe5lfv3l2xpmwj8s` (`user_id`),
  CONSTRAINT `FKk3mprwb52pe5lfv3l2xpmwj8s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalog`
--

LOCK TABLES `catalog` WRITE;
/*!40000 ALTER TABLE `catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalog_servies`
--

DROP TABLE IF EXISTS `catalog_servies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalog_servies` (
  `catalog_id` int(11) NOT NULL,
  `servies_childtype` varchar(255) NOT NULL,
  `servies_tmdb_id` int(11) NOT NULL,
  PRIMARY KEY (`catalog_id`,`servies_childtype`,`servies_tmdb_id`),
  KEY `FK4ru4rmejqastn3vix5ynolpl` (`servies_childtype`,`servies_tmdb_id`),
  CONSTRAINT `FK4ru4rmejqastn3vix5ynolpl` FOREIGN KEY (`servies_childtype`, `servies_tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`),
  CONSTRAINT `FKmus4n9b47wd3c9ep8mfdbsrx9` FOREIGN KEY (`catalog_id`) REFERENCES `catalog` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalog_servies`
--

LOCK TABLES `catalog_servies` WRITE;
/*!40000 ALTER TABLE `catalog_servies` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalog_servies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `created_by`
--

DROP TABLE IF EXISTS `created_by`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `created_by` (
  `credit_id` varchar(255) NOT NULL,
  `gender` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `created_by`
--

LOCK TABLES `created_by` WRITE;
/*!40000 ALTER TABLE `created_by` DISABLE KEYS */;
/*!40000 ALTER TABLE `created_by` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ecast`
--

DROP TABLE IF EXISTS `ecast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecast` (
  `credit_id` varchar(255) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `media_character` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `known_for_department` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ecast`
--

LOCK TABLES `ecast` WRITE;
/*!40000 ALTER TABLE `ecast` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ecrew`
--

DROP TABLE IF EXISTS `ecrew`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ecrew` (
  `credit_id` varchar(255) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `known_for_department` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ecrew`
--

LOCK TABLES `ecrew` WRITE;
/*!40000 ALTER TABLE `ecrew` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecrew` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `episode`
--

DROP TABLE IF EXISTS `episode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `episode` (
  `id` varchar(255) NOT NULL,
  `air_date` date DEFAULT NULL,
  `episode_no` int(11) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `overview` varchar(10000) DEFAULT NULL,
  `production_code` varchar(255) DEFAULT NULL,
  `runtime` int(11) DEFAULT NULL,
  `season_no` int(11) DEFAULT NULL,
  `still_path` varchar(255) DEFAULT NULL,
  `tmdb_id` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `vote_average` double DEFAULT NULL,
  `vote_count` int(11) DEFAULT NULL,
  `season_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr5ifuxa82mfaxrhgahps7iu2m` (`season_id`),
  CONSTRAINT `FKr5ifuxa82mfaxrhgahps7iu2m` FOREIGN KEY (`season_id`) REFERENCES `season` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `episode`
--

LOCK TABLES `episode` WRITE;
/*!40000 ALTER TABLE `episode` DISABLE KEYS */;
/*!40000 ALTER TABLE `episode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `episode_cast`
--

DROP TABLE IF EXISTS `episode_cast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `episode_cast` (
  `episode_id` varchar(255) NOT NULL,
  `credit_id` varchar(255) NOT NULL,
  PRIMARY KEY (`episode_id`,`credit_id`),
  KEY `FKper89v7t1x8bcig3m7mk45f5h` (`credit_id`),
  CONSTRAINT `FKfvi74v7xc49htdr1x2t0ud2h8` FOREIGN KEY (`episode_id`) REFERENCES `episode` (`id`),
  CONSTRAINT `FKper89v7t1x8bcig3m7mk45f5h` FOREIGN KEY (`credit_id`) REFERENCES `ecast` (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `episode_cast`
--

LOCK TABLES `episode_cast` WRITE;
/*!40000 ALTER TABLE `episode_cast` DISABLE KEYS */;
/*!40000 ALTER TABLE `episode_cast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `episode_crew`
--

DROP TABLE IF EXISTS `episode_crew`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `episode_crew` (
  `episode_id` varchar(255) NOT NULL,
  `credit_id` varchar(255) NOT NULL,
  PRIMARY KEY (`episode_id`,`credit_id`),
  KEY `FK6muv68f4kr5jnsgbpd1qdgmsw` (`credit_id`),
  CONSTRAINT `FK35km6f29tlj4it6lo3tv97e71` FOREIGN KEY (`episode_id`) REFERENCES `episode` (`id`),
  CONSTRAINT `FK6muv68f4kr5jnsgbpd1qdgmsw` FOREIGN KEY (`credit_id`) REFERENCES `ecrew` (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `episode_crew`
--

LOCK TABLES `episode_crew` WRITE;
/*!40000 ALTER TABLE `episode_crew` DISABLE KEYS */;
/*!40000 ALTER TABLE `episode_crew` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genre` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mcast`
--

DROP TABLE IF EXISTS `mcast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mcast` (
  `credit_id` varchar(255) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `cast_id` int(11) DEFAULT NULL,
  `media_character` varchar(500) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `known_for_department` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mcast`
--

LOCK TABLES `mcast` WRITE;
/*!40000 ALTER TABLE `mcast` DISABLE KEYS */;
/*!40000 ALTER TABLE `mcast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mcrew`
--

DROP TABLE IF EXISTS `mcrew`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mcrew` (
  `credit_id` varchar(255) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `known_for_department` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mcrew`
--

LOCK TABLES `mcrew` WRITE;
/*!40000 ALTER TABLE `mcrew` DISABLE KEYS */;
/*!40000 ALTER TABLE `mcrew` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie`
--

DROP TABLE IF EXISTS `movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie` (
  `collection_backdrop_path` varchar(255) DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `collection_name` varchar(255) DEFAULT NULL,
  `collection_poster_path` varchar(255) DEFAULT NULL,
  `budget` int(11) DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  `revenue` bigint(20) DEFAULT NULL,
  `runtime` int(11) DEFAULT NULL,
  `video` bit(1) DEFAULT NULL,
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `collection_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`),
  KEY `FKp4intneimdm41sl8562qooyci` (`collection_id`),
  CONSTRAINT `FKndiarbgh4agisf9lxqpafhm76` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`),
  CONSTRAINT `FKp4intneimdm41sl8562qooyci` FOREIGN KEY (`collection_id`) REFERENCES `movie_collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie`
--

LOCK TABLES `movie` WRITE;
/*!40000 ALTER TABLE `movie` DISABLE KEYS */;
/*!40000 ALTER TABLE `movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_cast`
--

DROP TABLE IF EXISTS `movie_cast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie_cast` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `credit_id` varchar(255) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`credit_id`),
  KEY `FKsdg6m1o6l520w05g7r3niws63` (`credit_id`),
  CONSTRAINT `FKsdg6m1o6l520w05g7r3niws63` FOREIGN KEY (`credit_id`) REFERENCES `mcast` (`credit_id`),
  CONSTRAINT `FKte3ouc1ti74179nurc24mlb35` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `movie` (`childtype`, `tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_cast`
--

LOCK TABLES `movie_cast` WRITE;
/*!40000 ALTER TABLE `movie_cast` DISABLE KEYS */;
/*!40000 ALTER TABLE `movie_cast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_collection`
--

DROP TABLE IF EXISTS `movie_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie_collection` (
  `id` int(11) NOT NULL,
  `backdrop_path` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `overview` varchar(10000) DEFAULT NULL,
  `poster_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_collection`
--

LOCK TABLES `movie_collection` WRITE;
/*!40000 ALTER TABLE `movie_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `movie_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_crew`
--

DROP TABLE IF EXISTS `movie_crew`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie_crew` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `credit_id` varchar(255) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`credit_id`),
  KEY `FKkp8wr4wjpf59mhd2xdkshblb4` (`credit_id`),
  CONSTRAINT `FKj2rd70xf77u4a82jo0ebu743` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `movie` (`childtype`, `tmdb_id`),
  CONSTRAINT `FKkp8wr4wjpf59mhd2xdkshblb4` FOREIGN KEY (`credit_id`) REFERENCES `mcrew` (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_crew`
--

LOCK TABLES `movie_crew` WRITE;
/*!40000 ALTER TABLE `movie_crew` DISABLE KEYS */;
/*!40000 ALTER TABLE `movie_crew` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies_bkp`
--

DROP TABLE IF EXISTS `movies_bkp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies_bkp` (
  `tmdb_id` int(11) NOT NULL,
  PRIMARY KEY (`tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies_bkp`
--

LOCK TABLES `movies_bkp` WRITE;
/*!40000 ALTER TABLE `movies_bkp` DISABLE KEYS */;
/*!40000 ALTER TABLE `movies_bkp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `network`
--

DROP TABLE IF EXISTS `network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `network` (
  `id` int(11) NOT NULL,
  `logo_path` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `origin_country` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `network`
--

LOCK TABLES `network` WRITE;
/*!40000 ALTER TABLE `network` DISABLE KEYS */;
/*!40000 ALTER TABLE `network` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `origin_country`
--

DROP TABLE IF EXISTS `origin_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `origin_country` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `origin_country` varchar(255) DEFAULT NULL,
  KEY `FKngpabhyetrpoclm1ad3hx0fmm` (`childtype`,`tmdb_id`),
  CONSTRAINT `FKngpabhyetrpoclm1ad3hx0fmm` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `series` (`childtype`, `tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `origin_country`
--

LOCK TABLES `origin_country` WRITE;
/*!40000 ALTER TABLE `origin_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `origin_country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `id` int(11) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `biography` varchar(255) DEFAULT NULL,
  `birth_place` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `deathday` date DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `homepage` varchar(255) DEFAULT NULL,
  `imdb_id` int(11) DEFAULT NULL,
  `known_for_department` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_company`
--

DROP TABLE IF EXISTS `production_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `production_company` (
  `id` int(11) NOT NULL,
  `logo_path` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `origin_country` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_company`
--

LOCK TABLES `production_company` WRITE;
/*!40000 ALTER TABLE `production_company` DISABLE KEYS */;
/*!40000 ALTER TABLE `production_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_country`
--

DROP TABLE IF EXISTS `production_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `production_country` (
  `iso_3166_1` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`iso_3166_1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_country`
--

LOCK TABLES `production_country` WRITE;
/*!40000 ALTER TABLE `production_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `production_country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permissions` (
  `role_id` int(11) NOT NULL,
  `permissions_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`permissions_id`),
  KEY `FKclluu29apreb6osx6ogt4qe16` (`permissions_id`),
  CONSTRAINT `FKclluu29apreb6osx6ogt4qe16` FOREIGN KEY (`permissions_id`) REFERENCES `permission` (`id`),
  CONSTRAINT `FKlodb7xh4a2xjv39gc3lsop95n` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scast`
--

DROP TABLE IF EXISTS `scast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scast` (
  `credit_id` varchar(255) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `media_character` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `known_for_department` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `profile_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scast`
--

LOCK TABLES `scast` WRITE;
/*!40000 ALTER TABLE `scast` DISABLE KEYS */;
/*!40000 ALTER TABLE `scast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season`
--

DROP TABLE IF EXISTS `season`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `season` (
  `id` varchar(255) NOT NULL,
  `air_date` date DEFAULT NULL,
  `episode_count` int(11) DEFAULT NULL,
  `_id` varchar(255) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `overview` varchar(10000) DEFAULT NULL,
  `poster_path` varchar(255) DEFAULT NULL,
  `season_no` int(11) DEFAULT NULL,
  `vote_average` double DEFAULT NULL,
  `childtype` varchar(255) DEFAULT NULL,
  `tmdb_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkvk22rb5vh4ul5t1q8laprk9f` (`childtype`,`tmdb_id`),
  CONSTRAINT `FKkvk22rb5vh4ul5t1q8laprk9f` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `series` (`childtype`, `tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `season`
--

LOCK TABLES `season` WRITE;
/*!40000 ALTER TABLE `season` DISABLE KEYS */;
/*!40000 ALTER TABLE `season` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `season_cast`
--

DROP TABLE IF EXISTS `season_cast`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `season_cast` (
  `season_id` varchar(255) NOT NULL,
  `credit_id` varchar(255) NOT NULL,
  PRIMARY KEY (`season_id`,`credit_id`),
  KEY `FKiob37suwtniy0951qfbaa3has` (`credit_id`),
  CONSTRAINT `FKiob37suwtniy0951qfbaa3has` FOREIGN KEY (`credit_id`) REFERENCES `scast` (`credit_id`),
  CONSTRAINT `FKntlw8k8f0sgvdlrryi76p1s77` FOREIGN KEY (`season_id`) REFERENCES `season` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `season_cast`
--

LOCK TABLES `season_cast` WRITE;
/*!40000 ALTER TABLE `season_cast` DISABLE KEYS */;
/*!40000 ALTER TABLE `season_cast` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series`
--

DROP TABLE IF EXISTS `series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `series` (
  `first_air_date` date DEFAULT NULL,
  `in_production` bit(1) DEFAULT NULL,
  `last_air_date` date DEFAULT NULL,
  `total_episodes` int(11) DEFAULT NULL,
  `total_seasons` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`),
  CONSTRAINT `FKhtxyfeeru5flm9wwqmxiid8lm` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series`
--

LOCK TABLES `series` WRITE;
/*!40000 ALTER TABLE `series` DISABLE KEYS */;
/*!40000 ALTER TABLE `series` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series_createdby`
--

DROP TABLE IF EXISTS `series_createdby`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `series_createdby` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `credit_id` varchar(255) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`credit_id`),
  KEY `FKkfqeqs2dmwd5uj30hsbo044ld` (`credit_id`),
  CONSTRAINT `FK1sp47koc7o17m0nvf6qf5evae` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `series` (`childtype`, `tmdb_id`),
  CONSTRAINT `FKkfqeqs2dmwd5uj30hsbo044ld` FOREIGN KEY (`credit_id`) REFERENCES `created_by` (`credit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series_createdby`
--

LOCK TABLES `series_createdby` WRITE;
/*!40000 ALTER TABLE `series_createdby` DISABLE KEYS */;
/*!40000 ALTER TABLE `series_createdby` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `series_networks`
--

DROP TABLE IF EXISTS `series_networks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `series_networks` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`id`),
  KEY `FK4ycbgyo2kjb1xp5jguwn9blx6` (`id`),
  CONSTRAINT `FK4ycbgyo2kjb1xp5jguwn9blx6` FOREIGN KEY (`id`) REFERENCES `network` (`id`),
  CONSTRAINT `FKlntnib8nvov0sfctdtellp2vb` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `series` (`childtype`, `tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `series_networks`
--

LOCK TABLES `series_networks` WRITE;
/*!40000 ALTER TABLE `series_networks` DISABLE KEYS */;
/*!40000 ALTER TABLE `series_networks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servie`
--

DROP TABLE IF EXISTS `servie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servie` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `backdrop_path` varchar(255) DEFAULT NULL,
  `homepage` varchar(255) DEFAULT NULL,
  `imdb_id` varchar(255) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `original_language` varchar(255) DEFAULT NULL,
  `original_title` varchar(255) DEFAULT NULL,
  `overview` varchar(10000) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `poster_path` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `tagline` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `vote_average` double DEFAULT NULL,
  `vote_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servie`
--

LOCK TABLES `servie` WRITE;
/*!40000 ALTER TABLE `servie` DISABLE KEYS */;
/*!40000 ALTER TABLE `servie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servie_genres`
--

DROP TABLE IF EXISTS `servie_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servie_genres` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `genre_id` int(11) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`genre_id`),
  KEY `FKfla7ofi80v1xcxrn4cqkf1dwr` (`genre_id`),
  CONSTRAINT `FKfla7ofi80v1xcxrn4cqkf1dwr` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`),
  CONSTRAINT `FKte593od6welkpiorb78rvhjjj` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servie_genres`
--

LOCK TABLES `servie_genres` WRITE;
/*!40000 ALTER TABLE `servie_genres` DISABLE KEYS */;
/*!40000 ALTER TABLE `servie_genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servie_production_companies`
--

DROP TABLE IF EXISTS `servie_production_companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servie_production_companies` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `production_company_id` int(11) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`production_company_id`),
  KEY `FKpb022lmlc5x3jns7faj5uw2m4` (`production_company_id`),
  CONSTRAINT `FKagj8hqw0ehphfh71btd4xeggs` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`),
  CONSTRAINT `FKpb022lmlc5x3jns7faj5uw2m4` FOREIGN KEY (`production_company_id`) REFERENCES `production_company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servie_production_companies`
--

LOCK TABLES `servie_production_companies` WRITE;
/*!40000 ALTER TABLE `servie_production_companies` DISABLE KEYS */;
/*!40000 ALTER TABLE `servie_production_companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servie_production_countries`
--

DROP TABLE IF EXISTS `servie_production_countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servie_production_countries` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `iso` varchar(255) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`iso`),
  KEY `FKg7nrxbh8330ww6ff5b4sln6pk` (`iso`),
  CONSTRAINT `FKg7nrxbh8330ww6ff5b4sln6pk` FOREIGN KEY (`iso`) REFERENCES `production_country` (`iso_3166_1`),
  CONSTRAINT `FKi8pshagsmcfb3fqhklmratdmo` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servie_production_countries`
--

LOCK TABLES `servie_production_countries` WRITE;
/*!40000 ALTER TABLE `servie_production_countries` DISABLE KEYS */;
/*!40000 ALTER TABLE `servie_production_countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servie_spoken_languages`
--

DROP TABLE IF EXISTS `servie_spoken_languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servie_spoken_languages` (
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `iso` varchar(255) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`iso`),
  KEY `FKcv9ig03t8m29h39g4ufw6arc3` (`iso`),
  CONSTRAINT `FK8nyabcwggh56gjm0418q4wsje` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`),
  CONSTRAINT `FKcv9ig03t8m29h39g4ufw6arc3` FOREIGN KEY (`iso`) REFERENCES `spoken_language` (`iso_639_1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servie_spoken_languages`
--

LOCK TABLES `servie_spoken_languages` WRITE;
/*!40000 ALTER TABLE `servie_spoken_languages` DISABLE KEYS */;
/*!40000 ALTER TABLE `servie_spoken_languages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spoken_language`
--

DROP TABLE IF EXISTS `spoken_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spoken_language` (
  `iso_639_1` varchar(255) NOT NULL,
  `eng_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`iso_639_1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spoken_language`
--

LOCK TABLES `spoken_language` WRITE;
/*!40000 ALTER TABLE `spoken_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `spoken_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tmdb_movie_exports`
--

DROP TABLE IF EXISTS `tmdb_movie_exports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tmdb_movie_exports` (
  `tmdb_id` int(11) NOT NULL,
  `adult` bit(1) DEFAULT NULL,
  `original_title` varchar(255) DEFAULT NULL,
  `popularity` double DEFAULT NULL,
  `video` bit(1) DEFAULT NULL,
  PRIMARY KEY (`tmdb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tmdb_movie_exports`
--

LOCK TABLES `tmdb_movie_exports` WRITE;
/*!40000 ALTER TABLE `tmdb_movie_exports` DISABLE KEYS */;
/*!40000 ALTER TABLE `tmdb_movie_exports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_episode_data`
--

DROP TABLE IF EXISTS `user_episode_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_episode_data` (
  `episode_no` int(11) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `watched` bit(1) NOT NULL,
  `season_no` int(11) NOT NULL,
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`episode_no`,`season_no`,`childtype`,`tmdb_id`,`user_id`),
  KEY `FKi6nbcv6v608aa0sp67yns5ya9` (`season_no`,`childtype`,`tmdb_id`,`user_id`),
  CONSTRAINT `FKi6nbcv6v608aa0sp67yns5ya9` FOREIGN KEY (`season_no`, `childtype`, `tmdb_id`, `user_id`) REFERENCES `user_season_data` (`season_no`, `childtype`, `tmdb_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_episode_data`
--

LOCK TABLES `user_episode_data` WRITE;
/*!40000 ALTER TABLE `user_episode_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_episode_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `user_id` int(11) NOT NULL,
  `roles_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`roles_id`),
  KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_season_data`
--

DROP TABLE IF EXISTS `user_season_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_season_data` (
  `season_no` int(11) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `poster_path` varchar(255) DEFAULT NULL,
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`season_no`,`childtype`,`tmdb_id`,`user_id`),
  KEY `FK3ieldhlqt7jn757cbigtr2ojj` (`childtype`,`tmdb_id`,`user_id`),
  CONSTRAINT `FK3ieldhlqt7jn757cbigtr2ojj` FOREIGN KEY (`childtype`, `tmdb_id`, `user_id`) REFERENCES `user_servie_data` (`childtype`, `tmdb_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_season_data`
--

LOCK TABLES `user_season_data` WRITE;
/*!40000 ALTER TABLE `user_season_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_season_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_servie_data`
--

DROP TABLE IF EXISTS `user_servie_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_servie_data` (
  `backdrop_path` varchar(255) DEFAULT NULL,
  `movie_watched` bit(1) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `poster_path` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `childtype` varchar(255) NOT NULL,
  `tmdb_id` int(11) NOT NULL,
  PRIMARY KEY (`childtype`,`tmdb_id`,`user_id`),
  KEY `FKmkestubsojqkki07hy91v0alp` (`user_id`),
  CONSTRAINT `FK8kc3uvydpth7wbdrfxt4177w3` FOREIGN KEY (`childtype`, `tmdb_id`) REFERENCES `servie` (`childtype`, `tmdb_id`),
  CONSTRAINT `FKmkestubsojqkki07hy91v0alp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_servie_data`
--

LOCK TABLES `user_servie_data` WRITE;
/*!40000 ALTER TABLE `user_servie_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_servie_data` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-20  0:20:40
