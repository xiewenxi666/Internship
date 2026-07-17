-- ===========================================
-- 财务域 — 报销管理
-- 菜单ID段: 5403-5410
-- ===========================================

-- 1. 创建报销表
CREATE TABLE `crm_reimbursement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '报销编号',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` bigint NULL DEFAULT NULL COMMENT '关联合同ID',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '申请人/负责人',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报销内容',
  `price` decimal(24, 6) NULL DEFAULT NULL COMMENT '报销金额',
  `type` int NULL DEFAULT NULL COMMENT '报销类型',
  `apply_date` datetime NULL DEFAULT NULL COMMENT '申请日期',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'CRM 报销表';

-- 2. 报销类型字典
INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES ('报销-报销类型', 'crm_reimbursement_type', 0, '报销-报销类型', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1, '差旅费', '1', 'crm_reimbursement_type', 0, '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2, '办公费', '2', 'crm_reimbursement_type', 0, '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3, '招待费', '3', 'crm_reimbursement_type', 0, '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4, '交通费', '4', 'crm_reimbursement_type', 0, '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5, '其他', '5', 'crm_reimbursement_type', 0, '', '1', NOW(), '1', NOW(), b'0');

-- 3. 菜单 — 报销管理 (5403)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5403, '报销管理', '', 2, 65, 2397, 'reimbursement', 'ep:money', 'crm/reimbursement/index', 'CrmReimbursement', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5404, '报销管理查询', 'crm:reimbursement:query', 3, 1, 5403, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5405, '报销管理创建', 'crm:reimbursement:create', 3, 2, 5403, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5406, '报销管理更新', 'crm:reimbursement:update', 3, 3, 5403, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5407, '报销管理删除', 'crm:reimbursement:delete', 3, 3, 5403, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5408, '报销管理导出', 'crm:reimbursement:export', 3, 4, 5403, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 4. 菜单 — 报销审批 (5409)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5409, '报销审批', '', 2, 66, 2397, 'reimbursement/approval', 'ep:checked', 'crm/reimbursement/approval/index', 'CrmReimbursementApproval', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5410, '报销审批查询', 'crm:reimbursement:query', 3, 1, 5409, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 5. 菜单国际化
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5403, 'zh-CN', '报销管理', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5403, 'en', 'Reimbursement', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5409, 'zh-CN', '报销审批', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5409, 'en', 'Reimbursement Approval', '1', NOW(), '1', NOW(), b'0');
