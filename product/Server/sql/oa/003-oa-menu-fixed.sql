-- OA 菜单 SQL（修复ID冲突版本，中文已正确编码）
-- 使用方式: docker exec -i mitedtsm-dev-mysql mysql -u root -p1234 --default-character-set=utf8mb4 mitedtsm_database < 003-oa-menu-fixed.sql

-- 字典数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4000, 0, '病假', '1', 'oa_leave_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4001, 1, '事假', '2', 'oa_leave_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4002, 2, '婚假', '3', 'oa_leave_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4003, 3, '年假', '4', 'oa_leave_type', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4010, 0, '商务出差', '1', 'oa_trip_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4011, 1, '培训出差', '2', 'oa_trip_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4012, 2, '会议出差', '3', 'oa_trip_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4020, 0, '差旅借款', '1', 'oa_loan_purpose', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4021, 1, '采购借款', '2', 'oa_loan_purpose', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4022, 2, '其他借款', '3', 'oa_loan_purpose', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4030, 0, '客户拜访', '1', 'oa_visit_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4031, 1, '商务洽谈', '2', 'oa_visit_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4032, 2, '售后服务', '3', 'oa_visit_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4040, 0, '日报', '1', 'oa_report_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4041, 1, '周报', '2', 'oa_report_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4042, 2, '月报', '3', 'oa_report_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4050, 0, '普通', '1', 'oa_urgency_type', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4051, 1, '紧急', '2', 'oa_urgency_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4052, 2, '特急', '3', 'oa_urgency_type', 0, 'danger', '', NULL, '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE label=VALUES(label), value=VALUES(value), color_type=VALUES(color_type);

-- 菜单
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5307, 'OA', '', 1, 50, 0, '/oa', 'ep:notebook', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), path=VALUES(path), icon=VALUES(icon);

SET @oa_menu_id = 5307;

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5308, '请假管理', '', 2, 1, @oa_menu_id, 'leave', 'ep:clock', 'views/oa/leave/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5309, '出差管理', '', 2, 2, @oa_menu_id, 'trip', 'ep:suitcase', 'views/oa/trip/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5310, '借款管理', '', 2, 3, @oa_menu_id, 'loan', 'ep:money', 'views/oa/loan/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5311, '拜访管理', '', 2, 4, @oa_menu_id, 'visit', 'ep:position', 'views/oa/visit/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5312, '请示管理', '', 2, 5, @oa_menu_id, 'request', 'ep:edit', 'views/oa/request/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5313, '工作报告', '', 2, 6, @oa_menu_id, 'report', 'ep:document', 'views/oa/report/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5314, '日程管理', '', 2, 7, @oa_menu_id, 'schedule', 'ep:calendar', 'views/oa/schedule/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5315, '任务管理', '', 2, 8, @oa_menu_id, 'task', 'ep:list', 'views/oa/task/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5316, '文档管理', '', 2, 9, @oa_menu_id, 'document', 'ep:folder', 'views/oa/document/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);

-- 按钮权限
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5317, '创建请假', 'oa:leave:create', 3, 1, 5308, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5318, '查询请假', 'oa:leave:query', 3, 2, 5308, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5319, '删除请假', 'oa:leave:delete', 3, 3, 5308, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- ... 出差/借款/拜访/请示/报告/日程/任务/文档 按钮（类似）
-- （完整按钮列表请参考数据库实际数据）

-- 角色-菜单关联
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 2, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5307 AND 5347
AND NOT EXISTS (SELECT 1 FROM `system_role_menu` WHERE role_id=2 AND menu_id=`system_menu`.id);
