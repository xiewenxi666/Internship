-- ==========================================
-- CRM 营销域 DDL + 菜单数据
-- 文件: database/new/crm/004-crm-marketing.sql
-- 包含: 营销活动/群发管理/客户关怀/发送分析
-- ==========================================

-- ==================== DDL ====================

-- 营销活动表（含 productIds 推广产品）
CREATE TABLE IF NOT EXISTS `crm_marketing_campaign` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '活动编号',
    `title` VARCHAR(100) NOT NULL COMMENT '活动标题',
    `product_ids` VARCHAR(500) DEFAULT NULL COMMENT '推广产品ID列表（逗号分隔）',
    `type` INT NOT NULL COMMENT '活动类型（1-促销活动 2-品牌活动 3-会议营销）',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `estimated_cost` DECIMAL(12,2) DEFAULT 0 COMMENT '预计成本',
    `estimated_revenue` DECIMAL(12,2) DEFAULT 0 COMMENT '预计收入',
    `owner_user_id` BIGINT NOT NULL COMMENT '负责人员ID',
    `participants` VARCHAR(500) DEFAULT NULL COMMENT '参与人员ID列表（逗号分隔）',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '活动地址',
    `description` TEXT DEFAULT NULL COMMENT '活动详情（富文本）',
    `status` INT NOT NULL DEFAULT 1 COMMENT '活动状态（1-筹备 2-进行中 3-已结束）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_owner_user_id` (`owner_user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='营销活动表';

-- 营销群发表（含 productIds 推广产品）
CREATE TABLE IF NOT EXISTS `crm_marketing_bulk_send` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '群发编号',
    `title` VARCHAR(100) NOT NULL COMMENT '任务标题',
    `product_ids` VARCHAR(500) DEFAULT NULL COMMENT '推广产品ID列表（逗号分隔）',
    `campaign_id` BIGINT DEFAULT NULL COMMENT '关联活动ID',
    `type` INT NOT NULL COMMENT '类型（1-短信 2-邮件）',
    `template_id` BIGINT DEFAULT NULL COMMENT '关联模板ID',
    `content` TEXT NOT NULL COMMENT '发送内容',
    `target_type` INT NOT NULL COMMENT '目标类型（1-全部客户 2-指定客户 3-指定线索 4-指定联系人）',
    `target_ids` TEXT DEFAULT NULL COMMENT '目标ID列表',
    `target_count` INT DEFAULT 0 COMMENT '目标数量',
    `success_count` INT DEFAULT 0 COMMENT '成功数量',
    `fail_count` INT DEFAULT 0 COMMENT '失败数量',
    `status` INT NOT NULL DEFAULT 1 COMMENT '状态（1-未提交 2-待审核 3-待发送 4-已发送 5-已驳回）',
    `owner_user_id` BIGINT NOT NULL COMMENT '负责人员ID',
    `send_time` DATETIME DEFAULT NULL COMMENT '发送时间',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_owner_user_id` (`owner_user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='营销群发表';

-- 客户关怀配置表
CREATE TABLE IF NOT EXISTS `crm_marketing_customer_care` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置编号',
    `sms_content` VARCHAR(500) DEFAULT NULL COMMENT '短信内容',
    `email_title` VARCHAR(200) DEFAULT NULL COMMENT '邮件标题',
    `email_body` TEXT DEFAULT NULL COMMENT '邮件正文（HTML）',
    `sender_email` VARCHAR(100) DEFAULT NULL COMMENT '发件人邮箱',
    `send_time` VARCHAR(10) DEFAULT NULL COMMENT '发送时间（HH:mm）',
    `sms_enabled` BIT(1) DEFAULT 0 COMMENT '是否启用短信',
    `email_enabled` BIT(1) DEFAULT 1 COMMENT '是否启用邮件',
    `holiday_list` TEXT DEFAULT NULL COMMENT '节假日列表（JSON）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户关怀配置表';

-- ==================== 菜单数据 ====================

SET NAMES utf8mb4;

-- 二级菜单：营销管理（父菜单 CRM 系统 ID=2397，置于商机之后 订单之前 sort=43）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5400, '营销管理', '', 2, 43, 2397, 'marketing', 'ep:present', '', 'CrmMarketing', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 营销活动
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5401, '营销活动', '', 2, 1, 5400, 'campaign', 'ep:data-board', 'crm/marketing/campaign/index', 'CrmCampaign', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5402, '营销活动查询', 'crm:campaign:query', 3, 1, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5403, '营销活动创建', 'crm:campaign:create', 3, 2, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5404, '营销活动更新', 'crm:campaign:update', 3, 3, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5405, '营销活动删除', 'crm:campaign:delete', 3, 4, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5406, '营销活动导出', 'crm:campaign:export', 3, 5, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5407, '营销活动导入', 'crm:campaign:import', 3, 6, 5401, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 群发管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5410, '群发管理', '', 2, 2, 5400, 'bulksend', 'ep:message', 'crm/marketing/bulksend/index', 'CrmBulkSend', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5411, '群发查询', 'crm:bulk-send:query', 3, 1, 5410, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5412, '群发创建', 'crm:bulk-send:create', 3, 2, 5410, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5413, '群发更新', 'crm:bulk-send:update', 3, 3, 5410, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5414, '群发删除', 'crm:bulk-send:delete', 3, 4, 5410, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 客户关怀
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5420, '客户关怀', '', 2, 3, 5400, 'customercare', 'ep:present', 'crm/marketing/customercare/index', 'CrmCustomerCare', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5421, '客户关怀查询', 'crm:customer-care:query', 3, 1, 5420, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5422, '客户关怀更新', 'crm:customer-care:update', 3, 2, 5420, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 发送分析
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5430, '发送分析', '', 2, 4, 5400, 'analysis', 'ep:data-line', 'crm/marketing/analysis/index', 'CrmMarketingAnalysis', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5431, '发送分析查询', 'crm:marketing-analysis:query', 3, 1, 5430, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
