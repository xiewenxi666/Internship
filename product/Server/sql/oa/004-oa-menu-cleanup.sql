-- OA 菜单清理 SQL（删除 002-oa-dict-menu.sql 的旧菜单 + 补充缺失的按钮权限）
-- 执行方式: docker exec -i mitedtsm-dev-mysql mysql -u root -p1234 --default-character-set=utf8mb4 mitedtsm_database < 004-oa-menu-cleanup.sql
-- 再执行: docker exec -i mitedtsm-dev-mysql mysql -u root -p1234 --default-character-set=utf8mb4 mitedtsm_database < 003-oa-menu-fixed.sql

-- ==========================================
-- Step 1: 删除 002-oa-dict-menu.sql 的旧菜单
-- 002 使用错误的类型（子菜单 type=1 应为 type=2，按钮 type=2 应为 type=3）
-- 导致 buildMenuTree() 不能正确过滤按钮，产生冲突路由
-- ==========================================

-- 1.1 先删除角色-菜单关联
DELETE FROM `system_role_menu` WHERE menu_id BETWEEN 2000 AND 2094;

-- 1.2 删除旧菜单
DELETE FROM `system_menu` WHERE id BETWEEN 2000 AND 2094;

-- ==========================================
-- Step 2: 将 003 菜单授权给 role_id=2
-- 003 已补充完整按钮权限（type=3），但角色关联只覆盖 5700-5740
-- 这里确保关联到位
-- ==========================================

INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 2, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5700 AND 5740
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 2)
  AND deleted = 0;

-- ==========================================
-- Step 3: 补充 BPM 审批相关菜单权限给 role_id=2
-- （审批人需要 BPM 任务处理权限）
-- ==========================================

-- 3.1 查找 BPM 任务菜单（type=2 或 type=1 的菜单节点）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 2, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE (path LIKE '/bpm%' OR name LIKE '%BPM%' OR name LIKE '%流程%')
  AND type IN (1, 2)
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 2)
  AND deleted = 0;
