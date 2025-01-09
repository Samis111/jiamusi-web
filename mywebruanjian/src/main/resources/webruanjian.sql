/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : webruanjian

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 17/12/2024 10:14:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for courseware_type
-- ----------------------------
DROP TABLE IF EXISTS `courseware_type`;
CREATE TABLE `courseware_type`  (
  `type_id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courseware_type
-- ----------------------------

-- ----------------------------
-- Table structure for discussion_replies
-- ----------------------------
DROP TABLE IF EXISTS `discussion_replies`;
CREATE TABLE `discussion_replies`  (
  `reply_id` int NOT NULL AUTO_INCREMENT,
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reply_creator_id` int NOT NULL,
  `reply_create_time` datetime NOT NULL,
  `topic_id` int NOT NULL,
  PRIMARY KEY (`reply_id`) USING BTREE,
  INDEX `reply_creator_id`(`reply_creator_id`) USING BTREE,
  INDEX `topic_id`(`topic_id`) USING BTREE,
  CONSTRAINT `discussion_replies_ibfk_1` FOREIGN KEY (`reply_creator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `discussion_replies_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `discussion_topics` (`topic_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discussion_replies
-- ----------------------------

-- ----------------------------
-- Table structure for discussion_topics
-- ----------------------------
DROP TABLE IF EXISTS `discussion_topics`;
CREATE TABLE `discussion_topics`  (
  `topic_id` int NOT NULL AUTO_INCREMENT,
  `topic_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `topic_creator_id` int NOT NULL,
  `topic_create_time` datetime NOT NULL,
  PRIMARY KEY (`topic_id`) USING BTREE,
  INDEX `topic_creator_id`(`topic_creator_id`) USING BTREE,
  CONSTRAINT `discussion_topics_ibfk_1` FOREIGN KEY (`topic_creator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discussion_topics
-- ----------------------------

-- ----------------------------
-- Table structure for exercise_questions
-- ----------------------------
DROP TABLE IF EXISTS `exercise_questions`;
CREATE TABLE `exercise_questions`  (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `question_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `question_type_id` int NOT NULL,
  `question_creator_id` int NOT NULL,
  `question_create_time` datetime NOT NULL,
  PRIMARY KEY (`question_id`) USING BTREE,
  INDEX `question_type_id`(`question_type_id`) USING BTREE,
  INDEX `question_creator_id`(`question_creator_id`) USING BTREE,
  CONSTRAINT `exercise_questions_ibfk_1` FOREIGN KEY (`question_type_id`) REFERENCES `exercise_type` (`type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `exercise_questions_ibfk_2` FOREIGN KEY (`question_creator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exercise_questions
-- ----------------------------

-- ----------------------------
-- Table structure for exercise_type
-- ----------------------------
DROP TABLE IF EXISTS `exercise_type`;
CREATE TABLE `exercise_type`  (
  `type_id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exercise_type
-- ----------------------------

-- ----------------------------
-- Table structure for multimedia_courseware
-- ----------------------------
DROP TABLE IF EXISTS `multimedia_courseware`;
CREATE TABLE `multimedia_courseware`  (
  `courseware_id` int NOT NULL AUTO_INCREMENT,
  `courseware_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `courseware_type_id` int NOT NULL,
  `courseware_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `courseware_creator_id` int NOT NULL,
  `courseware_create_time` datetime NOT NULL,
  PRIMARY KEY (`courseware_id`) USING BTREE,
  INDEX `courseware_type_id`(`courseware_type_id`) USING BTREE,
  INDEX `courseware_creator_id`(`courseware_creator_id`) USING BTREE,
  CONSTRAINT `multimedia_courseware_ibfk_1` FOREIGN KEY (`courseware_type_id`) REFERENCES `courseware_type` (`type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `multimedia_courseware_ibfk_2` FOREIGN KEY (`courseware_creator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of multimedia_courseware
-- ----------------------------

-- ----------------------------
-- Table structure for paper_question_relation
-- ----------------------------
DROP TABLE IF EXISTS `paper_question_relation`;
CREATE TABLE `paper_question_relation`  (
  `relation_id` int NOT NULL AUTO_INCREMENT,
  `paper_id` int NOT NULL,
  `question_id` int NOT NULL,
  PRIMARY KEY (`relation_id`) USING BTREE,
  INDEX `paper_id`(`paper_id`) USING BTREE,
  INDEX `question_id`(`question_id`) USING BTREE,
  CONSTRAINT `paper_question_relation_ibfk_1` FOREIGN KEY (`paper_id`) REFERENCES `test_papers` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `paper_question_relation_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `exercise_questions` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper_question_relation
-- ----------------------------

-- ----------------------------
-- Table structure for question_answer
-- ----------------------------
DROP TABLE IF EXISTS `question_answer`;
CREATE TABLE `question_answer`  (
  `qa_id` int NOT NULL AUTO_INCREMENT,
  `question_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `question_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `question_creator_id` int NOT NULL,
  `question_create_time` datetime NOT NULL,
  `answer_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `answer_creator_id` int NULL DEFAULT NULL,
  `answer_create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`qa_id`) USING BTREE,
  INDEX `question_creator_id`(`question_creator_id`) USING BTREE,
  INDEX `answer_creator_id`(`answer_creator_id`) USING BTREE,
  CONSTRAINT `question_answer_ibfk_1` FOREIGN KEY (`question_creator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `question_answer_ibfk_2` FOREIGN KEY (`answer_creator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_answer
-- ----------------------------

-- ----------------------------
-- Table structure for student_answers
-- ----------------------------
DROP TABLE IF EXISTS `student_answers`;
CREATE TABLE `student_answers`  (
  `answer_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `paper_id` int NOT NULL,
  `question_id` int NOT NULL,
  `student_answer_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `answer_time` datetime NOT NULL,
  PRIMARY KEY (`answer_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `paper_id`(`paper_id`) USING BTREE,
  INDEX `question_id`(`question_id`) USING BTREE,
  CONSTRAINT `student_answers_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `student_answers_ibfk_2` FOREIGN KEY (`paper_id`) REFERENCES `test_papers` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `student_answers_ibfk_3` FOREIGN KEY (`question_id`) REFERENCES `exercise_questions` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_answers
-- ----------------------------

-- ----------------------------
-- Table structure for student_scores
-- ----------------------------
DROP TABLE IF EXISTS `student_scores`;
CREATE TABLE `student_scores`  (
  `score_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `paper_id` int NOT NULL,
  `total_score` decimal(5, 2) NULL DEFAULT NULL,
  `test_time` datetime NOT NULL,
  PRIMARY KEY (`score_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `paper_id`(`paper_id`) USING BTREE,
  CONSTRAINT `student_scores_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `student_scores_ibfk_2` FOREIGN KEY (`paper_id`) REFERENCES `test_papers` (`paper_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_scores
-- ----------------------------

-- ----------------------------
-- Table structure for test_papers
-- ----------------------------
DROP TABLE IF EXISTS `test_papers`;
CREATE TABLE `test_papers`  (
  `paper_id` int NOT NULL AUTO_INCREMENT,
  `paper_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `paper_creator_id` int NOT NULL,
  `paper_create_time` datetime NOT NULL,
  PRIMARY KEY (`paper_id`) USING BTREE,
  INDEX `paper_creator_id`(`paper_creator_id`) USING BTREE,
  CONSTRAINT `test_papers_ibfk_1` FOREIGN KEY (`paper_creator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_papers
-- ----------------------------

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
