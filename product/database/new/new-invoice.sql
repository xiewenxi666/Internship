-- ===========================================
-- 财务域 — 发票管理
-- 菜单ID段: 5401-5403 (5400已用)
-- ===========================================

-- 1. 创建发票表
CREATE TABLE `crm_invoice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发票编号',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联订单编号',
  `order_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联订单名称',
  `invoice_date` datetime NULL DEFAULT NULL COMMENT '开票日期',
  `type` int NULL DEFAULT NULL COMMENT '票据类型',
  `price` decimal(24, 6) NULL DEFAULT NULL COMMENT '开票金额',
  `invoice_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '税务发票号码',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '票据内容',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '订单所属人员',
  `handler_user_id` bigint NULL DEFAULT NULL COMMENT '发票经手人员',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'CRM 发票表';

-- 2. 票据类型字典
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (NULL, '发票-票据类型', 'crm_invoice_type', 0, '发票-票据类型', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 1, '增值税专用发票', '1', 'crm_invoice_type', 0, '', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 2, '增值税普通发票', '2', 'crm_invoice_type', 0, '', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 3, '电子发票', '3', 'crm_invoice_type', 0, '', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 4, '收据', '4', 'crm_invoice_type', 0, '', '', '', '1', NOW(), '1', NOW(), b'0');

-- 3. 菜单 — 发票管理 (5401)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5401, '发票管理', '', 2, 63, 2397, 'invoice', 'ep:list', 'crm/invoice/index', 'CrmInvoice', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5411, '发票管理查询', 'crm:invoice:query', 3, 1, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5412, '发票管理创建', 'crm:invoice:create', 3, 2, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5413, '发票管理更新', 'crm:invoice:update', 3, 3, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5414, '发票管理删除', 'crm:invoice:delete', 3, 4, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5415, '发票管理导出', 'crm:invoice:export', 3, 5, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 4. 菜单 — 发票记录 (5402)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5402, '发票记录', '', 2, 64, 2397, 'invoice/record', 'ep:list', 'crm/invoice/record/index', 'CrmInvoiceRecord', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5421, '开票记录报表查询', 'crm:invoice:query', 3, 1, 5402, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5422, '开票记录报表导出', 'crm:invoice:export', 3, 2, 5402, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 5. 菜单国际化
INSERT INTO `system_menu_i18n` (`id`, `menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 5401, 'zh-CN', '发票管理', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`id`, `menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 5401, 'en', 'Invoice Management', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`id`, `menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 5402, 'zh-CN', '发票记录', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu_i18n` (`id`, `menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 5402, 'en', 'Invoice Records', '1', NOW(), '1', NOW(), b'0');
