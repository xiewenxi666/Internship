-- ==========================================
-- CRM 订单报表菜单
-- 文件: InstallPackage/database/new/crm/003-crm-order-report.sql
-- 版本: 2026.01-SNAPSHOT
-- 日期: 2026-07-16
--
-- 注意：报表数据集需要在积木报表设计器中手动配置
-- 1. 系统管理 → 报表管理 → 报表设计器
-- 2. 点击「新增」→ 填写报表名称
-- 3. 数据集 → 新增 → SQL 模式
-- 4. 粘贴下方 SQL，调整参数
-- 5. 设计布局后保存
-- ==========================================

-- 获取当前最大菜单 ID
SET @max_menu_id = (SELECT MAX(id) FROM system_menu);

-- 订单报表菜单 (订单管理下的按钮)
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (5306, '订单报表', '', 2, 46, 2397, 'order-report', 'ep:data-analysis', 'crm/order/report/index', 'CrmOrderReport', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- ==========================================
-- 订单报表 SQL 数据集（参考，在报表设计器中配置时使用）
-- ==========================================
--
-- 带有日期/状态搜索参数的完整 SQL：
--
-- SELECT
--     o.id,
--     o.no AS order_no,
--     o.name AS order_name,
--     c.name AS customer_name,
--     o.owner_user_id,
--     su.nickname AS owner_name,
--     o.status,
--     CASE o.status
--         WHEN 0 THEN '未提交'
--         WHEN 10 THEN '已提交'
--         WHEN 15 THEN '审批中'
--         WHEN 20 THEN '已通过'
--         WHEN 30 THEN '已拒绝'
--         WHEN 60 THEN '已完成'
--         WHEN 70 THEN '已取消'
--     END AS status_name,
--     o.total_product_price,
--     o.discount_percent,
--     o.total_price,
--     o.order_date,
--     o.create_time,
--     o.update_time,
--     o.remark
-- FROM crm_order o
-- LEFT JOIN crm_customer c ON c.id = o.customer_id AND c.deleted = 0
-- LEFT JOIN system_users su ON su.id = o.owner_user_id
-- WHERE o.deleted = 0
--   AND o.tenant_id = ${tenant_id}
--   <#if (orderDateStart)?? && orderDateStart != ''> AND o.order_date >= '${orderDateStart}' </#if>
--   <#if (orderDateEnd)?? && orderDateEnd != ''> AND o.order_date <= '${orderDateEnd}' </#if>
--   <#if (status)?? && status != ''> AND o.status = ${status} </#if>
-- ORDER BY o.create_time DESC
--
-- 参数说明：
--   tenant_id      - 租户ID（积木报表自动注入）
--   orderDateStart - 下单日期开始（日期选择器控件）
--   orderDateEnd   - 下单日期结束（日期选择器控件）
--   status         - 订单状态（下拉框控件，数据字典 CRM_ORDER_STATUS）

-- 回滚脚本
-- DELETE FROM system_menu WHERE id IN (5300, 5301, 5302, 5303, 5304, 5305, 5306);
