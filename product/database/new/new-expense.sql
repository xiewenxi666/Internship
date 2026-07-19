-- ===========================================
-- 财务域 — 费用管理
-- 菜单ID段: 5446-5453
-- ===========================================

-- 1. 创建费用表
CREATE TABLE `crm_expense` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '费用编号',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` bigint NULL DEFAULT NULL COMMENT '关联合同ID',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '负责人',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '费用内容',
  `price` decimal(24, 6) NULL DEFAULT NULL COMMENT '费用金额',
  `type` int NULL DEFAULT NULL COMMENT '费用类型',
  `apply_date` datetime NULL DEFAULT NULL COMMENT '申请日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'CRM 费用表';

-- 2. 费用类型字典
INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES ('费用-费用类型', 'crm_expense_type', 0, '费用-费用类型', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1,'差旅费','1','crm_expense_type',0,'1',NOW(),'1',NOW(),b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2,'办公费','2','crm_expense_type',0,'1',NOW(),'1',NOW(),b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3,'招待费','3','crm_expense_type',0,'1',NOW(),'1',NOW(),b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4,'交通费','4','crm_expense_type',0,'1',NOW(),'1',NOW(),b'0');
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5,'其他','5','crm_expense_type',0,'1',NOW(),'1',NOW(),b'0');

-- 3. 菜单 — 费用管理 (5446)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5446,'费用管理','',2,74,2397,'expense','ep:money','crm/expense/index','CrmExpense',0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5447,'费用管理查询','crm:expense:query',3,1,5446,'','','',NULL,0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5448,'费用管理创建','crm:expense:create',3,2,5446,'','','',NULL,0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5449,'费用管理更新','crm:expense:update',3,3,5446,'','','',NULL,0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5450,'费用管理删除','crm:expense:delete',3,4,5446,'','','',NULL,0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5451,'费用管理导出','crm:expense:export',3,5,5446,'','','',NULL,0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');

-- 4. 菜单 — 费用记录 (5452)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5452,'费用记录','',2,75,2397,'expense/record','ep:list','crm/expense/record/index','CrmExpenseRecord',0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5453,'费用记录查询','crm:expense:query',3,1,5452,'','','',NULL,0,b'1',b'1',b'1','1',NOW(),'1',NOW(),b'0');

-- 5. 菜单国际化
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5446,'zh-CN','费用管理','1',NOW(),'1',NOW(),b'0'),(5446,'en','Expense Management','1',NOW(),'1',NOW(),b'0'),
(5452,'zh-CN','费用记录','1',NOW(),'1',NOW(),b'0'),(5452,'en','Expense Records','1',NOW(),'1',NOW(),b'0');
