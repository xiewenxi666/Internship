-- 营销模块表字段补全（与 CrmCampaignDO / CrmBulkSendDO / CrmCustomerCareConfigDO 对齐）

ALTER TABLE crm_marketing_customer_care ADD COLUMN test_email VARCHAR(100) DEFAULT NULL COMMENT '测试收件人邮箱' AFTER send_time;

ALTER TABLE crm_marketing_bulk_send ADD COLUMN product_ids VARCHAR(500) DEFAULT NULL COMMENT '关联产品ID列表' AFTER title;

ALTER TABLE crm_marketing_bulk_send ADD COLUMN campaign_id BIGINT DEFAULT NULL COMMENT '营销活动ID' AFTER product_ids;

ALTER TABLE crm_marketing_campaign ADD COLUMN product_ids VARCHAR(500) DEFAULT NULL COMMENT '关联产品ID列表' AFTER title;
