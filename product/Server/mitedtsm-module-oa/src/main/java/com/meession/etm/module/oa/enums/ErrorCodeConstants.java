package com.meession.etm.module.oa.enums;

import com.meession.etm.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode OA_LEAVE_NOT_EXISTS = new ErrorCode(1_021_001_001, "请假申请不存在");
    ErrorCode OA_BUSINESS_TRIP_NOT_EXISTS = new ErrorCode(1_021_001_002, "出差申请不存在");
    ErrorCode OA_LOAN_NOT_EXISTS = new ErrorCode(1_021_001_003, "借款申请不存在");
    ErrorCode OA_VISIT_NOT_EXISTS = new ErrorCode(1_021_001_004, "拜访记录不存在");
    ErrorCode OA_WORK_REPORT_NOT_EXISTS = new ErrorCode(1_021_001_005, "工作报告不存在");
    ErrorCode OA_SCHEDULE_NOT_EXISTS = new ErrorCode(1_021_001_006, "日程不存在");
    ErrorCode OA_REQUEST_NOT_EXISTS = new ErrorCode(1_021_001_007, "请示申请不存在");
    ErrorCode OA_DOCUMENT_DIR_NOT_EXISTS = new ErrorCode(1_021_001_008, "文档目录不存在");
    ErrorCode OA_DOCUMENT_DIR_NAME_DUPLICATE = new ErrorCode(1_021_001_009, "同目录下已存在同名文件");
    ErrorCode OA_TASK_NOT_EXISTS = new ErrorCode(1_021_001_010, "任务不存在");
    ErrorCode OA_MESSAGE_NOT_EXISTS = new ErrorCode(1_021_002_001, "内部消息不存在");

    ErrorCode OA_LEAVE_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_021_003_001, "请假申请不是草稿状态，无法提交审批");
    ErrorCode OA_BUSINESS_TRIP_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_021_003_002, "出差申请不是草稿状态，无法提交审批");
    ErrorCode OA_LOAN_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_021_003_003, "借款申请不是草稿状态，无法提交审批");
    ErrorCode OA_REQUEST_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_021_003_004, "请示申请不是草稿状态，无法提交审批");
    ErrorCode OA_REQUEST_CANCEL_FAIL_NOT_RUNNING = new ErrorCode(1_021_003_005, "请示申请不是审批中状态，无法取消");
    ErrorCode OA_REQUEST_RECONSIDER_FAIL_NOT_REJECTED = new ErrorCode(1_021_003_006, "请示申请不是审批不通过状态，无法重新提交");
    ErrorCode OA_VISIT_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_021_003_007, "拜访记录不是草稿状态，无法提交审批");

}