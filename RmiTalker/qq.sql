# Host: 127.0.0.1  (Version: 5.6.14)
# Date: 2014-12-13 11:45:25
# Generator: MySQL-Front 5.3  (Build 4.118)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "users"
#

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT 'qq number',
  `password` varchar(32) NOT NULL DEFAULT '',
  `name` char(15) NOT NULL DEFAULT '',
  `realname` varchar(32) DEFAULT NULL,
  `sex` tinyint(1) unsigned DEFAULT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `time` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#
# Data for table "users"
#

INSERT INTO `users` VALUES (1,10000,'123456','Sam','wangzhenyu',1,1,''),(2,10001,'123456','Sun','sunny',1,1,''),(3,10002,'123456','Shake','yangyan',1,1,''),(4,1,'123456','test1','test',0,0,''),(5,2,'123456','test2','test2',1,0,''),(6,3,'123456','test3','test3',0,0,'');

#
# Structure for table "relation"
#

DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `fid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `relation` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0-no 1-friend',
  PRIMARY KEY (`Id`),
  KEY `uid` (`uid`),
  KEY `fid` (`fid`),
  CONSTRAINT `relation_ibfk_1` FOREIGN KEY (`fid`) REFERENCES `users` (`uid`),
  CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

#
# Data for table "relation"
#

INSERT INTO `relation` VALUES (5,10002,10000,1),(6,10000,10002,1),(7,10000,10001,1),(8,10001,10000,1);

#
# Structure for table "message"
#

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `from` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `to` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `message` text NOT NULL,
  `send_date` varchar(30) NOT NULL DEFAULT '',
  `isread` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0-false 1-true',
  PRIMARY KEY (`Id`),
  KEY `sender` (`from`),
  KEY `receiver` (`to`),
  CONSTRAINT `receiver` FOREIGN KEY (`to`) REFERENCES `users` (`uid`),
  CONSTRAINT `sender` FOREIGN KEY (`from`) REFERENCES `users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

#
# Data for table "message"
#

INSERT INTO `message` VALUES (1,10000,10002,'2014-9-12','2014-12-13',0),(2,10002,10000,'123456','2014-12-13 00:18:53',0),(3,10002,10000,'123456','2014-12-13 11:25:48',1),(4,10000,10002,'1234568','2014-12-13 11:26:12',1),(5,10002,10000,'123456','2014-12-13 11:26:26',1),(6,10000,10002,'12345588','2014-12-13 11:26:33',1),(7,10000,10002,'1234566','2014-12-13 11:26:38',1),(8,10001,10000,'q213214','2014-12-13 11:37:59',1);
