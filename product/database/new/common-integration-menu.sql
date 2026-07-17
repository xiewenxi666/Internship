-- ===========================================
-- 公共集成域 — 菜单初始化 SQL
-- 菜单ID段: 5000-5099
-- 说明: 公共集成域主要是后端服务能力，前端只需少量管理页面入口
-- ===========================================

-- 导入模板管理 (菜单ID: 5000)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5000, '导入管理', '', 1, 1, 0, '/crm/import', 'ep:upload', 'crm/common/import/index', 'CrmImport', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 菜单国际化 (中文)
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (5000, 'zh-CN', '导入管理');

-- 菜单国际化 (英文)
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (5000, 'en', 'Import Management');
