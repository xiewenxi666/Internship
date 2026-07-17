-- OA 菜单 ID 迁移：5307–5347 → 5700–5740
-- 原因：公共集成域分配 OA 域 ID 段为 5700–5799，原 5307–5347 落在订单域(5300–5399)范围内
-- 执行: docker exec -i mitedtsm-dev-mysql mysql -u root -p1234 --default-character-set=utf8mb4 mitedtsm_database < 006-oa-menu-id-migrate.sql

SET FOREIGN_KEY_CHECKS = 0;

-- 1. 删除旧的 system_role_menu 关联（后续用新 ID 重新插入）
DELETE FROM `system_role_menu` WHERE menu_id BETWEEN 5307 AND 5347;

-- 2. 更新 system_menu.id：旧 ID → 新 ID
--    使用临时列来存储新 ID，避免冲突
--    先添加一个临时列
ALTER TABLE `system_menu` ADD COLUMN `new_id` BIGINT NULL AFTER `id`;

--    设置新 ID 映射
UPDATE `system_menu` SET `new_id` = 5700 WHERE `id` = 5307;
UPDATE `system_menu` SET `new_id` = 5701 WHERE `id` = 5308;
UPDATE `system_menu` SET `new_id` = 5702 WHERE `id` = 5309;
UPDATE `system_menu` SET `new_id` = 5703 WHERE `id` = 5310;
UPDATE `system_menu` SET `new_id` = 5704 WHERE `id` = 5311;
UPDATE `system_menu` SET `new_id` = 5705 WHERE `id` = 5312;
UPDATE `system_menu` SET `new_id` = 5706 WHERE `id` = 5313;
UPDATE `system_menu` SET `new_id` = 5707 WHERE `id` = 5314;
UPDATE `system_menu` SET `new_id` = 5708 WHERE `id` = 5315;
UPDATE `system_menu` SET `new_id` = 5709 WHERE `id` = 5316;
UPDATE `system_menu` SET `new_id` = 5710 WHERE `id` = 5317;
UPDATE `system_menu` SET `new_id` = 5711 WHERE `id` = 5318;
UPDATE `system_menu` SET `new_id` = 5712 WHERE `id` = 5319;
UPDATE `system_menu` SET `new_id` = 5713 WHERE `id` = 5320;
UPDATE `system_menu` SET `new_id` = 5714 WHERE `id` = 5321;
UPDATE `system_menu` SET `new_id` = 5715 WHERE `id` = 5322;
UPDATE `system_menu` SET `new_id` = 5716 WHERE `id` = 5323;
UPDATE `system_menu` SET `new_id` = 5717 WHERE `id` = 5324;
UPDATE `system_menu` SET `new_id` = 5718 WHERE `id` = 5325;
UPDATE `system_menu` SET `new_id` = 5719 WHERE `id` = 5326;
UPDATE `system_menu` SET `new_id` = 5720 WHERE `id` = 5327;
UPDATE `system_menu` SET `new_id` = 5721 WHERE `id` = 5328;
UPDATE `system_menu` SET `new_id` = 5722 WHERE `id` = 5329;
UPDATE `system_menu` SET `new_id` = 5723 WHERE `id` = 5330;
UPDATE `system_menu` SET `new_id` = 5724 WHERE `id` = 5331;
UPDATE `system_menu` SET `new_id` = 5725 WHERE `id` = 5332;
UPDATE `system_menu` SET `new_id` = 5726 WHERE `id` = 5333;
UPDATE `system_menu` SET `new_id` = 5727 WHERE `id` = 5334;
UPDATE `system_menu` SET `new_id` = 5728 WHERE `id` = 5335;
UPDATE `system_menu` SET `new_id` = 5729 WHERE `id` = 5336;
UPDATE `system_menu` SET `new_id` = 5730 WHERE `id` = 5337;
UPDATE `system_menu` SET `new_id` = 5731 WHERE `id` = 5338;
UPDATE `system_menu` SET `new_id` = 5732 WHERE `id` = 5339;
UPDATE `system_menu` SET `new_id` = 5733 WHERE `id` = 5340;
UPDATE `system_menu` SET `new_id` = 5734 WHERE `id` = 5341;
UPDATE `system_menu` SET `new_id` = 5735 WHERE `id` = 5342;
UPDATE `system_menu` SET `new_id` = 5736 WHERE `id` = 5343;
UPDATE `system_menu` SET `new_id` = 5737 WHERE `id` = 5344;
UPDATE `system_menu` SET `new_id` = 5738 WHERE `id` = 5345;
UPDATE `system_menu` SET `new_id` = 5739 WHERE `id` = 5346;
UPDATE `system_menu` SET `new_id` = 5740 WHERE `id` = 5347;

--    把 new_id 复制到 id
UPDATE `system_menu` SET `id` = `new_id` WHERE `new_id` IS NOT NULL;

--    parent_id 更新：5307→5700, 5308→5701, etc.
UPDATE `system_menu` SET `parent_id` = 5700 WHERE `parent_id` = 5307;
UPDATE `system_menu` SET `parent_id` = 5701 WHERE `parent_id` = 5308;
UPDATE `system_menu` SET `parent_id` = 5702 WHERE `parent_id` = 5309;
UPDATE `system_menu` SET `parent_id` = 5703 WHERE `parent_id` = 5310;
UPDATE `system_menu` SET `parent_id` = 5704 WHERE `parent_id` = 5311;
UPDATE `system_menu` SET `parent_id` = 5705 WHERE `parent_id` = 5312;
UPDATE `system_menu` SET `parent_id` = 5706 WHERE `parent_id` = 5313;
UPDATE `system_menu` SET `parent_id` = 5707 WHERE `parent_id` = 5314;
UPDATE `system_menu` SET `parent_id` = 5708 WHERE `parent_id` = 5315;
UPDATE `system_menu` SET `parent_id` = 5709 WHERE `parent_id` = 5316;

--    删除临时列
ALTER TABLE `system_menu` DROP COLUMN `new_id`;

-- 3. 用新 ID 重新插入 system_role_menu 关联
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 2, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5700 AND 5740
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 2);

INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 109, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5700 AND 5740
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 109);

INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 111, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5700 AND 5740
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 111);

INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 155, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5700 AND 5740
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 155);

INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 160, id, '1', NOW(), '1', NOW(), 0, 1
FROM `system_menu`
WHERE id BETWEEN 5700 AND 5740
  AND id NOT IN (SELECT menu_id FROM `system_role_menu` WHERE role_id = 160);

SET FOREIGN_KEY_CHECKS = 1;

-- 回滚脚本：
-- SET FOREIGN_KEY_CHECKS = 0;
-- UPDATE system_menu SET parent_id = 5700 - 373 WHERE parent_id BETWEEN 5701 AND 5709;
-- UPDATE system_menu SET id = id - 373 WHERE id BETWEEN 5700 AND 5740;
-- DELETE FROM system_role_menu WHERE menu_id BETWEEN 5700 AND 5740;
-- INSERT INTO system_role_menu ... (原脚本)
-- SET FOREIGN_KEY_CHECKS = 1;
