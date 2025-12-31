-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 192.168.31.233    Database: surpass
-- ------------------------------------------------------
-- Server version	8.4.4

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `surpass_api_publish`
--

DROP TABLE IF EXISTS `surpass_api_publish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_api_publish` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `api_id` varchar(45) NOT NULL COMMENT 'API ID',
  `version_id` varchar(45) NOT NULL COMMENT '版本ID',
  `publish_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `operator` varchar(100) DEFAULT NULL COMMENT '操作人',
  `description` varchar(500) DEFAULT NULL COMMENT '发布描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='API发布记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_api_publish`
--

LOCK TABLES `surpass_api_publish` WRITE;
/*!40000 ALTER TABLE `surpass_api_publish` DISABLE KEYS */;
INSERT INTO `surpass_api_publish` VALUES ('1202941564486156288','1201980708541693952','1201981113845678080','2025-12-29 09:01:24','admin',NULL),('1202961077923479552','1202960586493657088','1202961051784577024','2025-12-29 10:18:57','admin',NULL);
/*!40000 ALTER TABLE `surpass_api_publish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_api_version`
--

DROP TABLE IF EXISTS `surpass_api_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_api_version` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `api_id` varchar(45) NOT NULL COMMENT 'API ID',
  `version` int NOT NULL COMMENT '版本号',
  `sql_template` text NOT NULL COMMENT 'SQL模板',
  `param_definition` text COMMENT '参数定义',
  `response_template` text COMMENT '响应结构模板',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-草稿，1-待发布，2-已发布，3-下线',
  `description` varchar(500) DEFAULT NULL COMMENT '版本描述',
  `supports_paging` tinyint(1) DEFAULT '0' COMMENT '是否允许分页',
  `page_size_max` int DEFAULT '20' COMMENT '最大分页大小',
  `rate_limit` int DEFAULT NULL COMMENT '限流控制（次/分钟）',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='API版本表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_api_version`
--

LOCK TABLES `surpass_api_version` WRITE;
/*!40000 ALTER TABLE `surpass_api_version` DISABLE KEYS */;
INSERT INTO `surpass_api_version` VALUES ('1201981113845678080','1201980708541693952',1,'select * from surpass_userinfo where username=#{username}','[\n  {\n    \"name\": \"username\",\n    \"type\": \"string\",\n    \"rules\": {},\n    \"description\": \"\"\n  }\n]',NULL,2,NULL,0,20,NULL,'1','2025-12-26 17:24:55','1','2025-12-29 09:01:24','n'),('1202961051784577024','1202960586493657088',1,'SELECT * FROM surpass.surpass_history_login','[\n  {\n    \"name\": \"_pageSize\",\n    \"type\": \"number\",\n    \"rules\": {\n      \"required\": true,\n      \"minValue\": 1,\n      \"maxValue\": 20\n    },\n    \"description\": \"每页条数\",\n    \"readOnly\": true\n  },\n  {\n    \"name\": \"_pageNum\",\n    \"type\": \"number\",\n    \"rules\": {\n      \"required\": true,\n      \"minValue\": 1\n    },\n    \"description\": \"页码\",\n    \"readOnly\": true\n  }\n]',NULL,2,NULL,1,20,NULL,'1','2025-12-29 10:18:50','1','2025-12-29 10:18:57','n');
/*!40000 ALTER TABLE `surpass_api_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_app`
--

DROP TABLE IF EXISTS `surpass_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_app` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `app_name` varchar(100) NOT NULL COMMENT '应用名称',
  `app_code` varchar(50) NOT NULL COMMENT '应用编码',
  `context_path` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '/' COMMENT '应用上下文路径',
  `login_url` varchar(255) DEFAULT NULL COMMENT '登录地址',
  `status` tinyint unsigned DEFAULT '1' COMMENT '状态:1-启用;0-禁用',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='应用表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_app`
--

LOCK TABLES `surpass_app` WRITE;
/*!40000 ALTER TABLE `surpass_app` DISABLE KEYS */;
INSERT INTO `surpass_app` VALUES ('1194279439010103296','开放API测试接口','api_test','/test','/idm',1,'1','2025-12-05 11:21:13','1','2025-12-30 10:27:46','n'),('1195437696353304576','门户管理系统','portal','/portal',NULL,1,'1','2025-12-08 16:03:43','1','2025-12-30 10:26:18','n');
/*!40000 ALTER TABLE `surpass_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_app_resources`
--

DROP TABLE IF EXISTS `surpass_app_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_app_resources` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `classify` varchar(20) NOT NULL COMMENT '类型-menu,api,openApi,button',
  `name` varchar(100) NOT NULL COMMENT '资源名称',
  `path` varchar(200) NOT NULL COMMENT '资源路径',
  `method` varchar(10) DEFAULT NULL COMMENT 'HTTP方法：GET,POST,PUT,DELETE',
  `params` varchar(100) DEFAULT NULL COMMENT '参数',
  `datasource_id` varchar(45) DEFAULT NULL COMMENT '所属数据源ID',
  `app_id` varchar(45) NOT NULL COMMENT '所属应用ID',
  `action_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `permission` varchar(64) DEFAULT NULL COMMENT '权限标识',
  `res_style` varchar(128) DEFAULT NULL COMMENT '样式',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父级ID',
  `is_open` varchar(1) DEFAULT NULL COMMENT '是否开放',
  `is_frame` varchar(1) DEFAULT NULL COMMENT '外部链接',
  `is_cache` varchar(1) DEFAULT NULL COMMENT '是否缓存',
  `is_visible` varchar(1) DEFAULT NULL COMMENT '是否可见',
  `sort_index` int DEFAULT '1' COMMENT '排序序号',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` varchar(45) DEFAULT NULL COMMENT '状态',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='应用-资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_app_resources`
--

LOCK TABLES `surpass_app_resources` WRITE;
/*!40000 ALTER TABLE `surpass_app_resources` DISABLE KEYS */;
INSERT INTO `surpass_app_resources` VALUES ('1201961593504530432','menu','菜单','111','GET',NULL,NULL,'1194279439010103296','r',NULL,NULL,NULL,'y','n','n','y',1,NULL,'1','2025-12-26 08:07:21',NULL,NULL,'1','n'),('1201980708541693952','openApi','测试资源A','/users','GET',NULL,'1189291146937892864','1194279439010103296','r',NULL,NULL,'1201961593504530432','y','n','n','y',1,NULL,'1','2025-12-26 09:23:18','1','2025-12-26 10:02:45','1','n'),('1201982116225613824','menu','cesa','//','GET',NULL,NULL,'1194279439010103296','r',NULL,NULL,'1201961593504530432','y','n','n','y',1,NULL,'1','2025-12-26 09:28:54',NULL,NULL,'1','n'),('1202960586493657088','openApi','用户分页查询','/users/page','GET',NULL,'1189291146937892864','1194279439010103296','r',NULL,NULL,'1201961593504530432','y','n','n','y',1,NULL,'1','2025-12-29 02:16:59',NULL,NULL,'1','n');
/*!40000 ALTER TABLE `surpass_app_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_client`
--

DROP TABLE IF EXISTS `surpass_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_client` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `client_name` varchar(100) NOT NULL COMMENT '客户端名称/用户名',
  `client_id` varchar(128) NOT NULL COMMENT '客户端ID（登录账号）',
  `client_secret` varchar(256) NOT NULL COMMENT '客户端密钥（登录密码）',
  `client_type` tinyint NOT NULL DEFAULT '1' COMMENT '客户端类型：1-内部员工 2-外部合作方 3-系统对接 4-其他',
  `description` varchar(500) DEFAULT NULL COMMENT '描述信息',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `department` varchar(100) DEFAULT NULL COMMENT '所属部门',
  `ip_white_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'IP白名单，多个IP用逗号分隔',
  `access_token_validity` int DEFAULT '7200' COMMENT 'Token有效期(秒)，默认2小时',
  `refresh_token_validity` int DEFAULT '86400' COMMENT '刷新Token有效期(秒)，默认24小时',
  `expire_time` datetime DEFAULT NULL COMMENT '账号过期时间，NULL表示永久有效',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `status` tinyint unsigned DEFAULT '1' COMMENT '状态:1-启用;0-禁用',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户端表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_client`
--

LOCK TABLES `surpass_client` WRITE;
/*!40000 ALTER TABLE `surpass_client` DISABLE KEYS */;
INSERT INTO `surpass_client` VALUES ('1196164797519888384','111','7ce752df66b34d0a8fd91b48b77011fd','$2a$10$HJfXzcv9thcJisIHk0U2WeZdspgigv35pyqoAIpqkoTczeDNalq7q',1,NULL,NULL,NULL,NULL,NULL,'127.0.0.1,',7200,86400,NULL,NULL,NULL,1,'1','2025-12-10 16:12:57','1','2025-12-17 15:44:17','y'),('1198696368193929216','测试客户端','09995e0a9e954535b7f3756c7bc46406','$2a$10$UsQFAgbMDaA89gctV07NRu04ffed221f2c4c5d09e1c1630083bb1bf923b4111dba2a460ce553d63624234667789d362c0f744a0b0b4a3e0c991871e35bf5f80b63ed0a',1,'222','姓名','15619854856','surpass@maxsso.net','CRM_IT测试部','127.0.0.1,0:0:0:0:0:0:0:1,',7200,86400,NULL,'2025-12-30 16:08:14',NULL,1,'1','2025-12-17 15:52:31','1','2025-12-29 17:04:15','n'),('1198696577456144384','测试客户端口2','806ca1b0b01f4d18a116466ac9c8a5dd','$2a$10$nOqtxxzlVBsurNAhBcOkE.073fcaaf811e383d18defd5a3cdf11a37c631b619e9bdbd6747b779709e1a7b94740e1fc3255bb990ecc30b5b4c64c973fa80bd3aae92b66',1,NULL,NULL,NULL,NULL,NULL,NULL,7200,86400,NULL,NULL,NULL,1,'1','2025-12-17 15:53:20','1','2025-12-17 16:48:49','n'),('1198697526945906688','测试客户端3','badc153586024476a39803d9dff9ef07','$2a$10$VU0FK40TUxdGXAqMDpqsbub838d741f8f47469a1ea0513550ac55b710c20b4c463cfa69ea609684f1d27e3c51a75c54e4abed6791d338710a9be629bc6a4cac1c493010c8e7d297042d58d',1,NULL,NULL,NULL,NULL,NULL,NULL,7200,86400,NULL,NULL,NULL,1,'1','2025-12-17 15:57:07','1','2025-12-17 16:09:12','n');
/*!40000 ALTER TABLE `surpass_client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_client_permission`
--

DROP TABLE IF EXISTS `surpass_client_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_client_permission` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `client_id` varchar(45) NOT NULL COMMENT '客户端ID',
  `resource_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源ID',
  `app_id` varchar(45) NOT NULL COMMENT '应用ID',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户端应用关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_client_permission`
--

LOCK TABLES `surpass_client_permission` WRITE;
/*!40000 ALTER TABLE `surpass_client_permission` DISABLE KEYS */;
INSERT INTO `surpass_client_permission` VALUES ('1201980006817857536','1198696368193929216','1201961593504530432','1194279439010103296','1','2025-12-26 17:20:31'),('1201981246322769920','1198696368193929216','1201980708541693952','1194279439010103296','1','2025-12-26 17:25:26');
/*!40000 ALTER TABLE `surpass_client_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_config_email_senders`
--

DROP TABLE IF EXISTS `surpass_config_email_senders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_config_email_senders` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `smtp_host` varchar(45) DEFAULT NULL COMMENT 'SMTP地址',
  `port` int DEFAULT NULL COMMENT '端口',
  `account` varchar(45) DEFAULT NULL COMMENT '账号',
  `credentials` varchar(500) DEFAULT NULL COMMENT '凭证',
  `ssl_switch` int DEFAULT NULL COMMENT 'SSL',
  `sender` varchar(45) DEFAULT NULL COMMENT '发送人',
  `protocol` varchar(45) DEFAULT NULL COMMENT '协议',
  `encoding` varchar(45) DEFAULT NULL COMMENT '编码',
  `status` int DEFAULT NULL COMMENT '状态',
  `book_id` varchar(45) DEFAULT NULL COMMENT '租户编码',
  `description` varchar(45) DEFAULT NULL COMMENT '描述',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='邮箱配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_config_email_senders`
--

LOCK TABLES `surpass_config_email_senders` WRITE;
/*!40000 ALTER TABLE `surpass_config_email_senders` DISABLE KEYS */;
/*!40000 ALTER TABLE `surpass_config_email_senders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_config_login_policy`
--

DROP TABLE IF EXISTS `surpass_config_login_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_config_login_policy` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `session_validity` tinyint unsigned DEFAULT '24' COMMENT '会话时间，默认24小时',
  `token_validity` tinyint unsigned DEFAULT '8' COMMENT '令牌时间，默认8小时',
  `is_first_password_modify` varchar(1) DEFAULT 'Y' COMMENT '首次登录密码修改',
  `captcha` varchar(10) DEFAULT 'NONE' COMMENT '认证端验证码',
  `captcha_mgt` varchar(10) DEFAULT 'NONE' COMMENT '管理端验证码',
  `two_factor` tinyint DEFAULT '0' COMMENT '二次认证方式',
  `is_auto_lock` varchar(1) DEFAULT 'Y' COMMENT '登录失败次数开启自动锁定',
  `lock_interval` tinyint unsigned DEFAULT '30' COMMENT '锁定时间',
  `login_attempts` tinyint unsigned DEFAULT '10' COMMENT '允许登录失败次数，后锁定',
  `password_attempts` tinyint unsigned DEFAULT '10' COMMENT '密码输入错误次数，后验证码',
  `password_attempts_captcha` varchar(1) DEFAULT NULL COMMENT '密码错误次数验证码',
  `terminals` tinyint unsigned DEFAULT '6' COMMENT '端终端数量',
  `scan_code` varchar(10) DEFAULT NULL COMMENT '扫码登录方式 NONE-无，LOCAL-本地，SOCIAL-第三方提供者',
  `is_mobile` varchar(2) DEFAULT 'N' COMMENT '手机验证码登录',
  `is_social` varchar(2) DEFAULT 'N' COMMENT '社交账号登录',
  `redirect_uri` varchar(250) DEFAULT NULL COMMENT '认证端登录后默认跳转地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='登录策略表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_config_login_policy`
--

LOCK TABLES `surpass_config_login_policy` WRITE;
/*!40000 ALTER TABLE `surpass_config_login_policy` DISABLE KEYS */;
INSERT INTO `surpass_config_login_policy` VALUES ('1',24,8,'Y','TEXT','ARITHMETIC',0,'Y',10,10,6,'Y',10,'NONE','N','N','');
/*!40000 ALTER TABLE `surpass_config_login_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_config_password_policy`
--

DROP TABLE IF EXISTS `surpass_config_password_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_config_password_policy` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `min_length` tinyint unsigned DEFAULT '0' COMMENT '最小长度',
  `max_length` tinyint unsigned DEFAULT '0' COMMENT '最大长度',
  `lower_case` tinyint unsigned DEFAULT '0' COMMENT '包含小写字母',
  `upper_case` tinyint unsigned DEFAULT '0' COMMENT '包含大写字母',
  `digits` tinyint unsigned DEFAULT '0' COMMENT '包含数字',
  `special_char` tinyint unsigned DEFAULT '0' COMMENT '特殊字符',
  `attempts` tinyint unsigned DEFAULT '0' COMMENT '登录尝试次数',
  `duration` tinyint unsigned DEFAULT '0' COMMENT '自动解除',
  `expiration` tinyint unsigned DEFAULT '0' COMMENT '密码过期时间',
  `username` tinyint unsigned DEFAULT '0' COMMENT '是否包含登录名称',
  `history` tinyint DEFAULT '0' COMMENT '历史密码次数',
  `dictionary` tinyint DEFAULT NULL COMMENT '简单密码字典',
  `alphabetical` tinyint DEFAULT NULL COMMENT '字母序列策略',
  `numerical` tinyint DEFAULT NULL COMMENT '数字序列策略',
  `qwerty` tinyint DEFAULT NULL COMMENT '键盘策略',
  `occurances` tinyint DEFAULT NULL COMMENT '字符重复次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='密码策略表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_config_password_policy`
--

LOCK TABLES `surpass_config_password_policy` WRITE;
/*!40000 ALTER TABLE `surpass_config_password_policy` DISABLE KEYS */;
INSERT INTO `surpass_config_password_policy` VALUES ('1',6,20,1,0,0,0,6,30,90,0,3,1,1,1,1,3);
/*!40000 ALTER TABLE `surpass_config_password_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_datasource`
--

DROP TABLE IF EXISTS `surpass_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_datasource` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `name` varchar(100) NOT NULL COMMENT '数据源名称',
  `driver_class_name` varchar(200) NOT NULL COMMENT '驱动类名',
  `url` varchar(500) NOT NULL COMMENT '数据库连接URL',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(500) NOT NULL COMMENT '加密后的密码',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `test_sql` varchar(200) DEFAULT 'SELECT 1' COMMENT '测试连接SQL',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='数据源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_datasource`
--

LOCK TABLES `surpass_datasource` WRITE;
/*!40000 ALTER TABLE `surpass_datasource` DISABLE KEYS */;
INSERT INTO `surpass_datasource` VALUES ('1','默认数据源','com.mysql.cj.jdbc.Driver','jdbc:mysql://localhost:3306/auto_apis','root','111',1,'SELECT 1',NULL,'2025-11-18 11:00:12','1','2025-12-29 17:22:28','n'),('1189291146937892864','233','com.mysql.cj.jdbc.Driver','jdbc:mysql://192.168.31.233:3306/surpass','surpass','Surpass321!',1,'SELECT 1','1','2025-11-21 16:59:31',NULL,NULL,'n'),('1189982408527577088','auto_apis','com.mysql.cj.jdbc.Driver','jdbc:mysql://www.zgllh.site:3306/auto_apis?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true','auto_apis','auto_apis',1,'SELECT 1','1','2025-11-23 14:46:21','1','2025-12-29 12:33:13','n');
/*!40000 ALTER TABLE `surpass_datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_history_login`
--

DROP TABLE IF EXISTS `surpass_history_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_history_login` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `category` tinyint DEFAULT NULL COMMENT '类型 1登录，2注销',
  `session_id` varchar(45) DEFAULT NULL COMMENT '会话标识',
  `style` varchar(10) DEFAULT NULL COMMENT '样式',
  `user_id` varchar(45) NOT NULL COMMENT '用户ID',
  `username` varchar(200) NOT NULL COMMENT '登录名称',
  `display_name` varchar(45) DEFAULT NULL COMMENT '姓名',
  `message` varchar(200) DEFAULT NULL COMMENT '状态',
  `ip_addr` varchar(200) DEFAULT NULL COMMENT '访问地址',
  `country` varchar(200) DEFAULT NULL COMMENT '国家',
  `province` varchar(200) DEFAULT NULL COMMENT '省',
  `city` varchar(200) DEFAULT NULL COMMENT '市',
  `location` varchar(500) DEFAULT NULL COMMENT '归属地',
  `login_type` varchar(45) DEFAULT NULL COMMENT '登录方式',
  `code` varchar(45) DEFAULT NULL COMMENT '代码',
  `provider` varchar(45) DEFAULT NULL COMMENT '提供商',
  `browser` varchar(45) DEFAULT NULL COMMENT '浏览器',
  `platform` varchar(45) DEFAULT NULL COMMENT '平台',
  `application` varchar(45) DEFAULT NULL COMMENT '应用',
  `device_id` varchar(200) DEFAULT NULL COMMENT '设备编码',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_history_login`
--

LOCK TABLES `surpass_history_login` WRITE;
/*!40000 ALTER TABLE `surpass_history_login` DISABLE KEYS */;
INSERT INTO `surpass_history_login` VALUES ('1188098069216886784',1,'1188098068344471552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,NULL,'2025-11-18 09:58:39'),('1189276651435851776',1,'1189276651184193536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,NULL,'2025-11-21 16:01:55'),('1189290607357460480',1,'1189290606921252864','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,NULL,'2025-11-21 16:57:22'),('1189292884487045120',1,'1189292884168278016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,NULL,'2025-11-21 17:06:25'),('1189294406583189504',1,'1189294406352502784','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,NULL,'2025-11-21 17:12:28'),('1189976363184947200',1,'1189976362656464896','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-23 14:22:19'),('1189987261177921536',1,'1189987259877687296','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-23 15:05:38'),('1190011381676507136',1,'1190011381227716608','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-23 16:41:28'),('1190025471094423552',1,'1190025470561746944','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-23 17:37:28'),('1190033194775216128',1,'1190033194280288256','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-23 18:08:09'),('1190055365954240512',1,'1190055365388009472','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-23 19:36:15'),('1190061298973409280',1,'1190061297408933888','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-23 19:59:50'),('1190269612265046016',1,'1190269611971444736','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-24 09:47:35'),('1190279035800780800',1,'1190279035704311808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-24 10:25:02'),('1190345094012600320',1,'1190345093643501568','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-24 14:47:32'),('1191822043889270784',1,'1191822043541143552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-11-28 16:36:24'),('1193175629894254592',1,'1193175629567098880','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-02 10:15:04'),('1193238485864022016',1,'1193238485713027072','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-02 14:24:50'),('1193245643326357504',1,'1193245643028561920','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-02 14:53:16'),('1193888200565719040',1,'1193888200012070912','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-04 09:26:34'),('1193891190248833024',1,'1193891189858762752','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-04 09:38:27'),('1193902246794887168',1,'1193902246287376384','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-04 10:22:23'),('1193913909845688320',1,'1193913909728247808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-04 11:08:44'),('1194000492208848896',1,'1194000491957190656','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-04 16:52:46'),('1194002184434352128',1,'1194002184061059072','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-04 16:59:30'),('1194003643263614976',1,'1194003642844184576','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-04 17:05:18'),('1194007860804059136',1,'1194007860569178112','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-04 17:22:03'),('1194008964052811776',1,'1194008963801153536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-04 17:26:26'),('1194013875666681856',1,'1194013875205308416','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-04 17:45:57'),('1194278832039788544',1,'1194278831771353088','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-05 11:18:48'),('1194645480760410112',1,'1194645480198373376','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-06 11:35:44'),('1194648554874863616',1,'1194648554392518656','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-06 11:47:57'),('1194655384418123776',1,'1194655383944167424','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-06 12:15:05'),('1194662483655131136',1,'1194662483353141248','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-06 12:43:17'),('1194675697126211584',1,'1194675696639672320','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-06 13:35:48'),('1195015705855000576',1,'1195015705389432832','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-07 12:06:52'),('1195346574092271616',1,'1195346573760921600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-08 10:01:37'),('1195348362598023168',1,'1195348362270867456','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-08 10:08:44'),('1195354623372689408',1,'1195354623062310912','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-08 10:33:36'),('1195433187132571648',1,'1195433186901884928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-08 15:45:48'),('1195786549845819392',1,'1195786548759494656','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-09 15:09:56'),('1195786576433512448',1,'1195786576374792192','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-09 15:10:02'),('1195832418582396928',1,'1195832418443984896','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-09 18:12:12'),('1196076453905563648',1,'1196076453741985792','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-10 10:21:54'),('1196157137944838144',1,'1196157137688985600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-10 15:42:31'),('1196158701610729472',1,'1196158701405208576','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-10 15:48:44'),('1196164189631021056',1,'1196164189341614080','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-10 16:10:32'),('1196164662719152128',1,'1196164662475882496','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-10 16:12:25'),('1196443499239571456',1,'1196443498971136000','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-11 10:40:25'),('1196461492023590912',1,'1196461491830652928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-11 11:51:54'),('1196520256906985472',1,'1196520256693075968','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-11 15:45:25'),('1196794738410258432',1,'1196794738171183104','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-12 09:56:07'),('1196823272751104000',1,'1196823272528805888','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-12 11:49:30'),('1196888740111319040',1,'1196888739981295616','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-12 16:09:38'),('1198248899421143040',1,'1198248899073015808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-16 10:14:26'),('1198324402433818624',1,'1198324402001805312','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-16 15:14:27'),('1198361498313818112',1,'1198361498083131392','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-16 17:41:51'),('1198362198867443712',1,'1198362198414458880','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-16 17:44:38'),('1198614534831472640',1,'1198614534663700480','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-17 10:27:20'),('1198625063297875968',1,'1198625062924582912','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-17 11:09:10'),('1198638329239699456',1,'1198638328891572224','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-17 12:01:53'),('1198680972741050368',1,'1198680972267094016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-17 14:51:20'),('1198683308200820736',1,'1198683307852693504','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-17 15:00:37'),('1198692829698719744',1,'1198692829551919104','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 15:38:27'),('1198697089895235584',1,'1198697089756823552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 15:55:23'),('1198699302688391168',1,'1198699302554173440','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 16:04:10'),('1198705611466866688',1,'1198705611315871744','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 16:29:14'),('1198706402126725120',1,'1198706401971535872','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 16:32:23'),('1198706835964559360',1,'1198706835763232768','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-17 16:34:06'),('1198707925170782208',1,'1198707925036564480','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 16:38:26'),('1198710504474804224',1,'1198710504323809280','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 16:48:41'),('1198718970069254144',1,'1198718969930842112','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 17:22:19'),('1198727597903577088',1,'1198727597735804928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,NULL,'2025-12-17 17:56:36'),('1199038945095057408',1,'1199038944797261824','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-18 14:33:47'),('1199085516339806208',1,'1199085516000067584','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-18 17:38:51'),('1199089537700069376',1,'1199089537519714304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-18 17:54:49'),('1199347263218909184',1,'1199347262891753472','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 10:58:56'),('1199351681024786432',1,'1199351680823459840','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 11:16:29'),('1199352548213915648',1,'1199352547874177024','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 11:19:56'),('1199355152545349632',1,'1199355152264331264','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 11:30:17'),('1199360129967849472',1,'1199360129745551360','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 11:50:04'),('1199360968161755136',1,'1199360967968817152','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 11:53:23'),('1199417726783717376',1,'1199417726645305344','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 15:38:56'),('1199429174494756864',1,'1199429174205349888','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 16:24:25'),('1199437174844424192',1,'1199437174542434304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 16:56:13'),('1199454969141395456',1,'1199454968654856192','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-19 18:06:55'),('1200490051344007168',1,'1200490051163652096','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-22 14:39:58'),('1200500088850350080',1,'1200500088812601344','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-22 15:19:51'),('1200534767204827136',1,'1200534767003500544','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-22 17:37:39'),('1200782065801363456',1,'1200782065537122304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-23 10:00:20'),('1200800261749080064',1,'1200800261530976256','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-23 11:12:38'),('1200866679261757440',1,'1200866679114956800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-23 15:36:33'),('1200893276618489856',1,'1200893276308111360','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-23 17:22:14'),('1201137605744263168',1,'1201137605291278336','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-24 09:33:07'),('1201156342757195776',1,'1201156342467788800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-24 10:47:34'),('1201227215300198400',1,'1201227215065317376','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-24 15:29:11'),('1201240595738132480',1,'1201240595683606528','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-24 16:22:22'),('1201259933178265600',1,'1201259932993716224','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-24 17:39:12'),('1201263959659773952',1,'1201263959370366976','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-24 17:55:12'),('1201267632066527232',1,'1201267631722594304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-24 18:09:48'),('1201505590686253056',1,'1201505590354903040','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 09:55:21'),('1201509901357023232',1,'1201509901122142208','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 10:12:29'),('1201510936477696000',1,'1201510936167317504','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 10:16:36'),('1201519186598166528',1,'1201519186426200064','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 10:49:23'),('1201520648287944704',1,'1201520648027897856','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 10:55:11'),('1201523943601602560',1,'1201523943236698112','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 11:08:17'),('1201526139042922496',1,'1201526138854178816','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 11:17:00'),('1201605565130735616',1,'1201605564962963456','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 16:32:37'),('1201620444134768640',1,'1201620444000550912','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 17:31:45'),('1201623520241516544',1,'1201623520077938688','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-25 17:43:58'),('1201865243869839360',1,'1201865243593015296','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 09:44:29'),('1201878965787033600',1,'1201878965619261440','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 10:39:01'),('1201945258653384704',1,'1201945258506584064','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 15:02:26'),('1201958468181819392',1,'1201958467569451008','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 15:54:56'),('1201961504555925504',1,'1201961504149078016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 16:07:00'),('1201961896928870400',1,'1201961896446525440','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 16:08:33'),('1201967348345470976',1,'1201967347955400704','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 16:30:13'),('1201979815582760960',1,'1201979815419183104','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 17:19:45'),('1201987830247587840',1,'1201987830100787200','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 17:51:36'),('1201988321664827392',1,'1201988321463500800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-26 17:53:33'),('1202941291264999424',1,'1202941291059478528','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 09:00:19'),('1202954885562105856',1,'1202954885427888128','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 09:54:20'),('1202959829748940800',1,'1202959829597945856','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 10:13:59'),('1202963146222862336',2,'1202959829597945856','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 10:27:10'),('1202964299568381952',1,'1202964299522244608','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 10:31:45'),('1202978123725930496',1,'1202978123696570369',NULL,'1202978123696570368','maxkey','not exist','user not exist','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','用户名或密码无效.',NULL,'Edg/123','Windows NT 10.0',NULL,NULL,'2025-12-29 11:26:41'),('1202978149504122880',1,'1202978149034360832','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,NULL,'2025-12-29 11:26:47'),('1202996166136954880',1,'1202996165998542848','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 12:38:22'),('1203024500124286976',1,'1203024499826491392','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 14:30:58'),('1203041824378191872',1,'1203041824231391232','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 15:39:48'),('1203047591126761472',1,'1203047590971572224','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 16:02:43'),('1203054079434031104',1,'1203054079031377920','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 16:28:30'),('1203057330296455168',1,'1203057330137071616','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 16:41:25'),('1203060969396240384',1,'1203060969366880256','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 16:55:53'),('1203062742781526016',1,'1203062742630531072','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 17:02:55'),('1203067480679907328',1,'1203067480528912384','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-29 17:21:45'),('1203113416785395712',1,'1203113416282079232','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,NULL,'2025-12-29 20:24:17'),('1203121046262120448',1,'1203121045775581184','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,NULL,'2025-12-29 20:54:36'),('1203140311283400704',1,'1203140310746529792','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,NULL,'2025-12-29 22:11:09'),('1203312332562759680',1,'1203312332231409664','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 09:34:42'),('1203315678145150976',1,'1203315677675388928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 09:48:00'),('1203319138701803520',1,'1203319138508865536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 10:01:45'),('1203319174118506496',1,'1203319173812322304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 10:01:53'),('1203321935807643648',1,'1203321935463710720','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 10:12:52'),('1203322206868733952',1,'1203322206721933312','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,NULL,'2025-12-30 10:13:56'),('1203324400590389248',1,'1203324400124821504','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,NULL,'2025-12-30 10:22:39'),('1203325812976123904',1,'1203325812799963136','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 10:28:16'),('1203327891459276800',2,'1203325812799963136','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 10:36:32'),('1203327938221572096',1,'1203327938024439808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 10:36:43'),('1203328104538308608',1,'1203328104475394048','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,NULL,'2025-12-30 10:37:23'),('1203392533514682368',1,'1203392532826816512','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,NULL,'2025-12-30 14:53:24'),('1203392819830456320',1,'1203392819033538560','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,NULL,'2025-12-30 14:54:32'),('1203393244465987584',1,'1203393244419850240','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 14:56:13'),('1203394101253242880',1,'1203394100959641600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 14:59:37'),('1203394123088789504',2,'1203394100959641600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 14:59:43'),('1203394231608016896',1,'1203394231536713728','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,NULL,'2025-12-30 15:00:08'),('1203394842147684352',1,'1203394842051215360','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 15:02:34'),('1203404693112356864',1,'1203404692873281536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 15:41:43'),('1203412168922365952',2,'1203404692873281536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 16:11:25'),('1203412211507134464',1,'1203412211460997120','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 16:11:35'),('1203513141212741632',1,'1203513140654899200','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 22:52:39'),('1203517284740497408',1,'1203517284417536000','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 23:09:07'),('1203520719405711360',1,'1203520716058656768','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 23:22:45'),('1203521361754980352',1,'1203521361331355648','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 23:25:19'),('1203523831944183808',1,'1203523831549919232','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 23:35:08'),('1203525103229337600',1,'1203525102843461632','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 23:40:11'),('1203527590032179200',1,'1203527589612748800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 23:50:04'),('1203529137730355200',1,'1203529137327702016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-30 23:56:13'),('1203536356987371520',1,'1203536356601495552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 00:24:54'),('1203537765543706624',1,'1203537765145247744','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 00:30:30'),('1203540073547563008',1,'1203540073153298432','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 00:39:40'),('1203541850804191232',1,'1203541850367983616','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 00:46:44'),('1203542702113685504',2,'1203541850367983616','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 00:50:07'),('1203542714998587392',1,'1203542714700791808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 00:50:10'),('1203675226978648064',1,'1203675226634715136','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 09:36:43'),('1203676429758234624',1,'1203676429611433984','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 09:41:30'),('1203679021494173696',1,'1203679021208961024','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,NULL,'2025-12-31 09:51:48'),('1983381774041997314',1,'1180879335964278784','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-29 11:53:59'),('1983436208784547841',1,'1180933770287513600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-29 15:30:17'),('1983437050191409153',1,'1180934612151435264','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-29 15:33:38'),('1983457252517953538',1,'1180954814587076608',NULL,'1','admin','系统管理员','password error','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-29 16:53:55'),('1983457270561849346',1,'1180954832584835072','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-29 16:53:59'),('1983459045264179202',1,'1180956607224217600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-29 17:01:02'),('1983459753971421185',1,'1180957315734437888','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-29 17:03:51'),('1983705022491283458',1,'1181202584476581888','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-30 09:18:27'),('1983707484434485249',1,'1181205046398812160','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-30 09:28:14'),('1983708384452530177',1,'1181205946332872704','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,NULL,'2025-10-30 09:31:49');
/*!40000 ALTER TABLE `surpass_history_login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_history_openapi`
--

DROP TABLE IF EXISTS `surpass_history_openapi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_history_openapi` (
  `id` varchar(45) NOT NULL COMMENT 'id',
  `request_id` varchar(45) NOT NULL,
  `request_uri` varchar(200) NOT NULL COMMENT 'request_uri',
  `request_method` varchar(10) NOT NULL DEFAULT 'get' COMMENT 'request_method',
  `ip_addr` varchar(200) DEFAULT NULL,
  `country` varchar(200) DEFAULT NULL,
  `province` varchar(200) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `location` varchar(500) DEFAULT NULL,
  `authned` varchar(10) NOT NULL DEFAULT 'n' COMMENT 'authned',
  `access` varchar(10) DEFAULT 'n',
  `client_id` varchar(45) DEFAULT NULL COMMENT 'client_id',
  `app_id` varchar(45) DEFAULT NULL COMMENT 'app_id',
  `app_name` varchar(100) DEFAULT NULL COMMENT 'appname',
  `resource_id` varchar(45) DEFAULT NULL COMMENT 'resource_id',
  `resource_name` varchar(100) DEFAULT NULL COMMENT 'resource_name',
  `resource_uri` varchar(200) DEFAULT NULL COMMENT 'resource_uri',
  `access_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'accesstime',
  `access_cost` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_ho_appid` (`app_id`),
  KEY `idx_ho_resourceid` (`resource_id`),
  KEY `idx_ho_accesstime` (`access_time` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='openapi接口访问日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_history_openapi`
--

LOCK TABLES `surpass_history_openapi` WRITE;
/*!40000 ALTER TABLE `surpass_history_openapi` DISABLE KEYS */;
INSERT INTO `surpass_history_openapi` VALUES ('1203083390962630656','1203083390832607232','/test/users','GET','1',NULL,'四川省',NULL,NULL,'y','y',NULL,NULL,NULL,NULL,NULL,'/users','2025-12-29 18:24:58',31),('1203083613080387584','1203083613000695808','/test/users','GET','1',NULL,'贵州省',NULL,NULL,'y','y','09995e0a9e954535b7f3756c7bc46406',NULL,NULL,NULL,NULL,'/users','2025-12-29 18:25:51',19),('1203085462646816768','1203085462487433216','/test/users','GET','1',NULL,'云南省',NULL,NULL,'y','y','09995e0a9e954535b7f3756c7bc46406','1194279439010103296','测试应用-1','1201980708541693952','测试资源A','/users','2025-12-29 18:33:12',39),('1203311468519358464','1203311468309643264','/test/users','GET','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','y','y','09995e0a9e954535b7f3756c7bc46406','1194279439010103296','测试应用-1','1201980708541693952','测试资源A','/users','2025-12-30 09:31:16',51),('1203315071720095744','1203315071208390656','/test/users','GET','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','y','y','09995e0a9e954535b7f3756c7bc46406','1194279439010103296','测试应用-1','1201980708541693952','测试资源A','/users','2025-12-30 09:45:35',123),('1203411402857906176','1203411402543333376','/test/users','GET','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','y','y','09995e0a9e954535b7f3756c7bc46406','1194279439010103296','开放API测试接口','1201980708541693952','测试资源A','/users','2025-12-30 16:08:22',76);
/*!40000 ALTER TABLE `surpass_history_openapi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_history_system_logs`
--

DROP TABLE IF EXISTS `surpass_history_system_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_history_system_logs` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `topic` varchar(100) NOT NULL COMMENT '主题',
  `target_id` varchar(45) DEFAULT NULL COMMENT '目标ID',
  `target_name` varchar(200) DEFAULT NULL COMMENT '目标名称',
  `cipher_text` varchar(500) DEFAULT NULL COMMENT '密文',
  `message` varchar(200) DEFAULT NULL COMMENT '内容',
  `message_action` varchar(45) DEFAULT NULL COMMENT '内容动作',
  `message_result` varchar(45) DEFAULT NULL COMMENT '内容结果',
  `user_id` varchar(45) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(45) DEFAULT NULL COMMENT '登录名称',
  `display_name` varchar(45) DEFAULT NULL COMMENT '显示名称',
  `execute_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_history_system_logs`
--

LOCK TABLES `surpass_history_system_logs` WRITE;
/*!40000 ALTER TABLE `surpass_history_system_logs` DISABLE KEYS */;
INSERT INTO `surpass_history_system_logs` VALUES ('1189276791420747776','resource',NULL,NULL,NULL,'API管理[MENU]','create','success','1','admin','系统管理员','2025-11-21 08:02:28'),('1189276993816887296','resource',NULL,NULL,NULL,'API[MENU]','create','success','1','admin','系统管理员','2025-11-21 08:03:17'),('1189277193994240000','resource',NULL,NULL,NULL,'数据源[MENU]','create','success','1','admin','系统管理员','2025-11-21 08:04:04'),('1189277409447247872','resource',NULL,NULL,NULL,'调试[MENU]','create','success','1','admin','系统管理员','2025-11-21 08:04:56'),('1189277602074853376','resource',NULL,NULL,NULL,'发布记录[MENU]','create','success','1','admin','系统管理员','2025-11-21 08:05:42'),('1189277746581209088','resource',NULL,NULL,NULL,'版本[MENU]','create','success','1','admin','系统管理员','2025-11-21 08:06:16'),('1189278023837286400','resource',NULL,NULL,NULL,'版本样例[MENU]','create','success','1','admin','系统管理员','2025-11-21 08:07:22'),('1189279465285353472','resource',NULL,NULL,NULL,'API[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:13:06'),('1189280121622626304','resource',NULL,NULL,NULL,'版本[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:15:42'),('1189280162689056768','resource',NULL,NULL,NULL,'发布记录[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:15:52'),('1189280239591620608','resource',NULL,NULL,NULL,'数据源[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:16:11'),('1189280254686920704','resource',NULL,NULL,NULL,'发布记录[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:16:14'),('1189280299985403904','resource',NULL,NULL,NULL,'版本[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:16:25'),('1189280324190732288','resource',NULL,NULL,NULL,'调试[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:16:31'),('1189280343165763584','resource',NULL,NULL,NULL,'版本样例[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:16:35'),('1189280399906308096','resource',NULL,NULL,NULL,'API管理[MENU]','update','success','1','admin','系统管理员','2025-11-21 08:16:49'),('1189992563902251008','resource',NULL,NULL,NULL,'接口定义[MENU]','update','success','1','admin','系统管理员','2025-11-23 07:26:42'),('1189992596710096896','resource',NULL,NULL,NULL,'数据源管理[MENU]','update','success','1','admin','系统管理员','2025-11-23 07:26:50'),('1189993469460873216','resource',NULL,NULL,NULL,'版本管理[MENU]','create','success','1','admin','系统管理员','2025-11-23 07:30:18'),('1189993507385769984','resource',NULL,NULL,NULL,'版本[MENU]','update','success','1','admin','系统管理员','2025-11-23 07:30:27'),('1190029300233928704','resource',NULL,NULL,NULL,'[1189278023690485760]','delete','success','1','admin','系统管理员','2025-11-23 09:52:40'),('1190029396669366272','resource',NULL,NULL,NULL,'接口调试[MENU]','update','success','1','admin','系统管理员','2025-11-23 09:53:03'),('1190029434401325056','resource',NULL,NULL,NULL,'接口配置[MENU]','update','success','1','admin','系统管理员','2025-11-23 09:53:12'),('1191822932356104192','resource',NULL,NULL,NULL,'应用管理[MENU]','create','success','1','admin','系统管理员','2025-11-28 08:39:56'),('1195433298935939072','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-08 07:46:14'),('1195786883813081088','resource',NULL,NULL,NULL,'客户端[MENU]','create','success','1','admin','系统管理员','2025-12-09 07:11:15'),('1195787017435217920','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-09 07:11:47'),('1195787060644937728','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-09 07:11:58'),('1195787079880015872','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-09 07:12:02'),('1195787231256641536','resource',NULL,NULL,NULL,'应用[MENU]','create','success','1','admin','系统管理员','2025-12-09 07:12:38'),('1195787280493576192','resource',NULL,NULL,NULL,'应用[MENU]','update','success','1','admin','系统管理员','2025-12-09 07:12:50'),('1195787307836243968','resource',NULL,NULL,NULL,'应用[MENU]','update','success','1','admin','系统管理员','2025-12-09 07:12:56'),('1195787332570054656','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-09 07:13:02'),('1195787344918085632','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-09 07:13:05'),('1196795486485348352','resource',NULL,NULL,NULL,'客户端[MENU]','update','success','1','admin','系统管理员','2025-12-12 01:59:05'),('1198249385247375360','resource',NULL,NULL,NULL,'新建资源[MENU]','create','success','1','admin','系统管理员','2025-12-16 02:16:21'),('1198615806343118848','resource',NULL,NULL,NULL,'接口配置[MENU]','update','success','1','admin','系统管理员','2025-12-17 02:32:23'),('1201138888265957376','resource',NULL,NULL,NULL,'平台资源管理[MENU]','update','success','1','admin','系统管理员','2025-12-24 01:38:13'),('1201138907471675392','resource',NULL,NULL,NULL,'平台资源[MENU]','update','success','1','admin','系统管理员','2025-12-24 01:38:17'),('1201138986861461504','resource',NULL,NULL,NULL,'平台权限[MENU]','update','success','1','admin','系统管理员','2025-12-24 01:38:36'),('1201228326862061568','resource',NULL,NULL,NULL,'客户端[MENU]','update','success','1','admin','系统管理员','2025-12-24 07:33:36'),('1201980454241042432','resource',NULL,NULL,NULL,'客户端[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:22:18'),('1201988682916036608','resource',NULL,NULL,NULL,'用户管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:55:00'),('1201988711563132928','resource',NULL,NULL,NULL,'角色管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:55:06'),('1201988872607629312','resource',NULL,NULL,NULL,'用户管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:55:45'),('1201988896506773504','resource',NULL,NULL,NULL,'角色管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:55:50'),('1201988963556917248','resource',NULL,NULL,NULL,'客户端[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:56:06'),('1201988986508148736','resource',NULL,NULL,NULL,'用户管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:56:12'),('1201989116036644864','resource',NULL,NULL,NULL,'角色管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:56:43'),('1201989280327532544','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 09:57:22'),('1201990608491642880','resource',NULL,NULL,NULL,'应用[MENU]','update','success','1','admin','系统管理员','2025-12-26 10:02:39'),('1201990803740688384','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-26 10:03:25'),('1201990873085116416','resource',NULL,NULL,NULL,'[1195787231126618112]','delete','success','1','admin','系统管理员','2025-12-26 10:03:42'),('1202997378303393792','resource',NULL,NULL,NULL,'资源授权[BUTTON]','create','success','1','admin','系统管理员','2025-12-29 04:43:11'),('1202997518216986624','resource',NULL,NULL,NULL,'资源授权[BUTTON]','update','success','1','admin','系统管理员','2025-12-29 04:43:45'),('1202997546390126592','resource',NULL,NULL,NULL,'资源授权[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:43:51'),('1203000150004334592','resource',NULL,NULL,NULL,'接口配置[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:54:12'),('1203000212952449024','resource',NULL,NULL,NULL,'发布记录[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:54:27'),('1203000255524634624','resource',NULL,NULL,NULL,'版本[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:54:37'),('1203000288135348224','resource',NULL,NULL,NULL,'接口调试[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:54:45'),('1203000389062885376','resource',NULL,NULL,NULL,'数据源管理[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:55:09'),('1203000504586600448','resource',NULL,NULL,NULL,'数据源管理[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:55:37'),('1203000540095578112','resource',NULL,NULL,NULL,'[1189276791173283840]','delete','success','1','admin','系统管理员','2025-12-29 04:55:45'),('1203000758300049408','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:56:37'),('1203000867310010368','resource',NULL,NULL,NULL,'应用配置[MENU]','create','success','1','admin','系统管理员','2025-12-29 04:57:03'),('1203001082096123904','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:57:54'),('1203001143702061056','resource',NULL,NULL,NULL,'资源授权[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:58:09'),('1203001161502687232','resource',NULL,NULL,NULL,'应用配置[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:58:13'),('1203001302477438976','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:58:47'),('1203001358202961920','resource',NULL,NULL,NULL,'资源授权[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:59:00'),('1203001381942722560','resource',NULL,NULL,NULL,'应用配置[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:59:06'),('1203001479724531712','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-29 04:59:29'),('1203038413213663232','resource',NULL,NULL,NULL,'版本[MENU]','update','success','1','admin','系统管理员','2025-12-29 07:26:15'),('1203038513835016192','resource',NULL,NULL,NULL,'发布记录[MENU]','update','success','1','admin','系统管理员','2025-12-29 07:26:39'),('1203038530591260672','resource',NULL,NULL,NULL,'接口调试[MENU]','update','success','1','admin','系统管理员','2025-12-29 07:26:43'),('1203067554608709632','resource',NULL,NULL,NULL,'数据源[MENU]','update','success','1','admin','系统管理员','2025-12-29 09:22:03'),('1203126793347792896','resource',NULL,NULL,NULL,'仪表盘[MENU]','update','success','1','admin','系统管理员','2025-12-29 13:17:26'),('1203126893906231296','resource',NULL,NULL,NULL,'仪表盘[MENU]','update','success','1','admin','系统管理员','2025-12-29 13:17:50'),('1203322146785329152','resource',NULL,NULL,NULL,'OpenApi访问日志[MENU]','create','success','1','admin','系统管理员','2025-12-30 02:13:42'),('1203410855476068352','resource',NULL,NULL,NULL,'接口日志[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:06:12'),('1203412769861271552','resource',NULL,NULL,NULL,'登录策略[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:13:48'),('1203412811472961536','resource',NULL,NULL,NULL,'登录策略[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:13:58'),('1203412827855912960','resource',NULL,NULL,NULL,'密码策略[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:14:02'),('1203413163823857664','resource',NULL,NULL,NULL,'数据源[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:15:22'),('1203413441411284992','resource',NULL,NULL,NULL,'系统配置[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:16:28'),('1203413894417088512','resource',NULL,NULL,NULL,'组织[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:18:16'),('1203413947516977152','resource',NULL,NULL,NULL,'组织[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:18:29'),('1203414019164078080','resource',NULL,NULL,NULL,'客户端[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:18:46'),('1203414044145352704','resource',NULL,NULL,NULL,'用户管理[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:18:52'),('1203414059609751552','resource',NULL,NULL,NULL,'角色管理[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:18:56'),('1203414078547034112','resource',NULL,NULL,NULL,'应用管理[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:19:00'),('1203414098352537600','resource',NULL,NULL,NULL,'系统配置[MENU]','update','success','1','admin','系统管理员','2025-12-30 08:19:05'),('1983437147415375874','resource','','','','[1879553833064067074]','delete','success','1','admin','系统管理员','2025-10-29 07:34:01'),('1983437176989413377','resource','','','','[1881535430596153345]','delete','success','1','admin','系统管理员','2025-10-29 07:34:08'),('1983437186141384705','resource','','','','[1881535629171281921]','delete','success','1','admin','系统管理员','2025-10-29 07:34:10'),('1983437195570180097','resource','','','','[1888073658178420737]','delete','success','1','admin','系统管理员','2025-10-29 07:34:13'),('1983437231418896386','resource','','','','[1869692874272862209]','delete','success','1','admin','系统管理员','2025-10-29 07:34:21'),('1983437240881246210','resource','','','','[1881534934875557889]','delete','success','1','admin','系统管理员','2025-10-29 07:34:23'),('1983437257310334977','resource','','','','[1886366126259052545]','delete','success','1','admin','系统管理员','2025-10-29 07:34:27'),('1983437264486789121','resource','','','','[1886384073945915394]','delete','success','1','admin','系统管理员','2025-10-29 07:34:29'),('1983437272032342018','resource','','','','[1886384309938429954]','delete','success','1','admin','系统管理员','2025-10-29 07:34:31'),('1983437279028441090','resource','','','','[1886384516205912065]','delete','success','1','admin','系统管理员','2025-10-29 07:34:32'),('1983437285651247105','resource','','','','[1891486309700673537]','delete','success','1','admin','系统管理员','2025-10-29 07:34:34'),('1983437292014006273','resource','','','','[1903024792422047745]','delete','success','1','admin','系统管理员','2025-10-29 07:34:36'),('1983437314994597889','resource','','','','[981334321270882304]','delete','success','1','admin','系统管理员','2025-10-29 07:34:41'),('1983437330186366977','resource','','','','[1917421261886033922]','delete','success','1','admin','系统管理员','2025-10-29 07:34:45'),('1983437336511377409','resource','','','','[1917421313257869313]','delete','success','1','admin','系统管理员','2025-10-29 07:34:46'),('1983437342173687810','resource','','','','[1917421497123573762]','delete','success','1','admin','系统管理员','2025-10-29 07:34:48'),('1983437357910716417','resource','','','','[1872485556229599233]','delete','success','1','admin','系统管理员','2025-10-29 07:34:51'),('1983437366299324417','resource','','','','[1879423541940375554]','delete','success','1','admin','系统管理员','2025-10-29 07:34:53'),('1983437373253480450','resource','','','','[1920446221202178049]','delete','success','1','admin','系统管理员','2025-10-29 07:34:55'),('1983437378836099074','resource','','','','[1911261101908295681]','delete','success','1','admin','系统管理员','2025-10-29 07:34:56'),('1983437387899990018','resource','','','','[1911018836640149506]','delete','success','1','admin','系统管理员','2025-10-29 07:34:58'),('1983437424684036098','resource','','','','[1874027145762447361]','delete','success','1','admin','系统管理员','2025-10-29 07:35:07'),('1983437459861663746','resource','','','','[1899369820127911938]','delete','success','1','admin','系统管理员','2025-10-29 07:35:16'),('1983437468170579969','resource','','','','[981623658751459329]','delete','success','1','admin','系统管理员','2025-10-29 07:35:18'),('1983437475657412609','resource','','','','[1902625741973843969]','delete','success','1','admin','系统管理员','2025-10-29 07:35:19'),('1983437482238275585','resource','','','','[1913072049310191618]','delete','success','1','admin','系统管理员','2025-10-29 07:35:21'),('1983437489532170242','resource','','','','[1889594633392771074]','delete','success','1','admin','系统管理员','2025-10-29 07:35:23'),('1983437501615960065','resource','','','','[1887317090379808769]','delete','success','1','admin','系统管理员','2025-10-29 07:35:26'),('1983437512596647938','resource','','','','[981336810628055040]','delete','success','1','admin','系统管理员','2025-10-29 07:35:28'),('1983437865543135234','resource','','','','[1881633896221446146]','delete','success','1','admin','系统管理员','2025-10-29 07:36:52'),('1983437900674625538','resource','','','','[1894665979168575489]','delete','success','1','admin','系统管理员','2025-10-29 07:37:01'),('1983437924523438081','resource','','','','[1889966284907286529]','delete','success','1','admin','系统管理员','2025-10-29 07:37:06'),('1983437943120982018','resource','','','','[1895302065003790337]','delete','success','1','admin','系统管理员','2025-10-29 07:37:11'),('1983437954114252801','resource','','','','[1917420357065609218]','delete','success','1','admin','系统管理员','2025-10-29 07:37:13'),('1983437964331577346','resource','','','','[1890934113406619650]','delete','success','1','admin','系统管理员','2025-10-29 07:37:16'),('1983437975022858241','resource','','','','[1886357455563137026]','delete','success','1','admin','系统管理员','2025-10-29 07:37:18'),('1983438045730435074','resource','','','','[1915219176348123138]','delete','success','1','admin','系统管理员','2025-10-29 07:37:35'),('1983457322999037954','resource','','','','[1899760631214723073]','delete','success','1','admin','系统管理员','2025-10-29 08:54:11'),('1983457336844435458','resource','','','','[981334814802051072]','delete','success','1','admin','系统管理员','2025-10-29 08:54:15'),('1983720132635119617','resource','','','','[981336354157756416]','delete','success','1','admin','系统管理员','2025-10-30 02:18:30'),('1983720163786215425','resource','','','','[981336403415662592]','delete','success','1','admin','系统管理员','2025-10-30 02:18:37'),('1983720314416254977','resource','','','','[981336054843834368]','delete','success','1','admin','系统管理员','2025-10-30 02:19:13'),('1983720339703713794','resource','','','','[981336254564007936]','delete','success','1','admin','系统管理员','2025-10-30 02:19:19');
/*!40000 ALTER TABLE `surpass_history_system_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_organizations`
--

DROP TABLE IF EXISTS `surpass_organizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_organizations` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `org_code` varchar(45) DEFAULT NULL COMMENT '组织代码',
  `org_name` varchar(100) NOT NULL COMMENT '组织名称',
  `full_name` varchar(200) DEFAULT NULL COMMENT '全称',
  `type` varchar(45) DEFAULT NULL COMMENT '类型',
  `level` int unsigned DEFAULT NULL COMMENT '等级',
  `parent_id` varchar(45) DEFAULT NULL COMMENT '父级ID',
  `parent_code` varchar(45) DEFAULT NULL COMMENT '父级代码',
  `parent_name` varchar(45) DEFAULT NULL COMMENT '父级名称',
  `code_path` varchar(500) DEFAULT NULL COMMENT 'id路径',
  `name_path` varchar(400) DEFAULT NULL COMMENT '名称路径',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `status` tinyint unsigned DEFAULT NULL COMMENT '状态',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `postal_code` varchar(45) DEFAULT NULL COMMENT '邮政编码',
  `phone` varchar(200) DEFAULT NULL COMMENT '手机号码',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `sort_index` int unsigned DEFAULT '0' COMMENT '排序序号',
  `division` varchar(45) DEFAULT NULL COMMENT '分支机构',
  `country` varchar(45) DEFAULT NULL COMMENT '国家',
  `region` varchar(45) DEFAULT NULL COMMENT '省',
  `locality` varchar(45) DEFAULT NULL COMMENT '市',
  `street` varchar(45) DEFAULT NULL COMMENT '街道',
  `has_child` varchar(45) DEFAULT NULL COMMENT 'haschild',
  `contact` varchar(45) DEFAULT NULL COMMENT '联系人',
  `email` varchar(45) DEFAULT NULL COMMENT '电子邮箱',
  `ldap_dn` varchar(1000) DEFAULT NULL COMMENT 'ldapdn',
  `extra_attrs` text COMMENT '扩展字段',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='组织部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_organizations`
--

LOCK TABLES `surpass_organizations` WRITE;
/*!40000 ALTER TABLE `surpass_organizations` DISABLE KEYS */;
/*!40000 ALTER TABLE `surpass_organizations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_permission`
--

DROP TABLE IF EXISTS `surpass_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_permission` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `role_id` varchar(50) NOT NULL COMMENT '组ID',
  `app_id` varchar(45) DEFAULT '1',
  `resource_id` varchar(50) NOT NULL COMMENT '资源ID',
  `category` varchar(2) DEFAULT '1' COMMENT '1 平台资源权限 2 应用资源权限',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='权限-组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_permission`
--

LOCK TABLES `surpass_permission` WRITE;
/*!40000 ALTER TABLE `surpass_permission` DISABLE KEYS */;
INSERT INTO `surpass_permission` VALUES ('1','ROLE_ADMINISTRATORS','1','981334679749656576','1',NULL,'2025-10-29 16:56:31',1),('1189278055013548033','ROLE_ADMINISTRATORS','1','1189276791173283840','1','1','2025-11-21 16:07:29',9),('1189278055013548034','ROLE_ADMINISTRATORS','1','1189276993674280960','1','1','2025-11-21 16:07:29',1),('1189278055013548035','ROLE_ADMINISTRATORS','1','1189277193906159616','1','1','2025-11-21 16:07:29',1),('1189278055013548036','ROLE_ADMINISTRATORS','1','1189277409334001664','1','1','2025-11-21 16:07:29',1),('1189278055013548037','ROLE_ADMINISTRATORS','1','1189277601965801472','1','1','2025-11-21 16:07:29',1),('1189278055013548038','ROLE_ADMINISTRATORS','1','1189277746438602752','1','1','2025-11-21 16:07:29',1),('1189278055013548039','ROLE_ADMINISTRATORS','1','1189278023690485760','1','1','2025-11-21 16:07:29',9),('1189994526580670467','ROLE_ADMINISTRATORS','1','1189993468827533312','1','1','2025-11-23 15:34:30',1),('1191822966241886217','ROLE_ADMINISTRATORS','1','1191822932163166208','1','1','2025-11-28 16:40:03',1),('1195787476438876169','ROLE_ADMINISTRATORS','1','1195786883515285504','1','1','2025-12-09 15:13:35',1),('1195787476438876170','ROLE_ADMINISTRATORS','1','1195787231126618112','1','1','2025-12-09 15:13:35',9),('1198249436677931018','ROLE_ADMINISTRATORS','1','1198249385020882944','1','1','2025-12-16 10:16:34',9),('1202997422087733256','ROLE_ADMINISTRATORS','1','1202997377175126016','1','1','2025-12-29 12:43:16',1),('1203000942350303240','ROLE_ADMINISTRATORS','1','1203000865514848256','1','1','2025-12-29 12:57:15',1),('1203322197494464537','ROLE_ADMINISTRATORS','1','1203322146487533568','1','1','2025-12-30 10:13:54',1),('1203675305877700608','ROLE_ADMINISTRATORS','1194279439010103296','1201980708541693952','2','1','2025-12-31 09:37:02',9),('1983459810168594434','ROLE_ADMINISTRATORS','1','981331493802475520','1','1','2025-10-29 17:04:05',1),('1983459810168594436','ROLE_ADMINISTRATORS','1','981335758977630208','1','1','2025-10-29 17:04:05',1),('1983459810168594437','ROLE_ADMINISTRATORS','1','981335810039087104','1','1','2025-10-29 17:04:05',1),('1983459810168594442','ROLE_ADMINISTRATORS','1','981336473196298240','1','1','2025-10-29 17:04:05',1),('1983459810168594443','ROLE_ADMINISTRATORS','1','981336523834130432','1','1','2025-10-29 17:04:05',1),('1983459810168594445','ROLE_ADMINISTRATORS','1','981568925764419584','1','1','2025-10-29 17:04:05',1),('1983459810168594446','ROLE_ADMINISTRATORS','1','981570045970743296','1','1','2025-10-29 17:04:05',1),('1983459810168594447','ROLE_ADMINISTRATORS','1','981569048993071104','1','1','2025-10-29 17:04:05',1),('1983459810168594448','ROLE_ADMINISTRATORS','1','1010265410274066432','1','1','2025-10-29 17:04:05',1),('1983459810168594449','ROLE_ADMINISTRATORS','1','1028746543856877568','1','1','2025-10-29 17:04:05',1),('1983459810168594450','ROLE_ADMINISTRATORS','1','981569201816731648','1','1','2025-10-29 17:04:05',1),('1983459810168594451','ROLE_ADMINISTRATORS','1','981623658751459328','1','1','2025-10-29 17:04:05',1),('1983459810168594453','ROLE_ADMINISTRATORS','1','981334866064834560','1','1','2025-10-29 17:04:05',1),('1983459810168594454','ROLE_ADMINISTRATORS','1','981337003041751040','1','1','2025-10-29 17:04:05',1),('1983459810168594455','ROLE_ADMINISTRATORS','1','981337094406275072','1','1','2025-10-29 17:04:05',1),('1983459810168594456','ROLE_ADMINISTRATORS','1','981337181773627392','1','1','2025-10-29 17:04:05',1),('2','ROLE_ADMINISTRATORS','1','981337246718230528','1',NULL,'2025-10-29 16:57:35',1),('3','ROLE_ADMINISTRATORS','1','981337555771326464','1',NULL,'2025-10-29 16:58:08',1);
/*!40000 ALTER TABLE `surpass_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_resources`
--

DROP TABLE IF EXISTS `surpass_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_resources` (
  `id` varchar(50) NOT NULL COMMENT '资源编码',
  `res_name` varchar(200) NOT NULL COMMENT '资源名称',
  `i18n` varchar(100) DEFAULT NULL COMMENT '前端国际化标识',
  `classify` varchar(20) DEFAULT NULL COMMENT '类型',
  `permission` varchar(64) DEFAULT NULL COMMENT '权限标识',
  `request_url` varchar(250) DEFAULT NULL COMMENT '地址',
  `request_method` varchar(20) DEFAULT NULL COMMENT '动作',
  `params` varchar(100) DEFAULT NULL COMMENT '参数',
  `action_type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `icon_selected` varchar(100) DEFAULT NULL COMMENT '图标选中',
  `res_style` varchar(128) DEFAULT NULL COMMENT '样式',
  `is_open` varchar(1) DEFAULT NULL COMMENT '是否开放',
  `is_frame` varchar(1) DEFAULT NULL COMMENT '外部链接',
  `is_cache` varchar(1) DEFAULT NULL COMMENT '是否缓存',
  `is_visible` varchar(1) DEFAULT NULL COMMENT '是否可见',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父级ID',
  `parent_name` varchar(200) DEFAULT NULL COMMENT '父级名称',
  `sort_index` int DEFAULT '1' COMMENT '排序序号',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` varchar(45) DEFAULT NULL COMMENT '状态',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_resources`
--

LOCK TABLES `surpass_resources` WRITE;
/*!40000 ALTER TABLE `surpass_resources` DISABLE KEYS */;
INSERT INTO `surpass_resources` VALUES ('1010265410274066432','权限管理-用户','mxk.menu.permissions.privileges','MENU','1010265410274066432','/permissions/apps/permissionuser','GET',NULL,'r',NULL,NULL,'anticon-carry-out','n','n','n','n','981569048993071104','权限管理',1,NULL,NULL,NULL,NULL,NULL,'1','n'),('1028746543856877568','权限管理-组织','mxk.menu.permissions.privileges','MENU','1028746543856877568','/permissions/apps/permissionorg','GET',NULL,'r',NULL,NULL,'anticon-carry-out','n','n','n','n','981569048993071104','权限管理',2,NULL,'1','2024-09-04 00:32:35','1','2024-09-04 00:32:57','1','n'),('1189276993674280960','接口配置','mxk.menu.api','MENU','1189276993674280960','/api/Api','GET',NULL,'r',NULL,NULL,'api','n','n','n','n','1191822932163166208','API管理',1,NULL,'1','2025-11-21 08:03:17','1','2025-12-29 04:54:12','1','n'),('1189277193906159616','数据源','mxk.menu.dataSource','MENU','1189277193906159616','/api/DataSource','GET',NULL,'r',NULL,NULL,'database','n','n','n','y','981334679749656576','API管理',2,NULL,'1','2025-11-21 08:04:04','1','2025-12-30 08:15:22','1','n'),('1189277409334001664','接口调试','mxk.menu.debug','MENU','1189277409334001664','/api/Debug','GET',NULL,'r',NULL,NULL,'control','n','n','n','n','1191822932163166208','API管理',5,NULL,'1','2025-11-21 08:04:56','1','2025-12-29 07:26:42','1','n'),('1189277601965801472','发布记录','mxk.menu.publish','MENU','1189277601965801472','/api/Publish','GET',NULL,'r',NULL,NULL,'history','n','n','n','n','1191822932163166208','API管理',3,NULL,'1','2025-11-21 08:05:42','1','2025-12-29 07:26:38','1','n'),('1189277746438602752','版本','mxk.menu.version','MENU','1189277746438602752','/api/Version','GET',NULL,'r',NULL,NULL,'job','n','n','n','n','1191822932163166208','API管理',4,NULL,'1','2025-11-21 08:06:16','1','2025-12-29 07:26:14','0','n'),('1189993468827533312','版本管理','版本管理','MENU','1189993468827533312','/api/Version','GET',NULL,'r',NULL,NULL,'delivered-procedure','n','n','n','n','1189276993674280960','接口定义',1,NULL,'1','2025-11-23 07:30:18',NULL,NULL,'1','n'),('1191822932163166208','应用管理','mxk.menu.app','MENU','1191822932163166208','/','GET',NULL,'r',NULL,NULL,'api','n','n','n','y','1','配置管理',5,NULL,'1','2025-11-28 08:39:56','1','2025-12-30 08:19:00','1','n'),('1195786883515285504','客户端','mxk.menu.client','MENU','1195786883515285504','/app/client/index','GET',NULL,'r',NULL,NULL,'password2','n','n','n','y','1','应用管理',2,NULL,'1','2025-12-09 07:11:15','1','2025-12-30 08:18:46','1','n'),('1198249385020882944','新建资源','mxk.menu.new-resource','MENU','1198249385020882944','/app/app-manage/apiBinding','GET',NULL,'r',NULL,NULL,NULL,'n','n','n','n','1195787231126618112','应用',1,NULL,'1','2025-12-16 02:16:21',NULL,NULL,'1','n'),('1202997377175126016','资源授权','资源授权','MENU','1202997377175126016','/app/app-manage/apiBinding','GET',NULL,'r',NULL,NULL,NULL,'n','n','n','n','1191822932163166208','应用管理',1,NULL,'1','2025-12-29 04:43:11','1','2025-12-29 04:59:00','1','n'),('1203000865514848256','应用配置','应用配置','MENU','1203000865514848256','/app/app-manage/index','GET',NULL,'r',NULL,NULL,'appstore-add','n','n','n','y','1191822932163166208','应用管理',1,NULL,'1','2025-12-29 04:57:03','1','2025-12-29 04:59:06','1','n'),('1203322146487533568','接口日志','mxk.menu.openapi.logs','MENU','1203322146487533568','/audit/audit-openapi-logs','GET',NULL,'r',NULL,NULL,'log','n','n','n','y','981334866064834560','日志审计',4,NULL,'1','2025-12-30 02:13:42','1','2025-12-30 08:06:12','1','n'),('981331493802475520','仪表盘','mxk.menu.home','MENU','981331493802475520','/index','GET',NULL,'r','',NULL,'anticon-home','n','n','n','y','1','MaxKey管理系统',1,NULL,NULL,NULL,'1','2025-12-29 13:17:49','1','n'),('981334679749656576','系统配置','mxk.menu.permissions','MENU','981334679749656576','','GET',NULL,'r',NULL,NULL,'anticon-radar-chart','n','n','n','y','1','MaxKey管理系统',6,NULL,NULL,NULL,'1','2025-12-30 08:19:05','1','n'),('981334866064834560','日志审计','mxk.menu.audit','MENU','981334866064834560','','GET',NULL,'r',NULL,NULL,'anticon-history','n','n','n','y','1','MaxKey管理系统',11,NULL,NULL,NULL,'1','2025-04-29 19:27:06','1','n'),('981335709019275264','组织','mxk.menu.organizations','MENU','981335709019275264','/idm/organizations','GET',NULL,'r',NULL,NULL,'anticon-cluster','n','n','n','n','981334679749656576','身份管理',1,NULL,NULL,NULL,'1','2025-12-30 08:18:29','0','n'),('981335758977630208','用户管理','mxk.menu.users','MENU','981335758977630208','/idm/users','GET',NULL,'r',NULL,NULL,'anticon-user','n','n','n','y','1','身份管理',3,NULL,NULL,NULL,'1','2025-12-30 08:18:52','1','n'),('981335810039087104','角色管理','mxk.menu.access.groups','MENU','981335810039087104','/idm/groups','GET',NULL,'r',NULL,NULL,'anticon-group','n','n','n','y','1','身份管理',4,NULL,NULL,NULL,'1','2025-12-30 08:18:56','1','n'),('981336473196298240','登录策略','mxk.menu.security.configloginpolicy','MENU','981336473196298240','/security/configloginpolicy','GET',NULL,'r',NULL,NULL,'anticon-file-protect','n','n','n','n','981334679749656576','安全配置',9,NULL,NULL,NULL,'1','2025-12-30 08:13:58','0','n'),('981336523834130432','密码策略','mxk.menu.security.passwordpolicy','MENU','981336523834130432','/security/passwordpolicy','GET',NULL,'r',NULL,NULL,'anticon-file-protect','n','n','n','n','981334679749656576','安全配置',10,NULL,NULL,NULL,'1','2025-12-30 08:14:02','0','n'),('981337003041751040','登录日志','mxk.menu.audit.logins','MENU','981337003041751040','/audit/audit-logins','GET',NULL,'r',NULL,NULL,'anticon-audit','n','n','n','y','981334866064834560','日志审计',1,NULL,NULL,NULL,'1','2024-12-19 01:48:33','1','n'),('981337094406275072','同步器日志','mxk.menu.audit.synchronizer','MENU','981337094406275072','/audit/audit-synchronizer','GET',NULL,'r',NULL,NULL,'anticon-audit','n','n','n','y','981334866064834560','日志审计',3,NULL,NULL,NULL,'1','2025-05-05 18:10:49','0','n'),('981337181773627392','系统日志','mxk.menu.audit.operate','MENU','981337181773627392','/audit/audit-system-logs','GET',NULL,'r',NULL,NULL,'anticon-audit','n','n','n','y','981334866064834560','日志审计',2,NULL,NULL,NULL,'1','2025-02-16 03:35:23','1','n'),('981337246718230528','平台资源','mxk.menu.permissions.resources','MENU','981337246718230528','/permissions/apps/resources','GET',NULL,'r',NULL,NULL,'anticon-read','n','n','n','y','981334679749656576','权限管理',3,NULL,NULL,NULL,'1','2025-12-24 01:38:17','1','n'),('981337555771326464','平台权限','mxk.menu.permissions.openapi','MENU','981337555771326464','/permissions/apps/permission','GET',NULL,'r',NULL,NULL,'anticon-carry-out','n','n','n','y','981334679749656576','权限管理',4,NULL,NULL,NULL,'1','2025-12-24 01:38:36','1','n'),('981568925764419584','角色管理','mxk.menu.permissions.roles','MENU','981568925764419584','/permissions/apps/roles','GET',NULL,'r',NULL,NULL,'anticon-carry-out','n','n','n','n','981337246718230528','资源管理',3,NULL,NULL,NULL,NULL,NULL,'1','n'),('981569048993071104','权限管理','mxk.menu.permissions.privileges','MENU','981569048993071104','/permissions/apps/permission','GET',NULL,'r',NULL,NULL,'anticon-carry-out','n','n','n','n','981337246718230528','资源管理',2,NULL,NULL,NULL,NULL,NULL,'1','n'),('981569201816731648','角色成员管理','mxk.menu.permissions.rolemembers','MENU','981569201816731648','/permissions/apps/rolemembers','GET',NULL,'r',NULL,NULL,'anticon-carry-out','n','n','n','n','981337246718230528','资源管理',4,NULL,NULL,NULL,NULL,NULL,'1','n'),('981570045970743296','权限授权角色',NULL,'MENU','981570045970743296',NULL,'GET',NULL,'r',NULL,NULL,NULL,'n','n','n','n','981568925764419584','角色管理',1,NULL,NULL,NULL,NULL,NULL,'1','n'),('981623658751459328','资源管理','mxk.menu.permissions.resources.resources','MENU','981623658751459328','/permissions/apps/resources','GET',NULL,'r',NULL,NULL,'anticon-read','n','n','n','n','981337246718230528','应用资源',1,NULL,NULL,NULL,'1','2025-02-17 23:48:56','1','n');
/*!40000 ALTER TABLE `surpass_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_role_member`
--

DROP TABLE IF EXISTS `surpass_role_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_role_member` (
  `ID` varchar(100) NOT NULL DEFAULT '' COMMENT 'ID',
  `role_id` varchar(100) NOT NULL COMMENT '组ID',
  `member_id` varchar(100) NOT NULL COMMENT '成员ID：用户ID或者组ID',
  `type` varchar(45) NOT NULL COMMENT '类型：用户  , USER-DYNAMIC 或者岗位',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_role_member`
--

LOCK TABLES `surpass_role_member` WRITE;
/*!40000 ALTER TABLE `surpass_role_member` DISABLE KEYS */;
INSERT INTO `surpass_role_member` VALUES ('622183103330254848','ROLE_ADMINISTRATORS','1','USER',NULL,'2020-12-12 11:03:10');
/*!40000 ALTER TABLE `surpass_role_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_roles`
--

DROP TABLE IF EXISTS `surpass_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_roles` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `role_code` varchar(45) DEFAULT NULL COMMENT '组编码',
  `role_name` varchar(100) NOT NULL COMMENT '组名称',
  `category` varchar(20) NOT NULL DEFAULT 'general' COMMENT '类型',
  `pattern` varchar(10) NOT NULL DEFAULT 'static' COMMENT '模型 静态-static，动态-dynamic',
  `filters` text COMMENT '过滤条件SQL',
  `org_ids_list` text COMMENT '机构列表',
  `status` tinyint unsigned DEFAULT NULL COMMENT '状态',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `isdefault` tinyint unsigned DEFAULT NULL COMMENT '是否默认',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_roles`
--

LOCK TABLES `surpass_roles` WRITE;
/*!40000 ALTER TABLE `surpass_roles` DISABLE KEYS */;
INSERT INTO `surpass_roles` VALUES ('ROLE_ADMINISTRATORS','1000','系统管理员组','supervisor','static',NULL,'',1,'admin',0,NULL,'1','2025-01-17 18:07:55','系统管理员组','n');
/*!40000 ALTER TABLE `surpass_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_session_list`
--

DROP TABLE IF EXISTS `surpass_session_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_session_list` (
  `id` varchar(45) NOT NULL COMMENT 'ID',
  `session_id` varchar(45) NOT NULL COMMENT '会话标识',
  `style` varchar(10) NOT NULL COMMENT '风格',
  `user_id` varchar(45) NOT NULL COMMENT '用户ID',
  `username` varchar(200) NOT NULL COMMENT '应用名称',
  `display_name` varchar(45) DEFAULT NULL COMMENT '显示名称',
  `message` varchar(200) DEFAULT NULL COMMENT '内容',
  `ip_addr` varchar(200) DEFAULT NULL COMMENT 'ip地址',
  `country` varchar(200) DEFAULT NULL COMMENT '国家',
  `province` varchar(200) DEFAULT NULL COMMENT '省',
  `city` varchar(200) DEFAULT NULL COMMENT '市',
  `location` varchar(500) DEFAULT NULL COMMENT '归属地',
  `login_type` varchar(45) DEFAULT NULL COMMENT '登录类型',
  `code` varchar(45) DEFAULT NULL COMMENT '代码',
  `provider` varchar(45) DEFAULT NULL COMMENT '供应商',
  `browser` varchar(45) DEFAULT NULL COMMENT '浏览器',
  `platform` varchar(45) DEFAULT NULL COMMENT '平台',
  `application` varchar(45) DEFAULT NULL COMMENT '应用',
  `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `book_id` varchar(45) DEFAULT NULL COMMENT '租户编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `SESSIONID_UNIQUE` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_session_list`
--

LOCK TABLES `surpass_session_list` WRITE;
/*!40000 ALTER TABLE `surpass_session_list` DISABLE KEYS */;
INSERT INTO `surpass_session_list` VALUES ('1181205946680999936','1181205946332872704','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004','','Chrome/141','Windows NT 10.0',NULL,'2025-10-30 09:31:49',NULL),('1188098069351104512','1188098068344471552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,'2025-11-18 09:58:39',NULL),('1189276651905613824','1189276651184193536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,'2025-11-21 16:01:55',NULL),('1189290607483289600','1189290606921252864','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,'2025-11-21 16:57:22',NULL),('1189292884768063488','1189292884168278016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,'2025-11-21 17:06:25',NULL),('1189294406738378752','1189294406352502784','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/141','Windows NT 10.0',NULL,'2025-11-21 17:12:28',NULL),('1189976364606816256','1189976362656464896','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-23 14:22:20',NULL),('1189987261790289920','1189987259877687296','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-23 15:05:38',NULL),('1190011382259515392','1190011381227716608','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-23 16:41:29',NULL),('1190025471790678016','1190025470561746944','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-23 17:37:28',NULL),('1190033195433721856','1190033194280288256','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-23 18:08:09',NULL),('1190055369204826112','1190055365388009472','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-23 19:36:16',NULL),('1190061299619332096','1190061297408933888','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-23 19:59:50',NULL),('1190269612613173248','1190269611971444736','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-24 09:47:35',NULL),('1190279035880472576','1190279035704311808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-24 10:25:02',NULL),('1190345094314590208','1190345093643501568','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-24 14:47:32',NULL),('1191822044115763200','1191822043541143552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-11-28 16:36:24',NULL),('1193175630246576128','1193175629567098880','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-02 10:15:04',NULL),('1193238486052765696','1193238485713027072','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-02 14:24:50',NULL),('1193245643439603712','1193245643028561920','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-02 14:53:16',NULL),('1193888201391996928','1193888200012070912','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-04 09:26:34',NULL),('1193891190399827968','1193891189858762752','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-04 09:38:27',NULL),('1193902247872823296','1193902246287376384','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-04 10:22:23',NULL),('1193913909946351616','1193913909728247808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-04 11:08:44',NULL),('1194000492477284352','1194000491957190656','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-04 16:52:47',NULL),('1194002184635678720','1194002184061059072','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-04 16:59:30',NULL),('1194003643418804224','1194003642844184576','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-04 17:05:18',NULL),('1194007860950859776','1194007860569178112','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-04 17:22:03',NULL),('1194008964170252288','1194008963801153536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-04 17:26:26',NULL),('1194013875821871104','1194013875205308416','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-04 17:45:57',NULL),('1194278832169811968','1194278831771353088','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-05 11:18:48',NULL),('1194645481423110144','1194645480198373376','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-06 11:35:44',NULL),('1194648555537563648','1194648554392518656','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-06 11:47:57',NULL),('1194655385080823808','1194655383944167424','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-06 12:15:05',NULL),('1194662484275888128','1194662483353141248','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-06 12:43:18',NULL),('1194675697772134400','1194675696639672320','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-06 13:35:48',NULL),('1195015706526089216','1195015705389432832','mgmt','1','admin','系统管理员','success','127.0.0.1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-07 12:06:52',NULL),('1195346574339735552','1195346573760921600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-08 10:01:37',NULL),('1195348362732240896','1195348362270867456','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-08 10:08:44',NULL),('1195354623569821696','1195354623062310912','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-08 10:33:37',NULL),('1195433187254206464','1195433186901884928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-08 15:45:48',NULL),('1195786550323970048','1195786548759494656','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-09 15:09:56',NULL),('1195786576588701696','1195786576374792192','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-09 15:10:02',NULL),('1195832418796306432','1195832418443984896','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-09 18:12:12',NULL),('1196076454115278848','1196076453741985792','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-10 10:21:54',NULL),('1196157138095833088','1196157137688985600','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-10 15:42:31',NULL),('1196158701761724416','1196158701405208576','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-10 15:48:44',NULL),('1196164189777821696','1196164189341614080','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-10 16:10:32',NULL),('1196164662824009728','1196164662475882496','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-10 16:12:25',NULL),('1196443499524784128','1196443498971136000','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-11 10:40:25',NULL),('1196461492287832064','1196461491830652928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-11 11:51:55',NULL),('1196520257154449408','1196520256693075968','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-11 15:45:25',NULL),('1196794738661916672','1196794738171183104','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-12 09:56:07',NULL),('1196823272977596416','1196823272528805888','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-12 11:49:30',NULL),('1196888740228759552','1196888739981295616','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-12 16:09:38',NULL),('1198248899643441152','1198248899073015808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-16 10:14:26',NULL),('1198324402731614208','1198324402001805312','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-16 15:14:27',NULL),('1198361498561282048','1198361498083131392','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-16 17:41:51',NULL),('1198362198993272832','1198362198414458880','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-16 17:44:38',NULL),('1198614535003439104','1198614534663700480','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-17 10:27:20',NULL),('1198625063524368384','1198625062924582912','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-17 11:09:10',NULL),('1198638329378111488','1198638328891572224','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-17 12:01:53',NULL),('1198680972925599744','1198680972267094016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-17 14:51:20',NULL),('1198683308444090368','1198683307852693504','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-17 15:00:37',NULL),('1198692829853908992','1198692829551919104','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 15:38:27',NULL),('1198697090025259008','1198697089756823552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 15:55:23',NULL),('1198699302801637376','1198699302554173440','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 16:04:10',NULL),('1198705611596890112','1198705611315871744','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 16:29:14',NULL),('1198706402231582720','1198706401971535872','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 16:32:23',NULL),('1198706836073611264','1198706835763232768','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-17 16:34:06',NULL),('1198707925279834112','1198707925036564480','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 16:38:26',NULL),('1198710504592244736','1198710504323809280','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 16:48:41',NULL),('1198718970203471872','1198718969930842112','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 17:22:19',NULL),('1198727598117486592','1198727597735804928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/142','Windows NT 10.0',NULL,'2025-12-17 17:56:36',NULL),('1199038945254440960','1199038944797261824','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-18 14:33:47',NULL),('1199085516662767616','1199085516000067584','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-18 17:38:51',NULL),('1199089537825898496','1199089537519714304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-18 17:54:49',NULL),('1199347263437012992','1199347262891753472','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 10:58:56',NULL),('1199351681175781376','1199351680823459840','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 11:16:29',NULL),('1199352548440408064','1199352547874177024','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 11:19:56',NULL),('1199355152666984448','1199355152264331264','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 11:30:17',NULL),('1199360130227896320','1199360129745551360','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 11:50:04',NULL),('1199360968275001344','1199360967968817152','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 11:53:24',NULL),('1199417726959878144','1199417726645305344','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 15:38:56',NULL),('1199429174675111936','1199429174205349888','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 16:24:25',NULL),('1199437174987030528','1199437174542434304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 16:56:13',NULL),('1199454969296584704','1199454968654856192','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-19 18:06:55',NULL),('1200490051465641984','1200490051163652096','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-22 14:39:58',NULL),('1200500088930041856','1200500088812601344','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-22 15:19:51',NULL),('1200534767464873984','1200534767003500544','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-22 17:37:39',NULL),('1200782066069798912','1200782065537122304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-23 10:00:20',NULL),('1200800261895880704','1200800261530976256','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-23 11:12:38',NULL),('1200866679425335296','1200866679114956800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-23 15:36:33',NULL),('1200893276777873408','1200893276308111360','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-23 17:22:14',NULL),('1201137606046253056','1201137605291278336','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-24 09:33:07',NULL),('1201156343075962880','1201156342467788800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-24 10:47:34',NULL),('1201227215585411072','1201227215065317376','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-24 15:29:12',NULL),('1201240595910098944','1201240595683606528','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-24 16:22:22',NULL),('1201259933396369408','1201259932993716224','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-24 17:39:12',NULL),('1201263959831740416','1201263959370366976','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-24 17:55:12',NULL),('1201267632167190528','1201267631722594304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-24 18:09:48',NULL),('1201505590908551168','1201505590354903040','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 09:55:21',NULL),('1201509901575127040','1201509901122142208','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 10:12:29',NULL),('1201510936586747904','1201510936167317504','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 10:16:36',NULL),('1201519186715607040','1201519186426200064','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 10:49:23',NULL),('1201520648459911168','1201520648027897856','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 10:55:11',NULL),('1201523943769374720','1201523943236698112','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 11:08:17',NULL),('1201526139189723136','1201526138854178816','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 11:17:01',NULL),('1201605565311090688','1201605564962963456','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 16:32:37',NULL),('1201620444319318016','1201620444000550912','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 17:31:45',NULL),('1201623520350568448','1201623520077938688','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-25 17:43:58',NULL),('1201865244071165952','1201865243593015296','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 09:44:29',NULL),('1201878965908668416','1201878965619261440','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 10:39:01',NULL),('1201945258741465088','1201945258506584064','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 15:02:26',NULL),('1201958468341202944','1201958467569451008','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 15:54:56',NULL),('1201961504836943872','1201961504149078016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 16:07:00',NULL),('1201961897075671040','1201961896446525440','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 16:08:33',NULL),('1201967348559380480','1201967347955400704','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 16:30:13',NULL),('1201979815704395776','1201979815419183104','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 17:19:45',NULL),('1201987830394388480','1201987830100787200','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 17:51:36',NULL),('1201988321790656512','1201988321463500800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-26 17:53:33',NULL),('1202941291516657664','1202941291059478528','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 09:00:19',NULL),('1202954885847318528','1202954885427888128','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 09:54:20',NULL),('1202964299694211072','1202964299522244608','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 10:31:45',NULL),('1202978150191988736','1202978149034360832','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,'2025-12-29 11:26:47',NULL),('1202996166271172608','1202996165998542848','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 12:38:22',NULL),('1203024500271087616','1203024499826491392','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 14:30:58',NULL),('1203041824592101376','1203041824231391232','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 15:39:48',NULL),('1203047591240007680','1203047590971572224','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 16:02:43',NULL),('1203054079631163392','1203054079031377920','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 16:28:30',NULL),('1203057330447450112','1203057330137071616','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 16:41:25',NULL),('1203060969484320768','1203060969366880256','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 16:55:53',NULL),('1203062742919938048','1203062742630531072','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 17:02:55',NULL),('1203067480826707968','1203067480528912384','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-29 17:21:45',NULL),('1203113417397764096','1203113416282079232','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,'2025-12-29 20:24:17',NULL),('1203121046991929344','1203121045775581184','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,'2025-12-29 20:54:36',NULL),('1203140314450100224','1203140310746529792','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Edg/123','Windows NT 10.0',NULL,'2025-12-29 22:11:10',NULL),('1203312332743114752','1203312332231409664','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 09:34:42',NULL),('1203315678384226304','1203315677675388928','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 09:48:00',NULL),('1203319138798272512','1203319138508865536','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 10:01:45',NULL),('1203319174399524864','1203319173812322304','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 10:01:53',NULL),('1203321936373874688','1203321935463710720','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 10:12:52',NULL),('1203322207200083968','1203322206721933312','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,'2025-12-30 10:13:56',NULL),('1203324402075172864','1203324400124821504','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,'2025-12-30 10:22:40',NULL),('1203327938443870208','1203327938024439808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 10:36:43',NULL),('1203328104861270016','1203328104475394048','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,'2025-12-30 10:37:23',NULL),('1203392536442306560','1203392532826816512','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,'2025-12-30 14:53:24',NULL),('1203392820816117760','1203392819033538560','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,'2025-12-30 14:54:32',NULL),('1203393246017880064','1203393244419850240','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 14:56:13',NULL),('1203394232392351744','1203394231536713728','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Firefox/146','Windows NT 10.0',NULL,'2025-12-30 15:00:09',NULL),('1203394842269319168','1203394842051215360','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 15:02:34',NULL),('1203412211846873088','1203412211460997120','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 16:11:35',NULL),('1203513141950939136','1203513140654899200','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 22:52:39',NULL),('1203517285390614528','1203517284417536000','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 23:09:07',NULL),('1203520720085188608','1203520716058656768','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 23:22:46',NULL),('1203521365332721664','1203521361331355648','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 23:25:19',NULL),('1203523835781971968','1203523831549919232','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 23:35:08',NULL),('1203525106454757376','1203525102843461632','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 23:40:11',NULL),('1203527590669713408','1203527589612748800','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 23:50:04',NULL),('1203529141522006016','1203529137327702016','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-30 23:56:13',NULL),('1203536357725569024','1203536356601495552','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-31 00:24:54',NULL),('1203537769452797952','1203537765145247744','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-31 00:30:31',NULL),('1203540074210263040','1203540073153298432','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-31 00:39:40',NULL),('1203542715640315904','1203542714700791808','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-31 00:50:10',NULL),('1203675227121254400','1203675226634715136','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-31 09:36:43',NULL),('1203676429871480832','1203676429611433984','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-31 09:41:30',NULL),('1203679021649362944','1203679021208961024','mgmt','1','admin','系统管理员','success','0:0:0:0:0:0:0:1',NULL,NULL,NULL,'local','normal','xe00000004',NULL,'Chrome/143','Windows NT 10.0',NULL,'2025-12-31 09:51:48',NULL);
/*!40000 ALTER TABLE `surpass_session_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surpass_userinfo`
--

DROP TABLE IF EXISTS `surpass_userinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surpass_userinfo` (
  `id` varchar(45) NOT NULL COMMENT '编号',
  `username` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(500) NOT NULL COMMENT '密码',
  `decipherable` varchar(500) NOT NULL COMMENT 'DE密码',
  `two_factor` tinyint unsigned DEFAULT '0' COMMENT '二次认证类型',
  `mobile` varchar(45) DEFAULT NULL COMMENT '手机号码',
  `mobile_verified` varchar(45) DEFAULT NULL COMMENT '手机号验证',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `email_verified` smallint unsigned DEFAULT NULL COMMENT '邮箱验证',
  `display_name` varchar(45) DEFAULT NULL COMMENT '显示名称',
  `nick_name` varchar(45) DEFAULT NULL COMMENT '昵称',
  `picture_id` varchar(200) DEFAULT NULL COMMENT '头像ID',
  `time_zone` varchar(45) DEFAULT 'Asia/Shanghai' COMMENT '时区',
  `locale` varchar(45) DEFAULT 'zh_CN' COMMENT '地址',
  `preferred_language` varchar(45) DEFAULT 'zh_CN' COMMENT '语言偏好',
  `password_question` varchar(45) DEFAULT NULL COMMENT '密码问题',
  `password_answer` varchar(45) DEFAULT NULL COMMENT '密码答案',
  `theme` varchar(45) DEFAULT 'default' COMMENT '主题',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数统计',
  `is_online` tinyint unsigned DEFAULT '0' COMMENT '在线状态',
  `status` tinyint unsigned DEFAULT NULL COMMENT '用户状态',
  `is_locked` tinyint unsigned DEFAULT NULL COMMENT '锁定状态',
  `un_lock_time` datetime DEFAULT '2020-01-01 01:01:01' COMMENT '解锁时间',
  `last_login_ip` varchar(45) DEFAULT NULL COMMENT '最近登录IP地址',
  `last_login_time` datetime DEFAULT '2020-01-01 01:01:01' COMMENT '最近登录时间',
  `last_logoff_time` datetime DEFAULT '2020-01-01 01:01:01' COMMENT '最近注销时间',
  `login_failed_count` smallint DEFAULT '0' COMMENT '登录失败次数',
  `login_failed_time` datetime DEFAULT NULL COMMENT '登录失败时间',
  `bad_password_time` datetime DEFAULT '2020-01-01 01:01:01' COMMENT '最近密码错误时间',
  `bad_password_count` smallint unsigned DEFAULT NULL COMMENT '密码错误次数',
  `password_last_set_time` datetime DEFAULT '2020-01-01 01:01:01' COMMENT '最近密码修改时间',
  `password_set_type` tinyint unsigned DEFAULT '0' COMMENT '密码重置类型',
  `shared_secret` varchar(500) DEFAULT NULL COMMENT 'TIME-OPT密钥',
  `shared_counter` varchar(45) DEFAULT '0' COMMENT 'COUNTER-OPT密钥',
  `user_type` varchar(45) DEFAULT 'Customer' COMMENT '用户类型',
  `user_state` varchar(45) DEFAULT 'RESIDENT' COMMENT '用户状态',
  `sort_order` tinyint unsigned DEFAULT '0' COMMENT '部门内排序',
  `name_zh_spell` varchar(100) DEFAULT NULL COMMENT '名字中文拼音',
  `name_zh_short_spell` varchar(45) DEFAULT NULL COMMENT '名字中文拼音简称',
  `web_site` varchar(50) DEFAULT NULL COMMENT '个人主页',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `description` varchar(400) DEFAULT NULL COMMENT '描述',
  `ldap_dn` varchar(1000) DEFAULT NULL COMMENT '最近访问book_id',
  `sort_index` int DEFAULT NULL COMMENT '排序',
  `deleted` varchar(1) DEFAULT 'n' COMMENT '删除标记',
  `inst_id` varchar(45) DEFAULT NULL COMMENT '租户编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `USERNAME_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surpass_userinfo`
--

LOCK TABLES `surpass_userinfo` WRITE;
/*!40000 ALTER TABLE `surpass_userinfo` DISABLE KEYS */;
INSERT INTO `surpass_userinfo` VALUES ('1','admin','{plain}surpass','$2a$10$uCfjmDHxUS2Aow79ZNaJhu5a0c0426c67e44f27630ecb09d7e99c1cfc7c21d49eb4edfb76e4fefe2dafe50bc56dd703a89211c',0,'15618724567','0','support@maxsso.net',0,'系统管理员','系统管理员','1','Asia/Shanghai','de','zh_CN','5','wusdfdsf','default',4168,1,1,1,'2024-02-17 13:26:07','0:0:0:0:0:0:0:1','2025-12-31 09:51:48','2025-12-31 00:50:06',0,'2024-11-01 15:40:33','2025-10-29 16:53:54',0,'2024-10-30 10:56:10',1,'$2a$10$kwA5OXSKwfud102tBwdbZe0763d93797aef4f6cd5e43637330aa95e8dabb3e9800c113474114ef3ef6e8af4a11a5e835c996427a97049e87ca0b7668ae8ffef9f2d298','0','EMPLOYEE','RESIDENT',0,NULL,NULL,'http://login.maxkey.org/',NULL,'2014-01-21 00:00:00','1','2025-02-16 18:49:58',NULL,NULL,1,'n',NULL);
/*!40000 ALTER TABLE `surpass_userinfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-31  9:52:53
