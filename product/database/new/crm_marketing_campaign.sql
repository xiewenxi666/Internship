-- 营销活动表
CREATE TABLE IF NOT EXISTS crm_marketing_campaign (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '活动编号',
    title VARCHAR(100) NOT NULL COMMENT '活动标题',
    type INT NOT NULL COMMENT '活动类型（1-促销活动 2-品牌活动 3-会议营销）',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    estimated_cost DECIMAL(12,2) DEFAULT 0 COMMENT '预计成本',
    estimated_revenue DECIMAL(12,2) DEFAULT 0 COMMENT '预计收入',
    owner_user_id BIGINT NOT NULL COMMENT '负责人员ID',
    participants VARCHAR(500) DEFAULT NULL COMMENT '参与人员ID列表（逗号分隔）',
    address VARCHAR(200) DEFAULT NULL COMMENT '活动地址',
    description TEXT DEFAULT NULL COMMENT '活动详情（富文本）',
    status INT NOT NULL DEFAULT 1 COMMENT '活动状态（1-筹备 2-进行中 3-已结束）',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='营销活动表';
