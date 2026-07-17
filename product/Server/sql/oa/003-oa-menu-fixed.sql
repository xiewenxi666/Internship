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
VALUES (5700, 'OA', '', 1, 50, 1185, 'oa', 'ep:notebook', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), path=VALUES(path), icon=VALUES(icon), parent_id=VALUES(parent_id);

SET @oa_menu_id = 5700;

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5701, '请假管理', '', 2, 1, @oa_menu_id, 'leave', 'ep:clock', 'views/oa/leave/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5702, '出差管理', '', 2, 2, @oa_menu_id, 'trip', 'ep:suitcase', 'views/oa/trip/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5703, '借款管理', '', 2, 3, @oa_menu_id, 'loan', 'ep:money', 'views/oa/loan/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5704, '拜访管理', '', 2, 4, @oa_menu_id, 'visit', 'ep:position', 'views/oa/visit/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5705, '请示管理', '', 2, 5, @oa_menu_id, 'request', 'ep:edit', 'views/oa/request/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5706, '工作报告', '', 2, 6, @oa_menu_id, 'report', 'ep:document', 'views/oa/report/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5707, '日程管理', '', 2, 7, @oa_menu_id, 'schedule', 'ep:calendar', 'views/oa/schedule/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5708, '任务管理', '', 2, 8, @oa_menu_id, 'task', 'ep:list', 'views/oa/task/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`)
VALUES (5709, '文档管理', '', 2, 9, @oa_menu_id, 'document', 'ep:folder', 'views/oa/document/index', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), type=VALUES(type), component=VALUES(component);

-- 按钮权限
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5710, '创建请假', 'oa:leave:create', 3, 1, 5701, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5711, '查询请假', 'oa:leave:query', 3, 2, 5701, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5712, '删除请假', 'oa:leave:delete', 3, 3, 5701, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 出差
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5713, '创建出差', 'oa:trip:create', 3, 1, 5702, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5714, '查询出差', 'oa:trip:query', 3, 2, 5702, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5715, '删除出差', 'oa:trip:delete', 3, 3, 5702, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 借款
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5716, '创建借款', 'oa:loan:create', 3, 1, 5703, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5717, '查询借款', 'oa:loan:query', 3, 2, 5703, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5718, '删除借款', 'oa:loan:delete', 3, 3, 5703, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 拜访
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5719, '创建拜访', 'oa:visit:create', 3, 1, 5704, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5720, '查询拜访', 'oa:visit:query', 3, 2, 5704, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5721, '删除拜访', 'oa:visit:delete', 3, 3, 5704, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 请示
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5722, '创建请示', 'oa:request:create', 3, 1, 5705, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5723, '查询请示', 'oa:request:query', 3, 2, 5705, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5724, '删除请示', 'oa:request:delete', 3, 3, 5705, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 工作报告
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5725, '创建报告', 'oa:report:create', 3, 1, 5706, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5726, '查询报告', 'oa:report:query', 3, 2, 5706, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5727, '更新报告', 'oa:report:update', 3, 3, 5706, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5728, '删除报告', 'oa:report:delete', 3, 4, 5706, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 日程
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5729, '创建日程', 'oa:schedule:create', 3, 1, 5707, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5730, '查询日程', 'oa:schedule:query', 3, 2, 5707, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5731, '更新日程', 'oa:schedule:update', 3, 3, 5707, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5732, '删除日程', 'oa:schedule:delete', 3, 4, 5707, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 任务
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5733, '创建任务', 'oa:task:create', 3, 1, 5708, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5734, '查询任务', 'oa:task:query', 3, 2, 5708, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5735, '更新任务', 'oa:task:update', 3, 3, 5708, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5736, '删除任务', 'oa:task:delete', 3, 4, 5708, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
-- 按钮权限: 文档
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5737, '创建文档', 'oa:document:create', 3, 1, 5709, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5738, '查询文档', 'oa:document:query', 3, 2, 5709, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5739, '更新文档', 'oa:document:update', 3, 3, 5709, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `component`, `status`)
VALUES (5740, '删除文档', 'oa:document:delete', 3, 4, 5709, '', '', 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), permission=VALUES(permission), type=VALUES(type);

-- 角色-菜单关联
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 2, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5700 AND 5740
AND NOT EXISTS (SELECT 1 FROM `system_role_menu` WHERE role_id=2 AND menu_id=`system_menu`.id);
