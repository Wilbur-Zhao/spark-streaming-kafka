# ************************************************************
# Sequel Pro SQL dump
# Version 4499
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 10.255.33.109 (MySQL 5.6.20-log)
# Database: athena_background
# Generation Time: 2017-08-09 03:26:20 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table loop_time
# ------------------------------------------------------------

DROP TABLE IF EXISTS `loop_time`;

CREATE TABLE `loop_time` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `loop_time` varchar(32) DEFAULT NULL COMMENT '查询时间起点',
  `enable` tinyint(3) DEFAULT '1' COMMENT '1:有效，0:无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table monitor_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `monitor_item`;

CREATE TABLE `monitor_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(55) NOT NULL DEFAULT '' COMMENT '监控名称',
  `operation_id` int(11) NOT NULL COMMENT '业务ID',
  `pointer_type` int(11) NOT NULL COMMENT '1 AVG  2: COUNT  3:SUM',
  `field_index` int(11) NOT NULL COMMENT '维度下标',
  `field` varchar(255) NOT NULL DEFAULT '' COMMENT '维度值',
  `field_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '维度描述',
  `threshold` int(11) NOT NULL COMMENT '报警阈值(时间默认毫秒)',
  `save_log` tinyint(3) DEFAULT '0' COMMENT '是否保存失败日志',
  `receiver_email` varchar(1024) DEFAULT NULL COMMENT '接收报警人邮件，逗号分隔',
  `receiver_mobile` varchar(1024) NOT NULL DEFAULT '' COMMENT '接收报警人电话，逗号分隔',
  `enabled` tinyint(3) NOT NULL DEFAULT '1',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table operation_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `operation_info`;

CREATE TABLE `operation_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `operation_name` varchar(55) DEFAULT NULL COMMENT '业务名称',
  `enabled` tinyint(3) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
