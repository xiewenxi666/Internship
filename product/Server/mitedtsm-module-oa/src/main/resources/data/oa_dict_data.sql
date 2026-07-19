-- OA 请假类型数据
-- 通过管理后台「系统管理 -> 字典管理」也可手动配置
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1200, 1, '年假',   '1', 'oa_leave_type', 0, 'primary',   '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1201, 2, '调休',   '2', 'oa_leave_type', 0, 'success',   '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1202, 3, '病假',   '3', 'oa_leave_type', 0, 'warning',   '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1203, 4, '事假',   '4', 'oa_leave_type', 0, 'info',      '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1204, 5, '婚假',   '5', 'oa_leave_type', 0, 'danger',    '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1205, 6, '产假',   '6', 'oa_leave_type', 0, 'primary',   '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1206, 7, '丧假',   '7', 'oa_leave_type', 0, 'info',      '', NULL, '1', NOW(), '1', NOW(), b'0');

-- OA 请假状态数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1210, 1, '待提交', '1', 'oa_leave_status', 0, 'info',    '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1211, 2, '审批中', '2', 'oa_leave_status', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1212, 3, '已通过', '3', 'oa_leave_status', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1213, 4, '未通过', '4', 'oa_leave_status', 0, 'danger',  '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1214, 5, '已取消', '5', 'oa_leave_status', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

-- OA 出差状态数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1220, 1, '待提交', '1', 'oa_business_trip_status', 0, 'info',    '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1221, 2, '审批中', '2', 'oa_business_trip_status', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1222, 3, '已通过', '3', 'oa_business_trip_status', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1223, 4, '未通过', '4', 'oa_business_trip_status', 0, 'danger',  '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1224, 5, '已取消', '5', 'oa_business_trip_status', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

-- OA 借款状态数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1230, 1, '待提交', '1', 'oa_loan_status', 0, 'info',    '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1231, 2, '审批中', '2', 'oa_loan_status', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1232, 3, '已通过', '3', 'oa_loan_status', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1233, 4, '未通过', '4', 'oa_loan_status', 0, 'danger',  '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1234, 5, '已还款', '5', 'oa_loan_status', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1235, 6, '已取消', '6', 'oa_loan_status', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

-- OA 拜访状态数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1240, 1, '待拜访', '1', 'oa_visit_status', 0, 'info',    '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1241, 2, '拜访中', '2', 'oa_visit_status', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1242, 3, '已完成', '3', 'oa_visit_status', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1243, 4, '已取消', '4', 'oa_visit_status', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');

-- OA 请示类型数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1250, 1, '经费请示', '1', 'oa_request_type', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1251, 2, '人事请示', '2', 'oa_request_type', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1252, 3, '物资请示', '3', 'oa_request_type', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1253, 4, '其他请示', '4', 'oa_request_type', 0, 'info',    '', NULL, '1', NOW(), '1', NOW(), b'0');

-- OA 请示状态数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
    (1260, 1, '待提交', '1', 'oa_request_status', 0, 'info',    '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1261, 2, '审批中', '2', 'oa_request_status', 0, 'primary', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1262, 3, '已通过', '3', 'oa_request_status', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1263, 4, '未通过', '4', 'oa_request_status', 0, 'danger',  '', NULL, '1', NOW(), '1', NOW(), b'0'),
    (1264, 5, '已取消', '5', 'oa_request_status', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');
