-- OA 菜单迁移：从顶级目录移至 工作流程(/bpm) 下
-- 执行: docker exec -i mitedtsm-dev-mysql mysql -u root -p1234 --default-character-set=utf8mb4 mitedtsm_database < 005-oa-menu-move.sql

-- 1. 删除旧 OA示例 的角色关联
DELETE FROM `system_role_menu` WHERE menu_id IN (5, 1118, 1119, 1120);

-- 2. 删除旧 OA示例 菜单（先删子节点，再删父节点）
DELETE FROM `system_menu` WHERE id IN (1119, 1120);
DELETE FROM `system_menu` WHERE id IN (5, 1118);

-- 3. 修改 OA 菜单（5307）的 parent_id 和 path
--    parent_id: 0 → 1185（工作流程）
--    path: '/oa' → 'oa'（相对路径，继承自 /bpm）
UPDATE `system_menu` SET parent_id = 1185, path = 'oa' WHERE id = 5307;

-- 4. 将 OA 菜单授权给所有原 OA示例 有但 5307 系列没有的角色
--    角色 109（租户管理员）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 109, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5307 AND 5347
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 109);

--    角色 111（租户管理员）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 111, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5307 AND 5347
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 111);

--    角色 155（测试数据权限1）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 155, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5307 AND 5347
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 155);

--    角色 160（租户管理员）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 160, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5307 AND 5347
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 160);
