/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : minion

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 08/10/2023 18:18:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '员工id',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '账户昵称',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `register_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '注册时间',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '上一次登录时间',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ix_account_name`(`nickname`) USING BTREE,
  UNIQUE INDEX `ix_account_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1709847593623777287 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (1, '2049448867@qq.com', '管理员', 'e10adc3949ba59abbe56e057f20f883e', '2023-10-05 13:12:43', NULL, NULL);
INSERT INTO `account` VALUES (26, 'zhangsan123@163.com', '法外狂徒', 'e10adc3949ba59abbe56e057f20f883e', '2022-03-29 18:40:09', '2023-09-26 02:54:45', '127.0.0.1');
INSERT INTO `account` VALUES (27, 'lisi@163.com', 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '2022-04-11 12:06:58', '2023-05-01 18:20:01', '127.0.0.1');
INSERT INTO `account` VALUES (28, 'wangwu@163.com', 'wangwu', '$2a$10$urwwrsjWmk6.ZZe80XGUEOsSG8Cms3TDB6RbOAS2Vg7euzryjpIXG', '2022-04-11 12:08:28', '2023-04-11 18:25:45', '127.0.0.1');
INSERT INTO `account` VALUES (29, 'annajin@qq.com', 'annajin', '$2a$10$urwwrsjWmk6.ZZe80XGUEOsSG8Cms3TDB6RbOAS2Vg7euzryjpIXG', '2022-04-11 13:27:02', '2023-03-14 19:45:44', NULL);

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '大小',
  `emp_id` bigint(0) NOT NULL COMMENT '员工id',
  `parent_id` bigint(0) NOT NULL COMMENT '父级id 0为根目录',
  `is_shared` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否公共',
  `content_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'content-type',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  `deleted_batch_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除批次号',
  `mount_share_folder_id` bigint(0) NULL DEFAULT NULL COMMENT '挂载的共享目录',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file
-- ----------------------------
INSERT INTO `file` VALUES (126, '图片', NULL, '文件夹', '-', 1, 0, 0, NULL, 'admin', '2023-10-06 16:30:05', 'admin', '2023-10-06 16:30:05', b'0', NULL, NULL);
INSERT INTO `file` VALUES (127, '666.jpeg', '1/2023/10/06/594489580a5b3fd3b9f8ca040f8d9a140902707a.jpeg', 'jpeg', '104KB', 1, 126, 0, 'image/jpeg', 'admin', '2023-10-06 16:30:49', 'admin', '2023-10-06 16:30:49', b'0', NULL, 141);
INSERT INTO `file` VALUES (128, '王炸.jpeg', '1/2023/10/06/594489580a6e7f6f35d2afe2904a56c6daded56c.jpeg', 'jpeg', '20KB', 1, 126, 0, 'image/jpeg', 'admin', '2023-10-06 16:30:49', 'admin', '2023-10-06 16:30:49', b'0', NULL, NULL);
INSERT INTO `file` VALUES (129, '1.jpg', '1/2023/10/06/594489581.jpg', 'jpg', '49KB', 1, 126, 0, 'image/jpeg', 'admin', '2023-10-06 16:30:49', 'admin', '2023-10-06 16:30:49', b'1', '1d4ea972-dc39-430a-828c-6e119229aa62', NULL);
INSERT INTO `file` VALUES (130, '音频文件', NULL, '文件夹', '-', 1, 0, 0, NULL, 'admin', '2023-10-06 16:31:11', 'admin', '2023-10-06 16:31:11', b'0', NULL, NULL);
INSERT INTO `file` VALUES (131, '青鸟飞鱼 - 此生不换.mp3', '1/2023/10/06/59503400青鸟飞鱼 - 此生不换.mp3', 'mp3', '10MB', 1, 130, 0, 'audio/mpeg', 'admin', '2023-10-06 16:31:45', 'admin', '2023-10-06 16:31:45', b'0', NULL, NULL);
INSERT INTO `file` VALUES (132, '张芸京 - 偏爱.mp3', '1/2023/10/06/59503400张芸京 - 偏爱.mp3', 'mp3', '8MB', 1, 130, 0, 'audio/mpeg', 'admin', '2023-10-06 16:31:45', 'admin', '2023-10-06 16:31:45', b'0', NULL, NULL);
INSERT INTO `file` VALUES (133, '视频文件', NULL, '文件夹', '-', 1, 0, 0, NULL, 'admin', '2023-10-06 16:32:38', 'admin', '2023-10-06 16:32:38', b'0', NULL, NULL);
INSERT INTO `file` VALUES (134, '《Lemon》笛子.mp4', '1/2023/10/06/59575970《Lemon》笛子.mp4', 'mp4', '52MB', 1, 133, 0, 'video/mp4', 'admin', '2023-10-06 16:33:00', 'admin', '2023-10-06 16:33:00', b'0', NULL, NULL);
INSERT INTO `file` VALUES (135, '《穿越时空的思念》笛子 .mp4', '1/2023/10/06/59575970《穿越时空的思念》笛子 .mp4', 'mp4', '4MB', 1, 133, 0, 'video/mp4', 'admin', '2023-10-06 16:33:00', 'admin', '2023-10-06 16:33:00', b'0', NULL, NULL);
INSERT INTO `file` VALUES (136, '《钢铁洪流进行曲》 - .mp4', '1/2023/10/06/59575970《钢铁洪流进行曲》 - .mp4', 'mp4', '15MB', 1, 133, 0, 'video/mp4', 'admin', '2023-10-06 16:33:00', 'admin', '2023-10-06 16:33:00', b'0', NULL, NULL);
INSERT INTO `file` VALUES (137, '文档文件', NULL, '文件夹', '-', 1, 0, 0, NULL, 'admin', '2023-10-06 16:35:03', 'admin', '2023-10-06 16:35:03', b'1', 'ef5c8f66-7d8a-432f-8a58-36f45710ae88', NULL);
INSERT INTO `file` VALUES (138, 'docker.pdf', '1/2023/10/06/59736491docker.pdf', 'pdf', '987KB', 1, 137, 0, 'application/pdf', 'admin', '2023-10-06 16:35:37', 'admin', '2023-10-06 16:35:37', b'1', 'ef5c8f66-7d8a-432f-8a58-36f45710ae88', NULL);
INSERT INTO `file` VALUES (139, 'java.pdf', '1/2023/10/06/59736491java.pdf', 'pdf', '7MB', 1, 137, 0, 'application/pdf', 'admin', '2023-10-06 16:35:37', 'admin', '2023-10-06 16:35:37', b'1', 'ef5c8f66-7d8a-432f-8a58-36f45710ae88', 0);
INSERT INTO `file` VALUES (140, 'Java开发手册（嵩山版）.pdf', '1/2023/10/06/59736491Java开发手册（嵩山版）.pdf', 'pdf', '0B', 1, 137, 0, 'application/pdf', 'admin', '2023-10-06 16:35:37', 'admin', '2023-10-06 16:35:37', b'1', 'ccf43c17-a3dd-4d12-a839-6022b1eb5f84', NULL);
INSERT INTO `file` VALUES (144, '1.jpg', '1/2023/10/07/663971621.jpg', 'jpg', '49KB', 1, 0, 1, 'image/jpeg', '管理员', '2023-10-07 18:26:37', NULL, NULL, b'1', '2021870b-178d-492a-ba19-324cf34fa197', NULL);
INSERT INTO `file` VALUES (145, '1.《起风了》笛子版.mp4', '1/2023/10/08/294812001.《起风了》笛子版.mp4', 'mp4', '12MB', 1, 0, 1, 'video/mp4', '管理员', '2023-10-08 08:11:21', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (146, '音频文件', NULL, '文件夹', '-', 27, 0, 0, NULL, 'lisi', '2023-10-08 08:14:42', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (147, 'lisi', NULL, '文件夹', '-', 27, 0, 1, NULL, 'lisi', '2023-10-08 08:14:58', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (148, '可能.mp4', '27/2023/10/08/29711629可能.mp4', 'mp4', '40MB', 27, 147, 1, 'video/mp4', 'lisi', '2023-10-08 08:15:12', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (149, 'pdf文件', NULL, '文件夹', '-', 1, 0, 0, NULL, '管理员', '2023-10-08 08:18:26', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (150, '去向登记表.pdf', '1/2023/10/08/29926436hkheyanbaogao.pdf', 'pdf', '46KB', 1, 149, 0, 'application/pdf', '管理员', '2023-10-08 08:18:46', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (151, '去向信息登记表.pdf', '1/2023/10/08/29926436daheyanbaogao.pdf', 'pdf', '50KB', 1, 149, 0, 'application/pdf', '管理员', '2023-10-08 08:18:46', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (152, '图片\n', NULL, '文件夹', '-', 27, 0, 0, NULL, 'lisi', '2023-10-08 08:50:49', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (153, '0a5b3fd3b9f8ca040f8d9a140902707a.jpeg', '27/2023/10/08/318697580a5b3fd3b9f8ca040f8d9a140902707a.jpeg', 'jpeg', '104KB', 27, 152, 0, 'image/jpeg', 'lisi', '2023-10-08 08:51:10', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (154, '2b25922a6aecec85209715a7fbaef788.jpeg', '27/2023/10/08/318697582b25922a6aecec85209715a7fbaef788.jpeg', 'jpeg', '48KB', 27, 152, 0, 'image/jpeg', 'lisi', '2023-10-08 08:51:10', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (155, '6c1b83ca899e2d76e9d6a1778f2983fa.jpg', '27/2023/10/08/318697586c1b83ca899e2d76e9d6a1778f2983fa.jpg', 'jpg', '96KB', 27, 152, 0, 'image/jpeg', 'lisi', '2023-10-08 08:51:10', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `file` VALUES (156, '斗地主', NULL, '文件夹', '-', 1, 0, 0, NULL, '管理员', '2023-10-08 16:59:32', NULL, NULL, b'0', 'ff440113-eb5f-4cfb-93b7-2bf81b67a7b1', NULL);
INSERT INTO `file` VALUES (157, 'w1.png', '1/2023/10/08/61235464w1.png', 'png', '44KB', 1, 156, 0, 'image/png', '管理员', '2023-10-08 17:00:35', NULL, NULL, b'0', 'c6fd0c05-1065-405f-bb25-b2d4aa9745aa', 147);

-- ----------------------------
-- Table structure for recycle_file
-- ----------------------------
DROP TABLE IF EXISTS `recycle_file`;
CREATE TABLE `recycle_file`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `emp_id` bigint(0) NOT NULL COMMENT '员工id',
  `file_id` bigint(0) NOT NULL COMMENT '文件id',
  `deleted_time` datetime(0) NOT NULL COMMENT '删除时间',
  `deleted_batch_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除批次号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回收站' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
