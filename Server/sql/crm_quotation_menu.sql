-- CRM 报价管理菜单（商机域，ID范围5200-5299）
-- 菜单ID: 5200 报价管理

-- 报价管理父菜单（挂在商机管理2410下）
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `visible`) VALUES
(5200, '报价管理', NULL, 2, 5, 2410, 'quotation', 'crm/quotation/index', 0, b'1');

-- 产品报价管理
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `visible`) VALUES
(5210, '产品报价管理', NULL, 2, 1, 5200, 'quotation', 'crm/quotation/index', 0, b'1');

-- 确认报价
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `visible`) VALUES
(5211, '确认报价', NULL, 2, 2, 5200, 'quotation/confirm', 'crm/quotation/confirm/index', 0, b'1');

-- 报价记录
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `visible`) VALUES
(5212, '报价记录', NULL, 2, 3, 5200, 'quotation/record', 'crm/quotation/record', 0, b'1');

-- 报价权限按钮
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `visible`) VALUES
(5201, '报价查询', 'crm:quotation:query', 3, 1, 5200, NULL, NULL, 0, b'1'),
(5202, '报价创建', 'crm:quotation:create', 3, 2, 5200, NULL, NULL, 0, b'1'),
(5203, '报价更新', 'crm:quotation:update', 3, 3, 5200, NULL, NULL, 0, b'1'),
(5204, '报价删除', 'crm:quotation:delete', 3, 4, 5200, NULL, NULL, 0, b'1'),
(5205, '报价导出', 'crm:quotation:export', 3, 5, 5200, NULL, NULL, 0, b'1');

-- 管理员角色（role_id=1）分配权限
INSERT IGNORE INTO `system_role_menu` (`role_id`, `menu_id`) VALUES
(1, 5200), (1, 5210), (1, 5211), (1, 5212),
(1, 5201), (1, 5202), (1, 5203), (1, 5204), (1, 5205);
