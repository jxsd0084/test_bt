DROP DATABASE IF EXISTS buried_point;

CREATE DATABASE buried_point DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE buried_point;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bp_fields`
-- ----------------------------
DROP TABLE IF EXISTS `bp_fields`;
CREATE TABLE `bp_fields` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`biz_id`  int(12) NOT NULL COMMENT '业务ID' ,
`biz_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务名称' ,
`con_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接名称' ,
`job_id`  int(12) NOT NULL ,
`table_id`  int(12) NOT NULL COMMENT '表ID' ,
`table_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名' ,
`field_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称' ,
`desensitize`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '脱敏字段' ,
`partition`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分区字段' ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=7

;

-- ----------------------------
-- Records of bp_fields
-- ----------------------------
BEGIN;
INSERT INTO `bp_fields` VALUES ('1', '0', null, null, '1', '1', 'table1', 'f1', '0', '0'), ('2', '0', null, null, '1', '1', 'table1', 'f2', '0', '0'), ('3', '0', null, null, '1', '1', 'table1', 'f3', '1', '1'), ('4', '0', null, null, '1', '1', 'table1', 'f4', '0', '0'), ('5', '0', null, null, '1', '1', 'table1', 'f5', '0', '0'), ('6', '0', null, null, '2', '1', 'table1', 'f1', '0', '0');
COMMIT;

-- ----------------------------
-- Table structure for `bp_job`
-- ----------------------------
DROP TABLE IF EXISTS `bp_job`;
CREATE TABLE `bp_job` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`job_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业名称' ,
`biz_id`  int(12) NOT NULL COMMENT '业务ID' ,
`biz_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务名称' ,
`connection`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库连接' ,
`table_id`  int(12) NOT NULL COMMENT '导出表ID' ,
`table_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名称' ,
`target_id`  int(12) NOT NULL COMMENT '导出目标表ID' ,
`target_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`target_address`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出目标信息(ip+端口)' ,
`target_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出路径或主题' ,
`field_id`  int(12) NOT NULL COMMENT '字段表ID' ,
`execute_time`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始执行时间' ,
`last_update`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间' ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=9

;

-- ----------------------------
-- Records of bp_job
-- ----------------------------
BEGIN;
INSERT INTO `bp_job` VALUES ('1', 'My_Exp_01', '1', '企信', 'MySQL,oss,172.31.101.12,3306', '1', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '1', 'KAFKA', 'vlnx043006.foneshare.cn', 'dcx.MonitorRequest', '0', '20160301 12:00:00', '2016-04-05 10:27:26'), ('2', 'My_Exp_02', '1', '企信', 'MySQL,oss,172.31.101.12,3306', '1', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '2', 'HDFS', '172.31.107.11', '/facishare-data', '0', '20160311 09:00:00', '2016-04-05 10:27:28'), ('3', 'My_Exp_03', '1', '企信', 'MySQL,oss,172.31.101.12,3306', '1', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '2', 'HDFS', '172.31.107.11', '/facishare-data', '0', '20160321 16:00:00', '2016-04-05 10:27:30'), ('4', 'My_Exp_04', '2', '协同', 'MySQL,oss,172.31.101.12,3306', '2', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '1', 'KAFKA', 'vlnx043006.foneshare.cn', 'broker.feeds-search0', '0', '20160304 09:00:00', '2016-04-05 10:27:34'), ('5', 'My_Exp_05', '2', '协同', 'MySQL,oss,172.31.101.12,3306', '2', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '2', 'HDFS', '172.31.107.11', '/facishare-data', '0', '20160317 17:00:00', '2016-04-05 10:27:36'), ('6', 'My_Exp_06', '3', 'CRM', 'MySQL,oss,172.31.101.12,3306', '3', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '1', 'KAFKA', 'vlnx043006.foneshare.cn', 'nginx.reverse', '0', '20160308 10:00:00', '2016-04-05 10:27:41'), ('7', 'My_Exp_07', '3', 'CRM', 'MySQL,oss,172.31.101.12,3306', '3', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '2', 'HDFS', '172.31.107.11', '/facishare-data', '0', '20160308 12:00:00', '2016-04-05 10:27:44'), ('8', 'My_Exp_08', '4', '汇聚', 'MySQL,oss,172.31.101.12,3306', '4', '[{\"name\":\"table1\",\"fields\":[{\"f1\":{\"des\":\"y\",\"par\":\"n\"},\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"},}]},{ \"name\":\"table2\",\"fields\":[{\"f2\":{\"des\":\"y\",\"par\":\"n\"},\"f3\":{\"des\":\"y\",\"par\":\"n\"}}]}]', '2', 'HDFS', '172.31.107.11', '/facishare-data', '0', '20160308 14:00:00', '2016-04-05 10:27:46');
COMMIT;

-- ----------------------------
-- Table structure for `bp_target`
-- ----------------------------
DROP TABLE IF EXISTS `bp_target`;
CREATE TABLE `bp_target` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出方式' ,
`ip`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`port`  int(12) NULL DEFAULT NULL ,
`path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`value`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=6

;

-- ----------------------------
-- Records of bp_target
-- ----------------------------
BEGIN;
INSERT INTO `bp_target` VALUES ('1', 'KAFKA', 'KAFKA', '192.168.1.1', '3306', 'nginx.reverse', ''), ('2', 'KAFKA', 'KAFKA', '192.168.1.2', '1433', 'dcx.MonitorRequest', ''), ('3', 'KAFKA', 'KAFKA', '112.123.12.1', '1000', 'broker.feeds-search0', null), ('4', 'HDFS', 'HDFS', '117.114.121.14', '10000', '/facishare-data/data/', null), ('5', 'HDFS', 'HDFS', '125.111.14.8', '10000', '/use/hive/db/', null);
COMMIT;

-- ----------------------------
-- Table structure for `buried_point`
-- ----------------------------
DROP TABLE IF EXISTS `buried_point`;
CREATE TABLE `buried_point` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT 'ID自增长' ,
`bp_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '埋点-名称' ,
`bp_value`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '埋点-内容' ,
`bp_value_desc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '埋点-内容-类型' ,
`parent_id`  int(12) NOT NULL COMMENT '父业务-ID' ,
`parent_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父业务-名称' ,
`child_id`  int(12) NOT NULL COMMENT '子业务-ID' ,
`child_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子业务-名称' ,
`is_checked`  tinyint(1) NOT NULL COMMENT '是否为必填项' ,
`regex`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '保存正则表达式规则' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=161

;

-- ----------------------------
-- Records of buried_point
-- ----------------------------
BEGIN;
INSERT INTO `buried_point` VALUES ('1', '1John', '文本', '3用户名称', '2', '后端埋点', '1', 'CEP', '0', '*?@'), ('4', 'OS', '数字', '操作系统', '2', '后端埋点', '2', '企信', '0', '$#@'), ('5', 'EI', '数字', '企业ID', '2', '后端埋点', '4', 'CEP', '0', '1234'), ('6', 'EnterpriseID', '数字', '企业ID', '2', '后端埋点', '5', 'CEP', '1', ''), ('9', 'M2', '文本', '用户ID', '1', '前端埋点', '1', 'Web', '0', ''), ('10', 'IP', '文本', 'IP地址', '1', '前端埋点', '3', 'Web', '1', '*(&987'), ('40', 'M3', '文本', '设备ID', '1', '前端埋点', '1', '', '1', ''), ('43', 'IP', '文本', '客户端来源IP', '2', '后端埋点', '5', '', '0', ''), ('44', 'UserID', '数字', '员工ID', '2', '后端埋点', '5', '', '0', ''), ('48', 'Platform', '数字', '终端平台类型', '2', '后端埋点', '5', '', '0', ''), ('49', 'OSVersion', '文本', '操作系统版本', '2', '后端埋点', '5', '', '0', ''), ('50', 'ProductVersion', '文本', '产品版本', '2', '后端埋点', '5', '', '0', ''), ('52', 'Action', '文本', '访问接口', '2', '后端埋点', '5', '', '0', ''), ('53', 'Time', '日期', '访问时间', '2', '后端埋点', '5', '', '1', ''), ('54', 'Duration', '数字', '访问耗时', '2', '后端埋点', '5', '', '0', ''), ('55', 'DeviceID', '文本', '设备ID', '2', '后端埋点', '5', '', '0', ''), ('56', 'ServiceType', '数字', '服务类型', '2', '后端埋点', '5', '', '0', ''), ('57', 'M4', '文本', '操作系统 （Android/iOS）', '1', '前端埋点', '1', '', '1', ''), ('58', 'M5', '文本', '操作系统版本', '1', '前端埋点', '1', '', '1', ''), ('59', 'M6', '文本', '纷享逍客应用版本号（5.1.0/5.2.0）', '1', '前端埋点', '1', '', '1', ''), ('60', 'M7', '文本', '纷享销客应用内部版本号', '1', '前端埋点', '1', '', '1', ''), ('61', 'M8', '文本', '设备型号', '1', '前端埋点', '1', '', '1', ''), ('62', 'M96', '文本', '网络类型', '1', '前端埋点', '1', '', '1', ''), ('63', 'M97', '文本', '事件类型', '1', '前端埋点', '1', '', '1', ''), ('64', 'M98', '日期', '事件发生时间', '1', '前端埋点', '1', '', '1', ''), ('66', 'M99.M1', '文本', '应用事件ID', '1', '前端埋点', '1', '', '1', ''), ('76', 'M2', '数字', '用户ID', '1', '前端埋点', '2', 'Web', '1', ''), ('77', 'M1', '文本', '企业ID', '1', '前端埋点', '2', 'Web2', '1', ''), ('78', 'M3', '文本', '设备ID', '1', '前端埋点', '2', '', '1', ''), ('79', 'M4', '文本', '操作系统 （Android/iOS）', '0', '前端埋点', '2', '', '1', ''), ('80', 'M5', '文本', '操作系统版本', '1', '前端埋点', '2', '', '1', ''), ('81', 'M6', '文本', '纷享逍客应用版本号（5.1.0/5.2.0）', '1', '前端埋点', '2', '', '1', ''), ('82', 'M7', '文本', '纷享销客应用内部版本号', '1', '前端埋点', '2', '', '1', ''), ('83', 'M8', '文本', '设备型号', '1', '前端埋点', '2', '', '1', ''), ('84', 'M96', '文本', '网络类型', '1', '前端埋点', '2', '', '1', ''), ('85', 'M97', '文本', '事件类型', '1', '前端埋点', '2', '', '1', ''), ('86', 'M98', '日期', '事件发生时间', '1', '前端埋点', '2', '', '1', ''), ('87', 'M99', '文本', '事件自定义字段', '1', '前端埋点', '2', '', '1', ''), ('88', 'M99.M1', '文本', '应用事件ID', '1', '前端埋点', '2', '', '0', ''), ('93', '_fuserid', '数字', '用户id', '1', '', '3', '', '1', ''), ('94', '_fuserName', '文本', '用户姓名', '1', '', '3', '', '1', ''), ('95', '_fcompanyid', '文本', '企业id', '1', '', '3', '', '1', ''), ('96', '_fcompanyname', '文本', '企业名称', '1', '', '3', '', '1', ''), ('97', '_source', '文本', '来源，写死为browser', '1', '', '3', '', '1', ''), ('98', '_fbrowser', '数字', '浏览器类型:1-ie8;2-1e9;3-ie10;4-ie11;5-safari;6-opera;7-chrome;8-liebao;9-qq;10-sougou;11-edge;100-other', '1', '', '3', '', '1', ''), ('99', 'actionid', '文本', '统计的key，表示某条统计点', '1', '', '3', '', '1', ''), ('100', '_fuserid', '数字', '用户id', '1', '', '6', '', '1', ''), ('101', '_fuserName', '文本', '用户姓名', '1', '', '6', '', '1', ''), ('102', '_fcompanyid', '数字', '企业id', '1', '', '6', '', '1', ''), ('103', '_fcompanyname', '文本', '企业名称', '1', '', '6', '', '1', ''), ('104', 'actionid', '文本', '统计key，标明某个统计项', '1', '', '6', '', '1', ''), ('105', '_source', '文本', '来源，写死pc', '1', '', '6', '', '1', ''), ('106', '_platform', '文本', '平台 win || mac', '1', '', '6', '', '1', ''), ('107', '_version', '文本', '版本号', '1', '', '6', '', '1', ''), ('108', '_osinfo', '文本', '系统详细信息', '1', '', '6', '', '1', ''), ('109', 'projectId', '文本', '项目id', '1', '', '7', '', '1', ''), ('110', 'appid', '文本', 'app id  来自开平', '1', '', '7', '', '1', ''), ('111', 'appversion', '文本', '版本', '1', '', '7', '', '1', ''), ('112', 'actionId', '文本', '统计key，表明某条统计项', '1', '', '7', '', '1', '/loadtime/'), ('113', '_source', '文本', '来源，写死h5', '1', '', '7', '', '1', ''), ('114', '_platform', '文本', '平台  pc || android || ios', '1', '', '7', '', '1', ''), ('115', '_version', '文本', '终端系统版本号', '1', '', '7', '', '1', ''), ('120', 'ttt2', '文本', 'ttt2', '2', '', '1', '', '1', ''), ('123', 't_blank', '数字', '白屏时间', '1', null, '7', null, '0', ''), ('124', 't_ready', '数字', '用户可操作时间', '1', null, '7', null, '0', ''), ('125', 't_load', '数字', 'load 时间', '1', null, '7', null, '1', ''), ('135', 'Browser', '文本', '浏览器', '2', null, '5', null, '0', ''), ('136', 'BrowserVersion', '文本', '浏览器版本', '2', null, '5', null, '0', ''), ('141', 'M1', '文本', '企业id', '1', null, '1', null, '1', ''), ('142', 'EID', '数字', '公司id', '2', null, '3', null, '1', ''), ('143', 'EA', '文本', '公司名称', '2', null, '3', null, '1', ''), ('144', 'UID', '数字', '用户id', '2', null, '3', null, '1', ''), ('145', 'IP', '文本', 'ip地址', '2', null, '3', null, '0', ''), ('146', 'PF', '文本', '平台类型', '2', null, '3', null, '0', ''), ('147', 'OSV', '文本', '操作系统版本', '2', null, '3', null, '0', ''), ('148', 'PROV', '文本', '产品内部版本', '2', null, '3', null, '0', ''), ('149', 'AC', '文本', '操作行为', '2', null, '3', null, '0', ''), ('150', 'LUT', '日期', '日志上报时间', '2', null, '3', null, '0', ''), ('151', 'D', '数字', '时长', '2', null, '3', null, '0', ''), ('152', 'DID', '文本', '物理设备id', '2', null, '3', null, '0', ''), ('153', 'T', '数字', '应用编号', '2', null, '3', null, '0', ''), ('154', 'P.WorkItemType', '数字', 'IGT类型', '2', null, '3', null, '0', ''), ('155', 'P.ParticipantNum', '数字', '群通知人数', '2', null, '3', null, '0', ''), ('156', 'P.Platform', '文本', '群通知平台', '2', null, '3', null, '0', ''), ('157', 'p_domready', '数字', '用户可操作时间', '1', null, '3', null, '1', ''), ('159', 'tload', '数字', '打点load时间', '1', null, '3', null, '1', ''), ('160', 'thead', '数字', '打点头部时间', '1', null, '3', null, '1', '');
COMMIT;

-- ----------------------------
-- Table structure for `buried_point0`
-- ----------------------------
DROP TABLE IF EXISTS `buried_point0`;
CREATE TABLE `buried_point0` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT 'ID自增长' ,
`bp_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '埋点-名称' ,
`bp_value`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '埋点-内容' ,
`bp_value_desc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '埋点-内容-类型' ,
`navigation_id`  int(12) NOT NULL COMMENT '导航栏-ID' ,
`is_checked`  tinyint(1) NOT NULL COMMENT '是否为必填项' ,
`regex`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '保存正则表达式规则' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=458

;

-- ----------------------------
-- Records of buried_point0
-- ----------------------------
BEGIN;
INSERT INTO `buried_point0` VALUES ('270', 'EI', '数字', '企业ID', '24', '0', '1234'), ('271', 'EnterpriseID', '数字', '企业ID', '15', '1', ''), ('272', 'M2', '文本', '用户ID', '11', '0', ''), ('273', 'IP', '文本', 'IP地址', '16', '1', '*(&987'), ('274', 'M3', '文本', '设备ID', '11', '1', ''), ('275', 'IP', '文本', '客户端来源IP', '15', '0', ''), ('276', 'UserID', '数字', '员工ID', '15', '0', ''), ('277', 'Platform', '数字', '终端平台类型', '15', '0', ''), ('278', 'OSVersion', '文本', '操作系统版本', '15', '0', ''), ('279', 'ProductVersion', '文本', '产品版本', '15', '0', ''), ('280', 'Action', '文本', '访问接口', '15', '0', ''), ('281', 'Time', '日期', '访问时间', '15', '1', ''), ('282', 'Duration', '数字', '访问耗时', '15', '0', ''), ('283', 'DeviceID', '文本', '设备ID', '15', '0', ''), ('284', 'ServiceType', '数字', '服务类型', '15', '0', ''), ('285', 'M4', '文本', '操作系统 （Android/iOS）', '11', '1', ''), ('286', 'M5', '文本', '操作系统版本', '11', '1', ''), ('287', 'M6', '文本', '纷享逍客应用版本号（5.1.0/5.2.0）', '11', '1', ''), ('288', 'M7', '文本', '纷享销客应用内部版本号', '11', '1', ''), ('289', 'M8', '文本', '设备型号', '11', '1', ''), ('290', 'M96', '文本', '网络类型', '11', '1', ''), ('291', 'M97', '文本', '事件类型', '11', '1', ''), ('292', 'M98', '日期', '事件发生时间', '11', '1', ''), ('294', 'M99.M1', '文本', '应用事件ID', '11', '1', ''), ('295', 'M2', '数字', '用户ID', '12', '1', ''), ('296', 'M1', '文本', '企业ID', '12', '1', ''), ('297', 'M3', '文本', '设备ID', '12', '1', ''), ('298', 'M4', '文本', '操作系统 （Android/iOS）', '2', '1', ''), ('299', 'M5', '文本', '操作系统版本', '12', '1', ''), ('300', 'M6', '文本', '纷享逍客应用版本号（5.1.0/5.2.0）', '12', '1', ''), ('301', 'M7', '文本', '纷享销客应用内部版本号', '12', '1', ''), ('302', 'M8', '文本', '设备型号', '12', '1', ''), ('303', 'M96', '文本', '网络类型', '12', '1', ''), ('304', 'M97', '文本', '事件类型', '12', '1', ''), ('305', 'M98', '日期', '事件发生时间', '12', '1', ''), ('306', 'M99', '文本', '事件自定义字段', '12', '1', ''), ('307', 'M99.M1', '文本', '应用事件ID', '12', '0', ''), ('308', '_fuserid', '数字', '用户id', '16', '1', ''), ('309', '_fuserName', '文本', '用户姓名', '16', '1', ''), ('310', '_fcompanyid', '文本', '企业id', '16', '1', ''), ('311', '_fcompanyname', '文本', '企业名称', '16', '1', ''), ('313', '_fbrowser', '数字', '浏览器类型:1-ie8;2-1e9;3-ie10;4-ie11;5-safari;6-opera;7-chrome;8-liebao;9-qq;10-sougou;11-edge;100-other', '16', '1', ''), ('314', 'actionid', '文本', '统计的key，表示某条统计点', '16', '1', ''), ('315', '_fuserid', '数字', '用户id', '18', '1', ''), ('316', '_fuserName', '文本', '用户姓名', '18', '1', ''), ('317', '_fcompanyid', '文本', '企业id', '18', '1', ''), ('318', '_fcompanyname', '文本', '企业名称', '18', '1', ''), ('319', 'actionid', '文本', '统计key，标明某个统计项', '18', '1', ''), ('320', '_source', '文本', '来源，写死pc', '18', '1', ''), ('321', '_platform', '文本', '平台 win || mac', '18', '1', ''), ('322', '_version', '文本', '版本号', '18', '1', ''), ('323', '_osinfo', '文本', '系统详细信息', '18', '1', ''), ('324', 'projectId', '文本', '项目id', '17', '1', ''), ('325', 'appid', '文本', 'app id  来自开平', '17', '1', ''), ('326', 'appversion', '文本', '版本', '17', '1', ''), ('327', 'actionId', '文本', '统计key，表明某条统计项', '17', '1', ''), ('329', '_fplatform', '文本', '平台  pc || android || ios', '17', '1', ''), ('330', '_fversion', '文本', '终端系统版本号', '17', '1', ''), ('335', 'Browser', '文本', '浏览器', '15', '0', ''), ('336', 'BrowserVersion', '文本', '浏览器版本', '15', '0', ''), ('337', 'M1', '文本', '企业id', '11', '1', ''), ('338', 'EID', '数字', '公司id', '59', '1', ''), ('339', 'EA', '文本', '公司名称', '59', '1', ''), ('340', 'UID', '数字', '用户id', '59', '1', ''), ('341', 'IP', '文本', 'ip地址', '59', '0', ''), ('342', 'PF', '文本', '平台类型', '59', '0', ''), ('343', 'OSV', '文本', '操作系统版本', '59', '0', ''), ('344', 'PROV', '文本', '产品内部版本', '59', '0', ''), ('345', 'AC', '文本', '操作行为', '59', '0', ''), ('346', 'LUT', '日期', '日志上报时间', '59', '0', ''), ('347', 'D', '数字', '时长', '59', '0', ''), ('348', 'DID', '文本', '物理设备id', '59', '0', ''), ('349', 'T', '数字', '应用编号', '59', '0', ''), ('350', 'P.WorkItemType', '数字', 'IGT类型', '59', '0', ''), ('351', 'P.ParticipantNum', '数字', '群通知人数', '59', '0', ''), ('352', 'P.Platform', '文本', '群通知平台', '59', '0', ''), ('398', 'EID', '数字', '公司id', '65', '1', ''), ('399', 'EA', '文本', '公司名称', '65', '1', ''), ('400', 'UID', '数字', '用户id', '65', '1', ''), ('401', 'IP', '文本', 'ip地址', '65', '0', ''), ('402', 'PF', '文本', '平台类型', '65', '0', ''), ('403', 'OSV', '文本', '操作系统版本', '65', '0', ''), ('404', 'PROV', '文本', '产品内部版本', '65', '0', ''), ('405', 'AC', '文本', '操作行为', '65', '0', ''), ('406', 'LUT', '日期', '日志上报时间', '65', '0', ''), ('407', 'D', '数字', '时长', '65', '0', ''), ('408', 'DID', '文本', '物理设备id', '65', '0', ''), ('409', 'T', '数字', '应用编号', '65', '0', ''), ('410', 'P.WorkItemType', '数字', 'IGT类型', '65', '0', ''), ('411', 'P.ParticipantNum', '数字', '群通知人数', '65', '0', ''), ('412', 'P.Platform', '文本', '群通知平台', '65', '0', ''), ('413', 'EID', '数字', '公司id', '66', '1', ''), ('414', 'EA', '文本', '公司名称', '66', '1', ''), ('415', 'UID', '数字', '用户id', '66', '1', ''), ('416', 'IP', '文本', 'ip地址', '66', '0', ''), ('417', 'PF', '文本', '平台类型', '66', '0', ''), ('418', 'OSV', '文本', '操作系统版本', '66', '0', ''), ('419', 'PROV', '文本', '产品内部版本', '66', '0', ''), ('420', 'AC', '文本', '操作行为', '66', '0', ''), ('421', 'LUT', '日期', '日志上报时间', '66', '0', '');
INSERT INTO `buried_point0` VALUES ('422', 'D', '数字', '时长', '66', '0', ''), ('423', 'DID', '文本', '物理设备id', '66', '0', ''), ('424', 'T', '数字', '应用编号', '66', '0', ''), ('425', 'P.WorkItemType', '数字', 'IGT类型', '66', '0', ''), ('426', 'P.ParticipantNum', '数字', '群通知人数', '66', '0', ''), ('427', 'P.Platform', '文本', '群通知平台', '66', '0', ''), ('428', 'p_domready', '数字', '用户可操作时间', '16', '1', ''), ('429', 'tload', '数字', '打点load时间', '16', '1', ''), ('430', 'thead', '数字', '打点头部时间', '16', '1', ''), ('433', 'enterpriseAccount', '文本', '企业标示', '73', '1', ''), ('434', 'sourceUser', '文本', '文件操作人', '73', '1', ''), ('435', 'size', '数字', '文件大小', '73', '1', ''), ('436', 'operation', '文本', '操作类型', '73', '1', ''), ('437', 'business', '文本', '业务来源', '73', '1', ''), ('438', 'fileExtension', '文本', '文件扩展名', '73', '1', ''), ('439', 'fileName', '文本', '文件名', '73', '1', ''), ('440', 'year', '数字', '年', '73', '1', ''), ('441', 'month', '文本', '月份', '73', '1', ''), ('442', 'day', '文本', '月几号', '73', '1', ''), ('443', 'hour', '文本', '当天第几时', '73', '1', ''), ('444', 'minute', '文本', '分', '73', '1', ''), ('445', 'quarter', '数字', '季度', '73', '1', ''), ('446', 'weekday', '数字', '周几', '73', '1', ''), ('447', 'statisticsTimestamp', '数字', '操作时间戳', '73', '1', ''), ('448', 'UserID', '文本', '用户ID', '12', '0', ''), ('449', 'M2', '文本', '用户ID', '12', '0', ''), ('452', 'M2', '文本', '用户ID', '11', '0', ''), ('453', 'M2', '数字', '用户ID', '12', '1', ''), ('454', 'EI', '数字', '企业ID', '76', '1', ''), ('455', 'EmployeeID', '数字', '用户ID', '76', '1', ''), ('456', 'ChartType', '文本', '报表类型', '76', '0', '');
COMMIT;

-- ----------------------------
-- Table structure for `database_biz`
-- ----------------------------
DROP TABLE IF EXISTS `database_biz`;
CREATE TABLE `database_biz` (
`id`  int(12) NOT NULL ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务名称' ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Records of database_biz
-- ----------------------------
BEGIN;
INSERT INTO `database_biz` VALUES ('1', '企信'), ('2', '协同'), ('3', 'CRM'), ('4', '汇聚');
COMMIT;

-- ----------------------------
-- Table structure for `database_info`
-- ----------------------------
DROP TABLE IF EXISTS `database_info`;
CREATE TABLE `database_info` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`biz_id`  int(12) NOT NULL COMMENT '业务ID' ,
`biz_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务名称' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义名称' ,
`db_ins`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库名称' ,
`db_type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库类型' ,
`db_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库URL' ,
`db_port`  int(12) NULL DEFAULT NULL COMMENT '端口号' ,
`db_driver`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库驱动' ,
`db_username`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名' ,
`db_password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码' ,
`db_encoding`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码' ,
`driver_clazz`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '驱动字节码' ,
`connect_param`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接参数' ,
`user_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID' ,
`last_update`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间' ,
PRIMARY KEY (`id`),
INDEX `fk_biz_id` USING BTREE (`biz_id`) 
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=14

;

-- ----------------------------
-- Records of database_info
-- ----------------------------
BEGIN;
INSERT INTO `database_info` VALUES ('1', '1', '企信', 'qx_db_con', null, 'QXDB001', 'MySQL', 'localhost', '3306', 'com.mysql.jdbc.Driver', 'root', 'root', 'UTF-8', null, null, null, '2016-04-19 19:31:25'), ('2', '2', '协同', 'xt_db_con', null, 'XTDB001', 'MySQL', 'localhost', '3306', 'com.mysql.jdbc.Driver', 'root', 'root', 'UTF-8', null, null, null, '2016-04-19 19:31:16'), ('3', '3', 'CRM', 'crm_db_con', null, 'FSCRM001', 'SQLServer', '172.31.103.14', '1433', 'net.sourceforge.jtds.jdbc.Driver', 'root', 'root', 'UTF-8', null, null, null, '2016-04-19 19:31:11'), ('5', '1', '企信', 'CRM_DB_CON', null, 'ibss', 'MySQL', '172.31.102.105', '3306', 'com.mysql.jdbc.Driver', 'wanpeng', 'wanpeng', null, null, null, null, '2016-04-19 19:31:08'), ('6', '1', '企信', 'CRM_DB_CON', null, 'R3-Query', 'MySQL', '172.31.102.105', '3306', 'com.mysql.jdbc.Driver', 'wanpeng', 'wanpeng', null, null, null, null, '2016-04-19 19:31:05'), ('7', '1', '企信', 'oss', null, 'oss', 'MySQL', '172.31.101.12', '3306', 'com.mysql.jdbc.Driver', 'fs', 'fsxiaoke', null, null, null, null, '2016-04-19 19:17:11'), ('12', '1', '企信', 'M33', null, 'MongoDB001', 'MongoDB', 'M33', '2223', 'M334', 'admin', 'pass', null, null, null, null, '2016-03-29 15:13:58');
COMMIT;

-- ----------------------------
-- Table structure for `job_info`
-- ----------------------------
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE `job_info` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`biz_id`  int(10) NULL DEFAULT NULL ,
`biz_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sou_id`  int(12) NULL DEFAULT NULL COMMENT '导出方式' ,
`sou_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`tar_id`  int(12) NULL DEFAULT NULL ,
`tar_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`exp_type`  varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`start_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`end_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`exe_time`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`memo`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
`update_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=26

;

-- ----------------------------
-- Records of job_info
-- ----------------------------
BEGIN;
INSERT INTO `job_info` VALUES ('19', '1', '企信', '啊', '5', 'zuoyemingcheng', '5', 'ES', 'all', '2016/04/06 19:46', '2016/04/06 19:46', '2016/04/05 19:46', '去', '2016-04-19 19:46:15', '2016-04-21 16:53:19'), ('21', '1', '企信', '饿', '5', 'zuoyemingcheng', '5', 'ES', 'add', '2016-04-11 10:01:00', '2016-04-12 10:01:00', '2016/04/23 10:01', '', '2016-04-20 10:01:30', null), ('18', '1', '企信', '导入测试', '7', 'oss', '9', 'ES导入', 'add', '2014-04-07 15:00:00', '2016-04-19 18:00:00', '2016/04/13 17:09', '无', '2016-04-19 17:09:24', '2016-04-19 17:47:55'), ('22', '1', '企信', '5', '5', 'zuoyemingcheng', '9', 'ES导入', 'add', '2016-04-12 16:49:00', '2016-03-29 16:49:00', '2016/04/19 16:49', '55', '2016-04-21 16:49:17', null), ('23', '1', '企信', '6', '2', '3', '4', 'HDFS', 'add', '2016-04-05 17:09:00', '2016-03-02 17:09:00', '2016/02/29 17:09', '6', '2016-04-21 17:09:49', null), ('24', '1', '企信', 'student数据导入HDFS', '7', 'oss', '10', 'student数据导入HDFS', 'all', null, null, '2016/04/21 18:00', '', '2016-04-21 17:21:18', null), ('25', '1', '企信', 'student导入ES', '7', 'oss', '11', 'student导入ES', 'add', '2016-04-14 18:00:00', '2016-04-21 20:00:00', '2016/04/21 19:00', '', '2016-04-21 19:22:45', null);
COMMIT;

-- ----------------------------
-- Table structure for `job_source`
-- ----------------------------
DROP TABLE IF EXISTS `job_source`;
CREATE TABLE `job_source` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_source_id`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_source_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`select_data`  varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`biz_id`  int(11) NULL DEFAULT NULL ,
`biz_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`memo`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`select_table`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
`update_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=12

;

-- ----------------------------
-- Records of job_source
-- ----------------------------
BEGIN;
INSERT INTO `job_source` VALUES ('7', 'oss', '7', 'oss_db_con', '{\"t_test02\":[{\"par\":0,\"tfd\":0,\"des\":0,\"pri\":1,\"name\":\"id\",\"type\":\"INT\",\"exp\":1,\"inc\":1},{\"par\":0,\"tfd\":0,\"des\":0,\"pri\":0,\"name\":\"name\",\"type\":\"VARCHAR\",\"exp\":1,\"inc\":0},{\"par\":0,\"tfd\":0,\"des\":0,\"pri\":0,\"name\":\"age\",\"type\":\"VARCHAR\",\"exp\":1,\"inc\":0},{\"par\":0,\"tfd\":1,\"des\":0,\"pri\":0,\"name\":\"updateTime\",\"type\":\"DATETIME\",\"exp\":1,\"inc\":0}],\"t_test01\":[{\"par\":0,\"tfd\":0,\"des\":0,\"pri\":1,\"name\":\"id\",\"type\":\"INT\",\"exp\":1,\"inc\":1},{\"par\":0,\"tfd\":0,\"des\":0,\"pri\":0,\"name\":\"name\",\"type\":\"VARCHAR\",\"exp\":1,\"inc\":0},{\"par\":0,\"tfd\":0,\"des\":0,\"pri\":0,\"name\":\"age\",\"type\":\"VARCHAR\",\"exp\":1,\"inc\":0}]}', '1', '企信', 'oss', 't_test01,t_test02', '2016-04-06 11:53:19', '2016-04-19 18:07:35'), ('2', '3', '1', 'qx_db_con', null, '1', '企信', '3', null, '2016-04-05 20:48:20', null), ('5', 'zuoyemingcheng', '5', 'CRM_DB_CON', '{\"ACT_GE_BYTEARRAY\":[{\"par\":0,\"des\":0,\"name\":\"ID_\",\"type\":\"VARCHAR\",\"exp\":1},{\"par\":0,\"des\":0,\"name\":\"REV_\",\"type\":\"INT\",\"exp\":1},{\"par\":0,\"des\":0,\"name\":\"NAME_\",\"type\":\"VARCHAR\",\"exp\":1},{\"par\":0,\"des\":0,\"name\":\"DEPLOYMENT_ID_\",\"type\":\"VARCHAR\",\"exp\":1},{\"par\":0,\"des\":0,\"name\":\"BYTES_\",\"type\":\"LONGBLOB\",\"exp\":1},{\"par\":0,\"des\":0,\"name\":\"GENERATED_\",\"type\":\"TINYINT\",\"exp\":1}]}', '1', '企信', 'beizhu', 'ACT_EVT_LOG,ACT_GE_BYTEARRAY', '2016-04-05 20:53:28', null);
COMMIT;

-- ----------------------------
-- Table structure for `job_target`
-- ----------------------------
DROP TABLE IF EXISTS `job_target`;
CREATE TABLE `job_target` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`biz_id`  int(10) NULL DEFAULT NULL ,
`biz_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出方式' ,
`ip`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`port`  int(12) NULL DEFAULT NULL ,
`path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`memo`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=12

;

-- ----------------------------
-- Records of job_target
-- ----------------------------
BEGIN;
INSERT INTO `job_target` VALUES ('11', '1', '企信', 'student导入ES', 'ES', '172.31.101.12', '3306', 'oss_es', ''), ('4', '1', '企信', 'HDFS', 'HDFS', '117.114.121.14', '10000', '/facishare-data/data/', '4'), ('5', '1', '企信', 'ES', 'ES', '125.111.14.8', '10000', '/use/hive/db/', '5'), ('9', '1', '企信', 'ES导入', 'ES', '127.0.0.1', '1111', 'dcx.MonitorRequest', '无'), ('10', '1', '企信', 'student数据导入HDFS', 'HDFS', '172.31.101.12', '3306', '/user/jijz/fromdb/mysql/oss/t_student', '');
COMMIT;

-- ----------------------------
-- Table structure for `level_one_fields`
-- ----------------------------
DROP TABLE IF EXISTS `level_one_fields`;
CREATE TABLE `level_one_fields` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '一级字段-ID(主键)' ,
`level1_field_tag`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级字段-标识' ,
`level1_field_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级字段-名称' ,
`level1_field_desc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级字段-描述' ,
`navigation_id`  int(11) NULL DEFAULT 0 ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=71

;

-- ----------------------------
-- Records of level_one_fields
-- ----------------------------
BEGIN;
INSERT INTO `level_one_fields` VALUES ('39', 'AV', '音视频', '', '11'), ('40', 'Register', '注册', '', '11'), ('41', 'Attendance', '考勤签到', '', '11'), ('42', 'FieldLocation', '外勤签到', '', '11'), ('43', 'function', '应用', '', '11'), ('44', 'setting', '设置', '', '11'), ('45', 'Login', '登录', '', '11'), ('46', 'Location', '定位', '', '11'), ('47', 'Session', '企信', '', '11'), ('48', 'PlugLoad', '插件加载', '', '11'), ('49', 'Contact', '通讯录', '', '11'), ('50', 'SessionService', '企信服务号', '', '11'), ('51', 'Crash', '崩溃信息', '', '11'), ('52', 'Approve', '审批统计', '', '11'), ('53', 'Meeting', '会议助手', '', '11'), ('54', 'r88', 'r88_mcD', 'r88_msD11', '11'), ('56', 'T9', 'T9_mc', 'T9_ms', '0'), ('57', 'T9', 'T9_mc', 'T9_ms', '0'), ('58', 'T43', 'T43_mc', 'T43_ms', '11'), ('63', 'lifecycle', '页面生命周期打点', '', '11'), ('66', 'performance', '性能监控', '', '17'), ('67', 'warbreief', '战报助手', '', '17'), ('68', 'TrainingAssistant', '培训助手', '', '11'), ('69', 'train', '培训助手埋点', '', '17'), ('70', 'TrainingAssistant', '培训助手', '', '12');
COMMIT;

-- ----------------------------
-- Table structure for `level_two_fields`
-- ----------------------------
DROP TABLE IF EXISTS `level_two_fields`;
CREATE TABLE `level_two_fields` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '二级字段-ID(主键)' ,
`level1_field_id`  int(12) NOT NULL COMMENT '一级字段-ID(外键)' ,
`level1_field_tag`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级字段-标识' ,
`level1_field_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级字段-名称' ,
`level2_field_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级字段-名称' ,
`level2_field_desc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级字段-描述' ,
PRIMARY KEY (`id`),
INDEX `2nd_fields_fk1` USING BTREE (`level1_field_id`) 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=214

;

-- ----------------------------
-- Records of level_two_fields
-- ----------------------------
BEGIN;
INSERT INTO `level_two_fields` VALUES ('1', '40', 'Register_1', '注册', '验证码点击次数', ''), ('2', '40', 'Register_2', '注册', '用户查看注册协议次数', ''), ('3', '40', 'Register_3', '注册', '使用国际区号(非+86)注册次数', ''), ('4', '40', 'Register_4', '注册', '用户点击“注册”次数', ''), ('5', '40', 'Register_5', '注册', '优惠码使用次数', ''), ('6', '40', 'Register_6', '注册', '用户点击“新建新企业”次数', ''), ('7', '40', 'Register_7', '注册', '销客400电话本页唤起次数', ''), ('8', '40', 'Register_8', '注册', '用户点击“登录我的企业”次数', ''), ('9', '41', 'Attendance_9', '考勤签到', '服务失败（失败原因或异常信息）', ''), ('10', '41', 'Attendance_10', '考勤签到', '定位超时失败', ''), ('11', '41', 'Attendance_11', '考勤签到', '点击考勤签到按钮（带距离偏差，和网络状态，签到信息参数）', ''), ('12', '39', 'AV_12', '音视频', '群音视频通话点击次数', ''), ('13', '39', 'AV_13', '音视频', '双人音视频通话点击次数', ''), ('14', '41', 'Attendance_14', '考勤签到', '考勤签到成功', ''), ('15', '41', 'Attendance_15', '考勤签到', '考勤签到失败', ''), ('16', '41', 'Attendance_16', '考勤签到', '点击考勤签到', ''), ('17', '41', 'Attendance_17', '考勤签到', '点击考勤签退', ''), ('18', '42', 'FieldLocation_18', '外勤签到', '外勤签到成功', ''), ('19', '42', 'FieldLocation_19', '外勤签到', '外勤签到失败', ''), ('20', '42', 'FieldLocation_20', '外勤签到', '点击外勤签到', ''), ('21', '41', 'Attendance_21', '考勤签到', 'IOS切换引擎', ''), ('22', '41', 'Attendance_22', '考勤签到', '考勤提交审批', ''), ('23', '41', 'Attendance_23', '考勤签到', '考勤IOS纠偏失败', ''), ('24', '42', 'FieldLocation_24', '外勤签到', '外勤反编码失败', ''), ('25', '42', 'FieldLocation_25', '外勤签到', '外勤IOS纠偏失败', ''), ('26', '41', 'Attendance_26', '考勤签到', '考勤不在范围内', ''), ('27', '41', 'Attendance_27', '考勤签到', '考勤定位超时失败', ''), ('28', '42', 'FieldLocation_28', '外勤签到', '外勤定位超时失败', ''), ('30', '41', 'Attendance_30', '考勤签到', '考勤签退成功', ''), ('31', '41', 'Attendance_31', '考勤签到', '考勤签退失败', ''), ('32', '43', 'function_32', '应用', '更多应用', ''), ('33', '43', 'function_33', '应用', '更多应用（无试用机会）', ''), ('34', '44', 'setting_34', '设置', '关于分享销客', ''), ('35', '46', 'Location_35', '定位', '定位成功', ''), ('36', '46', 'Location_36', '定位', '定位失败', ''), ('38', '42', 'FieldLocation_38', '外勤签到', '外勤点击重新定位', ''), ('39', '41', 'Attendance_39', '考勤签到', '考勤点击重新定位', ''), ('40', '47', 'Session_40', '企信', 'Session_1', ''), ('41', '47', 'Session_41', '企信', '红包表情单击事件', ''), ('42', '47', 'Session_42', '企信', '红包表情长按事件', ''), ('43', '47', 'Session_43', '企信', '企信搜索语音输入中，小话筒点击次数', ''), ('44', '47', 'Session_44', '企信', '企信搜索语音输入中，小话筒点击次数', ''), ('45', '47', 'Session_45', '企信', '企信搜索语音输入中，清空按钮的点击次数', ''), ('46', '47', 'Session_46', '企信', '搜索对话记录点击次数', ''), ('47', '47', 'Session_47', '企信', '一键邀请同事点击次数', ''), ('48', '47', 'Session_48', '企信', '几种邀请方式的使用次数', ''), ('49', '47', 'Session_49', '企信', '新人审核点击次数', ''), ('50', '47', 'Session_50', '企信', '模拟帐号体验点击次数', ''), ('51', '47', 'Session_51', '企信', '短信验证码授权链接点击次数', ''), ('52', '47', 'Session_52', '企信', '2分钟弹窗提示的确定点击次数', ''), ('53', '47', 'Session_53', '企信', '取消授权二次确认点击次数', ''), ('54', '47', 'Session_54', '企信', '强制下线二次确认点击次数', ''), ('55', '47', 'Session_55', '企信', '修改密码点击次数', ''), ('56', '47', 'Session_56', '企信', '设备管理点击次数', ''), ('57', '47', 'Session_57', '企信', '登录日志点击次数', ''), ('58', '47', 'Session_58', '企信', '问号引导的点击', ''), ('59', '47', 'Session_59', '企信', '登录日志中修改密码的链接点击', ''), ('60', '47', 'Session_60', '企信', '扫码登录点击次数', ''), ('61', '43', 'function_61', '应用', 'function_10001', ''), ('62', '43', 'function_62', '应用', 'banner点击', ''), ('63', '43', 'function_63', '应用', '我的应用列表应用点击', ''), ('64', '43', 'function_64', '应用', '精品推荐营销套件点击', ''), ('65', '43', 'function_65', '应用', '精品推荐应用点击', ''), ('66', '43', 'function_66', '应用', '更多应用页面访问量', ''), ('67', '43', 'function_67', '应用', '更多应用列表项点击', ''), ('68', '47', 'Session_68', '企信', '邀请方式中微信邀请的次数', ''), ('69', '47', 'Session_69', '企信', '邀请方式中短信邀请的次数', ''), ('70', '47', 'Session_70', '企信', '邀请方式中邮箱邀请的次数', ''), ('71', '47', 'Session_71', '企信', '邀请方式中复制邀请链接的次数', ''), ('72', '43', 'function_72', '应用', '更多应用列表项添加按钮点击次数', ''), ('73', '48', 'PlugLoad_73', '插件加载', 'PlugLoad_1', ''), ('74', '48', 'PlugLoad_74', '插件加载', 'PlugLoad_dex', ''), ('75', '48', 'PlugLoad_75', '插件加载', 'PlugLoad_regist', ''), ('76', '48', 'PlugLoad_76', '插件加载', 'PlugLoad_build_fs', ''), ('77', '48', 'PlugLoad_77', '插件加载', 'PlugLoad_build_crm', ''), ('78', '48', 'PlugLoad_78', '插件加载', 'PlugLoad_build_pay', 'TEST'), ('79', '49', 'Contact_79', '通讯录', 'Contact_db_wirte', ''), ('80', '49', 'Contact_80', '通讯录', 'Contact_db_read', ''), ('81', '50', 'SessionService_81', '企信服务号', '进入服务号', ''), ('82', '50', 'SessionService_82', '企信服务号', '服务号消息点击事件', ''), ('83', '50', 'SessionService_83', '企信服务号', '服务号菜单点击事件', ''), ('84', '50', 'SessionService_84', '企信服务号', '服务号消息发送操作', ''), ('86', '41', 'Attendance_86', '考勤签到', 'Attendance_12', ''), ('87', '41', 'Attendance_87', '考勤签到', '发送签到-向服务器端发起签到请求', ''), ('88', '41', 'Attendance_88', '考勤签到', '签到失败-业务异常-未设置考勤规则-code=132', ''), ('89', '41', 'Attendance_89', '考勤签到', '签到失败-业务异常-不在考勤时间范围-code=135/137', ''), ('90', '41', 'Attendance_90', '考勤签到', '签到失败-业务异常-重复签到-code=136', ''), ('91', '41', 'Attendance_91', '考勤签到', '签到失败-网络请求超时', ''), ('92', '41', 'Attendance_92', '考勤签到', '签到失败-服务器异常', ''), ('93', '41', 'Attendance_93', '考勤签到', '发送签退-向服务器端发起签退请求', ''), ('94', '41', 'Attendance_94', '考勤签到', '签退失败-业务异常-未设置考勤规则-code=132', ''), ('95', '41', 'Attendance_95', '考勤签到', '签退失败-业务异常-不在考勤时间范围-code=135/137', ''), ('96', '41', 'Attendance_96', '考勤签到', '签退失败-业务异常-重复签到-code=136', ''), ('97', '41', 'Attendance_97', '考勤签到', '签退失败-网络请求超时', ''), ('98', '41', 'Attendance_98', '考勤签到', '签退失败-服务器异常', ''), ('99', '41', 'Attendance_99', '考勤签到', '签到提示-不在考勤地点范围内（要上报用户经纬度坐标，规则经纬度坐标，规则误差距离）', ''), ('100', '41', 'Attendance_100', '考勤签到', '签退提示-不在考勤地点范围内（要上报用户经纬度坐标，规则经纬度坐标，规则误差距离）', ''), ('102', '42', 'FieldLocation_102', '外勤签到', '外勤签到网络异常', ''), ('103', '42', 'FieldLocation_103', '外勤签到', '外勤签到超时', ''), ('104', '51', 'Crash_104', '崩溃信息', '崩溃日志', '');
INSERT INTO `level_two_fields` VALUES ('105', '48', 'PlugLoad_105', '插件加载', 'PlugLoad_build_avcall', ''), ('106', '49', 'Contact_106', '通讯录', '通讯录导出埋点统计', ''), ('108', '43', 'function_108', '应用', 'fdfdsfdsfdfdfds', ''), ('109', '43', 'function_109', '应用', '编辑按钮点击', ''), ('110', '43', 'function_110', '应用', '应用管理应用点击', ''), ('111', '43', 'function_111', '应用', '应用管理 取消按钮', ''), ('112', '43', 'function_112', '应用', '应用管理 拖动', ''), ('113', '43', 'function_113', '应用', '应用管理隐藏', ''), ('114', '43', 'function_114', '应用', '应用管理显示', ''), ('116', '43', 'function_116', '应用', '应用管理页面访问量', ''), ('119', '52', 'Approve_119', '审批统计', '新建普通审批的次数', ''), ('120', '52', 'Approve_120', '审批统计', '新建请假单的次数', ''), ('121', '52', 'Approve_121', '审批统计', '新建报销单的次数', ''), ('122', '52', 'Approve_122', '审批统计', '新建差旅单的次数', ''), ('123', '52', 'Approve_123', '审批统计', '新建借款单的次数', ''), ('124', '52', 'Approve_124', '审批统计', '新建付款单的次数', ''), ('125', '52', 'Approve_125', '审批统计', '新建折扣申请的次数', ''), ('126', '52', 'Approve_126', '审批统计', '批复普通审批的次数', ''), ('127', '52', 'Approve_127', '审批统计', '批复请假单的次数', ''), ('128', '52', 'Approve_128', '审批统计', '批复报销单的次数', ''), ('129', '52', 'Approve_129', '审批统计', '批复差旅单的次数', ''), ('130', '52', 'Approve_130', '审批统计', '批复借款单的次数', ''), ('131', '52', 'Approve_131', '审批统计', '批复付款单的次数', ''), ('132', '52', 'Approve_132', '审批统计', '批复折扣申请的次数', ''), ('133', '52', 'Approve_133', '审批统计', '同意时关注了审批的人数', ''), ('134', '52', 'Approve_134', '审批统计', '同意时“更换了审批人”的人数', ''), ('135', '52', 'Approve_135', '审批统计', '操作”换审批人“的人数', ''), ('136', '41', 'Attendance_136', '考勤签到', '进入考勤界面', ''), ('137', '42', 'FieldLocation_137', '外勤签到', '进入外勤签到界面', ''), ('138', '52', 'Approve_138', '审批统计', '打开发送审批页面次数', ''), ('139', '52', 'Approve_139', '审批统计', '多少人点击审批制度', ''), ('140', '52', 'Approve_140', '审批统计', '多少人添加了多级审批人', ''), ('141', '52', 'Approve_141', '审批统计', '多少人换了审批人', ''), ('142', '53', 'Meeting_142', '会议助手', '企信“+”中的会议新建点击次数', ''), ('143', '53', 'Meeting_143', '会议助手', '会议助手列表页：会议新建点击次数', ''), ('144', '53', 'Meeting_144', '会议助手', '会议编辑页面:保存按钮点击次数', ''), ('145', '53', 'Meeting_145', '会议助手', '会议编辑:时间修改点击次数(包含开始时间和结束时间)', ''), ('146', '53', 'Meeting_146', '会议助手', '会议编辑:参会人员调整次数(点击进编辑界面就统计)', ''), ('147', '53', 'Meeting_147', '会议助手', '会议管理员调整次数(点击进编辑界面就统计)', ''), ('148', '53', 'Meeting_148', '会议助手', '会议编辑页面:议程调整次数(点击进编辑界面就统计)', ''), ('149', '53', 'Meeting_149', '会议助手', '会议详情:签到统计查看次数', ''), ('150', '53', 'Meeting_150', '会议助手', '会议详情:签到位置的点击次数', ''), ('151', '53', 'Meeting_151', '会议助手', '企信“+”中的会场签到点击次数', ''), ('152', '53', 'Meeting_152', '会议助手', '会议签到:未签到页签切换(切换到未签到的次数)', ''), ('153', '53', 'Meeting_153', '会议助手', '会议详情:人员变更记录点击次数', ''), ('154', '53', 'Meeting_154', '会议助手', '会议详情:会议提问功能入口的点击次数', ''), ('155', '53', 'Meeting_155', '会议助手', '企信“+”中的会议提问点击次数', ''), ('156', '53', 'Meeting_156', '会议助手', '企信群中:提问推送的小灰条中前往查看的点击次数', ''), ('157', '53', 'Meeting_157', '会议助手', '群设置:会议历史的查看次数', ''), ('158', '53', 'Meeting_158', '会议助手', '群组加号:图片点击次数', ''), ('159', '53', 'Meeting_159', '会议助手', '群组加号:位置点击次数', ''), ('160', '53', 'Meeting_160', '会议助手', '群组加号:红包点击次数', ''), ('161', '53', 'Meeting_161', '会议助手', '群组加号:日程点击次数', ''), ('162', '53', 'Meeting_162', '会议助手', '群组加号:任务点击次数', ''), ('163', '53', 'Meeting_163', '会议助手', '群组加号:文档点击次数', ''), ('164', '53', 'Meeting_164', '会议助手', '会议状态中退出群组的操作', ''), ('165', '53', 'Meeting_165', '会议助手', '会议状态中拉人进群的次数', ''), ('166', '53', 'Meeting_166', '会议助手', '会议状态中移人出群的次数', ''), ('167', '53', 'Meeting_167', '会议助手', '会议详情:会议资料入口点击次数', ''), ('168', '53', 'Meeting_168', '会议助手', '会议详情:会议纪要入口点击次数', ''), ('169', '53', 'Meeting_169', '会议助手', '会议详情:会议议程附件预览次数', ''), ('170', '53', 'Meeting_170', '会议助手', '会议待办列表页:会议代办新建次数', ''), ('171', '53', 'Meeting_171', '会议助手', '会议详情:会议转发到企信群次数', ''), ('172', '53', 'Meeting_172', '会议助手', '会议详情页面:会议转发到分享次数', ''), ('173', '48', 'PlugLoad_173', '插件加载', 'Pl', ''), ('174', '39', 'AV', '音视频', '点击通话177', '通话177'), ('175', '43', 'function_175', '应用', '应用中心impression', ''), ('176', '39', 'AV', '音视频', 'AV_mc', 'AV_ms'), ('178', '54', 'r88', 'r88_mcD', 'r3_mc', 'r3_ms'), ('179', '39', 'AV', '音视频', 'DC_DC', 'W_W'), ('180', '54', 'r88', 'r88_mcD', 'r2_mc', 'r2_ms'), ('181', '54', 'r88', 'r88_mcD', 'r11_mc', 'r11_ms'), ('182', '58', '12_id', 'T43_mc', '12_mc', '12_ms'), ('183', '58', 'T1t_ID', 'T43_mc', 'T1t_MC', 'T1t_MS'), ('185', '63', 'lifecycle', '页面生命周期打点', '页面生命周期', ''), ('186', '54', '12', 'r88_mcD', '12', '12'), ('187', '39', 'TEST', '音视频', 'TTT', 'TC'), ('188', '66', 'loadtime', '性能监控', '首屏耗时', ''), ('189', '66', 'jserror', '性能监控', 'js错误和异常监控', ''), ('190', '66', 'api', '性能监控', '接口请求耗时', ''), ('191', '66', 'apierror', '性能监控', '接口请求错误', ''), ('192', '67', 'warbreief_callPay', '战报助手', '点击赏并发起付款', ''), ('193', '67', 'warbreief_selectUserWhenAward', '战报助手', '勾选掉签单人', ''), ('194', '67', 'warbreief_refreshPayMoney', '战报助手', '金额刷新', ''), ('195', '67', 'warbreief_btnDelWarClick', '战报助手', '删除按钮的点击', ''), ('196', '67', 'warbreief_btnAwardClick', '战报助手', '赏大按钮的点击', ''), ('197', '67', 'warbreief_paySuccess', '战报助手', '赏成功', ''), ('198', '67', 'warbreief_supportSuccess', '战报助手', '赞成功', ''), ('199', '67', 'warbreief_replySuccess', '战报助手', '回复成功', ''), ('200', '67', 'warbreief_btnAwardClick', '战报助手', '赏的点击', ''), ('201', '67', 'warbreief_btnReplyClick', '战报助手', '回复的点击', ''), ('202', '67', 'warbreief_searchWars', '战报助手', '搜索的使用', ''), ('203', '67', 'warbreief_btnSendClick', '战报助手', '立即发送的点击', ''), ('204', '67', 'warbreief_btnNpSendClick', '战报助手', '预览并发送的点击', ''), ('205', '67', 'warbreief_btnNewWarClick', '战报助手', '新建按钮点击', ''), ('206', '68', 'Training_206', '培训助手', 'ios端上传视频课件的数量', ''), ('207', '68', 'Training_207', '培训助手', 'ios端查看课程的次数', ''), ('208', '68', 'Training_208', '培训助手', 'ios端参加考试的次数', ''), ('209', '68', 'Training_209', '培训助手', 'ios端视频上传失败', ''), ('210', '68', 'Training_210', '培训助手', 'ios端视频播放失败', '');
INSERT INTO `level_two_fields` VALUES ('211', '68', 'Training_211', '培训助手', 'ios端H5加载失败', '详细信息中需要拼接哪个界面失败'), ('212', '68', 'Training_212', '培训助手', 'ios培训助手网络请求失败', ''), ('213', '70', 'Training_209', '培训助手', 'android端上传视频失败', '');
COMMIT;

-- ----------------------------
-- Table structure for `m99_fields`
-- ----------------------------
DROP TABLE IF EXISTS `m99_fields`;
CREATE TABLE `m99_fields` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`level_one_id`  int(12) NULL DEFAULT NULL COMMENT 'M99.M1-ID' ,
`level_two_id`  int(12) NULL DEFAULT NULL ,
`m1_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'M99.M1-名称' ,
`field_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'M99-字段' ,
`field_desc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'M99-描述' ,
`field_type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型' ,
`field_regex`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '正则表达式' ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=94

;

-- ----------------------------
-- Records of m99_fields
-- ----------------------------
BEGIN;
INSERT INTO `m99_fields` VALUES ('2', '39', '174', 'AV', 'M3', 'asasd', '数字', '*))(*'), ('4', '39', '174', 'AV', 'M2', 'm2_ms', '文本', 'a13)*)&+'), ('76', '43', '62', 'function', 'M4', 'banner类型，1代表h5, 2代表应用', '数字', ''), ('9', '41', '14', 'Attendance', 'M2', '坐标经、纬度', '文本', ''), ('71', '54', '178', 'r88', '弗罗多', '魔戒', '数字', '霍比特人'), ('11', '41', '14', 'Attendance', 'M4', 'HTTP返回码', '数字', ''), ('12', '41', '14', 'Attendance', 'M5', '时间戳', '日期', ''), ('13', '41', '14', 'Attendance', 'M5', null, '日期', null), ('14', '41', '14', 'Attendance', 'M6', null, '文本', '@#'), ('15', '41', '14', 'Attendance', 'M7', null, '日期', '(_+*'), ('16', '41', '14', 'FieldLocation', 'M0', '外勤签到', '数字', 'g'), ('17', '41', '14', 'FieldLocation', 'M2', null, '数字', null), ('18', '41', '14', 'FieldLocation', 'M3', null, '文本', 'A'), ('19', '41', '14', 'FieldLocation', 'M4', null, '数字', 'ASDF'), ('20', '41', '14', 'FieldLocation', 'M5', null, '文本', null), ('21', '41', '14', 'FieldLocation', 'M6', null, '日期', 'a'), ('22', '41', '14', 'FieldLocation', 'M7', null, '文本', null), ('23', '41', '14', 'FieldLocation', 'M8', null, '数字', '&#%'), ('24', '41', '14', 'FieldLocation', 'M9', null, '日期', null), ('25', '41', '14', 'function', 'M3', '应用', '文本', '^@'), ('26', '41', '14', 'function', 'M2', null, '文本', null), ('32', '41', '14', 'setting', 'F22', 'F22', '日期', 'F22'), ('28', '41', '14', 'Register', 'M9', 'M9', '文本', 'M9'), ('29', '41', '14', 'Register', 'M8', 'M8', '文本', 'M8'), ('30', '41', '14', 'Register', 'M7', 'M7', '文本', 'M7'), ('31', '41', '14', 'setting', 'M1', 'M1', '数字', 'M1'), ('33', '41', '14', 'setting', 'M3', 'M3', '文本', 'M3'), ('34', '41', '14', 'function', 'M3', 'M3', '日期', 'M3'), ('35', '41', '14', 'function', 'M1X', '注册1X', '文本', 'asdX'), ('36', '41', '14', 'Login', 'M1', 'M1', '文本', 'M1'), ('37', '41', '14', 'Login', 'M2', 'M2', '数字', 'M2'), ('38', '41', '14', 'Login', 'M5', 'M5', '日期', 'M5'), ('40', '41', '14', 'Login', 'M3', 'M3', '文本', 'M3'), ('39', '41', '14', 'Login', 'M4', 'M4', '数字', 'M4'), ('41', '41', '14', 'Login', 'M93', 'M93', '数字', 'M93'), ('42', '41', '14', 'Location', 'M1', 'M1', '日期', 'M1'), ('43', '41', '14', 'Location', 'M2', 'M2', '文本', 'M2'), ('44', '41', '14', 'Contact', 'M1', 'M1', '数字', 'M1'), ('45', '41', '14', 'Contact', 'M2', 'M2', '日期', 'M2'), ('46', '41', '14', 'Contact', 'M3', 'M3', '文本', 'M3'), ('47', '41', '14', 'Contact', 'M4', 'M4', '文本', 'M4'), ('48', '41', '14', 'SessionService', 'M11', 'M11', '日期', 'M11'), ('49', '41', '14', 'SessionService', 'M2', 'M2', '文本', 'M2'), ('50', '41', '14', 'SessionService', 'M3', 'M3', '日期', 'M3'), ('51', '41', '14', 'SessionService', 'M4', 'M4', '文本', 'M4'), ('52', '41', '14', 'Session', 'M1', 'M1', '文本', 'M1'), ('53', '41', '14', 'Session', 'M2', 'M2', '文本', 'M2'), ('54', '41', '14', 'Session', 'M3', 'M3', '数字', 'M3'), ('55', '41', '14', 'Session', 'M4', 'M4', '日期', 'M4'), ('56', '41', '14', 'PlugLoad', 'M1', 'M1', '文本', 'M1'), ('57', '41', '14', 'PlugLoad', 'M2', 'M2', '日期', 'M2'), ('58', '41', '14', 'PlugLoad', 'M3', 'M3', '数字', 'M3'), ('59', '41', '14', 'Meeting', 'M1', 'M1', '文本', 'M1'), ('60', '41', '14', 'Meeting', 'M2', 'M2', '日期', 'M2'), ('63', '39', '174', 'AV', 'M5', 'M5_ms', '日期', '(*^89-+'), ('75', '43', '62', 'function', 'M3', 'banner位置，以0为开始，从左向右计数', '数字', ''), ('74', '43', '62', 'function', 'M2', 'banner ID', '文本', ''), ('70', '39', '176', 'AV', 'ASDF', 'ASDF', '数字', 'ASDF'), ('62', '39', '174', 'AV', 'M4', 'M4_ms', '数字', 'www'), ('65', '39', '12', 'AV', 'M1', 'MAAS', '数字', '123#$!q@'), ('66', '39', '12', 'AV', 'M2', 'AS;LKDF', '数字', '65456'), ('77', '43', '62', 'function', 'M5', 'h5页面连接', '文本', ''), ('73', '54', '181', 'r88', 'AAD', 'ABD', '日期', 'ABCD'), ('78', '43', '62', 'function', 'M6', 'h5 banner的名称', '文本', ''), ('79', '43', '62', 'function', 'M7', '应用ID', '文本', ''), ('80', '43', '62', 'function', 'M8', '应用名称', '文本', ''), ('81', '58', '183', 'T43', 'M11_id', 'M11_ms', '数字', '11)(%$'), ('83', '63', '185', 'lifecycle', 'M2', '页面生命周期回调(oncreate|onstart|onstop)', '文本', ''), ('84', '63', '185', 'lifecycle', 'M3', '页面类名称', '文本', ''), ('85', '54', '181', 'r88', '12', '12', '数字', '12'), ('86', '66', '188', 'performance', 't_blank', '白屏时间', '数字', ''), ('87', '66', '188', 'performance', 't_ready', '页面准备完毕耗时', '数字', ''), ('88', '66', '188', 'performance', 't_load', '页面可用时间', '数字', ''), ('89', '66', '189', 'performance', 'msg', '错误和异常的描述内容', '数字', ''), ('90', '66', '190', 'performance', 'api', '接口名', '文本', ''), ('91', '66', '190', 'performance', 'time', '接口请求耗时', '数字', ''), ('92', '66', '191', 'performance', 'msg', '错误描述内容', '文本', ''), ('93', '70', '213', 'TrainingAssistant', 'M2', '视频上传失败原因', '文本', '');
COMMIT;

-- ----------------------------
-- Table structure for `navigation_item`
-- ----------------------------
DROP TABLE IF EXISTS `navigation_item`;
CREATE TABLE `navigation_item` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT 'ID自增长' ,
`parent_id`  int(12) NOT NULL DEFAULT 0 COMMENT '导航项-父-ID' ,
`parent_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导航项-父-名称' ,
`child_id`  int(12) NOT NULL DEFAULT 0 COMMENT '导航项-子-ID' ,
`child_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导航项-子-名称' ,
`grand_son_id`  int(12) NOT NULL COMMENT '导航项-孙子-ID' ,
`grand_son_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导航项-孙子-名称' ,
`topic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消费主题' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=10

;

-- ----------------------------
-- Records of navigation_item
-- ----------------------------
BEGIN;
INSERT INTO `navigation_item` VALUES ('1', '1', '前端埋点', '1', 'iOS', '1', 'iOS1', 'dcx.MonitorRequest'), ('2', '1', '前端埋点', '2', 'Android', '1', 'Android1', 'dcx.MonitorRequest'), ('3', '2', '后端埋点', '5', 'CEP', '1', 'CEP', 'cep.fs-cep-provider'), ('4', '2', '后端埋点', '1', '开平', '1', '开平-搜索', 'broker.feeds-search0'), ('5', '2', '后端埋点', '2', '协同', '1', '开平-搜索', 'broker.feeds-search1'), ('6', '1', '前端埋点', '6', 'PC客户端', '1', 'PC客户端', 'nginx.pgif'), ('7', '1', '前端埋点', '7', 'H5-WEB', '1', 'H5-WEB', 'nginx.mgif'), ('8', '1', '前端埋点', '3', '网站', '1', '网站', 'nginx.fgif'), ('9', '2', '后端埋点', '3', '企信', '1', null, 'broker.qixin');
COMMIT;

-- ----------------------------
-- Table structure for `navigation_item0`
-- ----------------------------
DROP TABLE IF EXISTS `navigation_item0`;
CREATE TABLE `navigation_item0` (
`id`  int(12) NOT NULL AUTO_INCREMENT COMMENT 'ID自增长' ,
`parent_id`  int(12) NOT NULL DEFAULT 0 COMMENT '导航项-父-ID' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点名称' ,
`item_type`  int(12) NOT NULL DEFAULT 0 COMMENT '节点类型' ,
`topic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消费主题' ,
`manager`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=81

;

-- ----------------------------
-- Records of navigation_item0
-- ----------------------------
BEGIN;
INSERT INTO `navigation_item0` VALUES ('1', '0', '前端埋点', '0', '', null), ('2', '0', '后端埋点', '0', '', null), ('7', '2', '企信', '0', '', null), ('11', '1', 'iOS', '1', 'dcx.MonitorRequest', '林麟'), ('12', '1', 'Android', '1', 'dcx.MonitorRequest', '李联玉 曹海涛'), ('13', '2', '开平-搜索-2', '1', 'broker.feeds-search1', null), ('14', '2', '开平-搜索-1', '1', 'broker.feeds-search0', null), ('15', '2', 'CEP', '1', 'cep.fs-cep-provider', '王毅'), ('16', '63', '网站', '1', 'nginx.fgif', '马锡月'), ('17', '63', 'H5-WEB', '1', 'nginx.mgif', '马锡月'), ('18', '63', 'PC客户端', '1', 'nginx.pgif', '马锡月'), ('59', '7', '企信PROVIDER', '1', 'fs-qixin-provider.qixinBI', '李异光'), ('63', '1', 'WEB', '0', '', null), ('65', '7', '企信WEB', '1', 'fs-qixin-web.qixinBI', '李异光'), ('66', '7', '企信Biz-WEB', '1', 'fs-qixin-biz-web.qixinBI', '李异光'), ('73', '2', '文件存储', '1', 'fs-warehouse.file_statistics', '翟付杰'), ('74', '2', '应用后台', '0', '', ''), ('75', '2', 'CRM', '0', '', ''), ('76', '75', 'CRM数据看板', '1', 'fs-app-server.data-board', '吴健鹏，刘鹏'), ('77', '74', '考勤', '1', 'fs-app-server.checkin-office', '吴健鹏，贺政忠'), ('78', '74', '外勤', '1', 'fs-app-server.checkin', '吴健鹏，王轩明'), ('79', '74', '会议助手', '1', 'fs-app-server.meeting-assistant', '吴健鹏，贺政忠'), ('80', '74', '企信看板', '1', 'fs-app-server.group-chat-board', '吴健鹏，贺政忠');
COMMIT;

-- ----------------------------
-- Auto increment value for `bp_fields`
-- ----------------------------
ALTER TABLE `bp_fields` AUTO_INCREMENT=7;

-- ----------------------------
-- Auto increment value for `bp_job`
-- ----------------------------
ALTER TABLE `bp_job` AUTO_INCREMENT=9;

-- ----------------------------
-- Auto increment value for `bp_target`
-- ----------------------------
ALTER TABLE `bp_target` AUTO_INCREMENT=6;

-- ----------------------------
-- Auto increment value for `buried_point`
-- ----------------------------
ALTER TABLE `buried_point` AUTO_INCREMENT=161;

-- ----------------------------
-- Auto increment value for `buried_point0`
-- ----------------------------
ALTER TABLE `buried_point0` AUTO_INCREMENT=458;

-- ----------------------------
-- Auto increment value for `database_info`
-- ----------------------------
ALTER TABLE `database_info` AUTO_INCREMENT=14;

-- ----------------------------
-- Auto increment value for `job_info`
-- ----------------------------
ALTER TABLE `job_info` AUTO_INCREMENT=26;

-- ----------------------------
-- Auto increment value for `job_source`
-- ----------------------------
ALTER TABLE `job_source` AUTO_INCREMENT=12;

-- ----------------------------
-- Auto increment value for `job_target`
-- ----------------------------
ALTER TABLE `job_target` AUTO_INCREMENT=12;

-- ----------------------------
-- Auto increment value for `level_one_fields`
-- ----------------------------
ALTER TABLE `level_one_fields` AUTO_INCREMENT=71;

-- ----------------------------
-- Auto increment value for `level_two_fields`
-- ----------------------------
ALTER TABLE `level_two_fields` AUTO_INCREMENT=214;

-- ----------------------------
-- Auto increment value for `m99_fields`
-- ----------------------------
ALTER TABLE `m99_fields` AUTO_INCREMENT=94;

-- ----------------------------
-- Auto increment value for `navigation_item`
-- ----------------------------
ALTER TABLE `navigation_item` AUTO_INCREMENT=10;

-- ----------------------------
-- Auto increment value for `navigation_item0`
-- ----------------------------
ALTER TABLE `navigation_item0` AUTO_INCREMENT=81;
