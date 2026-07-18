-- ==========================================
-- CRM 订单域 DDL
-- 文件: database/new/crm/001-crm-order.sql
-- 日期: 2026-07-14
-- ==========================================

SET NAMES utf8mb4;

-- ----------------------------
-- Table structure for crm_order
-- ----------------------------
DROP TABLE IF EXISTS `crm_order`;
CREATE TABLE `crm_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `no` varchar(64) NOT NULL DEFAULT '' COMMENT '订单编号',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '订单名称',
  `customer_id` bigint NOT NULL COMMENT '客户编号',
  `business_id` bigint DEFAULT NULL COMMENT '商机编号',
  `order_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `owner_user_id` bigint DEFAULT NULL COMMENT '负责人编号',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '审批流程实例编号',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态：0-未提交 10-已提交 15-审批中 20-已通过 30-已驳回 40-处理中 60-已完成 70-已取消',
  `total_product_price` decimal(24,6) DEFAULT NULL COMMENT '产品总金额',
  `discount_percent` decimal(24,6) DEFAULT NULL COMMENT '整单折扣百分比',
  `total_price` decimal(24,6) DEFAULT NULL COMMENT '订单总金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `contact_last_time` datetime DEFAULT NULL COMMENT '最后跟进时间',
  `contact_next_time` datetime DEFAULT NULL COMMENT '下次联系时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_no` (`no`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_business_id` (`business_id`) USING BTREE,
  KEY `idx_owner_user_id` (`owner_user_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 订单表';

-- ----------------------------
-- Table structure for crm_order_item
-- ----------------------------
DROP TABLE IF EXISTS `crm_order_item`;
CREATE TABLE `crm_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `product_price` decimal(24,6) NOT NULL COMMENT '产品单价',
  `order_price` decimal(24,6) NOT NULL COMMENT '订单价格(折扣后单价)',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `total_price` decimal(24,6) NOT NULL COMMENT '总计价格',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 订单产品关联表';

-- 回滚脚本
-- DROP TABLE IF EXISTS `crm_order_item`;
-- DROP TABLE IF EXISTS `crm_order`;
