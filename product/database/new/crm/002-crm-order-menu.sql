-- ==========================================
-- CRM 订单菜单与权限数据
-- 文件: database/new/crm/002-crm-order-menu.sql
-- 日期: 2026-07-14
-- ==========================================

SET NAMES utf8mb4;

-- 二级菜单：订单管理（父菜单 CRM 系统 ID=2397，置于商机之后 合同之前 sort=45）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (6352, '订单管理', '', 2, 45, 2397, 'order', 'ep:list', 'crm/order/index', 'CrmOrder', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 按钮权限
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (6353, '订单查询', 'crm:order:query', 3, 1, 6352, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (6354, '订单创建', 'crm:order:create', 3, 2, 6352, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (6355, '订单更新', 'crm:order:update', 3, 3, 6352, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (6356, '订单删除', 'crm:order:delete', 3, 4, 6352, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (6357, '订单导出', 'crm:order:export', 3, 5, 6352, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
