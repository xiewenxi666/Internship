#!/bin/sh
# TDengine 初始化脚本 — 创建 mitedtsm_database
sleep 10  # 等待 TDengine 完全启动
taos -s "CREATE DATABASE IF NOT EXISTS mitedtsm_database KEEP 3650 DURATION 10 BUFFER 256;"
echo "TDengine database initialized successfully"
