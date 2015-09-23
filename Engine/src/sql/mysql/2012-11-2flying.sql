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
) ENGINE=InnoDB AUTO_INCREMENT=377 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_field` */

insert  into `t_base_field`(`ZDID`,`ZDMC`,`ZDLX`,`ZDCD`,`SFZJ`,`SFWK`,`MRZ`,`ZDZS`,`BID`) values (29,'DEPARTMENT_ID','int','11',1,1,NULL,'部门ID',5),(30,'DEPARTMENT_CODE','varchar','50',0,0,NULL,'部门编码',5),(31,'DEPARTMENT_NAME','varchar','200',0,0,NULL,'部门名称',5),(32,'PID','int','11',0,0,NULL,'父ID',5),(33,'LEVEL_ID','int','11',0,0,NULL,'层级ID',5),(34,'USER_ID','int','11',1,1,NULL,'用户ID',6),(35,'USER_NAME','varchar','50',0,0,NULL,'用户姓名',6),(36,'LOGIN_NAME','varchar','20',0,0,NULL,'登陆账号',6),(37,'PASSWORD','varchar','50',0,0,NULL,'密码',6),(38,'EXPIRED','date','20',0,0,NULL,'账号有效期',6),(39,'BE_USE','int','11',0,0,NULL,'是否启用',6),(40,'MODIFY_TIME','date','20',0,0,NULL,'上次密码修改时间',6),(41,'DEPARTMENT_ID','int','11',0,0,NULL,'部门ID',6),(42,'STATE','int','11',0,0,NULL,'用户状态',6),(119,'FLOW_ID','int','11',1,1,NULL,'流程ID',19),(120,'FLOW_NAME','varchar','20',0,0,NULL,'流程名称',19),(121,'CREATETIME','date','20',0,0,NULL,'流程创建的时间',19),(122,'DELETETIME','date','20',0,0,NULL,'流程删除的时间',19),(123,'STATE','int','11',0,0,NULL,'流程状态值',19),(124,'STEP_ID','int','11',1,1,NULL,'步骤ID',20),(125,'STEP_NAME','varchar','20',0,0,NULL,'步骤名称',20),(126,'STEP_TYPEID','int','11',0,0,NULL,'步骤类型ID',20),(127,'STEP_LEFT','int','11',0,0,NULL,'左边距',20),(128,'STEP_TOP','int','11',0,0,NULL,'上边距',20),(129,'FLOW_ID','int','11',0,0,NULL,'流程ID',20),(130,'ACTION_ID','int','11',1,1,NULL,'动作ID',21),(131,'ACTION_NAME','varchar','20',0,0,NULL,'动作名称',21),(132,'ACTION_TYPEID','int','11',0,0,NULL,'动作类型',21),(133,'ACTION_FROMSTEP','int','11',0,0,NULL,'上一步骤ID',21),(134,'ACTION_TOSTEP','int','11',0,0,NULL,'下一步骤ID',21),(135,'ACTION_LEFT','int','11',0,0,NULL,'左边距',21),(136,'ACTION_TOP','int','11',0,0,NULL,'上边距',21),(137,'FLOW_ID','int','11',0,0,NULL,'流程ID',21),(138,'STEP_TYPEID','int','11',1,1,NULL,'步骤类型ID',22),(139,'STEP_TYPENAME','varchar','20',0,0,NULL,'步骤类型名称',22),(140,'ACTION_TYPEID','int','11',1,1,NULL,'动作类型ID',23),(141,'ACTION_TYPENAME','varchar','20',0,0,NULL,'动作类型名称',23),(154,'ROLE_TYPE_ID','int','11',1,1,NULL,'角色类型ID',26),(155,'ROLE_TYPE_NAME','varchar','20',0,0,NULL,'角色类型名称',26),(156,'ROLE_TYPE_DESC','varchar','200',0,0,NULL,'角色类型描述',26),(157,'ROLE_ID','int','11',1,1,NULL,'角色ID',27),(158,'ROLE_TYPE_ID','int','11',0,0,NULL,'角色类型ID',27),(159,'ROLE_NAME','varchar','20',0,0,NULL,'角色名称',27),(160,'PID','int','11',0,0,NULL,'父角色ID',27),(161,'ROLE_DESC','varchar','200',0,0,NULL,'角色描述',27),(162,'BE_USE','int','11',0,0,NULL,'角色锁定',27),(163,'ROLE_CODE','varchar','50',0,0,NULL,'角色编码',27),(164,'STATE','int','11',0,0,NULL,'角色状态',27),(165,'USER_ROLE_ID','int','11',1,1,NULL,'用户角色ID',28),(166,'USER_ID','int','11',0,0,NULL,'用户ID',28),(167,'ROLE_ID','int','11',0,0,NULL,'角色ID',28),(190,'STEP_ROLE_ID','int','11',1,1,NULL,'步骤角色关系ID',35),(191,'STEP_ID','int','11',0,0,NULL,'步骤ID',35),(192,'ROLE_ID','int','11',0,0,NULL,'角色ID',35),(193,'FORM_ID','int','11',1,1,NULL,'表单ID',36),(194,'FORM_NAME','varchar','20',0,0,NULL,'表单名称',36),(195,'FORM_TYPE','int','11',0,0,NULL,'表单类型',36),(196,'FORM_ADDR','varchar','200',0,0,NULL,'表单地址',36),(197,'FORM_DESC','varchar','200',0,0,NULL,'字段描述',36),(198,'STEP_FORM_ID','int','11',1,1,NULL,'步骤表单ID',37),(199,'STEP_ID','int','11',0,0,NULL,'步骤ID',37),(200,'FORM_ID','int','11',0,0,NULL,'表单ID',37),(201,'INSTANCE_ID','int','11',1,1,NULL,'流程实例ID',38),(202,'FLOW_ID','int','11',0,0,NULL,'流程ID',38),(203,'STEP_ID','int','11',0,0,NULL,'步骤ID',38),(204,'CREATETIME','date','20',0,0,NULL,'流程开始的时间',38),(205,'STARTTIME','date','20',0,0,NULL,'步骤开始时间',38),(206,'ENDTIME','date','20',0,0,NULL,'流程结束时间',38),(207,'INSTANCE_ID','int','11',0,0,NULL,'实例ID',39),(208,'OPINION_DESC','varchar','500',0,0,NULL,'意见描述',39),(209,'OPINION_ID','int','11',1,1,NULL,'意见ID',39),(210,'OPINION_TIME','date','20',0,0,NULL,'意见时间',39),(211,'OPINION_TYPE','int','11',0,0,NULL,'意见类型',39),(212,'USER_ID','int','11',0,0,NULL,'人员ID',39),(213,'RESOURCE_ID','int','11',1,1,NULL,'资源ID',40),(214,'RESOURCE_CODE','varchar','100',0,0,NULL,'资源编码',40),(215,'RESOURCE_TYPE_ID','int','11',0,0,NULL,'资源类型ID',40),(216,'RESOURCE_NAME','varchar','100',0,0,NULL,'资源名称',40),(217,'RESOURCE_ADDR','varchar','200',0,0,NULL,'资源地址',40),(218,'RESOURCE_PIC','varchar','200',0,0,NULL,'资源图标',40),(219,'RESOURCE_HELPINFO','varchar','2000',0,0,NULL,'帮助信息',40),(220,'SORT_NUM','int','11',0,0,NULL,'排序序号',40),(221,'PID','int','11',0,0,NULL,'父菜单ID',40),(222,'FACE_TYPE','int','11',0,0,NULL,'FACE类型',40),(223,'SHOW_FACE','varchar','20',0,0,NULL,'使用的FACE类型',40),(224,'TAB_NAME','varchar','20',0,0,NULL,'EMIA模式TAB页名称',40),(225,'RESOURCE_ROLE_ID','int','11',1,1,NULL,'资源角色ID',41),(226,'RESOURCE_ID','int','11',0,0,NULL,'资源ID',41),(227,'ROLE_ID','int','11',0,0,NULL,'角色ID',41),(228,'RESOURCE_TYPE_ID','int','11',1,1,NULL,'资源类型ID',42),(229,'RESOURCE_TYPE_NAME','varchar','50',0,0,NULL,'资源类型名称',42),(240,'QJID','int','11',1,1,NULL,'请假ID',46),(241,'QJSJ','int','11',0,0,NULL,'请假时间',46),(242,'QJRY','int','11',0,0,NULL,'请假人员',46),(243,'QJYY','varchar','200',0,0,NULL,'请假原因',46),(244,'INSTANCE_ID','int','11',0,0,NULL,'流程实例ID',46),(245,'APPLYCAR_ID','varchar','20',1,1,NULL,'车辆申请ID',47),(246,'SQSJ','date','20',0,0,NULL,'申请时间',47),(247,'SQRY','int','11',0,0,NULL,'申请人员',47),(248,'SQYY','varchar','200',0,0,NULL,'申请原因',47),(249,'CPH','varchar','200',0,0,NULL,'车牌号',47),(250,'KSLC','varchar','20',0,0,NULL,'开始里程',47),(251,'JSLC','varchar','20',0,0,NULL,'结束里程',47),(252,'INSTANCE_ID','int','11',0,0,NULL,'流程实例ID',47),(253,'BXID','int','11',1,1,NULL,'报销ID',48),(254,'BXRY','int','11',0,0,NULL,'报销人员',48),(255,'BXYY','varchar','200',0,0,NULL,'报销原因',48),(256,'BXJE','int','11',0,0,NULL,'报销金额',48),(257,'INSTANCE_ID','int','11',0,0,NULL,'流程实例ID',48),(258,'FUN_ID','int','11',1,1,NULL,'处理ID',49),(259,'FUN_NAME','varchar','200',0,0,NULL,'SQLID',49),(260,'FUN_TYPE','int','11',0,0,NULL,'处理类型',49),(261,'STEP_ID','int','11',0,0,NULL,'步骤ID',49),(268,'FREEFLOW_ID','int','11',1,1,NULL,'自由流ID',51),(269,'FROM_STEPID','int','11',0,0,NULL,'起始步骤',51),(270,'TO_STEPID','int','11',0,0,NULL,'结束步骤',51),(271,'OPINION_ID','int','11',0,0,NULL,'轨迹ID',51),(272,'USER_ID','int','11',0,0,NULL,'用户ID',51),(273,'CREATETIME','date','20',0,0,NULL,'创建时间',51),(288,'MSG_ID','int','11',1,1,NULL,'消息ID',54),(289,'MSG_TITLE','varchar','400',0,0,NULL,'消息标题',54),(290,'MSG_ADDR','varchar','800',0,0,NULL,'链接地址',54),(291,'MSG_ADDR_PARAM','varchar','800',0,0,NULL,'链接地址参数',54),(292,'MSG_RECIPIENT','int','11',0,0,NULL,'收件人',54),(293,'MSG_SENDER','int','11',0,0,NULL,'发件人',54),(294,'SEND_DATE','date','20',0,0,NULL,'发送时间',54),(295,'SYS_NAME','varchar','50',0,0,NULL,'系统简称',54),(296,'IS_VIEW','boolean','11',0,0,NULL,'是否查看',54),(297,'REMIND_ID','int','11',1,1,NULL,'提醒ID',55),(298,'SYS_NAME','varchar','50',0,0,NULL,'系统简称',55),(299,'LINK_ADDR','varchar','800',0,0,NULL,'链接地址',55),(300,'USER_ID','int','11',0,0,NULL,'用户ID',55),(301,'REMIND_SIZE','int','11',0,0,NULL,'提醒数量',55),(302,'USER_RESOURCE_ID','int','11',1,1,NULL,'用户资源ID',56),(303,'USER_ID','int','11',0,0,NULL,'用户ID',56),(304,'RESOURCE_ID','int','11',0,0,NULL,'资源ID',56),(305,'FACETYPE','varchar','50',0,0,NULL,'模板类型',56),(306,'DESKTOP','boolean','20',0,0,NULL,'是否显示在桌面',56),(307,'ICON','varchar','200',0,0,NULL,'显示图标',56),(308,'SORT_NUM','int','11',0,0,NULL,'排序',56);

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
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_foreignkey` */

insert  into `t_base_foreignkey`(`WJID`,`WJMC`,`ZBMC`,`WBMC`,`ZBZD`,`WBZD`,`state`) values (3,'FK_T_SYS_USERINFO_T_SYS_DEPARTMENT_11','T_SYS_USERINFO','T_SYS_DEPARTMENT','DEPARTMENT_ID','DEPARTMENT_ID',1),(12,'FK_T_WF_STEP_T_WF_FLOW_13','T_WF_STEP','T_WF_FLOW','FLOW_ID','FLOW_ID',1),(13,'FK_T_WF_ACTION_T_WF_FLOW_14','T_WF_ACTION','T_WF_FLOW','FLOW_ID','FLOW_ID',1),(14,'FK_T_WF_ACTION_T_WF_ACTIONTYPE_20','T_WF_ACTION','T_WF_ACTIONTYPE','ACTION_TYPEID','ACTION_TYPEID',1),(15,'FK_T_WF_STEP_T_WF_STEPTYPE_19','T_WF_STEP','T_WF_STEPTYPE','STEP_TYPEID','STEP_TYPEID',1),(18,'FK_T_SYS_ROLE_T_SYS_ROLETYPE_14','T_SYS_ROLE','T_SYS_ROLETYPE','ROLE_TYPE_ID','ROLE_TYPE_ID',1),(19,'FK_T_SYS_USERROLE_T_SYS_ROLE_17','T_SYS_USERROLE','T_SYS_ROLE','ROLE_ID','ROLE_ID',1),(20,'FK_T_SYS_USERROLE_T_SYS_USERINFO_16','T_SYS_USERROLE','T_SYS_USERINFO','USER_ID','USER_ID',1),(27,'FK_T_WF_STEPFORM_T_WF_STEP_30','T_WF_STEPFORM','T_WF_STEP','STEP_ID','STEP_ID',1),(28,'FK_T_WF_STEPFORM_T_WF_FORM_29','T_WF_STEPFORM','T_WF_FORM','FORM_ID','FORM_ID',1),(29,'FK_T_WF_STEPROLE_T_WF_STEP_26','T_WF_STEPROLE','T_WF_STEP','STEP_ID','STEP_ID',1),(30,'FK_T_WF_INSTANCE_T_WF_FLOW_33','T_WF_INSTANCE','T_WF_FLOW','FLOW_ID','FLOW_ID',1),(31,'FK_T_WF_INSTANCE_T_WF_STEP_32','T_WF_INSTANCE','T_WF_STEP','STEP_ID','STEP_ID',1),(32,'FK_T_WF_OPINION_T_WF_INSTANCE_35','T_WF_OPINION','T_WF_INSTANCE','INSTANCE_ID','INSTANCE_ID',1),(34,'FK_T_SYS_RESOURCE_T_SYS_RESOURCETYPE_23','T_SYS_RESOURCE','T_SYS_RESOURCETYPE','RESOURCE_TYPE_ID','RESOURCE_TYPE_ID',1),(35,'FK_T_SYS_RESOURCEROLE_T_SYS_RESOURCE_21','T_SYS_RESOURCEROLE','T_SYS_RESOURCE','RESOURCE_ID','RESOURCE_ID',1),(36,'FK_T_SYS_RESOURCEROLE_T_SYS_ROLE_20','T_SYS_RESOURCEROLE','T_SYS_ROLE','ROLE_ID','ROLE_ID',1),(39,'FK_QJ_INSTANCE_40','T_WF_QJ','T_WF_INSTANCE','INSTANCE_ID','INSTANCE_ID',1),(40,'FK_APPLYCAR_INSTANCE_42','T_WF_APPLYCAR','T_WF_INSTANCE','INSTANCE_ID','INSTANCE_ID',1),(41,'FK_BX_INSTANCE_44','T_WF_BX','T_WF_INSTANCE','INSTANCE_ID','INSTANCE_ID',1),(42,'FK_STEPFUNCTION_STEP_46','T_WF_STEPFUNCTION','T_WF_STEP','STEP_ID','STEP_ID',1),(44,'FK_FREEFLOW_OPINION_48','T_WF_FREEFLOW','T_WF_OPINION','OPINION_ID','OPINION_ID',1),(45,'FK_USERRESOURCE_USERINFO_31','T_SYS_USERRESOURCE','T_SYS_USERINFO','USER_ID','USER_ID',1),(46,'FK_USERRESOURCE_RESOURCE_30','T_SYS_USERRESOURCE','T_SYS_RESOURCE','RESOURCE_ID','RESOURCE_ID',1);

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
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_base_table` */

insert  into `t_base_table`(`BID`,`BMC`,`BZS`,`BMS`,`x`,`y`,`height`,`width`,`state`) values (5,'T_SYS_DEPARTMENT','部门信息',NULL,889,20,128,100,1),(6,'T_SYS_USERINFO','用户信息',NULL,615,18,192,100,1),(19,'T_WF_FLOW','流程管理',NULL,542,241,128,100,1),(20,'T_WF_STEP','步骤管理',NULL,687,63,144,100,1),(21,'T_WF_ACTION','动作管理',NULL,733,298,176,100,1),(22,'T_WF_STEPTYPE','步骤类型',NULL,933,-7,100,100,1),(23,'T_WF_ACTIONTYPE','动作类型',NULL,953,345,100,100,1),(26,'T_SYS_ROLETYPE','角色类型信息',NULL,788,447,100,100,1),(27,'T_SYS_ROLE','角色信息',NULL,645,247,176,100,1),(28,'T_SYS_USERROLE','用户角色信息',NULL,912,219,100,100,1),(35,'T_WF_STEPROLE','步骤角色关系',NULL,1077,65,100,100,1),(36,'T_WF_FORM','表单管理',NULL,1174,305,128,100,1),(37,'T_WF_STEPFORM','步骤表单关系',NULL,964,207,100,100,1),(38,'T_WF_INSTANCE','流程实例管理',NULL,303,90,144,100,1),(39,'T_WF_OPINION','处理日志',NULL,171,347,144,100,1),(40,'T_SYS_RESOURCE','资源信息',NULL,238,105,224,100,1),(41,'T_SYS_RESOURCEROLE','资源角色信息',NULL,434,351,100,100,1),(42,'T_SYS_RESOURCETYPE','资源类型信息',NULL,48,84,100,100,1),(46,'T_WF_QJ','请假业务',NULL,116,-1,128,100,1),(47,'T_WF_APPLYCAR','车辆申请业务',NULL,25,104,176,100,1),(48,'T_WF_BX','报销业务',NULL,46,317,128,100,1),(49,'T_WF_STEPFUNCTION','步骤的处理信息',NULL,410,16,112,100,1),(51,'T_WF_FREEFLOW','自由流信息',NULL,467,347,144,100,1),(54,'T_SYS_MESSAGE','消息表管理',NULL,1173,155,192,100,1),(55,'T_SYS_REMIND','提醒管理',NULL,1059,400,128,100,1),(56,'T_SYS_USERRESOURCE','用户资源表',NULL,440,16,160,100,1);

/*Table structure for table `t_sys_department` */

DROP TABLE IF EXISTS `t_sys_department`;

CREATE TABLE `t_sys_department` (
  `DEPARTMENT_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `DEPARTMENT_CODE` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '部门编码',
  `DEPARTMENT_NAME` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '部门名称',
  `PID` int(11) NOT NULL COMMENT '父ID',
  `LEVEL_ID` int(11) NOT NULL COMMENT '层级ID',
  `ORDER_ID` int(11) DEFAULT '1' COMMENT '排序号',
  PRIMARY KEY (`DEPARTMENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_department` */

insert  into `t_sys_department`(`DEPARTMENT_ID`,`DEPARTMENT_CODE`,`DEPARTMENT_NAME`,`PID`,`LEVEL_ID`,`ORDER_ID`) values (104,'0','信息部',0,1,1),(105,'1000','办公室',104,2,1);

/*Table structure for table `t_sys_message` */

DROP TABLE IF EXISTS `t_sys_message`;

CREATE TABLE `t_sys_message` (
  `MSG_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `MSG_TITLE` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '消息标题',
  `MSG_ADDR` varchar(800) COLLATE utf8_bin DEFAULT NULL COMMENT '链接地址',
  `MSG_ADDR_PARAM` varchar(800) COLLATE utf8_bin DEFAULT NULL COMMENT '链接地址参数',
  `MSG_RECIPIENT` int(11) DEFAULT NULL COMMENT '收件人',
  `MSG_SENDER` int(11) DEFAULT NULL COMMENT '发件人',
  `SEND_DATE` datetime DEFAULT NULL COMMENT '发送时间',
  `SYS_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '系统简称',
  `IS_VIEW` int(1) DEFAULT NULL COMMENT '是否查看',
  `MSG_TOKEN` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '发给同一角色的相同消息',
  PRIMARY KEY (`MSG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_message` */

insert  into `t_sys_message`(`MSG_ID`,`MSG_TITLE`,`MSG_ADDR`,`MSG_ADDR_PARAM`,`MSG_RECIPIENT`,`MSG_SENDER`,`SEND_DATE`,`SYS_NAME`,`IS_VIEW`,`MSG_TOKEN`) values (105,'添加了一个新的用户，用户名zdf4','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":107}',105,104,'2012-09-28 00:00:00','sys',1,NULL),(106,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":264}',105,104,'2012-09-28 00:00:00','工作流',1,NULL),(107,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":264}',106,104,'2012-09-28 00:00:00','工作流',1,NULL),(108,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":267}',105,106,'2012-09-28 00:00:00','工作流',1,NULL),(109,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":267}',106,106,'2012-09-28 00:00:00','工作流',1,NULL),(110,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":272}',105,104,'2012-09-29 00:00:00','工作流',1,'1348886346143'),(111,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":272}',106,104,'2012-09-29 00:00:00','工作流',1,'1348886346143'),(112,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":274}',105,104,'2012-09-29 00:00:00','工作流',1,'104time1348886627344'),(113,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":274}',106,104,'2012-09-29 00:00:00','工作流',1,'104time1348886627344'),(114,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":277}',105,105,'2012-09-29 00:00:00','工作流',1,'105time1348887164140'),(115,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":277}',106,105,'2012-09-29 00:00:00','工作流',1,'105time1348887164140'),(116,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":279}',105,105,'2012-09-29 00:00:00','工作流',1,'105time1348887235148'),(117,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":279}',106,105,'2012-09-29 00:00:00','工作流',1,'105time1348887235148'),(119,'添加了一个新的用户，用户名郑迪','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":109}',105,104,'2012-10-08 00:00:00','sys',1,NULL),(120,'添加了一个新的用户，用户名郑中','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":110}',105,104,'2012-10-08 00:00:00','sys',1,NULL),(121,'添加了一个新的用户，用户名郑好','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":111}',105,104,'2012-10-08 00:00:00','sys',1,NULL),(122,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":281}',105,104,'2012-10-08 00:00:00','工作流',1,'104time1349680914936'),(123,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":281}',106,104,'2012-10-08 00:00:00','工作流',1,'104time1349680914936'),(124,'添加了一个新的用户，用户名null','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":112}',105,104,'2012-10-08 00:00:00','sys',1,NULL),(125,'添加了一个新的用户，用户名null','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":113}',105,104,'2012-10-08 00:00:00','sys',1,NULL),(126,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":289}',105,105,'2012-10-09 00:00:00','工作流',1,'105time1349752256423'),(127,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":289}',106,105,'2012-10-09 00:00:00','工作流',1,'105time1349752256423'),(128,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":292}',105,105,'2012-10-09 00:00:00','工作流',1,'105time1349753040707'),(129,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":292}',106,105,'2012-10-09 00:00:00','工作流',1,'105time1349753040707'),(130,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":295}',105,105,'2012-10-09 00:00:00','工作流',0,'105time1349754213448'),(131,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":295}',106,105,'2012-10-09 00:00:00','工作流',0,'105time1349754213448'),(132,'添加了一个新的用户，用户名zdf8','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":112}',105,104,'2012-10-09 00:00:00','sys',0,NULL),(133,'添加了一个新的用户，用户名zdf9','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":113}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(134,'添加了一个新的用户，用户名zdf10','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":114}',105,105,'2012-10-09 00:00:00','sys',1,NULL),(135,'添加了一个新的用户，用户名zdf11','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":115}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(136,'添加了一个新的用户，用户名zdf12','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":116}',105,105,'2012-10-09 00:00:00','sys',1,NULL),(137,'添加了一个新的用户，用户名zdf13','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":117}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(138,'添加了一个新的用户，用户名zdf14','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":118}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(139,'添加了一个新的用户，用户名zdf15','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":119}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(140,'添加了一个新的用户，用户名zdf16','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":120}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(141,'添加了一个新的用户，用户名zdf20','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":121}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(142,'添加了一个新的用户，用户名zdf19','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":122}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(143,'添加了一个新的用户，用户名zdf23','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":123}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(144,'添加了一个新的用户，用户名zdf22','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":124}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(145,'添加了一个新的用户，用户名zdf23','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":125}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(146,'添加了一个新的用户，用户名zdf24','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":126}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(147,'添加了一个新的用户，用户名zdf25','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":127}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(148,'添加了一个新的用户，用户名zdf26','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":128}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(149,'添加了一个新的用户，用户名zdf29','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":129}',105,105,'2012-10-09 00:00:00','sys',0,NULL),(150,'添加了一个新的用户，用户名zdf30','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":130}',105,105,'2012-10-09 00:00:00','sys',1,NULL),(151,'添加了一个新的用户，用户名zdf32','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":131}',105,105,'2012-10-09 00:00:00','sys',1,NULL),(152,'添加了一个新的用户，用户名zdf31','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":132}',105,105,'2012-10-09 00:00:00','sys',1,NULL),(153,'添加了一个新的用户，用户名zdf35','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":133}',105,105,'2012-10-09 00:00:00','sys',1,NULL),(154,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":298}',105,105,'2012-10-09 00:00:00','工作流',1,'105time1349776992677'),(155,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":298}',106,105,'2012-10-09 00:00:00','工作流',1,'105time1349776992677'),(156,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":300}',105,105,'2012-10-10 00:00:00','工作流',1,'105time1349829572882'),(157,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":300}',106,105,'2012-10-10 00:00:00','工作流',1,'105time1349829572882'),(158,'添加了一个新的用户，用户名zdf36','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":134}',105,105,'2012-10-10 00:00:00','sys',0,NULL),(159,'添加了一个新的用户，用户名zdf37','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":135}',105,105,'2012-10-10 00:00:00','sys',0,NULL),(160,'添加了一个新的用户，用户名zdf38','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":136}',105,105,'2012-10-10 00:00:00','sys',0,NULL),(161,'添加了一个新的用户，用户名zdf39','biz/sys/T_SYS_USERINFO.js','{\"USER_ID\":137}',105,105,'2012-10-10 00:00:00','sys',0,NULL),(162,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":303}',105,105,'2012-10-10 00:00:00','工作流',0,'105time1349831713579'),(163,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":303}',106,105,'2012-10-10 00:00:00','工作流',0,'105time1349831713579'),(164,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":306}',106,104,'2012-10-11 00:00:00','工作流',0,'104time1349945581838'),(165,'您有一个待处理任务','biz/wf/T_WF_MYAUDIT.js','{\"OPINION_ID\":306}',105,104,'2012-10-11 00:00:00','工作流',0,'104time1349945581838');

/*Table structure for table `t_sys_person` */

DROP TABLE IF EXISTS `t_sys_person`;

CREATE TABLE `t_sys_person` (
  `PERSON_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '人员ID',
  `PERSON_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `IDCARD` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号',
  `PEROSN_PIC` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '头像地址',
  `POST_DATE` datetime DEFAULT NULL COMMENT '入职时间',
  `DEPARTMENT_ID` int(11) DEFAULT NULL COMMENT '所属部门',
  `SEX` varchar(4) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `BIRTHDAY` datetime DEFAULT NULL COMMENT '出生年月日',
  `EMAIL` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `PHONE` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '手机',
  `QQ` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'QQ号',
  PRIMARY KEY (`PERSON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `t_sys_person` */

/*Table structure for table `t_sys_remind` */

DROP TABLE IF EXISTS `t_sys_remind`;

CREATE TABLE `t_sys_remind` (
  `REMIND_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '提醒ID',
  `SYS_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '系统简称',
  `LINK_ADDR` varchar(800) COLLATE utf8_bin DEFAULT NULL COMMENT '链接地址',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID',
  `REMIND_SIZE` int(11) DEFAULT NULL COMMENT '提醒数量',
  PRIMARY KEY (`REMIND_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_remind` */

insert  into `t_sys_remind`(`REMIND_ID`,`SYS_NAME`,`LINK_ADDR`,`USER_ID`,`REMIND_SIZE`) values (1,'待分配角色','biz/sys/T_SYS_USERINFO.js',105,27),(3,'待办任务','biz/wf/T_WF_MYAUDIT.js',106,7),(6,'待办任务','biz/wf/T_WF_MYAUDIT.js',105,1);

/*Table structure for table `t_sys_resource` */

DROP TABLE IF EXISTS `t_sys_resource`;

CREATE TABLE `t_sys_resource` (
  `RESOURCE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `RESOURCE_CODE` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源编码',
  `RESOURCE_TYPE_ID` int(11) DEFAULT NULL COMMENT '资源类型ID',
  `RESOURCE_NAME` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源名称',
  `RESOURCE_ADDR` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '资源地址',
  `RESOURCE_HELPINFO` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '帮助信息',
  `SECURITY_NAME` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '权限名称',
  `PID` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `CACHE` tinyint(1) DEFAULT NULL COMMENT '是否缓存',
  `FACETYPE` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '模板通用类型',
  PRIMARY KEY (`RESOURCE_ID`),
  KEY `FK_T_SYS_RESOURCE_T_SYS_RESOURCETYPE_23` (`RESOURCE_TYPE_ID`),
  CONSTRAINT `FK_T_SYS_RESOURCE_T_SYS_RESOURCETYPE_23` FOREIGN KEY (`RESOURCE_TYPE_ID`) REFERENCES `t_sys_resourcetype` (`RESOURCE_TYPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_resource` */

insert  into `t_sys_resource`(`RESOURCE_ID`,`RESOURCE_CODE`,`RESOURCE_TYPE_ID`,`RESOURCE_NAME`,`RESOURCE_ADDR`,`RESOURCE_HELPINFO`,`SECURITY_NAME`,`PID`,`CACHE`,`FACETYPE`) values (1,'0',NULL,'flying开发团队','core',NULL,NULL,0,NULL,NULL),(2,'100',1,'报表管理器','biz/core/T_BASE_STATICS.js','自动生成表报管理',NULL,1,1,'permission'),(3,'101',1,'权限系统','sys','权限系统集合','SYS',1,1,'subSystem'),(251,'101100',1,'账号管理','biz/sys/T_SYS_USERINFO.js',NULL,NULL,3,1,'permission'),(252,'101101',1,'角色管理','biz/sys/T_SYS_ROLE.js',NULL,NULL,3,1,'permission'),(253,'101102',1,'系统资源管理','biz/sys/T_SYS_RESOURCE.js',NULL,NULL,3,1,'empty'),(254,'101103',1,'人员管理','biz/sys/T_SYS_PERSON.js',NULL,NULL,3,1,'permission'),(255,'101104',1,'部门管理','biz/sys/T_SYS_DEPARTMENT.js',NULL,NULL,3,1,'permission'),(256,'101100100',4,'查询所有','T_SYS_USERINFO.selectAll',NULL,NULL,251,NULL,NULL),(257,'101101100',4,'查询所有','T_SYS_ROLE.selectAll',NULL,NULL,252,NULL,NULL),(258,'101103100',4,'查询所有','T_SYS_PERSON.selectAll',NULL,NULL,254,NULL,NULL),(259,'101104100',4,'查询所有','T_SYS_DEPARTMENT.selectAll',NULL,NULL,255,NULL,NULL),(260,'101100101',2,'修改',NULL,NULL,'T_SYS_USERINFO_btnModify',251,NULL,NULL),(261,'101100102',2,'导出',NULL,NULL,'T_SYS_USERINFO_btnDownload',251,NULL,NULL),(262,'101101101',2,'添加',NULL,NULL,'T_SYS_ROLE_btnAdd',252,NULL,NULL),(263,'101101102',2,'修改',NULL,NULL,'T_SYS_ROLE_btnModify',252,NULL,NULL),(264,'101101103',2,'删除',NULL,NULL,'T_SYS_ROLE_btnDelete',252,NULL,NULL),(265,'101101104',2,'导出',NULL,NULL,'T_SYS_ROLE_btnDownload',252,NULL,NULL),(266,'101103101',2,'添加',NULL,NULL,'T_SYS_PERSON_btnAdd',254,NULL,NULL),(267,'101103102',2,'修改',NULL,NULL,'T_SYS_PERSON_btnModify',254,NULL,NULL),(268,'101103103',2,'删除',NULL,NULL,'T_SYS_PERSON_btnDelete',254,NULL,NULL),(269,'101103104',2,'导出',NULL,NULL,'T_SYS_PERSON_btnDownload',254,NULL,NULL),(270,'101104101',2,'添加',NULL,NULL,'T_SYS_DEPARTMENT_btnAdd',255,NULL,NULL),(271,'101104102',2,'修改',NULL,NULL,'T_SYS_DEPARTMENT_btnModify',255,NULL,NULL),(272,'101104103',2,'删除',NULL,NULL,'T_SYS_DEPARTMENT_btnDelete',255,NULL,NULL),(273,'101104104',2,'导出',NULL,NULL,'T_SYS_DEPARTMENT_btnDownload',255,NULL,NULL),(274,'101100101100',4,'修改账号','T_SYS_USERINFO.update',NULL,NULL,260,NULL,NULL),(275,'101101101100',4,'添加角色','T_SYS_ROLE.insert',NULL,NULL,262,NULL,NULL),(276,'101101102100',4,'修改角色','T_SYS_ROLE.update',NULL,NULL,263,NULL,NULL),(277,'101101103100',4,'删除角色','T_SYS_ROLE.delete',NULL,NULL,264,NULL,NULL),(278,'101103101100',4,'添加人员','T_SYS_PERSON.insert',NULL,NULL,266,NULL,NULL),(279,'101103102100',4,'修改人员','T_SYS_PERSON.update',NULL,NULL,267,NULL,NULL),(280,'101103103100',4,'删除人员','T_SYS_PERSON.delete',NULL,NULL,268,NULL,NULL),(281,'101104101100',4,'添加部门','T_SYS_DEPARTMENT.insert',NULL,NULL,270,NULL,NULL),(282,'101104102100',4,'修改部门','T_SYS_DEPARTMENT.update',NULL,NULL,271,NULL,NULL),(283,'101104103100',4,'删除部门','T_SYS_DEPARTMENT.delete',NULL,NULL,272,NULL,NULL),(284,'100100',4,'查询所有','T_BASE_STATICS.selectAll',NULL,NULL,2,NULL,NULL),(285,'100101',2,'添加',NULL,NULL,'T_BASE_STATICS_btnAdd',2,NULL,NULL),(286,'100102',2,'修改',NULL,NULL,'T_BASE_STATICS_btnModify',2,NULL,NULL),(287,'100103',2,'删除',NULL,NULL,'T_BASE_STATICS_btnDelete',2,NULL,NULL),(288,'100104',2,'导出',NULL,NULL,'T_BASE_STATICS_btnDownload',2,NULL,NULL),(289,'101101105',4,'查询角色类型','T_SYS_ROLETYPE.selectAll',NULL,NULL,252,NULL,NULL),(290,'101102100',4,'查询所有菜单','T_SYS_RESOURCE.selectAllMenu',NULL,NULL,253,NULL,NULL),(291,'101102101',4,'根据PID查询子菜单','T_SYS_RESOURCE.selectByPId',NULL,NULL,253,NULL,NULL),(292,'101102102',4,'修改资源','T_SYS_RESOURCE.update',NULL,NULL,253,NULL,NULL),(293,'101102103',4,'添加资源','T_SYS_RESOURCE.insert',NULL,NULL,253,NULL,NULL),(295,'101102104',4,'删除资源','T_SYS_RESOURCE.delete',NULL,NULL,253,NULL,NULL),(318,'101106',1,'测试subSystem','aa',NULL,NULL,3,1,'subSystem'),(319,'101106100',1,'人员测试','biz/sys/T_SYS_USERINFO.js',NULL,NULL,318,1,'permission');

/*Table structure for table `t_sys_resourcerole` */

DROP TABLE IF EXISTS `t_sys_resourcerole`;

CREATE TABLE `t_sys_resourcerole` (
  `RESOURCE_ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源角色ID',
  `RESOURCE_ID` int(11) DEFAULT NULL COMMENT '资源ID',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`RESOURCE_ROLE_ID`),
  KEY `FK_T_SYS_RESOURCEROLE_T_SYS_RESOURCE_21` (`RESOURCE_ID`),
  KEY `FK_T_SYS_RESOURCEROLE_T_SYS_ROLE_20` (`ROLE_ID`),
  CONSTRAINT `FK_T_SYS_RESOURCEROLE_T_SYS_RESOURCE_21` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_sys_resource` (`RESOURCE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_T_SYS_RESOURCEROLE_T_SYS_ROLE_20` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_sys_role` (`ROLE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_resourcerole` */

insert  into `t_sys_resourcerole`(`RESOURCE_ROLE_ID`,`RESOURCE_ID`,`ROLE_ID`) values (152,1,105),(153,2,105),(154,3,105),(155,251,105),(156,260,105),(157,261,105),(158,252,105),(159,262,105),(160,263,105),(161,264,105),(162,265,105),(164,254,105),(165,266,105),(166,267,105),(167,268,105),(168,269,105),(169,255,105),(170,270,105),(171,271,105),(172,272,105),(173,273,105),(174,285,105),(175,286,105),(176,287,105),(177,288,105),(178,253,105),(179,1,104),(180,2,104),(181,288,104),(182,3,104),(183,251,104),(184,261,104),(185,252,104),(186,265,104),(187,254,104),(188,269,104),(189,255,104),(190,273,104),(193,318,105),(194,319,105);

/*Table structure for table `t_sys_resourcetype` */

DROP TABLE IF EXISTS `t_sys_resourcetype`;

CREATE TABLE `t_sys_resourcetype` (
  `RESOURCE_TYPE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源类型ID',
  `RESOURCE_TYPE_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '资源类型名称',
  PRIMARY KEY (`RESOURCE_TYPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_resourcetype` */

insert  into `t_sys_resourcetype`(`RESOURCE_TYPE_ID`,`RESOURCE_TYPE_NAME`) values (1,'菜单'),(2,'按钮'),(3,'表格列'),(4,'子资源');

/*Table structure for table `t_sys_role` */

DROP TABLE IF EXISTS `t_sys_role`;

CREATE TABLE `t_sys_role` (
  `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_TYPE_ID` int(11) DEFAULT NULL COMMENT '角色类型ID',
  `ROLE_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '角色名称',
  `PID` int(11) DEFAULT NULL COMMENT '父角色ID',
  `ROLE_DESC` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '角色描述',
  `BE_USE` int(11) DEFAULT NULL COMMENT '角色锁定',
  `ROLE_CODE` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '角色编码',
  `STATE` int(11) DEFAULT NULL COMMENT '角色状态',
  PRIMARY KEY (`ROLE_ID`),
  KEY `FK_T_SYS_ROLE_T_SYS_ROLETYPE_14` (`ROLE_TYPE_ID`),
  CONSTRAINT `FK_T_SYS_ROLE_T_SYS_ROLETYPE_14` FOREIGN KEY (`ROLE_TYPE_ID`) REFERENCES `t_sys_roletype` (`ROLE_TYPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_role` */

insert  into `t_sys_role`(`ROLE_ID`,`ROLE_TYPE_ID`,`ROLE_NAME`,`PID`,`ROLE_DESC`,`BE_USE`,`ROLE_CODE`,`STATE`) values (104,104,'普通员工',0,'一般员工',1,NULL,3),(105,104,'普通管理员',0,'可以进行普通的审核',1,NULL,3),(106,104,'车辆管理员',0,'审核车辆申请',1,NULL,2),(107,104,'后勤处长',0,'审核车辆',1,NULL,2),(108,104,'报销经理',0,'负责报销的经理',1,NULL,2),(109,104,'老板',0,'老板',1,NULL,2);

/*Table structure for table `t_sys_roletype` */

DROP TABLE IF EXISTS `t_sys_roletype`;

CREATE TABLE `t_sys_roletype` (
  `ROLE_TYPE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色类型ID',
  `ROLE_TYPE_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '角色类型名称',
  `ROLE_TYPE_DESC` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '角色类型描述',
  PRIMARY KEY (`ROLE_TYPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_roletype` */

insert  into `t_sys_roletype`(`ROLE_TYPE_ID`,`ROLE_TYPE_NAME`,`ROLE_TYPE_DESC`) values (104,'普通角色','一般权限'),(105,'超级管理员','最高权限');

/*Table structure for table `t_sys_userinfo` */

DROP TABLE IF EXISTS `t_sys_userinfo`;

CREATE TABLE `t_sys_userinfo` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USER_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户姓名',
  `LOGIN_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '登陆账号',
  `PASSWORD` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `EXPIRED` datetime DEFAULT NULL COMMENT '账号有效期',
  `BE_USE` int(11) DEFAULT NULL COMMENT '是否启用',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '上次密码修改时间',
  `DEPARTMENT_ID` int(11) DEFAULT NULL COMMENT '部门ID',
  `STATE` int(11) DEFAULT NULL COMMENT '用户状态',
  PRIMARY KEY (`USER_ID`),
  KEY `FK_T_SYS_USERINFO_T_SYS_DEPARTMENT_11` (`DEPARTMENT_ID`),
  CONSTRAINT `FK_T_SYS_USERINFO_T_SYS_DEPARTMENT_11` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `t_sys_department` (`DEPARTMENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_userinfo` */

insert  into `t_sys_userinfo`(`USER_ID`,`USER_NAME`,`LOGIN_NAME`,`PASSWORD`,`EXPIRED`,`BE_USE`,`MODIFY_TIME`,`DEPARTMENT_ID`,`STATE`) values (104,'zdf1','zdf1','c4ca4238a0b923820dcc509a6f75849b',NULL,1,'2012-11-01 00:00:00',105,4),(105,'zdf2','zdf2','c4ca4238a0b923820dcc509a6f75849b',NULL,1,'2012-11-01 00:00:00',104,4),(106,'zdf3','zdf3','c4ca4238a0b923820dcc509a6f75849b',NULL,1,'2012-10-31 00:00:00',105,2),(107,'zdf4','zdf4','c4ca4238a0b923820dcc509a6f75849b',NULL,1,'2012-10-31 00:00:00',105,2),(109,'郑迪','zdf5','c4ca4238a0b923820dcc509a6f75849b',NULL,1,'2012-10-31 00:00:00',NULL,2),(110,'郑中','zdf6','c4ca4238a0b923820dcc509a6f75849b',NULL,1,'2012-10-31 00:00:00',NULL,2),(111,'郑好','zdf7',NULL,NULL,1,'2012-10-08 00:00:00',NULL,2),(112,'zdf8','zdf8','c4ca4238a0b923820dcc509a6f75849b',NULL,1,'2012-10-31 00:00:00',NULL,2),(113,'zdf9','zdf9',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(114,'zdf10','zdf10',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(115,'zdf11','zdf11',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(116,'zdf12','zdf12',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(117,'zdf13','zdf13',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(118,'zdf14','zdf14',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(119,'zdf15','zdf15',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(120,'zdf16','zdf16',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(121,'zdf20','zdf20',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(122,'zdf19','zdf19',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(123,'zdf23','zdf23',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(124,'zdf22','zdf22',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(125,'zdf23','zdf23',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(126,'zdf24','zdf24',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(127,'zdf25','zdf25',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(128,'zdf26','zdf26',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(129,'zdf29','zdf29',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(130,'zdf30','zdf30',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(131,'zdf32','zdf32',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(132,'zdf31','zdf31',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(133,'zdf35','zdf35',NULL,NULL,1,'2012-10-09 00:00:00',NULL,2),(134,'zdf36','zdf36',NULL,NULL,1,'2012-10-10 00:00:00',NULL,2),(135,'zdf37','zdf37',NULL,NULL,1,'2012-10-10 00:00:00',NULL,2),(136,'zdf38','zdf38',NULL,NULL,1,'2012-10-10 00:00:00',NULL,2),(137,'zdf39','zdf39',NULL,NULL,1,'2012-10-10 00:00:00',NULL,2);

/*Table structure for table `t_sys_userresource` */

DROP TABLE IF EXISTS `t_sys_userresource`;

CREATE TABLE `t_sys_userresource` (
  `USER_RESOURCE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户资源ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID',
  `RESOURCE_ID` int(11) DEFAULT NULL COMMENT '资源ID',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色ID',
  `FACETYPE` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '模板类型',
  `DESKTOP` int(1) DEFAULT NULL COMMENT '是否显示在桌面',
  `ICON` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '显示图标',
  `SORT_NUM` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`USER_RESOURCE_ID`),
  KEY `FK_USERRESOURCE_RESOURCE_30` (`RESOURCE_ID`),
  KEY `FK_USERRESOURCE_USERINFO_31` (`USER_ID`),
  KEY `FK_USERRESOURCE_ROLE_32` (`ROLE_ID`),
  CONSTRAINT `FK_USERRESOURCE_RESOURCE_30` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_sys_resource` (`RESOURCE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USERRESOURCE_ROLE_32` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_sys_role` (`ROLE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USERRESOURCE_USERINFO_31` FOREIGN KEY (`USER_ID`) REFERENCES `t_sys_userinfo` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=274 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_userresource` */

insert  into `t_sys_userresource`(`USER_RESOURCE_ID`,`USER_ID`,`RESOURCE_ID`,`ROLE_ID`,`FACETYPE`,`DESKTOP`,`ICON`,`SORT_NUM`) values (229,104,1,105,NULL,1,'img/icon/splash_pink.png',1),(230,104,2,105,'permission',1,'img/icon/splash_pink.png',1),(231,104,3,105,'subSystem',1,'img/icon/splash_pink.png',1),(232,104,251,105,'permission',1,'img/icon/splash_pink.png',1),(233,104,260,105,NULL,1,'img/icon/splash_pink.png',1),(234,104,261,105,NULL,1,'img/icon/splash_pink.png',1),(235,104,252,105,'permission',1,'img/icon/splash_pink.png',1),(236,104,262,105,NULL,1,'img/icon/splash_pink.png',1),(237,104,263,105,NULL,1,'img/icon/splash_pink.png',1),(238,104,264,105,NULL,1,'img/icon/splash_pink.png',1),(239,104,265,105,NULL,1,'img/icon/splash_pink.png',1),(241,104,254,105,'permission',1,'img/icon/splash_pink.png',1),(242,104,266,105,NULL,1,'img/icon/splash_pink.png',1),(243,104,267,105,NULL,1,'img/icon/splash_pink.png',1),(244,104,268,105,NULL,1,'img/icon/splash_pink.png',1),(245,104,269,105,NULL,1,'img/icon/splash_pink.png',1),(246,104,255,105,'permission',1,'img/icon/splash_pink.png',1),(247,104,270,105,NULL,1,'img/icon/splash_pink.png',1),(248,104,271,105,NULL,1,'img/icon/splash_pink.png',1),(249,104,272,105,NULL,1,'img/icon/splash_pink.png',1),(250,104,273,105,NULL,1,'img/icon/splash_pink.png',1),(251,104,285,105,NULL,1,'img/icon/splash_pink.png',1),(252,104,286,105,NULL,1,'img/icon/splash_pink.png',1),(253,104,287,105,NULL,1,'img/icon/splash_pink.png',1),(254,104,288,105,NULL,1,'img/icon/splash_pink.png',1),(257,104,253,105,'empty',1,'img/icon/splash_pink.png',1),(258,105,1,104,NULL,1,'img/icon/splash_pink.png',1),(259,105,2,104,'permission',1,'img/icon/splash_pink.png',1),(260,105,288,104,NULL,1,'img/icon/splash_pink.png',1),(261,105,3,104,'subSystem',0,'img/icon/splash_pink.png',1),(262,105,251,104,'permission',0,'img/icon/splash_pink.png',1),(263,105,261,104,NULL,1,'img/icon/splash_pink.png',1),(264,105,252,104,'permission',1,'img/icon/splash_pink.png',1),(265,105,265,104,NULL,1,'img/icon/splash_pink.png',1),(266,105,254,104,'permission',0,'img/icon/splash_pink.png',1),(267,105,269,104,NULL,1,'img/icon/splash_pink.png',1),(268,105,255,104,'permission',0,'img/icon/splash_pink.png',1),(269,105,273,104,NULL,1,'img/icon/splash_pink.png',1),(272,104,318,105,'subSystem',1,'img/icon/splash_pink.png',1),(273,104,319,105,'permission',1,'img/icon/splash_pink.png',1);

/*Table structure for table `t_sys_userrole` */

DROP TABLE IF EXISTS `t_sys_userrole`;

CREATE TABLE `t_sys_userrole` (
  `USER_ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`USER_ROLE_ID`),
  KEY `FK_T_SYS_USERROLE_T_SYS_ROLE_17` (`ROLE_ID`),
  KEY `FK_T_SYS_USERROLE_T_SYS_USERINFO_16` (`USER_ID`),
  CONSTRAINT `FK_T_SYS_USERROLE_T_SYS_ROLE_17` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_sys_role` (`ROLE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_T_SYS_USERROLE_T_SYS_USERINFO_16` FOREIGN KEY (`USER_ID`) REFERENCES `t_sys_userinfo` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_sys_userrole` */

insert  into `t_sys_userrole`(`USER_ROLE_ID`,`USER_ID`,`ROLE_ID`) values (18,104,105),(19,105,104);

/*Table structure for table `t_wf_action` */

DROP TABLE IF EXISTS `t_wf_action`;

CREATE TABLE `t_wf_action` (
  `ACTION_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '动作ID',
  `ACTION_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '动作名称',
  `ACTION_TYPEID` int(11) DEFAULT NULL COMMENT '动作类型',
  `ACTION_FROMSTEP` int(11) DEFAULT NULL COMMENT '上一步骤ID',
  `ACTION_TOSTEP` int(11) DEFAULT NULL COMMENT '下一步骤ID',
  `ACTION_LEFT` int(11) DEFAULT NULL COMMENT '左边距',
  `ACTION_TOP` int(11) DEFAULT NULL COMMENT '上边距',
  `FLOW_ID` int(11) DEFAULT NULL COMMENT '流程ID',
  `XML_ID` int(11) DEFAULT NULL COMMENT '图形上面的ID',
  PRIMARY KEY (`ACTION_ID`),
  KEY `FK_T_WF_ACTION_T_WF_FLOW_14` (`FLOW_ID`),
  KEY `FK_T_WF_ACTION_T_WF_ACTIONTYPE_20` (`ACTION_TYPEID`),
  CONSTRAINT `FK_T_WF_ACTION_T_WF_ACTIONTYPE_20` FOREIGN KEY (`ACTION_TYPEID`) REFERENCES `t_wf_actiontype` (`ACTION_TYPEID`),
  CONSTRAINT `FK_T_WF_ACTION_T_WF_FLOW_14` FOREIGN KEY (`FLOW_ID`) REFERENCES `t_wf_flow` (`FLOW_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_action` */

insert  into `t_wf_action`(`ACTION_ID`,`ACTION_NAME`,`ACTION_TYPEID`,`ACTION_FROMSTEP`,`ACTION_TOSTEP`,`ACTION_LEFT`,`ACTION_TOP`,`FLOW_ID`,`XML_ID`) values (104,'开始请假流程',106,104,106,1,1,104,NULL),(105,'管理员同意',104,106,105,1,1,104,NULL),(106,'管理员否决',105,106,105,2,2,104,NULL),(107,'发起车辆申请',106,107,108,1,1,105,NULL),(108,'同意',104,108,109,1,1,105,NULL),(109,'同意',106,109,110,1,1,105,NULL),(110,'拒绝',106,108,110,1,1,105,NULL),(111,'拒绝',106,109,110,1,1,105,NULL),(112,'发起报销申请',106,111,112,3,3,106,NULL),(113,'大于或者等于',106,112,113,3,3,106,NULL),(114,'小于',106,112,114,3,3,106,NULL),(115,'同意',106,113,115,3,3,106,NULL),(116,'同意',106,114,116,3,3,106,NULL),(117,'同意',106,115,116,3,3,106,NULL),(118,'报销开始',106,117,118,4,4,107,NULL),(119,NULL,106,118,119,NULL,NULL,107,NULL),(120,NULL,106,118,120,NULL,NULL,107,NULL),(121,'同意',106,119,121,NULL,NULL,107,NULL),(122,'同意',106,120,121,NULL,NULL,107,NULL),(123,NULL,106,121,122,NULL,NULL,107,NULL),(128,'同意',NULL,130,131,NULL,NULL,114,5),(129,'请假申请2',NULL,129,130,NULL,NULL,114,4),(130,'同意',NULL,133,134,NULL,NULL,115,5),(131,'请假申请2',NULL,132,133,NULL,NULL,115,4),(132,'同意',NULL,136,137,NULL,NULL,116,5),(133,'请假申请2',NULL,135,136,NULL,NULL,116,4),(134,'t',NULL,139,140,NULL,NULL,117,5),(135,'发起请假5',NULL,138,139,NULL,NULL,117,4);

/*Table structure for table `t_wf_actiontype` */

DROP TABLE IF EXISTS `t_wf_actiontype`;

CREATE TABLE `t_wf_actiontype` (
  `ACTION_TYPEID` int(11) NOT NULL AUTO_INCREMENT COMMENT '动作类型ID',
  `ACTION_TYPENAME` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '动作类型名称',
  PRIMARY KEY (`ACTION_TYPEID`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_actiontype` */

insert  into `t_wf_actiontype`(`ACTION_TYPEID`,`ACTION_TYPENAME`) values (104,'同意'),(105,'否决'),(106,'流动');

/*Table structure for table `t_wf_applycar` */

DROP TABLE IF EXISTS `t_wf_applycar`;

CREATE TABLE `t_wf_applycar` (
  `APPLYCAR_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '车辆申请ID',
  `SQSJ` datetime DEFAULT NULL COMMENT '申请时间',
  `SQRY` int(11) DEFAULT NULL COMMENT '申请人员',
  `SQYY` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '申请原因',
  `CPH` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '车牌号',
  `KSLC` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '开始里程',
  `JSLC` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '结束里程',
  `INSTANCE_ID` int(11) DEFAULT NULL COMMENT '流程实例ID',
  PRIMARY KEY (`APPLYCAR_ID`),
  KEY `FK_APPLYCAR_INSTANCE_42` (`INSTANCE_ID`),
  CONSTRAINT `FK_APPLYCAR_INSTANCE_42` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `t_wf_instance` (`INSTANCE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_applycar` */

insert  into `t_wf_applycar`(`APPLYCAR_ID`,`SQSJ`,`SQRY`,`SQYY`,`CPH`,`KSLC`,`JSLC`,`INSTANCE_ID`) values (8,'2012-09-25 00:00:00',105,'出差了，购物','10011','12','14',90),(9,'2012-09-27 00:00:00',105,'出差','10011','15','16',94),(10,'2012-09-27 00:00:00',106,'出差','10011','14','15',95),(11,'2012-09-27 00:00:00',105,'出差','10012','15','16',96),(12,'2012-09-25 00:00:00',105,'购物','10011','16','17',98);

/*Table structure for table `t_wf_bx` */

DROP TABLE IF EXISTS `t_wf_bx`;

CREATE TABLE `t_wf_bx` (
  `BXID` int(11) NOT NULL AUTO_INCREMENT COMMENT '报销ID',
  `BXRY` int(11) DEFAULT NULL COMMENT '报销人员',
  `BXYY` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '报销原因',
  `BXJE` int(11) DEFAULT NULL COMMENT '报销金额',
  `INSTANCE_ID` int(11) DEFAULT NULL COMMENT '流程实例ID',
  PRIMARY KEY (`BXID`),
  KEY `FK_BX_INSTANCE_44` (`INSTANCE_ID`),
  CONSTRAINT `FK_BX_INSTANCE_44` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `t_wf_instance` (`INSTANCE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_bx` */

insert  into `t_wf_bx`(`BXID`,`BXRY`,`BXYY`,`BXJE`,`INSTANCE_ID`) values (136,105,'购物',5000,91),(137,105,'购物',6000,92),(138,105,'考试',3001,99),(139,105,'请客吃饭',500,100),(140,105,'回扣',2000,101);

/*Table structure for table `t_wf_flow` */

DROP TABLE IF EXISTS `t_wf_flow`;

CREATE TABLE `t_wf_flow` (
  `FLOW_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '流程ID',
  `FLOW_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '流程名称',
  `CREATETIME` datetime DEFAULT NULL COMMENT '流程创建的时间',
  `DELETETIME` datetime DEFAULT NULL COMMENT '流程删除的时间',
  `FLOW_XML` text COLLATE utf8_bin COMMENT '流程数据',
  `STATE` int(11) DEFAULT NULL COMMENT '流程状态值',
  PRIMARY KEY (`FLOW_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_flow` */

insert  into `t_wf_flow`(`FLOW_ID`,`FLOW_NAME`,`CREATETIME`,`DELETETIME`,`FLOW_XML`,`STATE`) values (104,'请假流程','2012-05-08 00:00:00',NULL,NULL,1),(105,'车辆申请流程','2012-05-08 16:35:31',NULL,NULL,1),(106,'报销流程','2012-08-24 12:29:14',NULL,NULL,1),(107,'报销（并行审批）','2012-08-31 17:14:36',NULL,NULL,1),(114,'请假流程2','2012-10-11 16:44:48',NULL,NULL,1),(115,'请假流程3','2012-10-11 16:50:48',NULL,NULL,1),(116,'请假申请4','2012-10-11 17:02:34',NULL,'<?xml version=\"1.0\" encoding=\"UTF-8\"?><flow name=\"请假申请4\">\n  <step id=\"1\" typeId=\"105\" name=\"开始\" formId=\"104\" beforeAction=\"null\" centerAction=\"null\" afterAction=\"null\" g=\"276,125,48,48\"/>\n  <step id=\"2\" typeId=\"106\" name=\"经理审批\" formId=\"105\" roleId=\"105\" beforeAction=\"null\" centerAction=\"null\" afterAction=\"null\" g=\"410,186,100,30\"/>\n  <step id=\"3\" typeId=\"104\" name=\"结束\" formId=\"104\" beforeAction=\"null\" centerAction=\"null\" afterAction=\"null\" g=\"459,371,48,48\"/>\n  <action id=\"5\" name=\"同意\" FromElementID=\"2\" ToElementID=\"3\" g=\"0,0\"/>\n  <action id=\"4\" name=\"请假申请2\" FromElementID=\"1\" ToElementID=\"2\" g=\"0,0\"/>\n</flow>',1),(117,'请假流程5','2012-10-12 15:18:02',NULL,'<?xml version=\"1.0\" encoding=\"UTF-8\"?><flow name=\"请假流程5\">\n  <step id=\"1\" typeId=\"105\" name=\"开始\" formId=\"0\" beforeAction=\"null\" centerAction=\"null\" afterAction=\"null\" g=\"203,111,48,48\"/>\n  <step id=\"2\" typeId=\"106\" name=\"经理审批\" formId=\"0\" roleId=\"0\" beforeAction=\"null\" centerAction=\"null\" afterAction=\"null\" g=\"260,191,100,30\"/>\n  <step id=\"3\" typeId=\"104\" name=\"结束\" formId=\"0\" beforeAction=\"null\" centerAction=\"null\" afterAction=\"null\" g=\"481,312,48,48\"/>\n  <action id=\"5\" name=\"t\" FromElementID=\"2\" ToElementID=\"3\" g=\"0,0\"/>\n  <action id=\"4\" name=\"发起请假5\" FromElementID=\"1\" ToElementID=\"2\" g=\"0,0\"/>\n</flow>',1);

/*Table structure for table `t_wf_form` */

DROP TABLE IF EXISTS `t_wf_form`;

CREATE TABLE `t_wf_form` (
  `FORM_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '表单ID',
  `FORM_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '表单名称',
  `FORM_TYPE` int(11) DEFAULT NULL COMMENT '表单类型',
  `FORM_ADDR` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '表单地址',
  `FORM_DESC` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '字段描述',
  PRIMARY KEY (`FORM_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_form` */

insert  into `t_wf_form`(`FORM_ID`,`FORM_NAME`,`FORM_TYPE`,`FORM_ADDR`,`FORM_DESC`) values (104,'请假表单',1,'biz/wf/T_WF_QJ.js','请假审核时使用的表单'),(105,'请假审核表单',1,'biz/wf/T_WF_QJ_2.js','报销过程中使用的表单'),(106,'车辆申请表单',1,'biz/wf/T_WF_APPLYCAR.js','车辆申请填写的表单'),(107,'车辆管理员审核表单',1,'biz/wf/T_WF_APPLYCAR_1.js',''),(108,'处长审核表单',1,'biz/wf/T_WF_APPLYCAR_2.js',''),(109,'报销申请表',1,'biz/wf/T_WF_BX.js','报销过程中填写表单'),(110,'经理审核表单',1,'biz/wf/T_WF_BX_1.js','经理审核过程的表单'),(111,'老板审核表单',1,'biz/wf/T_WF_BX_2.js','老板审核过程的表单');

/*Table structure for table `t_wf_freeflow` */

DROP TABLE IF EXISTS `t_wf_freeflow`;

CREATE TABLE `t_wf_freeflow` (
  `FREEFLOW_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自由流ID',
  `FROM_STEPID` int(11) DEFAULT NULL COMMENT '起始步骤',
  `TO_STEPID` int(11) DEFAULT NULL COMMENT '结束步骤',
  `OPINION_ID` int(11) DEFAULT NULL COMMENT '轨迹ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID',
  `CREATETIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`FREEFLOW_ID`),
  KEY `FK_FREEFLOW_OPINION_48` (`OPINION_ID`),
  CONSTRAINT `FK_FREEFLOW_OPINION_48` FOREIGN KEY (`OPINION_ID`) REFERENCES `t_wf_opinion` (`OPINION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_freeflow` */

insert  into `t_wf_freeflow`(`FREEFLOW_ID`,`FROM_STEPID`,`TO_STEPID`,`OPINION_ID`,`USER_ID`,`CREATETIME`) values (106,109,108,231,106,'2012-09-24 00:00:00');

/*Table structure for table `t_wf_instance` */

DROP TABLE IF EXISTS `t_wf_instance`;

CREATE TABLE `t_wf_instance` (
  `INSTANCE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '流程实例ID',
  `FLOW_ID` int(11) DEFAULT NULL COMMENT '流程ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '流程发起人',
  `STATE` int(11) DEFAULT NULL COMMENT '流程实例的状态（state=0:流动状态；state=1:完成状态）',
  `CREATETIME` datetime DEFAULT NULL COMMENT '流程开始的时间',
  `STARTTIME` datetime DEFAULT NULL COMMENT '步骤开始时间',
  `ENDTIME` datetime DEFAULT NULL COMMENT '流程结束时间',
  PRIMARY KEY (`INSTANCE_ID`),
  KEY `FK_T_WF_INSTANCE_T_WF_FLOW_33` (`FLOW_ID`),
  CONSTRAINT `FK_T_WF_INSTANCE_T_WF_FLOW_33` FOREIGN KEY (`FLOW_ID`) REFERENCES `t_wf_flow` (`FLOW_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_instance` */

insert  into `t_wf_instance`(`INSTANCE_ID`,`FLOW_ID`,`USER_ID`,`STATE`,`CREATETIME`,`STARTTIME`,`ENDTIME`) values (89,104,104,1,'2012-09-24 09:23:11','2012-09-24 09:23:11','2012-09-24 10:21:41'),(90,105,105,1,'2012-09-24 10:22:52','2012-09-24 10:22:52','2012-09-24 10:23:55'),(91,106,105,1,'2012-09-24 10:32:40','2012-09-24 10:32:40','2012-09-24 10:33:57'),(92,107,105,1,'2012-09-24 10:36:09','2012-09-24 10:36:09','2012-09-24 10:37:01'),(93,104,105,1,'2012-09-24 14:30:42','2012-09-24 14:30:42','2012-10-08 15:31:09'),(94,105,105,1,'2012-09-24 14:42:40','2012-09-24 14:42:40','2012-09-24 14:44:41'),(95,105,106,1,'2012-09-24 14:49:09','2012-09-24 14:49:09','2012-09-24 14:57:16'),(96,105,105,1,'2012-09-24 14:58:55','2012-09-24 14:58:55','2012-09-24 15:03:43'),(97,104,105,1,'2012-09-24 15:05:15','2012-09-24 15:05:15','2012-09-24 15:05:24'),(98,105,105,1,'2012-09-24 15:06:26','2012-09-24 15:06:26','2012-09-24 15:07:20'),(99,106,105,1,'2012-09-24 15:08:08','2012-09-24 15:08:08','2012-09-24 15:10:17'),(100,106,105,1,'2012-09-24 15:09:08','2012-09-24 15:09:08','2012-09-24 15:09:28'),(101,107,105,1,'2012-09-24 15:11:03','2012-09-24 15:11:03','2012-09-24 15:11:37'),(102,104,105,1,'2012-09-24 15:12:39','2012-09-24 15:12:39','2012-10-08 15:32:54'),(105,104,104,1,'2012-09-28 17:46:26','2012-09-28 17:46:26','2012-09-28 17:50:31'),(106,104,106,1,'2012-09-28 17:51:04','2012-09-28 17:51:04','2012-09-28 17:51:24'),(109,104,104,1,'2012-09-29 10:39:05','2012-09-29 10:39:05','2012-09-29 10:45:18'),(110,104,104,1,'2012-09-29 10:43:47','2012-09-29 10:43:47','2012-10-08 15:30:55'),(111,104,105,1,'2012-09-29 10:52:44','2012-09-29 10:52:44','2012-10-08 15:30:42'),(112,104,105,1,'2012-09-29 10:53:55','2012-09-29 10:53:55','2012-10-08 16:02:49'),(113,104,104,1,'2012-10-08 15:21:54','2012-10-08 15:21:54','2012-10-08 15:29:42'),(114,104,105,1,'2012-10-09 11:10:56','2012-10-09 11:10:56','2012-10-09 11:11:47'),(115,104,105,1,'2012-10-09 11:24:00','2012-10-09 11:24:00','2012-10-09 11:42:45'),(116,104,105,1,'2012-10-09 11:43:33','2012-10-09 11:43:33','2012-10-09 13:09:50'),(117,104,105,0,'2012-10-09 18:03:12','2012-10-09 18:03:12',NULL),(118,104,105,1,'2012-10-10 08:39:32','2012-10-10 08:39:32','2012-10-10 08:40:10'),(119,104,105,1,'2012-10-10 09:15:13','2012-10-10 09:15:13','2012-10-10 09:15:36'),(121,115,104,1,'2012-10-11 16:53:01','2012-10-11 16:53:01','2012-10-11 16:53:27');

/*Table structure for table `t_wf_opinion` */

DROP TABLE IF EXISTS `t_wf_opinion`;

CREATE TABLE `t_wf_opinion` (
  `OPINION_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '流程轨迹ID',
  `OPINION_DESC` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '意见描述',
  `OPINION_TIME` datetime DEFAULT NULL COMMENT '意见时间',
  `INSTANCE_ID` int(11) DEFAULT NULL COMMENT '流程实例ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '人员ID',
  `STEP_ID` int(11) DEFAULT NULL COMMENT '步骤ID',
  `ACTION_ID` int(11) DEFAULT NULL COMMENT '动作ID',
  `STATE` int(11) DEFAULT NULL COMMENT '当前状态（0：流程已经经过；1：流程处于当前节点）',
  PRIMARY KEY (`OPINION_ID`),
  KEY `FK_T_WF_OPINION_T_WF_INSTANCE_35` (`INSTANCE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_opinion` */

insert  into `t_wf_opinion`(`OPINION_ID`,`OPINION_DESC`,`OPINION_TIME`,`INSTANCE_ID`,`USER_ID`,`STEP_ID`,`ACTION_ID`,`STATE`) values (201,'启动流程','2012-09-24 09:23:11',89,104,104,104,0),(202,'好的','2012-09-24 10:21:41',89,105,106,105,0),(203,'流程结束','2012-09-24 10:21:41',89,NULL,105,NULL,1),(204,'启动流程','2012-09-24 10:22:52',90,105,107,107,0),(205,'好的','2012-09-24 10:23:11',90,105,108,108,0),(206,'好的','2012-09-24 10:23:55',90,106,109,109,0),(207,'流程结束','2012-09-24 10:23:55',90,NULL,110,NULL,1),(208,'启动流程','2012-09-24 10:32:40',91,105,111,112,0),(209,'流程判断步骤','2012-09-24 10:32:40',91,NULL,112,113,0),(210,'好的','2012-09-24 10:33:21',91,105,113,115,0),(211,'好的','2012-09-24 10:33:57',91,106,115,117,0),(212,'流程结束','2012-09-24 10:33:57',91,NULL,116,NULL,1),(213,'启动流程','2012-09-24 10:36:09',92,105,117,118,0),(214,'并行步骤运行','2012-09-24 10:36:09',92,NULL,118,120,0),(215,'好的','2012-09-24 10:36:30',92,105,119,121,0),(216,' 没有问题','2012-09-24 10:37:01',92,106,120,122,0),(217,'汇合步骤运行','2012-09-24 10:37:01',92,NULL,121,123,0),(218,'流程结束','2012-09-24 10:37:01',92,NULL,122,NULL,1),(219,'启动流程','2012-09-24 14:30:42',93,105,104,104,0),(220,'不太好','2012-10-08 15:31:09',93,106,106,106,0),(221,'启动流程','2012-09-24 14:42:40',94,105,107,107,0),(222,'好的','2012-09-24 14:43:32',94,105,108,108,0),(223,'好的','2012-09-24 14:44:41',94,106,109,109,0),(224,'流程结束','2012-09-24 14:44:41',94,NULL,110,NULL,1),(225,'启动流程','2012-09-24 14:49:09',95,106,107,107,0),(226,'好的','2012-09-24 14:49:42',95,105,108,108,0),(227,'好的','2012-09-24 14:57:16',95,106,109,109,0),(228,'流程结束','2012-09-24 14:57:16',95,NULL,110,NULL,1),(229,'启动流程','2012-09-24 14:58:55',96,105,107,107,0),(230,'厚道','2012-09-24 14:59:22',96,105,108,108,0),(231,'好的','2012-09-24 15:01:22',96,106,109,109,0),(232,'好的','2012-09-24 15:03:14',96,105,108,108,0),(233,'好的','2012-09-24 15:03:42',96,106,109,109,0),(234,'流程结束','2012-09-24 15:03:42',96,NULL,110,NULL,1),(235,'启动流程','2012-09-24 15:05:15',97,105,104,104,0),(236,'好的','2012-09-24 15:05:24',97,105,106,105,0),(237,'流程结束','2012-09-24 15:05:24',97,NULL,105,NULL,1),(238,'启动流程','2012-09-24 15:06:26',98,105,107,107,0),(239,'好的','2012-09-24 15:06:49',98,105,108,108,0),(240,'好的','2012-09-24 15:07:20',98,106,109,109,0),(241,'流程结束','2012-09-24 15:07:20',98,NULL,110,NULL,1),(242,'启动流程','2012-09-24 15:08:08',99,105,111,112,0),(243,'流程判断步骤','2012-09-24 15:08:08',99,NULL,112,113,0),(244,'好的','2012-09-24 15:08:24',99,105,113,115,0),(245,'好的','2012-09-24 15:10:17',99,106,115,117,0),(246,'启动流程','2012-09-24 15:09:08',100,105,111,112,0),(247,'流程判断步骤','2012-09-24 15:09:08',100,NULL,112,114,0),(248,'好的','2012-09-24 15:09:28',100,105,114,116,0),(249,'流程结束','2012-09-24 15:09:28',100,NULL,116,NULL,1),(250,'流程结束','2012-09-24 15:10:17',99,NULL,116,NULL,1),(251,'启动流程','2012-09-24 15:11:03',101,105,117,118,0),(252,'并行步骤运行','2012-09-24 15:11:03',101,NULL,118,120,0),(253,'好的','2012-09-24 15:11:13',101,105,119,121,0),(254,'好的','2012-09-24 15:11:37',101,106,120,122,0),(255,'汇合步骤运行','2012-09-24 15:11:37',101,NULL,121,123,0),(256,'流程结束','2012-09-24 15:11:37',101,NULL,122,NULL,1),(257,'启动流程','2012-09-24 15:12:39',102,105,104,104,0),(258,'好的','2012-10-08 15:32:54',102,105,106,105,0),(263,'启动流程','2012-09-28 17:46:26',105,104,104,104,0),(264,'好的','2012-09-28 17:50:31',105,106,106,105,0),(265,'流程结束','2012-09-28 17:50:31',105,NULL,105,NULL,1),(266,'启动流程','2012-09-28 17:51:04',106,106,104,104,0),(267,'好的','2012-09-28 17:51:23',106,106,106,105,0),(268,'流程结束','2012-09-28 17:51:23',106,NULL,105,NULL,1),(271,'启动流程','2012-09-29 10:39:05',109,104,104,104,0),(272,'很好','2012-09-29 10:45:18',109,105,106,105,0),(273,'启动流程','2012-09-29 10:43:47',110,104,104,104,0),(274,'好的','2012-10-08 15:30:54',110,106,106,105,0),(275,'流程结束','2012-09-29 10:45:18',109,NULL,105,NULL,1),(276,'启动流程','2012-09-29 10:52:44',111,105,104,104,0),(277,'好的','2012-10-08 15:30:42',111,106,106,105,0),(278,'启动流程','2012-09-29 10:53:55',112,105,104,104,0),(279,'好的','2012-10-08 16:02:49',112,105,106,105,0),(280,'启动流程','2012-10-08 15:21:54',113,104,104,104,0),(281,'好的','2012-10-08 15:29:42',113,105,106,105,0),(282,'流程结束','2012-10-08 15:29:42',113,NULL,105,NULL,1),(283,'流程结束','2012-10-08 15:30:42',111,NULL,105,NULL,1),(284,'流程结束','2012-10-08 15:30:54',110,NULL,105,NULL,1),(285,'流程结束','2012-10-08 15:31:09',93,NULL,105,NULL,1),(286,'流程结束','2012-10-08 15:32:54',102,NULL,105,NULL,1),(287,'流程结束','2012-10-08 16:02:49',112,NULL,105,NULL,1),(288,'启动流程','2012-10-09 11:10:56',114,105,104,104,0),(289,'好的','2012-10-09 11:11:47',114,105,106,105,0),(290,'流程结束','2012-10-09 11:11:47',114,NULL,105,NULL,1),(291,'启动流程','2012-10-09 11:24:00',115,105,104,104,0),(292,'好的','2012-10-09 11:42:45',115,105,106,105,0),(293,'流程结束','2012-10-09 11:42:45',115,NULL,105,NULL,1),(294,'启动流程','2012-10-09 11:43:33',116,105,104,104,0),(295,'好的','2012-10-09 13:09:50',116,105,106,105,0),(296,'流程结束','2012-10-09 13:09:50',116,NULL,105,NULL,1),(297,'启动流程','2012-10-09 18:03:12',117,105,104,104,0),(298,NULL,NULL,117,NULL,106,NULL,1),(299,'启动流程','2012-10-10 08:39:32',118,105,104,104,0),(300,'好的','2012-10-10 08:40:10',118,105,106,105,0),(301,'流程结束','2012-10-10 08:40:10',118,NULL,105,NULL,1),(302,'启动流程','2012-10-10 09:15:13',119,105,104,104,0),(303,'好的','2012-10-10 09:15:36',119,105,106,105,0),(304,'流程结束','2012-10-10 09:15:36',119,NULL,105,NULL,1),(305,'启动流程','2012-10-11 16:53:01',121,104,132,131,0),(306,'好的','2012-10-11 16:53:27',121,105,133,130,0),(307,'流程结束','2012-10-11 16:53:27',121,NULL,134,NULL,1);

/*Table structure for table `t_wf_qj` */

DROP TABLE IF EXISTS `t_wf_qj`;

CREATE TABLE `t_wf_qj` (
  `QJID` int(11) NOT NULL AUTO_INCREMENT COMMENT '请假ID',
  `QJSJ` int(11) DEFAULT NULL COMMENT '请假时间',
  `QJRY` int(11) DEFAULT NULL COMMENT '请假人员',
  `QJYY` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '请假原因',
  `INSTANCE_ID` int(11) DEFAULT NULL COMMENT '流程实例ID',
  PRIMARY KEY (`QJID`),
  KEY `FK_QJ_INSTANCE_40` (`INSTANCE_ID`),
  CONSTRAINT `FK_QJ_INSTANCE_40` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `t_wf_instance` (`INSTANCE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_qj` */

insert  into `t_wf_qj`(`QJID`,`QJSJ`,`QJRY`,`QJYY`,`INSTANCE_ID`) values (6,3,104,'休息',89),(7,4,105,'考试',93),(8,4,105,'考试',97),(9,5,105,'休息',102),(10,5,104,'出差了',105),(11,2,106,'旅游了',106),(12,2,104,'休息',109),(13,3,104,'出差',110),(14,3,105,'休息',111),(15,4,105,'旅游',112),(16,3,104,'出差了',113),(17,2,105,'出差',114),(18,3,105,'出差了',115),(19,4,105,'出差',116),(20,3,105,'出差',117),(21,2,105,'在出差',118),(22,4,105,'又出差了',119),(23,3,104,'出差了',121);

/*Table structure for table `t_wf_step` */

DROP TABLE IF EXISTS `t_wf_step`;

CREATE TABLE `t_wf_step` (
  `STEP_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '步骤ID',
  `STEP_NAME` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '步骤名称',
  `STEP_TYPEID` int(11) DEFAULT NULL COMMENT '步骤类型ID',
  `STEP_LEFT` int(11) DEFAULT NULL COMMENT '左边距',
  `STEP_TOP` int(11) DEFAULT NULL COMMENT '上边距',
  `FLOW_ID` int(11) DEFAULT NULL COMMENT '流程ID',
  `XML_ID` int(11) DEFAULT NULL COMMENT '图形上面的ID',
  PRIMARY KEY (`STEP_ID`),
  KEY `FK_T_WF_STEP_T_WF_FLOW_13` (`FLOW_ID`),
  KEY `FK_T_WF_STEP_T_WF_STEPTYPE_19` (`STEP_TYPEID`),
  CONSTRAINT `FK_T_WF_STEP_T_WF_FLOW_13` FOREIGN KEY (`FLOW_ID`) REFERENCES `t_wf_flow` (`FLOW_ID`),
  CONSTRAINT `FK_T_WF_STEP_T_WF_STEPTYPE_19` FOREIGN KEY (`STEP_TYPEID`) REFERENCES `t_wf_steptype` (`STEP_TYPEID`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_step` */

insert  into `t_wf_step`(`STEP_ID`,`STEP_NAME`,`STEP_TYPEID`,`STEP_LEFT`,`STEP_TOP`,`FLOW_ID`,`XML_ID`) values (104,'开始',105,1,1,104,NULL),(105,'结束',104,2,2,104,NULL),(106,'请假审批',106,3,3,104,NULL),(107,'开始',105,1,1,105,NULL),(108,'车辆管理审核',106,2,2,105,NULL),(109,'处长审核',106,3,3,105,NULL),(110,'结束',104,4,4,105,NULL),(111,'开始',105,1,1,106,NULL),(112,'金额是否大于3000',107,2,2,106,NULL),(113,'经理审核1',106,3,3,106,NULL),(114,'经理审核2',106,4,4,106,NULL),(115,'老板审核',106,5,5,106,NULL),(116,'结束',104,6,6,106,NULL),(117,'开始',105,5,5,107,NULL),(118,'并发审批',108,5,5,107,NULL),(119,'经理审核',106,5,5,107,NULL),(120,'老板审核',106,5,5,107,NULL),(121,'汇合审核',109,5,5,107,NULL),(122,'结束',104,5,5,107,NULL),(129,'开始',105,NULL,NULL,114,1),(130,'经理审批',106,NULL,NULL,114,2),(131,'结束',104,NULL,NULL,114,3),(132,'开始',105,NULL,NULL,115,1),(133,'经理审批',106,NULL,NULL,115,2),(134,'结束',104,NULL,NULL,115,3),(135,'开始',105,NULL,NULL,116,1),(136,'经理审批',106,NULL,NULL,116,2),(137,'结束',104,NULL,NULL,116,3),(138,'开始',105,NULL,NULL,117,1),(139,'经理审批',106,NULL,NULL,117,2),(140,'结束',104,NULL,NULL,117,3);

/*Table structure for table `t_wf_stepform` */

DROP TABLE IF EXISTS `t_wf_stepform`;

CREATE TABLE `t_wf_stepform` (
  `STEP_FORM_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '步骤表单ID',
  `STEP_ID` int(11) DEFAULT NULL COMMENT '步骤ID',
  `FORM_ID` int(11) DEFAULT NULL COMMENT '表单ID',
  PRIMARY KEY (`STEP_FORM_ID`),
  KEY `FK_T_WF_STEPFORM_T_WF_FORM_29` (`FORM_ID`),
  KEY `FK_T_WF_STEPFORM_T_WF_STEP_30` (`STEP_ID`),
  CONSTRAINT `FK_T_WF_STEPFORM_T_WF_FORM_29` FOREIGN KEY (`FORM_ID`) REFERENCES `t_wf_form` (`FORM_ID`),
  CONSTRAINT `FK_T_WF_STEPFORM_T_WF_STEP_30` FOREIGN KEY (`STEP_ID`) REFERENCES `t_wf_step` (`STEP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_stepform` */

insert  into `t_wf_stepform`(`STEP_FORM_ID`,`STEP_ID`,`FORM_ID`) values (109,104,104),(110,106,105),(111,107,106),(112,108,107),(113,109,108),(114,111,109),(116,113,110),(117,114,110),(118,115,111),(119,117,109),(120,119,110),(121,120,111),(122,105,104),(123,110,108),(124,116,111),(125,129,104),(126,130,105),(127,131,104),(128,132,104),(129,133,105),(130,134,104),(131,135,104),(132,136,105),(133,137,104);

/*Table structure for table `t_wf_stepfunction` */

DROP TABLE IF EXISTS `t_wf_stepfunction`;

CREATE TABLE `t_wf_stepfunction` (
  `FUN_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '处理ID',
  `FUN_NAME` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'SQLID',
  `FUN_TYPE` int(11) DEFAULT NULL COMMENT '处理类型(1:节点执行动作；2：节点前执行动作；3：节点后执行动作)',
  `STEP_ID` int(11) DEFAULT NULL COMMENT '步骤ID',
  PRIMARY KEY (`FUN_ID`),
  KEY `FK_STEPFUNCTION_STEP_46` (`STEP_ID`),
  CONSTRAINT `FK_STEPFUNCTION_STEP_46` FOREIGN KEY (`STEP_ID`) REFERENCES `t_wf_step` (`STEP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_stepfunction` */

insert  into `t_wf_stepfunction`(`FUN_ID`,`FUN_NAME`,`FUN_TYPE`,`STEP_ID`) values (104,'T_WF_BX.autoAudit',1,112),(105,'T_WF_APPLYCAR.managerAudit',1,109);

/*Table structure for table `t_wf_steprole` */

DROP TABLE IF EXISTS `t_wf_steprole`;

CREATE TABLE `t_wf_steprole` (
  `STEP_ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '步骤角色关系ID',
  `STEP_ID` int(11) DEFAULT NULL COMMENT '步骤ID',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`STEP_ROLE_ID`),
  KEY `FK_T_WF_STEPROLE_T_WF_STEP_26` (`STEP_ID`),
  CONSTRAINT `FK_T_WF_STEPROLE_T_WF_STEP_26` FOREIGN KEY (`STEP_ID`) REFERENCES `t_wf_step` (`STEP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_steprole` */

insert  into `t_wf_steprole`(`STEP_ROLE_ID`,`STEP_ID`,`ROLE_ID`) values (104,106,105),(105,108,106),(106,109,107),(107,113,108),(108,114,108),(109,115,109),(110,119,108),(111,120,109),(112,130,105),(113,133,105),(114,136,105);

/*Table structure for table `t_wf_steptype` */

DROP TABLE IF EXISTS `t_wf_steptype`;

CREATE TABLE `t_wf_steptype` (
  `STEP_TYPEID` int(11) NOT NULL AUTO_INCREMENT COMMENT '步骤类型ID',
  `STEP_TYPENAME` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '步骤类型名称',
  PRIMARY KEY (`STEP_TYPEID`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_wf_steptype` */

insert  into `t_wf_steptype`(`STEP_TYPEID`,`STEP_TYPENAME`) values (104,'结束'),(105,'开始'),(106,'普通步骤'),(107,'判断步骤'),(108,'并发步骤'),(109,'汇合步骤');

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
