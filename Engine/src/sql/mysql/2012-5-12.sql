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
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_field` */

insert  into `t_base_field`(`ZDID`,`ZDMC`,`ZDLX`,`ZDCD`,`SFZJ`,`SFWK`,`MRZ`,`ZDZS`,`BID`) values (29,'DEPARTMENT_ID','int','11',1,1,NULL,'部门ID',5),(30,'DEPARTMENT_CODE','varchar','50',0,0,NULL,'部门编码',5),(31,'DEPARTMENT_NAME','varchar','200',0,0,NULL,'部门名称',5),(32,'PID','int','11',0,0,NULL,'父ID',5),(33,'CJ_ID','int','11',0,0,NULL,'层级ID',5),(34,'USER_ID','int','11',1,1,NULL,'用户ID',6),(35,'USER_NAME','varchar','50',0,0,NULL,'用户姓名',6),(36,'LOGIN_NAME','varchar','20',0,0,NULL,'登陆账号',6),(37,'PASSWORD','varchar','50',0,0,NULL,'密码',6),(38,'EXPIRED','date','20',0,0,NULL,'账号有效期',6),(39,'BE_USE','int','11',0,0,NULL,'是否启用',6),(40,'MODIFY_TIME','date','20',0,0,NULL,'上次密码修改时间',6),(41,'DEPARTMENT_ID','int','11',0,0,NULL,'部门ID',6),(42,'STATE','int','11',0,0,NULL,'用户状态',6);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_foreignkey` */

insert  into `t_base_foreignkey`(`WJID`,`WJMC`,`ZBMC`,`WBMC`,`ZBZD`,`WBZD`,`state`) values (3,'FK_T_SYS_USERINFO_T_SYS_DEPARTMENT_11','T_SYS_USERINFO','T_SYS_DEPARTMENT','DEPARTMENT_ID','DEPARTMENT_ID',1);

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
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_menu` */

insert  into `t_base_menu`(`ID`,`CJ`,`PID`,`TEXT`,`TYPE`,`SRC`,`EXPANDED`,`CACHE`,`LEAF`) values (104,1,0,'权限系统管理','','',1,0,0),(105,1,0,'报表','','',1,0,0),(110,2,105,'菜单管理','permission','biz/core/T_BASE_MENU.js',1,1,1),(111,2,105,'报表管理','common','biz/core/T_BASE_STATICS.js',1,1,1),(112,1,0,'Flex管理','','',1,0,0),(113,2,112,'人员管理','flex','biz.Employee',1,1,1),(114,2,112,'flex版菜单管理','permission','biz/core/T_BASE_MENU.js',1,1,1),(118,2,112,'flex版报表管理','common','biz/core/T_BASE_STATICS.js',1,1,1),(123,2,104,'部门信息','common','biz/sys/T_SYS_DEPARTMENT.js',1,1,1),(124,2,104,'用户信息','permission','biz/sys/T_SYS_USERINFO.js',1,1,1);

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_table` */

insert  into `t_base_table`(`BID`,`BMC`,`BZS`,`BMS`,`x`,`y`,`height`,`width`,`state`) values (5,'T_SYS_DEPARTMENT','部门信息',NULL,257,161,128,100,1),(6,'T_SYS_USERINFO','用户信息',NULL,706,114,192,100,1);

/*Table structure for table `t_sys_department` */

DROP TABLE IF EXISTS `t_sys_department`;

CREATE TABLE `t_sys_department` (
  `DEPARTMENT_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `DEPARTMENT_CODE` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '部门编码',
  `DEPARTMENT_NAME` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '部门名称',
  `PID` int(11) NOT NULL COMMENT '父ID',
  `LEVEL_ID` int(11) NOT NULL COMMENT '层级ID',
  PRIMARY KEY (`DEPARTMENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_department` */

insert  into `t_sys_department`(`DEPARTMENT_ID`,`DEPARTMENT_CODE`,`DEPARTMENT_NAME`,`PID`,`LEVEL_ID`) values (104,'0','信息部',0,1),(105,'1000','办公室',104,2);

/*Table structure for table `t_sys_userinfo` */

DROP TABLE IF EXISTS `t_sys_userinfo`;

CREATE TABLE `t_sys_userinfo` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USER_NAME` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户姓名',
  `LOGIN_NAME` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '登陆账号',
  `PASSWORD` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `EXPIRED` datetime NOT NULL COMMENT '账号有效期',
  `BE_USE` int(11) NOT NULL COMMENT '是否启用',
  `MODIFY_TIME` datetime NOT NULL COMMENT '上次密码修改时间',
  `DEPARTMENT_ID` int(11) NOT NULL COMMENT '部门ID',
  `STATE` int(11) NOT NULL COMMENT '用户状态',
  PRIMARY KEY (`USER_ID`),
  KEY `FK_T_SYS_USERINFO_T_SYS_DEPARTMENT_11` (`DEPARTMENT_ID`),
  CONSTRAINT `FK_T_SYS_USERINFO_T_SYS_DEPARTMENT_11` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `t_sys_department` (`DEPARTMENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_userinfo` */

insert  into `t_sys_userinfo`(`USER_ID`,`USER_NAME`,`LOGIN_NAME`,`PASSWORD`,`EXPIRED`,`BE_USE`,`MODIFY_TIME`,`DEPARTMENT_ID`,`STATE`) values (104,'zdf1','zdf1','e10adc3949ba59abbe56e057f20f883e','2012-04-28 00:00:00',0,'2012-04-14 18:34:11',105,1);

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
