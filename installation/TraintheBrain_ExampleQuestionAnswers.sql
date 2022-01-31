# ************************************************************
# Sequel Ace SQL dump
# Version 20019
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# Host: localhost (MySQL 5.5.5-10.6.4-MariaDB)
# Datenbank: trainthebrain
# Verarbeitungszeit: 2022-01-20 14:22:58 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Tabellen-Dump tbl_answers
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tbl_answers`;

CREATE TABLE `tbl_answers` (
  `answer_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `question_id` int(10) unsigned DEFAULT NULL,
  `answer_correct` tinyint(1) DEFAULT 0,
  `answer_text` text DEFAULT NULL,
  PRIMARY KEY (`answer_id`),
  KEY `FK_QUESTIONID` (`question_id`),
  CONSTRAINT `FK_QUESTIONID` FOREIGN KEY (`question_id`) REFERENCES `tbl_questions` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `tbl_answers` WRITE;
/*!40000 ALTER TABLE `tbl_answers` DISABLE KEYS */;

INSERT INTO `tbl_answers` (`answer_id`, `question_id`, `answer_correct`, `answer_text`)
VALUES
	(72,30,0,'Calvin Cordozar Broadus Jr.'),
	(73,30,1,'Marshall Bruce Mathers III'),
	(74,30,0,'André Tanneberger'),
	(75,30,0,'Taylor Mannings Sr.'),
	(76,31,1,'„Ich glaub, ich bin in der falschen Zeit.“'),
	(77,31,0,'„Ich liebe Jeff Goldblum.“'),
	(78,31,0,'„Ich fühle mich irgendwie zu groß.“'),
	(79,31,0,'„Ich mag doch gar kein Fleisch.“'),
	(80,32,0,'4'),
	(81,32,1,'3'),
	(82,32,0,'9'),
	(83,32,0,'12'),
	(84,33,0,'Frankreich'),
	(85,33,1,'Italien'),
	(86,33,0,'Rumänien'),
	(87,33,0,'Mexiko'),
	(88,34,0,'Reifen'),
	(89,34,1,'Helm'),
	(90,34,0,'Pilot'),
	(91,34,0,'Box'),
	(92,35,0,'Den Millenium Falcon'),
	(93,35,1,'Den DeLorean'),
	(94,35,0,'Die Titanic'),
	(95,35,0,'Die Discovery'),
	(96,36,1,'Enterprise Klasse'),
	(97,36,0,'Defiant Klasse'),
	(98,36,0,'Intrepid Klasse'),
	(99,36,1,'Voyager Klasse'),
	(100,37,0,'...Butter zu geschnitten Brot'),
	(101,37,1,'...Phoebe zu Ursula'),
	(102,37,0,'...Ilsa Lund zu Rick Blaine'),
	(103,37,1,'...Mary-Kate zu Ashley Olsen');

/*!40000 ALTER TABLE `tbl_answers` ENABLE KEYS */;
UNLOCK TABLES;


# Tabellen-Dump tbl_questions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tbl_questions`;

CREATE TABLE `tbl_questions` (
  `question_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `question_text` text DEFAULT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `tbl_questions` WRITE;
/*!40000 ALTER TABLE `tbl_questions` DISABLE KEYS */;

INSERT INTO `tbl_questions` (`question_id`, `question_text`)
VALUES
	(30,'Batman verhält sich zu Bruce Wayne wie Eminem zu..'),
	(31,'Hätte man die tierischen Laute des Tyrannosaurus in Jurassic Park in unsere Sprache übersetzt, welchen Satz hätte er wahrheitsgemäß sprechen können?'),
	(32,'Ich denke mir eine Zahl zwischen 1 und 10, welche ist es?'),
	(33,'Welches Land würde man wohl \"förmlich\" als Geburtsstätte für Fuss-Fetischisten bezeichnen?'),
	(34,'Harte Schutzhaube & In Spaceballs: Lord ____chen!'),
	(35,'Wenn wir uns \"Zurück in die Zukunft\" bewegen nutzen wir dafür:'),
	(36,'Welche Schiffsklasse existiert in Star Trek nicht?'),
	(37,'Lea Organa verhält sich zu Luke Skywalker wie...');

/*!40000 ALTER TABLE `tbl_questions` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
