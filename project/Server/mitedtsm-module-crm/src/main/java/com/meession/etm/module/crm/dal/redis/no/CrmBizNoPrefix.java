// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.dal.redis.no;

/**
 * CRM 业务编号前缀常量
 *
 * @author 密讯
 */
public interface CrmBizNoPrefix {

    /**
     * 合同 {@link com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO}
     */
    String CONTRACT = "HT";

    /**
     * 回款 {@link com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO}
     */
    String RECEIVABLE = "HK";

    /**
     * 回款计划
     */
    String RECEIVABLE_PLAN = "HKJH";

    /**
     * 工单 {@link com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO}
     */
    String WORK_ORDER = "GD";

    /**
     * 发票
     */
    String INVOICE = "FP";

    /**
     * 报销
     */
    String REIMBURSEMENT = "BX";

    /**
     * 退款
     */
    String REFUND = "TK";

    /**
     * 营销活动
     */
    String CAMPAIGN = "HD";

    /**
     * 客户关怀
     */
    String CUSTOMER_CARE = "GH";

    /**
     * 拜访
     */
    String VISIT = "BF";

    /**
     * 任务
     */
    String TASK = "RW";

    /**
     * 报告
     */
    String REPORT = "BG";

}
