package com.meession.etm.module.crm.dal.redis.no;

/**
 * CRM 各业务模块编号前缀常量
 * <p>
 * 各域引用此常量，调用 {@link CrmNoRedisDAO#generate(String)} 生成编号
 * <p>
 * 编号格式：{PREFIX} + yyyyMMdd + 6位自增
 * 例如：HT20260714000001
 *
 * @author 密讯
 */
public interface CrmBizNoPrefix {

    /** 合同 */
    String CONTRACT = "HT";
    /** 回款 */
    String RECEIVABLE = "HK";
    /** 工单 */
    String WORK_ORDER = "GD";
    /** 发票 */
    String INVOICE = "FP";
    /** 报销 */
    String REIMBURSEMENT = "BX";
    /** 退款 */
    String REFUND = "TK";
    /** 营销活动 */
    String CAMPAIGN = "HD";
    /** 客户关怀 */
    String CUSTOMER_CARE = "GH";
    /** 拜访 */
    String VISIT = "BF";
    /** 任务 */
    String TASK = "RW";
    /** 工作报告 */
    String REPORT = "BG";
    /** 费用 */
    String EXPENSE = "FY";
}
