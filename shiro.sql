/*
SQLyog v10.2 
MySQL - 5.5.13 : Database - shiro
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shiro` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `shiro`;

/*Table structure for table `sys_permission` */

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id` int(4) NOT NULL,
  `available` char(20) NOT NULL,
  `name` char(20) NOT NULL,
  `parent_id` int(4) NOT NULL,
  `parent_ids` char(20) NOT NULL,
  `permission` char(20) NOT NULL,
  `resource_type` char(20) NOT NULL,
  `url` char(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_permission` */

insert  into `sys_permission`(`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`) values (1,'0','用户管理',0,'0/','view','menu','userInfo/userList'),(2,'0','用户添加',1,'0/1','add','button','userInfo/userAdd'),(3,'0','用户删除',1,'0/1','del','button','userInfo/userDel');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` int(4) NOT NULL,
  `available` char(20) NOT NULL,
  `description` char(20) NOT NULL,
  `role` char(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`available`,`description`,`role`) values (0,'0','经理','经理'),(1,'0','管理员','admin'),(2,'0','VIP会员','vip'),(3,'0','普通会员','common');

/*Table structure for table `sys_role_permission` */

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `permission_id` int(4) NOT NULL,
  `role_id` int(4) NOT NULL,
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `sys_role_permission_ibfk_1` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`),
  CONSTRAINT `sys_role_permission_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role_permission` */

insert  into `sys_role_permission`(`permission_id`,`role_id`) values (1,0),(2,0),(3,0),(1,1),(2,1),(3,1),(1,2),(2,2),(3,2),(1,3),(2,3),(3,3);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `role_id` int(4) NOT NULL,
  `uid` int(4) NOT NULL,
  PRIMARY KEY (`role_id`,`uid`),
  KEY `uid` (`uid`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`),
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user_info` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`role_id`,`uid`) values (1,1),(2,1),(3,1),(2,2),(3,2),(0,3),(2,7);

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `uid` int(20) NOT NULL AUTO_INCREMENT,
  `userName` char(20) NOT NULL,
  `name` char(20) NOT NULL,
  `password` char(50) NOT NULL,
  `salt` char(50) NOT NULL,
  `state` char(20) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

/*Data for the table `user_info` */

insert  into `user_info`(`uid`,`userName`,`name`,`password`,`salt`,`state`) values (1,'admin','管理员','4f251ab52c7b431254a7adc8ea31724b','saltadmin','0'),(2,'u12','name1','111','wwwaq','1'),(3,'uadmin','admin','1a502229f78ba36f0b3423f6436c7010','9686','0'),(5,'张伟2512','admin23','12325','33324','0'),(7,'opp','opp','qwew12','qqqq','0'),(10,'abc777','aaaa','3456','8888','0'),(11,'qq','qq','50db216df7bb770faa05aea1ecf6e882','1157','0'),(12,'qq1','qq','592932ab78d7e8e21b6fb7984a2bc0ba','5321','0'),(13,'aa','bb','123456','11','0'),(14,'qq','q2','111','qwewqeq','0'),(15,'qq3','q23','111','qwewqeq','0'),(16,'aa','aa1','123','2222','0'),(17,'aa2','aa1','123','2222122','0'),(18,'aa3','aa1','123','2222','0'),(19,'aa5','aa1','123','2222','0'),(20,'aa5','aa1','123','2222','0'),(22,'ds','ds','123','11','0'),(23,'ds','ds','123','11','0'),(24,'uiiii','uiii','222','111','0'),(25,'xxx','xx','11','11','0'),(26,'ffff','fff','111','111','0'),(29,'涨水1','张思1','444444444444','44444444444','0'),(30,'李晓明','大明','33333','111111','0'),(31,'cccc','aaa','111','22','0'),(32,'tt','asaa','22','11','0'),(33,'展示那','aa','22','111','0'),(34,'航三','ssss','11','1','0'),(35,'asda','fff','1','222','0'),(36,'asdas','asd','1','22','0'),(37,'asdasd','assdas','111','1','0'),(38,'asdas','adsad','1','1','0'),(39,'萨达','沾上干','22','11','0'),(40,'asd','asdas','11','1','0'),(42,'张三','你好','33','111','0'),(49,'啥1','assda1','11','1','0'),(51,'a11a11','刘浩','43a3b20d75c38fd56bf02d9ffa2f481a','3512','0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
