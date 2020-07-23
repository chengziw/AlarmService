/*
 Navicat Premium Data Transfer

 Source Server         : 39.107.50.176
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 39.107.50.176:3306
 Source Schema         : fincyAlert

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 21/07/2020 10:30:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alert_group_subscription
-- ----------------------------
DROP TABLE IF EXISTS `alert_group_subscription`;
CREATE TABLE `alert_group_subscription`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alertType` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提醒类型（telegram等）',
  `groupName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报警群组标识',
  `appId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用Id（作为应用多套部署时的唯一标识）',
  `appName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用名称',
  `moduleName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `eventName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `alertLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警级别',
  `priorityLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级',
  `intervalInt` int(11) NOT NULL DEFAULT 1 COMMENT '提醒频率间隔',
  `remark` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifyTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alert_group_subscription
-- ----------------------------
INSERT INTO `alert_group_subscription` VALUES (10, 'telegram', 'TestAlert', '*', '*', '*', '*', '*', '*', 1, NULL, '2019-01-08 17:24:32', '2019-01-08 17:24:34');

-- ----------------------------
-- Table structure for alert_group_subscription_black
-- ----------------------------
DROP TABLE IF EXISTS `alert_group_subscription_black`;
CREATE TABLE `alert_group_subscription_black`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `subId` bigint(20) NULL DEFAULT NULL COMMENT '订阅Id',
  `appId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '应用Id（作为应用多套部署时的唯一标识）',
  `appName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '应用名称',
  `moduleName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `eventName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `alertLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '报警级别',
  `priorityLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '优先级',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifyTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `subId`(`subId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alert_group_subscription_black
-- ----------------------------
INSERT INTO `alert_group_subscription_black` VALUES (1, 1, '*', '*', '*', '测试通知', '*', '*', '2019-01-25 21:08:12', '2019-01-25 21:08:12');

-- ----------------------------
-- Table structure for alert_group_subscription_lazy
-- ----------------------------
DROP TABLE IF EXISTS `alert_group_subscription_lazy`;
CREATE TABLE `alert_group_subscription_lazy`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用Id（作为应用多套部署时的唯一标识）',
  `appName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `moduleName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `eventName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `alertLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警级别',
  `priorityLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级',
  `lazyNum` int(11) NOT NULL COMMENT '积攒次数',
  `intervalInt` int(11) NOT NULL DEFAULT 1 COMMENT '积攒有效时间（分钟）',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifyTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alert_group_subscription_lazy
-- ----------------------------
INSERT INTO `alert_group_subscription_lazy` VALUES (1, '*', '*', '同步汇率', '*', '*', '*', 10, 2, '2019-01-29 15:52:29', '2019-01-29 15:52:29');
INSERT INTO `alert_group_subscription_lazy` VALUES (2, '*', '*', '*', 'VET钱包接口', '*', '*', 15, 5, '2019-01-30 19:20:34', '2019-01-30 19:20:34');

-- ----------------------------
-- Table structure for alert_param_record
-- ----------------------------
DROP TABLE IF EXISTS `alert_param_record`;
CREATE TABLE `alert_param_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serialNumber` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `appId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用Id（作为应用多套部署时的唯一标识）',
  `appName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用名称',
  `moduleName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `eventName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `alertLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警级别',
  `priorityLevel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级',
  `content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contentKey` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `eventTime` datetime(0) NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifyTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 427902 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for alert_telegram_group_info
-- ----------------------------
DROP TABLE IF EXISTS `alert_telegram_group_info`;
CREATE TABLE `alert_telegram_group_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `botName` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `groupId` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifyTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alert_telegram_group_info
-- ----------------------------
INSERT INTO `alert_telegram_group_info` VALUES (1, 'TestAlert', 'testSkyServerAlertDifferentbot', '-436399713', '2020-07-16 19:03:16', '2020-07-16 19:03:16');

SET FOREIGN_KEY_CHECKS = 1;
