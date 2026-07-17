-- 营销群发表
CREATE TABLE IF NOT EXISTS crm_marketing_bulk_send (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '群发编号',
    title VARCHAR(100) NOT NULL COMMENT '任务标题',
    type INT NOT NULL COMMENT '类型（1-短信群发 2-邮件群发）',
    template_id BIGINT DEFAULT NULL COMMENT '关联模板ID',
    content TEXT NOT NULL COMMENT '发送内容',
    target_type INT NOT NULL COMMENT '目标类型（1-全部客户 2-指定客户 3-指定线索 4-指定联系人）',
    target_ids TEXT DEFAULT NULL COMMENT '目标ID列表（JSON或逗号分隔）',
    target_count INT DEFAULT 0 COMMENT '目标数量',
    success_count INT DEFAULT 0 COMMENT '成功数量',
    fail_count INT DEFAULT 0 COMMENT '失败数量',
    status INT NOT NULL DEFAULT 1 COMMENT '状态（1-未提交 2-待审核 3-待发送 4-已发送 5-已驳回）',
    owner_user_id BIGINT NOT NULL COMMENT '负责人员ID',
    send_time DATETIME DEFAULT NULL COMMENT '发送时间',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='营销群发表';
