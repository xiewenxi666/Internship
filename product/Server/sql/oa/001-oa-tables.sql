-- ==========================================
-- OA 模块 DDL
-- 文件: sql/oa/001-oa-tables.sql
-- ==========================================

-- 请假表
CREATE TABLE `oa_leave` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '申请人ID',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '请假类型',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '原因',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `day` BIGINT DEFAULT NULL COMMENT '请假天数',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审批状态: 1-审批中 2-通过 3-不通过 4-已取消',
    `process_instance_id` VARCHAR(64) DEFAULT NULL COMMENT '流程实例ID',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA请假表';

-- 出差表
CREATE TABLE `oa_trip` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '申请人ID',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '出差类型',
    `destination` VARCHAR(200) DEFAULT NULL COMMENT '目的地',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `day` BIGINT DEFAULT NULL COMMENT '出差天数',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '出差原因',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审批状态: 1-审批中 2-通过 3-不通过 4-已取消',
    `process_instance_id` VARCHAR(64) DEFAULT NULL COMMENT '流程实例ID',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA出差表';

-- 借款表
CREATE TABLE `oa_loan` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '申请人ID',
    `purpose` VARCHAR(100) DEFAULT NULL COMMENT '借款用途',
    `amount` BIGINT DEFAULT NULL COMMENT '借款金额（分）',
    `expected_repay_time` DATETIME DEFAULT NULL COMMENT '预计归还时间',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '借款原因',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审批状态: 1-审批中 2-通过 3-不通过 4-已取消',
    `process_instance_id` VARCHAR(64) DEFAULT NULL COMMENT '流程实例ID',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA借款表';

-- 拜访表
CREATE TABLE `oa_visit` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '申请人ID',
    `customer_id` BIGINT DEFAULT NULL COMMENT '客户ID',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `visit_time` DATETIME DEFAULT NULL COMMENT '拜访时间',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '拜访地点',
    `purpose` VARCHAR(500) DEFAULT NULL COMMENT '拜访目的',
    `notes` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审批状态: 1-审批中 2-通过 3-不通过 4-已取消',
    `process_instance_id` VARCHAR(64) DEFAULT NULL COMMENT '流程实例ID',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_customer_id` (`customer_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA拜访表';

-- 请示表
CREATE TABLE `oa_request` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '申请人ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `urgency` TINYINT DEFAULT 1 COMMENT '紧急程度: 1-普通 2-紧急 3-特急',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审批状态: 1-审批中 2-通过 3-不通过 4-已取消',
    `process_instance_id` VARCHAR(64) DEFAULT NULL COMMENT '流程实例ID',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA请示表';

-- 工作报告表
CREATE TABLE `oa_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` TINYINT NOT NULL COMMENT '报告类型: 1-日报 2-周报 3-月报',
    `report_date` DATE NOT NULL COMMENT '报告日期',
    `content` TEXT NOT NULL COMMENT '工作内容',
    `plan` TEXT COMMENT '工作计划',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_report_date` (`report_date`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA工作报告表';

-- 日程表
CREATE TABLE `oa_schedule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '日程标题',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `all_day` TINYINT DEFAULT 0 COMMENT '是否全天: 0-否 1-是',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '地点',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_end_time` (`end_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA日程表';

-- 任务表
CREATE TABLE `oa_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '创建人ID',
    `assignee_id` BIGINT DEFAULT NULL COMMENT '负责人ID',
    `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
    `description` TEXT COMMENT '任务描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1-待办 2-进行中 3-已完成',
    `priority` TINYINT DEFAULT 2 COMMENT '优先级: 1-低 2-中 3-高',
    `deadline` DATETIME DEFAULT NULL COMMENT '截止时间',
    `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_assignee_id` (`assignee_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA任务表';

-- 文档表
CREATE TABLE `oa_document` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级ID',
    `name` VARCHAR(200) NOT NULL COMMENT '名称',
    `is_folder` TINYINT NOT NULL DEFAULT 0 COMMENT '是否文件夹: 0-否 1-是',
    `file_id` BIGINT DEFAULT NULL COMMENT '文件ID（infra_file.id）',
    `file_url` VARCHAR(500) DEFAULT NULL COMMENT '文件URL',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小（字节）',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OA文档表';
