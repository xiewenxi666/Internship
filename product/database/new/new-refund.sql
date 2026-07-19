-- ===========================================
-- 财务域 — 退款管理
-- 菜单ID段: 5426-5435
-- ===========================================

-- 1. 创建退款表
CREATE TABLE `crm_refund` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '退款编号',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` bigint NULL DEFAULT NULL COMMENT '关联合同ID',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '负责人',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退款内容/原因',
  `price` decimal(24, 6) NULL DEFAULT NULL COMMENT '退款金额',
  `type` int NULL DEFAULT NULL COMMENT '退款类型',
  `refund_date` datetime NULL DEFAULT NULL COMMENT '退款日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `audit_status` tinyint NOT NULL DEFAULT 0 COMMENT '审批状态',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工作流编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'CRM 退款表';

-- 2. 退款类型字典
INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES ('退款-退款类型', 'crm_refund_type', 0, '退款-退款类型', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1, '全额退款', '1', 'crm_refund_type', 0, '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2, '部分退款', '2', 'crm_refund_type', 0, '', '1', NOW(), '1', NOW(), b'0');

-- 3. 菜单 — 退款管理 (5426)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5426, '退款管理', '', 2, 70, 2397, 'refund', 'ep:money', 'crm/refund/index', 'CrmRefund', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5427, '退款管理查询', 'crm:refund:query', 3, 1, 5426, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5428, '退款管理创建', 'crm:refund:create', 3, 2, 5426, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5429, '退款管理更新', 'crm:refund:update', 3, 3, 5426, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5430, '退款管理删除', 'crm:refund:delete', 3, 4, 5426, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5431, '退款管理导出', 'crm:refund:export', 3, 5, 5426, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 4. 菜单 — 退款审批 (5432)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5432, '退款审批', '', 2, 71, 2397, 'refund/approval', 'ep:checked', 'crm/refund/approval/index', 'CrmRefundApproval', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5433, '退款审批查询', 'crm:refund:query', 3, 1, 5432, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 5. 菜单 — 退款记录 (5434)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5434, '退款记录', '', 2, 72, 2397, 'refund/record', 'ep:list', 'crm/refund/record/index', 'CrmRefundRecord', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5435, '退款记录查询', 'crm:refund:query', 3, 1, 5434, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 6. 菜单国际化
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5426, 'zh-CN', '退款管理', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5426, 'en', 'Refund Management', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5432, 'zh-CN', '退款审批', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5432, 'en', 'Refund Approval', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5434, 'zh-CN', '退款记录', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5434, 'en', 'Refund Records', '1', NOW(), '1', NOW(), b'0');
