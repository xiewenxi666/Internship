-- =============================================
-- OA 模块初始化脚本
-- 8张业务表：请假/出差/借款/拜访/工作报告/日程/请示 + 文档目录扩展
-- 执行此脚本以创建OA模块所需的全部数据库表
-- =============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Table structure for oa_leave (请假表)
-- 审批流程类型，对接Flowable工作流引擎
-- 流程定义Key: oa_leave
-- 审批状态对齐 BpmProcessInstanceStatusEnum (-1未开始/1审批中/2审批通过/3审批不通过/4已取消)
-- ----------------------------
DROP TABLE IF EXISTS `oa_leave`;
CREATE TABLE `oa_leave`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '请假表单主键',
  `user_id` bigint NOT NULL COMMENT '申请人的用户编号',
  `type` tinyint NOT NULL COMMENT '请假类型（1-事假，2-病假，3-年假，4-婚假，5-产假，6-丧假，7-调休，8-其他）',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请假原因',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `day` decimal(5, 1) NOT NULL COMMENT '请假天数（支持0.5半天）',
  `status` tinyint NOT NULL DEFAULT -1 COMMENT '审批状态（-1-未开始，1-审批中，2-审批通过，3-审批不通过，4-已取消）',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流程实例的编号（关联 act_ru_execution.ID_）',
  `start_user_select_assignees` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发起人选定的审批人Map（JSON格式，{taskKey1:[userId1,userId2]}）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_process_instance_id`(`process_instance_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 请假申请表';

-- ----------------------------
-- Table structure for oa_business_trip (出差表)
-- 审批流程类型，对接Flowable工作流引擎
-- 流程定义Key: oa_business_trip
-- ----------------------------
DROP TABLE IF EXISTS `oa_business_trip`;
CREATE TABLE `oa_business_trip`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出差表单主键',
  `user_id` bigint NOT NULL COMMENT '申请人的用户编号',
  `destination` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '出差目的地',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '出差事由',
  `companion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '同行人员',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `day` decimal(5, 1) NOT NULL COMMENT '出差天数（支持0.5半天）',
  `vehicle` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '交通工具',
  `estimated_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '预估费用（元）',
  `status` tinyint NOT NULL DEFAULT -1 COMMENT '审批状态（-1-未开始，1-审批中，2-审批通过，3-审批不通过，4-已取消）',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流程实例的编号（关联 act_ru_execution.ID_）',
  `start_user_select_assignees` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发起人选定的审批人Map（JSON格式）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_process_instance_id`(`process_instance_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 出差申请表';

-- ----------------------------
-- Table structure for oa_loan (借款表)
-- 审批流程类型，对接Flowable工作流引擎
-- 流程定义Key: oa_loan
-- 状态5-已还款 是审批通过后的业务状态
-- ----------------------------
DROP TABLE IF EXISTS `oa_loan`;
CREATE TABLE `oa_loan`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '借款表单主键',
  `user_id` bigint NOT NULL COMMENT '申请人的用户编号',
  `amount` decimal(12, 2) NOT NULL COMMENT '借款金额（元）',
  `purpose` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '借款事由',
  `repayment_plan` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '还款计划',
  `expected_repayment_time` datetime NULL DEFAULT NULL COMMENT '预计还款时间',
  `actual_repayment_time` datetime NULL DEFAULT NULL COMMENT '实际还款时间',
  `status` tinyint NOT NULL DEFAULT -1 COMMENT '审批状态（-1-未开始，1-审批中，2-审批通过，3-审批不通过，4-已取消，5-已还款）',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流程实例的编号（关联 act_ru_execution.ID_）',
  `start_user_select_assignees` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发起人选定的审批人Map（JSON格式）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_process_instance_id`(`process_instance_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 借款申请表';

-- ----------------------------
-- Table structure for oa_visit (拜访记录表)
-- 非审批流程类型，纯业务记录表
-- 可关联CRM客户数据
-- ----------------------------
DROP TABLE IF EXISTS `oa_visit`;
CREATE TABLE `oa_visit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '拜访表单主键',
  `user_id` bigint NOT NULL COMMENT '拜访人的用户编号',
  `crm_customer_id` bigint NULL DEFAULT NULL COMMENT '关联CRM客户编号（关联 crm_customer.id）',
  `customer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `visit_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '拜访地址',
  `visit_time` datetime NOT NULL COMMENT '拜访时间',
  `purpose` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拜访目的',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '拜访结果',
  `next_visit_time` datetime NULL DEFAULT NULL COMMENT '下次拜访时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0-待拜访，1-已完成，2-已取消）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_crm_customer_id`(`crm_customer_id` ASC) USING BTREE,
  INDEX `idx_visit_time`(`visit_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 拜访记录表';

-- ----------------------------
-- Table structure for oa_work_report (工作报告表)
-- 非审批流程类型，提交/审阅模式
-- ----------------------------
DROP TABLE IF EXISTS `oa_work_report`;
CREATE TABLE `oa_work_report`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工作报告主键',
  `user_id` bigint NOT NULL COMMENT '报告人的用户编号',
  `type` tinyint NOT NULL COMMENT '报告类型（1-日报，2-周报，3-月报，4-年报）',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '报告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '报告内容',
  `plan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '明日/下周/下月计划',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '工作总结',
  `report_date` date NOT NULL COMMENT '报告日期（日报为当天，周报为当周周一，月报为当月1号）',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0-草稿，1-已提交，2-已审阅）',
  `reviewer_user_id` bigint NULL DEFAULT NULL COMMENT '审阅人用户编号',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审阅时间',
  `review_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审阅意见',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_report_date`(`report_date` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 工作报告表';

-- ----------------------------
-- Table structure for oa_schedule (日程表)
-- 非审批流程类型，个人日程管理
-- ----------------------------
DROP TABLE IF EXISTS `oa_schedule`;
CREATE TABLE `oa_schedule`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日程主键',
  `user_id` bigint NOT NULL COMMENT '所属人的用户编号',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '日程标题',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '日程描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `is_all_day` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否全天事件',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地点',
  `type` tinyint NOT NULL DEFAULT 1 COMMENT '日程类型（1-个人，2-会议，3-任务，4-纪念日，5-其他）',
  `priority` tinyint NOT NULL DEFAULT 0 COMMENT '优先级（0-普通，1-重要，2-紧急）',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0-待办，1-进行中，2-已完成，3-已取消）',
  `reminder_time` int NULL DEFAULT NULL COMMENT '提前提醒分钟数',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '日程颜色标识',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_start_time`(`start_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 日程表';

-- ----------------------------
-- Table structure for oa_request (请示表)
-- 审批流程类型，对接Flowable工作流引擎
-- 流程定义Key: oa_request
-- 相比请假更通用，支持多种请示类型和附件
-- ----------------------------
DROP TABLE IF EXISTS `oa_request`;
CREATE TABLE `oa_request`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '请示表单主键',
  `user_id` bigint NOT NULL COMMENT '申请人的用户编号',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请示标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请示内容',
  `type` tinyint NOT NULL DEFAULT 1 COMMENT '请示类型（1-人事审批，2-财务审批，3-行政审批，4-采购审批，5-其他）',
  `urgency` tinyint NOT NULL DEFAULT 0 COMMENT '紧急程度（0-普通，1-紧急，2-特急）',
  `attachment` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '附件地址（多个用逗号分隔，关联 infra_file 表）',
  `expected_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '涉及金额（元）',
  `status` tinyint NOT NULL DEFAULT -1 COMMENT '审批状态（-1-未开始，1-审批中，2-审批通过，3-审批不通过，4-已取消）',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流程实例的编号（关联 act_ru_execution.ID_）',
  `start_user_select_assignees` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发起人选定的审批人Map（JSON格式）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_process_instance_id`(`process_instance_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_urgency`(`urgency` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 请示申请表';

-- ----------------------------
-- Table structure for oa_document_dir (文档目录扩展表)
-- 树形结构目录 + 文档管理
-- 支持多层级目录、权限控制、文件版本管理
-- ----------------------------
DROP TABLE IF EXISTS `oa_document_dir`;
CREATE TABLE `oa_document_dir`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文档目录主键',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父目录ID（0表示根目录）',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目录名称/文档名称',
  `type` tinyint NOT NULL DEFAULT 1 COMMENT '类型（1-目录，2-文档）',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序（值越小越靠前）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目录描述/文档摘要',
  `keywords` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '搜索关键字（多个用逗号分隔，用于全文检索）',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '所属人用户编号（NULL表示公共目录）',
  `permission` tinyint NOT NULL DEFAULT 0 COMMENT '访问权限（0-公开，1-部门可见，2-仅自己可见）',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文档附件地址（type=2时，关联 infra_file 表）',
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原始文件名',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型（doc/pdf/xlsx等）',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文档大小（字节）',
  `version` int NULL DEFAULT 1 COMMENT '文档版本号',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0-正常，1-归档）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_owner_user_id`(`owner_user_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_sort`(`sort` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 文档目录扩展表';

-- ----------------------------
-- Table structure for oa_message (内部消息表)
-- ----------------------------
DROP TABLE IF EXISTS `oa_message`;
CREATE TABLE `oa_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息主键',
  `sender_user_id` bigint NOT NULL COMMENT '发送人用户编号',
  `receiver_user_id` bigint NOT NULL COMMENT '接收人用户编号',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息内容',
  `read_status` tinyint NOT NULL DEFAULT 0 COMMENT '阅读状态（0-未读，1-已读）',
  `read_time` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_user_id`(`sender_user_id` ASC) USING BTREE,
  INDEX `idx_receiver_user_id`(`receiver_user_id` ASC) USING BTREE,
  INDEX `idx_read_status`(`read_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OA 内部消息表';