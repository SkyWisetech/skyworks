/*
 Navicat MySQL Dump SQL

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 90000 (9.0.0)
 Source Host           : localhost:3306
 Source Schema         : info_share

 Target Server Type    : MySQL
 Target Server Version : 90000 (9.0.0)
 File Encoding         : 65001

 Date: 07/08/2024 15:32:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '正文',
  `upvote_num` int NULL DEFAULT 0 COMMENT '点赞数',
  `collect_num` int NULL DEFAULT 0 COMMENT '收藏数',
  `comment_num` int NULL DEFAULT 0 COMMENT '评论数',
  `view_num` int NULL DEFAULT 0 COMMENT '浏览量',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签ID，逗号分割',
  `image_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片名称',
  `priority` int NULL DEFAULT 0 COMMENT '权重 首页文章排序使用',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '该文章是否被删除 0否 1是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `article_id` int NULL DEFAULT NULL COMMENT '文章ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int NULL DEFAULT 0 COMMENT '父级ID：0表示评论文章 其他表示评价评论',
  `article_id` int NULL DEFAULT NULL COMMENT '文章ID',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评价详情',
  `upvote` int NULL DEFAULT 0 COMMENT '被赞数量',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '该文章是否被删除 0否 1是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for daily_data
-- ----------------------------
DROP TABLE IF EXISTS `daily_data`;
CREATE TABLE `daily_data`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `new_article` int NULL DEFAULT 0 COMMENT '新增文章数',
  `new_upvote` int NULL DEFAULT 0 COMMENT '新增点赞数',
  `new_comment` int NULL DEFAULT 0 COMMENT '新增评论数',
  `new_user` int NULL DEFAULT 0 COMMENT '新增用户数',
  `new_view` int NULL DEFAULT 0 COMMENT '新增浏览量',
  `daily_date` date NULL DEFAULT '2099-01-01' COMMENT '统计日期',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日数据统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int NULL DEFAULT 0 COMMENT '父级ID：0 一级标签 其余是二级标签',
  `priority` int NULL DEFAULT 0 COMMENT '权重 首页文章排序，字段值从小到大，文章排版从左往右',
  `color` int NULL DEFAULT 0 COMMENT '颜色-待使用',
  `label_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签名',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for upvote
-- ----------------------------
DROP TABLE IF EXISTS `upvote`;
CREATE TABLE `upvote`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` tinyint NULL DEFAULT NULL COMMENT '1文章点赞 2评论点赞',
  `art_com_id` int NULL DEFAULT 0 COMMENT '文章或评论ID',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `forbid_comment_date` date NULL DEFAULT NULL COMMENT '禁止评论日期，该日期前禁止评论',
  `is_black` tinyint NULL DEFAULT 0 COMMENT '是否进入黑名单，黑名单禁止登录：0否 1是',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '该用户是否被删除 0否 1是',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人简介',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册日期',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
