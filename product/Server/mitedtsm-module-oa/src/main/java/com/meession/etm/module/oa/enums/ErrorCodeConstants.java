package com.meession.etm.module.oa.enums;

import com.meession.etm.framework.common.exception.ErrorCode;

/**
 * OA 错误码枚举类
 *
 * oa 系统，使用 1-010-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== OA 流程模块 1-010-001-000 ==========
    ErrorCode OA_LEAVE_NOT_EXISTS = new ErrorCode(1_010_001_001, "请假申请不存在");
    ErrorCode OA_TRIP_NOT_EXISTS = new ErrorCode(1_010_001_002, "出差申请不存在");
    ErrorCode OA_LOAN_NOT_EXISTS = new ErrorCode(1_010_001_003, "借款申请不存在");
    ErrorCode OA_VISIT_NOT_EXISTS = new ErrorCode(1_010_001_004, "拜访申请不存在");
    ErrorCode OA_REQUEST_NOT_EXISTS = new ErrorCode(1_010_001_005, "请示申请不存在");
    ErrorCode OA_REPORT_NOT_EXISTS = new ErrorCode(1_010_001_006, "工作报告不存在");
    ErrorCode OA_SCHEDULE_NOT_EXISTS = new ErrorCode(1_010_001_007, "日程不存在");
    ErrorCode OA_TASK_NOT_EXISTS = new ErrorCode(1_010_001_008, "任务不存在");
    ErrorCode OA_DOCUMENT_NOT_EXISTS = new ErrorCode(1_010_001_009, "文档不存在");

}
