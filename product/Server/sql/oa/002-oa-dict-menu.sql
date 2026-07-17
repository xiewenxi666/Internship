-- ==========================================
-- OA 字典与菜单 SQL
-- 文件: sql/oa/002-oa-dict-menu.sql
-- ==========================================

-- 1. 字典类型
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (200, 'OA 请假类型', 'oa_leave_type', 0, NULL, '1', NOW(), '1', NOW(), b'0', NULL);

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (201, 'OA 出差类型', 'oa_trip_type', 0, NULL, '1', NOW(), '1', NOW(), b'0', NULL);

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (202, 'OA 借款用途', 'oa_loan_purpose', 0, NULL, '1', NOW(), '1', NOW(), b'0', NULL);

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (203, 'OA 拜访类型', 'oa_visit_type', 0, NULL, '1', NOW(), '1', NOW(), b'0', NULL);

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (204, 'OA 报告类型', 'oa_report_type', 0, NULL, '1', NOW(), '1', NOW(), b'0', NULL);

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (205, 'OA 报告类型', 'oa_urgency_type', 0, NULL, '1', NOW(), '1', NOW(), b'0', NULL);

-- 2. 字典数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2000, 0, '病假', '1', 'oa_leave_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2001, 1, '事假', '2', 'oa_leave_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2002, 2, '婚假', '3', 'oa_leave_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2003, 3, '年假', '4', 'oa_leave_type', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2010, 0, '商务出差', '1', 'oa_trip_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2011, 1, '培训出差', '2', 'oa_trip_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2012, 2, '会议出差', '3', 'oa_trip_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2020, 0, '差旅借款', '1', 'oa_loan_purpose', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2021, 1, '采购借款', '2', 'oa_loan_purpose', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2022, 2, '其他借款', '3', 'oa_loan_purpose', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2030, 0, '客户拜访', '1', 'oa_visit_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2031, 1, '商务洽谈', '2', 'oa_visit_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2032, 2, '售后服务', '3', 'oa_visit_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2040, 0, '日报', '1', 'oa_report_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2041, 1, '周报', '2', 'oa_report_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2042, 2, '月报', '3', 'oa_report_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2050, 0, '普通', '1', 'oa_urgency_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2051, 1, '紧急', '2', 'oa_urgency_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2052, 2, '特急', '3', 'oa_urgency_type', 0, 'danger', '', NULL, '1', NOW(), '1', NOW(), b'0');

-- 3. 菜单
-- 一级菜单: OA
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2000, 'OA', NULL, 1, 50, 0, '/oa', 'ep:notebook', '', 0, 1);

SET @oa_menu_id = 2000;

-- 二级菜单
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2010, '请假管理', NULL, 1, 1, @oa_menu_id, 'leave', 'ep:time', 'views/oa/leave/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2020, '出差管理', NULL, 1, 2, @oa_menu_id, 'trip', 'ep:airplane', 'views/oa/trip/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2030, '借款管理', NULL, 1, 3, @oa_menu_id, 'loan', 'ep:money', 'views/oa/loan/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2040, '拜访管理', NULL, 1, 4, @oa_menu_id, 'visit', 'ep:position', 'views/oa/visit/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2050, '请示管理', NULL, 1, 5, @oa_menu_id, 'request', 'ep:edit', 'views/oa/request/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2060, '工作报告', NULL, 1, 6, @oa_menu_id, 'report', 'ep:document', 'views/oa/report/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2070, '日程管理', NULL, 1, 7, @oa_menu_id, 'schedule', 'ep:calendar', 'views/oa/schedule/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2080, '任务管理', NULL, 1, 8, @oa_menu_id, 'task', 'ep:list', 'views/oa/task/index', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `tenant_id`)
VALUES (2090, '文档管理', NULL, 1, 9, @oa_menu_id, 'document', 'ep:folder', 'views/oa/document/index', 0, 1);

-- 按钮权限: 请假
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2011, '创建请假', 'oa:leave:create', 2, 1, 2010, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2012, '查询请假', 'oa:leave:query', 2, 2, 2010, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2013, '删除请假', 'oa:leave:delete', 2, 3, 2010, '', '', 0, 1);

-- 按钮权限: 出差
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2021, '创建出差', 'oa:trip:create', 2, 1, 2020, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2022, '查询出差', 'oa:trip:query', 2, 2, 2020, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2023, '删除出差', 'oa:trip:delete', 2, 3, 2020, '', '', 0, 1);

-- 按钮权限: 借款
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2031, '创建借款', 'oa:loan:create', 2, 1, 2030, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2032, '查询借款', 'oa:loan:query', 2, 2, 2030, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2033, '删除借款', 'oa:loan:delete', 2, 3, 2030, '', '', 0, 1);

-- 按钮权限: 拜访
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2041, '创建拜访', 'oa:visit:create', 2, 1, 2040, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2042, '查询拜访', 'oa:visit:query', 2, 2, 2040, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2043, '删除拜访', 'oa:visit:delete', 2, 3, 2040, '', '', 0, 1);

-- 按钮权限: 请示
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2051, '创建请示', 'oa:request:create', 2, 1, 2050, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2052, '查询请示', 'oa:request:query', 2, 2, 2050, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2053, '删除请示', 'oa:request:delete', 2, 3, 2050, '', '', 0, 1);

-- 按钮权限: 工作报告
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2061, '创建报告', 'oa:report:create', 2, 1, 2060, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2062, '查询报告', 'oa:report:query', 2, 2, 2060, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2063, '更新报告', 'oa:report:update', 2, 3, 2060, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2064, '删除报告', 'oa:report:delete', 2, 4, 2060, '', '', 0, 1);

-- 按钮权限: 日程
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2071, '创建日程', 'oa:schedule:create', 2, 1, 2070, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2072, '查询日程', 'oa:schedule:query', 2, 2, 2070, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2073, '更新日程', 'oa:schedule:update', 2, 3, 2070, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2074, '删除日程', 'oa:schedule:delete', 2, 4, 2070, '', '', 0, 1);

-- 按钮权限: 任务
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2081, '创建任务', 'oa:task:create', 2, 1, 2080, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2082, '查询任务', 'oa:task:query', 2, 2, 2080, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2083, '更新任务', 'oa:task:update', 2, 3, 2080, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2084, '删除任务', 'oa:task:delete', 2, 4, 2080, '', '', 0, 1);

-- 按钮权限: 文档
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2091, '创建文档', 'oa:document:create', 2, 1, 2090, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2092, '查询文档', 'oa:document:query', 2, 2, 2090, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2093, '更新文档', 'oa:document:update', 2, 3, 2090, '', '', 0, 1);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`, `tenant_id`)
VALUES (2094, '删除文档', 'oa:document:delete', 2, 4, 2090, '', '', 0, 1);
