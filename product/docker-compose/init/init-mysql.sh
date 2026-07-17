#!/bin/bash
# MySQL 初始化脚本
# 按指定顺序执行 database 目录下的所有 SQL 文件

set -e

echo "Starting MySQL initialization..."

# 等待 MySQL 完全启动
until mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" -e "SELECT 1"; do
    echo "Waiting for MySQL to be ready..."
    sleep 2
done

echo "MySQL is ready, executing SQL files..."

# MySQL 客户端编码设置（关键：避免中文乱码）
MYSQL_CMD="mysql -uroot -p${MYSQL_ROOT_PASSWORD} --default-character-set=utf8mb4"

# SQL 文件目录
SQL_BASE_DIR="/docker-init-sql/base"
SQL_NEW_DIR="/docker-init-sql/new"

# 1. 先执行 quartz.sql
echo "Executing: quartz.sql"
${MYSQL_CMD} "${MYSQL_DATABASE}" < "${SQL_BASE_DIR}/quartz.sql"

# 2. 再执行 ruoyi-vue-pro.sql
echo "Executing: ruoyi-vue-pro.sql"
${MYSQL_CMD} "${MYSQL_DATABASE}" < "${SQL_BASE_DIR}/ruoyi-vue-pro.sql"

# 3. 执行 base 目录下其余 SQL 文件（按字母顺序，排除已执行的）
for sql_file in "${SQL_BASE_DIR}"/*.sql; do
    filename=$(basename "$sql_file")
    if [[ "$filename" != "quartz.sql" && "$filename" != "ruoyi-vue-pro.sql" ]]; then
        echo "Executing: ${filename}"
        ${MYSQL_CMD} "${MYSQL_DATABASE}" < "$sql_file"
    fi
done

# 4. 执行 new 目录下的 SQL 文件（按依赖顺序）
if [ -d "$SQL_NEW_DIR" ]; then
    # 4.1 先执行 new-i18n.sql（创建基础表 system_menu_i18n）
    if [ -f "${SQL_NEW_DIR}/new-i18n.sql" ]; then
        echo "Executing: new-i18n.sql"
        ${MYSQL_CMD} "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-i18n.sql"
    fi

    # 4.2 再执行 new-mall-i18n.sql（创建 promotion_diy_menu_i18n 表，修改 system_tenant 表添加 currency_code）
    if [ -f "${SQL_NEW_DIR}/new-mall-i18n.sql" ]; then
        echo "Executing: new-mall-i18n.sql"
        ${MYSQL_CMD} "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-mall-i18n.sql"
    fi

    # 4.3 执行 new-i18n-ar.sql（插入多语言数据，依赖 system_menu_i18n 表存在）
    if [ -f "${SQL_NEW_DIR}/new-i18n-ar.sql" ]; then
        echo "Executing: new-i18n-ar.sql"
        ${MYSQL_CMD} "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-i18n-ar.sql"
    fi

    # 4.4 执行 new-product-category-i18n.sql
    if [ -f "${SQL_NEW_DIR}/new-product-category-i18n.sql" ]; then
        echo "Executing: new-product-category-i18n.sql"
        ${MYSQL_CMD} "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-product-category-i18n.sql"
    fi

    # 4.5 执行 new-large-file-upload.sql
    if [ -f "${SQL_NEW_DIR}/new-large-file-upload.sql" ]; then
        echo "Executing: new-large-file-upload.sql"
        ${MYSQL_CMD} "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-large-file-upload.sql"
    fi

    # 4.6 执行 new 子目录下的 SQL 文件（如 crm/、erp/ 等模块增量脚本）
    for subdir in "${SQL_NEW_DIR}"/*/; do
        if [ -d "$subdir" ]; then
            subdir_name=$(basename "$subdir")
            echo "Processing new/${subdir_name}/ ..."
            for sql_file in "${subdir}"*.sql; do
                if [ -f "$sql_file" ]; then
                    sql_name=$(basename "$sql_file")
                    echo "  Executing: ${subdir_name}/${sql_name}"
                    ${MYSQL_CMD} "${MYSQL_DATABASE}" < "$sql_file"
                fi
            done
        fi
    done
fi

echo "MySQL initialization completed successfully!"