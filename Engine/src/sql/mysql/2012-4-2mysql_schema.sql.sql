/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.4.2-beta-community : Database - flying
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`flying` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `flying`;

/*Table structure for table `t_base_field` */

DROP TABLE IF EXISTS `t_base_field`;

CREATE TABLE `t_base_field` (
  `ZDID` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段ID',
  `ZDMC` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字段名称',
  `ZDLX` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字段类型',
  `ZDCD` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字段长度',
  `SFZJ` tinyint(1) DEFAULT NULL COMMENT '是否主键',
  `SFWK` tinyint(1) DEFAULT NULL COMMENT '是否可为空',
  `MRZ` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '默认值',
  `ZDZS` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '字段注释',
  `BID` int(11) DEFAULT NULL COMMENT '表ID',
  PRIMARY KEY (`ZDID`),
  KEY `FK_t_base_field` (`BID`),
  CONSTRAINT `FK_t_base_field` FOREIGN KEY (`BID`) REFERENCES `t_base_table` (`BID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_field` */

/*Table structure for table `t_base_foreignkey` */

DROP TABLE IF EXISTS `t_base_foreignkey`;

CREATE TABLE `t_base_foreignkey` (
  `WJID` int(11) NOT NULL AUTO_INCREMENT COMMENT '外键ID',
  `WJMC` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '外键名称',
  `ZBMC` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '主表名称',
  `WBMC` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '外表名称',
  `ZBZD` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '主表字段',
  `WBZD` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '外表字段',
  `state` int(11) DEFAULT NULL COMMENT '生成状态',
  PRIMARY KEY (`WJID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_foreignkey` */

/*Table structure for table `t_base_menu` */

DROP TABLE IF EXISTS `t_base_menu`;

CREATE TABLE `t_base_menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `CJ` int(11) NOT NULL COMMENT '菜单层级',
  `PID` int(11) NOT NULL COMMENT '父ID',
  `TEXT` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `TYPE` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '模板类型',
  `SRC` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '请求地址',
  `EXPANDED` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否展开',
  `CACHE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用缓存',
  `LEAF` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是叶子节点',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_menu` */

insert  into `t_base_menu`(`ID`,`CJ`,`PID`,`TEXT`,`TYPE`,`SRC`,`EXPANDED`,`CACHE`,`LEAF`) values (104,1,0,'权限系统管理','','',1,0,0),(105,1,0,'报表','','',1,0,0),(110,2,104,'菜单管理','permission','biz/core/T_BASE_MENU.js',1,1,1),(111,2,104,'报表管理','permission','biz/core/T_BASE_STATICS.js',1,1,1);

/*Table structure for table `t_base_statics` */

DROP TABLE IF EXISTS `t_base_statics`;

CREATE TABLE `t_base_statics` (
  `BBID` int(11) NOT NULL AUTO_INCREMENT COMMENT '报表ID',
  `BBMC` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '报表名称',
  `SQLID` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '执行sqlid',
  `BBJY` varchar(1000) COLLATE utf8_bin NOT NULL COMMENT '报表语句',
  PRIMARY KEY (`BBID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_statics` */

/*Table structure for table `t_base_table` */

DROP TABLE IF EXISTS `t_base_table`;

CREATE TABLE `t_base_table` (
  `BID` int(11) NOT NULL AUTO_INCREMENT COMMENT '表ID',
  `BMC` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '表名称',
  `BZS` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '表注释',
  `BMS` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '表描述',
  `x` int(11) DEFAULT NULL COMMENT '横坐标',
  `y` int(11) DEFAULT NULL COMMENT '纵坐标',
  `height` int(11) DEFAULT NULL COMMENT '高度',
  `width` int(11) DEFAULT NULL COMMENT '宽度',
  `state` int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`BID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_table` */

/* Function  structure for function  `RelationTableName` */

/*!50003 DROP FUNCTION IF EXISTS `RelationTableName` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `RelationTableName`(
	MC varchar(50)
    ) RETURNS varchar(50) CHARSET utf8
BEGIN
	DECLARE ZS varchar(50);
	
	SELECT BZS into ZS from t_base_table where BMC = MC;
	
	return ZS;
    END */$$
DELIMITER ;

/* Procedure structure for procedure `RelationTable` */

/*!50003 DROP PROCEDURE IF EXISTS  `RelationTable` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `RelationTable`(
	in BMC varchar(50)
    )
BEGIN
	SELECT BMC ZBMC,BMC WBMC,BMC,RelationTableName(BMC) BZS,0 type,NULL ZBZD,NULL WBZD from dual
	union
	SELECT ZBMC,WBMC,WBMC BMC,RelationTableName(WBMC) BZS,1 type,ZBZD,WBZD from t_base_foreignkey where ZBMC = BMC
	union
	SELECT ZBMC,WBMC,ZBMC BMC,RelationTableName(ZBMC) BZS,2 type,ZBZD,WBZD from t_base_foreignkey where WBMC = BMC;
    END */$$
DELIMITER ;

/* Procedure structure for procedure `RelationTableOfwb` */

/*!50003 DROP PROCEDURE IF EXISTS  `RelationTableOfwb` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `RelationTableOfwb`(
	in BMC varchar(50),
	IN WB VARCHAR(50)
    )
BEGIN
	SELECT ZBMC,WBMC,WBMC BMC,RelationTableName(WBMC) BZS,2 type,ZBZD,WBZD from t_base_foreignkey where ZBMC = BMC AND WBMC != WB;
	
    END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
