/*
Navicat MySQL Data Transfer

Source Server         : ailian
Source Server Version : 50022
Source Host           : localhost:3306
Source Database       : crawl

Target Server Type    : MYSQL
Target Server Version : 50022
File Encoding         : 65001

Date: 2016-07-13 08:06:55
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `bdsola_url_detail`
-- ----------------------------
DROP TABLE IF EXISTS `bdsola_url_detail`;
CREATE TABLE `bdsola_url_detail` (
  `id` varchar(50) NOT NULL,
  `share_title` varchar(55) default NULL,
  `sharer` varchar(40) default NULL,
  `share_time` datetime default NULL,
  `file_type` varchar(10) default NULL,
  `first_level_url` varchar(150) default NULL,
  `second_level_url` varchar(150) default NULL,
  `yun_share_id` varchar(50) default NULL,
  `use_cache` tinyint(1) default NULL,
  `file_path` varchar(180) default NULL,
  `last_crawl_time` datetime default NULL,
  `crawl_times` int(11) default NULL,
  `is_crawled` tinyint(1) default NULL,
  `list_id` varchar(50) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_if8nif3dnsk9cr9aniwonyu2m` (`list_id`),
  CONSTRAINT `FK_if8nif3dnsk9cr9aniwonyu2m` FOREIGN KEY (`list_id`) REFERENCES `bdsola_url_list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bdsola_url_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `bdsola_url_list`
-- ----------------------------
DROP TABLE IF EXISTS `bdsola_url_list`;
CREATE TABLE `bdsola_url_list` (
  `id` varchar(50) NOT NULL,
  `top_url` varchar(100) default NULL,
  `kw` varchar(40) default NULL,
  `search_type` varchar(20) default NULL,
  `sort` int(11) default NULL,
  `current_page` int(11) default NULL,
  `is_cached` tinyint(1) default NULL,
  `cached_dir` varchar(150) default NULL,
  `if_exists` tinyint(1) default NULL,
  `pages` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bdsola_url_list
-- ----------------------------
INSERT INTO bdsola_url_list VALUES ('bootstrap_ALL_0_1', 'http://www.bdsola.com/search.php?kw=bootstrap&searchType=ALL&sort=0&cur_page=1', 'bootstrap', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/bootstrap', '1', '17');
INSERT INTO bdsola_url_list VALUES ('flume_ALL_0_1', 'http://www.bdsola.com/search.php?kw=flume&searchType=ALL&sort=0&cur_page=1', 'flume', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/flume', '1', '2');
INSERT INTO bdsola_url_list VALUES ('hadoop_ALL_0_1', 'http://www.bdsola.com/search.php?kw=hadoop&searchType=ALL&sort=0&cur_page=1', 'hadoop', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/hadoop', '1', '17');
INSERT INTO bdsola_url_list VALUES ('hbase_ALL_0_1', 'http://www.bdsola.com/search.php?kw=hbase&searchType=ALL&sort=0&cur_page=1', 'hbase', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/hbase', '1', '5');
INSERT INTO bdsola_url_list VALUES ('hibernate_ALL_0_1', 'http://www.bdsola.com/search.php?kw=hibernate&searchType=ALL&sort=0&cur_page=1', 'hibernate', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/hibernate', '1', '17');
INSERT INTO bdsola_url_list VALUES ('hive_ALL_0_1', 'http://www.bdsola.com/search.php?kw=hive&searchType=ALL&sort=0&cur_page=1', 'hive', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/hive', '1', '17');
INSERT INTO bdsola_url_list VALUES ('html5_ALL_0_1', 'http://www.bdsola.com/search.php?kw=html5&searchType=ALL&sort=0&cur_page=1', 'html5', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/html5', '1', '17');
INSERT INTO bdsola_url_list VALUES ('javascript_ALL_0_1', 'http://www.bdsola.com/search.php?kw=javascript&searchType=ALL&sort=0&cur_page=1', 'javascript', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/javascript', '1', '17');
INSERT INTO bdsola_url_list VALUES ('java_ALL_0_1', 'http://www.bdsola.com/search.php?kw=java&searchType=ALL&sort=0&cur_page=1', 'java', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/java', '1', '17');
INSERT INTO bdsola_url_list VALUES ('kafka_ALL_0_1', 'http://www.bdsola.com/search.php?kw=kafka&searchType=ALL&sort=0&cur_page=1', 'kafka', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/kafka', '1', '2');
INSERT INTO bdsola_url_list VALUES ('linux_ALL_0_1', 'http://www.bdsola.com/search.php?kw=linux&searchType=ALL&sort=0&cur_page=1', 'linux', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/linux', '1', '17');
INSERT INTO bdsola_url_list VALUES ('mybatis_ALL_0_1', 'http://www.bdsola.com/search.php?kw=mybatis&searchType=ALL&sort=0&cur_page=1', 'mybatis', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/mybatis', '1', '17');
INSERT INTO bdsola_url_list VALUES ('nodejs_ALL_0_1', 'http://www.bdsola.com/search.php?kw=nodejs&searchType=ALL&sort=0&cur_page=1', 'nodejs', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/nodejs', '1', '5');
INSERT INTO bdsola_url_list VALUES ('php_ALL_0_1', 'http://www.bdsola.com/search.php?kw=php&searchType=ALL&sort=0&cur_page=1', 'php', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/php', '1', '17');
INSERT INTO bdsola_url_list VALUES ('python_ALL_0_1', 'http://www.bdsola.com/search.php?kw=python&searchType=ALL&sort=0&cur_page=1', 'python', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/python', '1', '17');
INSERT INTO bdsola_url_list VALUES ('spark_ALL_0_1', 'http://www.bdsola.com/search.php?kw=spark&searchType=ALL&sort=0&cur_page=1', 'spark', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/spark', '1', '17');
INSERT INTO bdsola_url_list VALUES ('springmvc_ALL_0_1', 'http://www.bdsola.com/search.php?kw=springmvc&searchType=ALL&sort=0&cur_page=1', 'springmvc', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/springmvc', '1', '17');
INSERT INTO bdsola_url_list VALUES ('spring_ALL_0_1', 'http://www.bdsola.com/search.php?kw=spring&searchType=ALL&sort=0&cur_page=1', 'spring', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/spring', '1', '17');
INSERT INTO bdsola_url_list VALUES ('sqoop_ALL_0_1', 'http://www.bdsola.com/search.php?kw=sqoop&searchType=ALL&sort=0&cur_page=1', 'sqoop', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/sqoop', '1', '1');
INSERT INTO bdsola_url_list VALUES ('ssh_ALL_0_1', 'http://www.bdsola.com/search.php?kw=ssh&searchType=ALL&sort=0&cur_page=1', 'ssh', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/ssh', '1', '17');
INSERT INTO bdsola_url_list VALUES ('storm_ALL_0_1', 'http://www.bdsola.com/search.php?kw=storm&searchType=ALL&sort=0&cur_page=1', 'storm', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/storm', '1', '17');
INSERT INTO bdsola_url_list VALUES ('ubuntu_ALL_0_1', 'http://www.bdsola.com/search.php?kw=ubuntu&searchType=ALL&sort=0&cur_page=1', 'ubuntu', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/ubuntu', '1', '17');
INSERT INTO bdsola_url_list VALUES ('webservice_ALL_0_1', 'http://www.bdsola.com/search.php?kw=webservice&searchType=ALL&sort=0&cur_page=1', 'webservice', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/webservice', '1', '11');
INSERT INTO bdsola_url_list VALUES ('zookeeper_ALL_0_1', 'http://www.bdsola.com/search.php?kw=zookeeper&searchType=ALL&sort=0&cur_page=1', 'zookeeper', 'ALL', '0', '1', '1', 'F:/tomcat_server/apache-tomcat-7.0.69-windows-x86/apache-tomcat-7.0.69/webapps/ROOT/WEB-INF/classes/bdsola/sourcehtml/firstlevel/zookeeper', '1', '4');

-- ----------------------------
-- Table structure for `crawl_url`
-- ----------------------------
DROP TABLE IF EXISTS `crawl_url`;
CREATE TABLE `crawl_url` (
  `id` varchar(40) NOT NULL,
  `share_title` varchar(50) default NULL,
  `sharer` varchar(20) default NULL,
  `share_time` datetime default NULL,
  `file_type` varchar(10) default NULL,
  `first_level_url` varchar(128) default NULL,
  `second_level_url` varchar(128) default NULL,
  `yun_share_id` varchar(36) default NULL,
  `use_cache` tinyint(1) default NULL,
  `file_path` varchar(128) default NULL,
  `last_crawl_time` datetime default NULL,
  `crawl_times` int(11) default NULL,
  `is_crawled` tinyint(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crawl_url
-- ----------------------------
