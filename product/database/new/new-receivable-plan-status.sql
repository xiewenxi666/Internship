-- 回款计划新增字段：计划状态、回款占比
ALTER TABLE `crm_receivable_plan` ADD COLUMN `status` tinyint NULL DEFAULT NULL COMMENT '计划状态(1=已完成 2=未完成 3=已逾期)' AFTER `remark`;
ALTER TABLE `crm_receivable_plan` ADD COLUMN `percent` decimal(24, 6) NULL DEFAULT NULL COMMENT '计划回款占比(%)' AFTER `status`;

-- 回款计划状态字典
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (200, '回款计划-计划状态', 'crm_receivable_plan_status', 0, '回款计划-计划状态', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1501, 1, '已完成', '1', 'crm_receivable_plan_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1502, 2, '未完成', '2', 'crm_receivable_plan_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (1503, 3, '已逾期', '3', 'crm_receivable_plan_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');
