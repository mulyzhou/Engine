/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.4.2-beta-community : Database - subtest
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`subtest` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `subtest`;

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
) ENGINE=InnoDB AUTO_INCREMENT=240 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_field` */

insert  into `t_base_field`(`ZDID`,`ZDMC`,`ZDLX`,`ZDCD`,`SFZJ`,`SFWK`,`MRZ`,`ZDZS`,`BID`) values (29,'DEPARTMENT_ID','int','11',1,1,NULL,'部门ID',5),(30,'DEPARTMENT_CODE','varchar','50',0,0,NULL,'部门编码',5),(31,'DEPARTMENT_NAME','varchar','200',0,0,NULL,'部门名称',5),(32,'PID','int','11',0,0,NULL,'父ID',5),(33,'CJ_ID','int','11',0,0,NULL,'层级ID',5),(34,'USER_ID','int','11',1,1,NULL,'用户ID',6),(35,'USER_NAME','varchar','50',0,0,NULL,'用户姓名',6),(36,'LOGIN_NAME','varchar','20',0,0,NULL,'登陆账号',6),(37,'PASSWORD','varchar','50',0,0,NULL,'密码',6),(38,'EXPIRED','date','20',0,0,NULL,'账号有效期',6),(39,'BE_USE','int','11',0,0,NULL,'是否启用',6),(40,'MODIFY_TIME','date','20',0,0,NULL,'上次密码修改时间',6),(41,'DEPARTMENT_ID','int','11',0,0,NULL,'部门ID',6),(42,'STATE','int','11',0,0,NULL,'用户状态',6),(154,'ROLE_TYPE_ID','int','11',1,1,NULL,'角色类型ID',26),(155,'ROLE_TYPE_NAME','varchar','20',0,0,NULL,'角色类型名称',26),(156,'ROLE_TYPE_DESC','varchar','200',0,0,NULL,'角色类型描述',26),(157,'ROLE_ID','int','11',1,1,NULL,'角色ID',27),(158,'ROLE_TYPE_ID','int','11',0,0,NULL,'角色类型ID',27),(159,'ROLE_NAME','varchar','20',0,0,NULL,'角色名称',27),(160,'PID','int','11',0,0,NULL,'父角色ID',27),(161,'ROLE_DESC','varchar','200',0,0,NULL,'角色描述',27),(162,'BE_USE','int','11',0,0,NULL,'角色锁定',27),(163,'ROLE_CODE','varchar','50',0,0,NULL,'角色编码',27),(164,'STATE','int','11',0,0,NULL,'角色状态',27),(165,'USER_ROLE_ID','int','11',1,1,NULL,'用户角色ID',28),(166,'USER_ID','int','11',0,0,NULL,'用户ID',28),(167,'ROLE_ID','int','11',0,0,NULL,'角色ID',28),(213,'RESOURCE_ID','int','11',1,1,NULL,'资源ID',40),(214,'RESOURCE_CODE','varchar','100',0,0,NULL,'资源编码',40),(215,'RESOURCE_TYPE_ID','int','11',0,0,NULL,'资源类型ID',40),(216,'RESOURCE_NAME','varchar','100',0,0,NULL,'资源名称',40),(217,'RESOURCE_ADDR','varchar','200',0,0,NULL,'资源地址',40),(218,'RESOURCE_PIC','varchar','200',0,0,NULL,'资源图标',40),(219,'RESOURCE_HELPINFO','varchar','2000',0,0,NULL,'帮助信息',40),(220,'SORT_NUM','int','11',0,0,NULL,'排序序号',40),(221,'PID','int','11',0,0,NULL,'父菜单ID',40),(222,'FACE_TYPE','int','11',0,0,NULL,'FACE类型',40),(223,'SHOW_FACE','varchar','20',0,0,NULL,'使用的FACE类型',40),(224,'TAB_NAME','varchar','20',0,0,NULL,'EMIA模式TAB页名称',40),(225,'RESOURCE_ROLE_ID','int','11',1,1,NULL,'资源角色ID',41),(226,'RESOURCE_ID','int','11',0,0,NULL,'资源ID',41),(227,'ROLE_ID','int','11',0,0,NULL,'角色ID',41),(228,'RESOURCE_TYPE_ID','int','11',1,1,NULL,'资源类型ID',42),(229,'RESOURCE_TYPE_NAME','varchar','50',0,0,NULL,'资源类型名称',42),(230,'SUBRESOURCE_ID','int','11',1,1,NULL,'子资源ID',43),(231,'RESOURCE_ID','int','11',0,0,NULL,'资源ID',43),(232,'SUBRESOURCE_NAME','varchar','50',0,0,NULL,'子资源名称',43),(233,'SUBRESOURCE_ADDR','varchar','200',0,0,NULL,'子资源地址',43);

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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_foreignkey` */

/*Table structure for table `t_base_menu` */

DROP TABLE IF EXISTS `t_base_menu`;

CREATE TABLE `t_base_menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `CJ` int(11) DEFAULT NULL COMMENT '菜单层级',
  `PID` int(11) DEFAULT NULL COMMENT '父ID',
  `TEXT` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '菜单名称',
  `TYPE` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '模板类型',
  `ICON` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '图片地址',
  `SRC` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '请求地址',
  `EXPANDED` tinyint(1) DEFAULT '0' COMMENT '是否展开',
  `CACHE` tinyint(1) DEFAULT '0' COMMENT '是否启用缓存',
  `LEAF` tinyint(1) DEFAULT '0' COMMENT '是否是叶子节点',
  `DESKTOP` tinyint(1) DEFAULT '0' COMMENT '是否放置在桌面',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_menu` */

insert  into `t_base_menu`(`ID`,`CJ`,`PID`,`TEXT`,`TYPE`,`ICON`,`SRC`,`EXPANDED`,`CACHE`,`LEAF`,`DESKTOP`) values (105,1,0,'报表','','img/icon/splash_brown.png','',1,0,0,0),(110,2,105,'菜单管理','email','img/icon/splash_green1.png','biz/core/T_BASE_MENU.js',1,1,1,1),(111,2,105,'报表管理','common','img/icon/splash_green2.png','biz/core/T_BASE_STATICS.js',1,1,1,1);

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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_table` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
