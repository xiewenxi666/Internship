package com.meession.etm.module.crm.enums;

/**
 * CRM 操作日志枚举
 * 目的：统一管理，也减少 Service 里各种“复杂”字符串
 *
 * @author HUIHUI
 */
public interface LogRecordConstants {

    // ======================= CRM_CLUE 线索 =======================

    String CRM_CLUE_TYPE = "CRM 线索";
    String CRM_CLUE_CREATE_SUB_TYPE = "创建线索";
    String CRM_CLUE_CREATE_SUCCESS = "创建了线索{{#clue.name}}";
    String CRM_CLUE_UPDATE_SUB_TYPE = "更新线索";
    String CRM_CLUE_UPDATE_SUCCESS = "更新了线索【{{#clueName}}】: {_DIFF{#updateReqVO}}";
    String CRM_CLUE_DELETE_SUB_TYPE = "删除线索";
    String CRM_CLUE_DELETE_SUCCESS = "删除了线索【{{#clueName}}】";
    String CRM_CLUE_TRANSFER_SUB_TYPE = "转移线索";
    String CRM_CLUE_TRANSFER_SUCCESS = "将线索【{{#clue.name}}】的负责人从【{getAdminUserById{#clue.ownerUserId}}】变更为了【{getAdminUserById{#reqVO.newOwnerUserId}}】";
    String CRM_CLUE_TRANSLATE_SUB_TYPE = "线索转化为客户";
    String CRM_CLUE_TRANSLATE_SUCCESS = "将线索【{{#clueName}}】转化为客户";
    String CRM_CLUE_FOLLOW_UP_SUB_TYPE = "线索跟进";
    String CRM_CLUE_FOLLOW_UP_SUCCESS = "线索跟进【{{#clueName}}】";

    // ======================= CRM_CUSTOMER 客户 =======================

    String CRM_CUSTOMER_TYPE = "CRM 客户";
    String CRM_CUSTOMER_CREATE_SUB_TYPE = "创建客户";
    String CRM_CUSTOMER_CREATE_SUCCESS = "创建了客户{{#customer.name}}";
    String CRM_CUSTOMER_UPDATE_SUB_TYPE = "更新客户";
    String CRM_CUSTOMER_UPDATE_SUCCESS = "更新了客户【{{#customerName}}】: {_DIFF{#updateReqVO}}";
    String CRM_CUSTOMER_DELETE_SUB_TYPE = "删除客户";
    String CRM_CUSTOMER_DELETE_SUCCESS = "删除了客户【{{#customerName}}】";
    String CRM_CUSTOMER_TRANSFER_SUB_TYPE = "转移客户";
    String CRM_CUSTOMER_TRANSFER_SUCCESS = "将客户【{{#customer.name}}】的负责人从【{getAdminUserById{#customer.ownerUserId}}】变更为了【{getAdminUserById{#reqVO.newOwnerUserId}}】";
    String CRM_CUSTOMER_LOCK_SUB_TYPE = "{{#customer.lockStatus ? '解锁客户' : '锁定客户'}}";
    String CRM_CUSTOMER_LOCK_SUCCESS = "{{#customer.lockStatus ? '将客户【' + #customer.name + '】解锁' : '将客户【' + #customer.name + '】锁定'}}";
    String CRM_CUSTOMER_POOL_SUB_TYPE = "客户放入公海";
    String CRM_CUSTOMER_POOL_SUCCESS = "将客户【{{#customerName}}】放入了公海";
    String CRM_CUSTOMER_RECEIVE_SUB_TYPE = "{{#ownerUserName != null ? '分配客户' : '领取客户'}}";
    String CRM_CUSTOMER_RECEIVE_SUCCESS = "{{#ownerUserName != null ? '将客户【' + #customer.name + '】分配给【' + #ownerUserName + '】' : '领取客户【' + #customer.name + '】'}}";
    String CRM_CUSTOMER_IMPORT_SUB_TYPE = "{{#isUpdate ? '导入并更新客户' : '导入客户'}}";
    String CRM_CUSTOMER_IMPORT_SUCCESS = "{{#isUpdate ? '导入并更新了客户【'+ #customer.name +'】' : '导入了客户【'+ #customer.name +'】'}}";
    String CRM_CUSTOMER_UPDATE_DEAL_STATUS_SUB_TYPE = "更新客户成交状态";
    String CRM_CUSTOMER_UPDATE_DEAL_STATUS_SUCCESS = "更新了客户【{{#customerName}}】的成交状态为【{{#dealStatus ? '已成交' : '未成交'}}】";
    String CRM_CUSTOMER_FOLLOW_UP_SUB_TYPE = "客户跟进";
    String CRM_CUSTOMER_FOLLOW_UP_SUCCESS = "客户跟进【{{#customerName}}】";

    // ======================= CRM_CUSTOMER_LIMIT_CONFIG 客户限制配置 =======================

    String CRM_CUSTOMER_LIMIT_CONFIG_TYPE = "CRM 客户限制配置";
    String CRM_CUSTOMER_LIMIT_CONFIG_CREATE_SUB_TYPE = "创建客户限制配置";
    String CRM_CUSTOMER_LIMIT_CONFIG_CREATE_SUCCESS = "创建了【{{#limitType}}】类型的客户限制配置";
    String CRM_CUSTOMER_LIMIT_CONFIG_UPDATE_SUB_TYPE = "更新客户限制配置";
    String CRM_CUSTOMER_LIMIT_CONFIG_UPDATE_SUCCESS = "更新了客户限制配置: {_DIFF{#updateReqVO}}";
    String CRM_CUSTOMER_LIMIT_CONFIG_DELETE_SUB_TYPE = "删除客户限制配置";
    String CRM_CUSTOMER_LIMIT_CONFIG_DELETE_SUCCESS = "删除了【{{#limitType}}】类型的客户限制配置";

    // ======================= CRM_CUSTOMER_POOL_CONFIG 客户公海规则 =======================

    String CRM_CUSTOMER_POOL_CONFIG_TYPE = "CRM 客户公海规则";
    String CRM_CUSTOMER_POOL_CONFIG_SUB_TYPE = "{{#isPoolConfigUpdate ? '更新客户公海规则' : '创建客户公海规则'}}";
    String CRM_CUSTOMER_POOL_CONFIG_SUCCESS = "{{#isPoolConfigUpdate ? '更新了客户公海规则' : '创建了客户公海规则'}}";

    // ======================= CRM_CONTACT 联系人 =======================

    String CRM_CONTACT_TYPE = "CRM 联系人";
    String CRM_CONTACT_CREATE_SUB_TYPE = "创建联系人";
    String CRM_CONTACT_CREATE_SUCCESS = "创建了联系人{{#contact.name}}";
    String CRM_CONTACT_UPDATE_SUB_TYPE = "更新联系人";
    String CRM_CONTACT_UPDATE_SUCCESS = "更新了联系人【{{#contactName}}】: {_DIFF{#updateReqVO}}";
    String CRM_CONTACT_DELETE_SUB_TYPE = "删除联系人";
    String CRM_CONTACT_DELETE_SUCCESS = "删除了联系人【{{#contactName}}】";
    String CRM_CONTACT_TRANSFER_SUB_TYPE = "转移联系人";
    String CRM_CONTACT_TRANSFER_SUCCESS = "将联系人【{{#contact.name}}】的负责人从【{getAdminUserById{#contact.ownerUserId}}】变更为了【{getAdminUserById{#reqVO.newOwnerUserId}}】";
    String CRM_CONTACT_FOLLOW_UP_SUB_TYPE = "联系人跟进";
    String CRM_CONTACT_FOLLOW_UP_SUCCESS = "联系人跟进【{{#contactName}}】";
    String CRM_CONTACT_UPDATE_OWNER_USER_SUB_TYPE = "更新联系人负责人";
    String CRM_CONTACT_UPDATE_OWNER_USER_SUCCESS = "将联系人【{{#contact.name}}】的负责人从【{getAdminUserById{#contact.ownerUserId}}】变更为了【{getAdminUserById{#ownerUserId}}】";

    // ======================= CRM_BUSINESS 商机 =======================

    String CRM_BUSINESS_TYPE = "CRM 商机";
    String CRM_BUSINESS_CREATE_SUB_TYPE = "创建商机";
    String CRM_BUSINESS_CREATE_SUCCESS = "创建了商机{{#business.name}}";
    String CRM_BUSINESS_UPDATE_SUB_TYPE = "更新商机";
    String CRM_BUSINESS_UPDATE_SUCCESS = "更新了商机【{{#businessName}}】: {_DIFF{#updateReqVO}}";
    String CRM_BUSINESS_DELETE_SUB_TYPE = "删除商机";
    String CRM_BUSINESS_DELETE_SUCCESS = "删除了商机【{{#businessName}}】";
    String CRM_BUSINESS_TRANSFER_SUB_TYPE = "转移商机";
    String CRM_BUSINESS_TRANSFER_SUCCESS = "将商机【{{#business.name}}】的负责人从【{getAdminUserById{#business.ownerUserId}}】变更为了【{getAdminUserById{#reqVO.newOwnerUserId}}】";
    String CRM_BUSINESS_FOLLOW_UP_SUB_TYPE = "商机跟进";
    String CRM_BUSINESS_FOLLOW_UP_SUCCESS = "商机跟进【{{#businessName}}】";
    String CRM_BUSINESS_UPDATE_STATUS_SUB_TYPE = "更新商机状态";
    String CRM_BUSINESS_UPDATE_STATUS_SUCCESS = "更新了商机【{{#businessName}}】的状态从【{{#oldStatusName}}】变更为了【{{#newStatusName}}】";

    // ======================= CRM_CONTRACT_CONFIG 合同配置 =======================

    String CRM_CONTRACT_CONFIG_TYPE = "CRM 合同配置";
    String CRM_CONTRACT_CONFIG_SUB_TYPE = "{{#isPoolConfigUpdate ? '更新合同配置' : '创建合同配置'}}";
    String CRM_CONTRACT_CONFIG_SUCCESS = "{{#isPoolConfigUpdate ? '更新了合同配置' : '创建了合同配置'}}";

    // ======================= CRM_CONTRACT 合同 =======================

    String CRM_CONTRACT_TYPE = "CRM 合同";
    String CRM_CONTRACT_CREATE_SUB_TYPE = "创建合同";
    String CRM_CONTRACT_CREATE_SUCCESS = "创建了合同{{#contract.name}}";
    String CRM_CONTRACT_UPDATE_SUB_TYPE = "更新合同";
    String CRM_CONTRACT_UPDATE_SUCCESS = "更新了合同【{{#contractName}}】: {_DIFF{#updateReqVO}}";
    String CRM_CONTRACT_DELETE_SUB_TYPE = "删除合同";
    String CRM_CONTRACT_DELETE_SUCCESS = "删除了合同【{{#contractName}}】";
    String CRM_CONTRACT_TRANSFER_SUB_TYPE = "转移合同";
    String CRM_CONTRACT_TRANSFER_SUCCESS = "将合同【{{#contract.name}}】的负责人从【{getAdminUserById{#contract.ownerUserId}}】变更为了【{getAdminUserById{#reqVO.newOwnerUserId}}】";
    String CRM_CONTRACT_SUBMIT_SUB_TYPE = "提交合同审批";
    String CRM_CONTRACT_SUBMIT_SUCCESS = "提交合同【{{#contractName}}】审批成功";
    String CRM_CONTRACT_FOLLOW_UP_SUB_TYPE = "合同跟进";
    String CRM_CONTRACT_FOLLOW_UP_SUCCESS = "合同跟进【{{#contractName}}】";

    // ======================= CRM_PRODUCT 产品 =======================

    String CRM_PRODUCT_TYPE = "CRM 产品";
    String CRM_PRODUCT_CREATE_SUB_TYPE = "创建产品";
    String CRM_PRODUCT_CREATE_SUCCESS = "创建了产品【{{#createReqVO.name}}】";
    String CRM_PRODUCT_UPDATE_SUB_TYPE = "更新产品";
    String CRM_PRODUCT_UPDATE_SUCCESS = "更新了产品【{{#updateReqVO.name}}】: {_DIFF{#updateReqVO}}";
    String CRM_PRODUCT_DELETE_SUB_TYPE = "删除产品";
    String CRM_PRODUCT_DELETE_SUCCESS = "删除了产品【{{#product.name}}】";

    // ======================= CRM_PRODUCT_CATEGORY 产品分类 =======================

    String CRM_PRODUCT_CATEGORY_TYPE = "CRM 产品分类";
    String CRM_PRODUCT_CATEGORY_CREATE_SUB_TYPE = "创建产品分类";
    String CRM_PRODUCT_CATEGORY_CREATE_SUCCESS = "创建了产品分类【{{#createReqVO.name}}】";
    String CRM_PRODUCT_CATEGORY_UPDATE_SUB_TYPE = "更新产品分类";
    String CRM_PRODUCT_CATEGORY_UPDATE_SUCCESS = "更新了产品分类【{{#updateReqVO.name}}】: {_DIFF{#updateReqVO}}";
    String CRM_PRODUCT_CATEGORY_DELETE_SUB_TYPE = "删除产品分类";
    String CRM_PRODUCT_CATEGORY_DELETE_SUCCESS = "删除了产品分类【{{#productCategory.name}}】";

    // ======================= CRM_RECEIVABLE 回款 =======================

    String CRM_RECEIVABLE_TYPE = "CRM 回款";
    String CRM_RECEIVABLE_CREATE_SUB_TYPE = "创建回款";
    String CRM_RECEIVABLE_CREATE_SUCCESS = "创建了合同【{getContractById{#receivable.contractId}}】的{{#period != null ? '【第'+ #period +'期】' : '编号为【'+ #receivable.no +'】的'}}回款";
    String CRM_RECEIVABLE_UPDATE_SUB_TYPE = "更新回款";
    String CRM_RECEIVABLE_UPDATE_SUCCESS = "更新了合同【{getContractById{#receivable.contractId}}】的{{#period != null ? '【第'+ #period +'期】' : '编号为【'+ #receivable.no +'】的'}}回款: {_DIFF{#updateReqVO}}";
    String CRM_RECEIVABLE_DELETE_SUB_TYPE = "删除回款";
    String CRM_RECEIVABLE_DELETE_SUCCESS = "删除了合同【{getContractById{#receivable.contractId}}】的{{#period != null ? '【第'+ #period +'期】' : '编号为【'+ #receivable.no +'】的'}}回款";
    String CRM_RECEIVABLE_SUBMIT_SUB_TYPE = "提交回款审批";
    String CRM_RECEIVABLE_SUBMIT_SUCCESS = "提交编号为【{{#receivableNo}}】的回款审批成功";

    // ======================= CRM_CAMPAIGN 营销活动 =======================

    String CRM_CAMPAIGN_TYPE = "CRM 营销活动";
    String CRM_CAMPAIGN_CREATE_SUB_TYPE = "创建营销活动";
    String CRM_CAMPAIGN_CREATE_SUCCESS = "创建了营销活动{{#campaign.title}}";
    String CRM_CAMPAIGN_UPDATE_SUB_TYPE = "更新营销活动";
    String CRM_CAMPAIGN_UPDATE_SUCCESS = "更新了营销活动【{{#campaignName}}】: {_DIFF{#updateReqVO}}";
    String CRM_CAMPAIGN_DELETE_SUB_TYPE = "删除营销活动";
    String CRM_CAMPAIGN_DELETE_SUCCESS = "删除了营销活动【{{#campaignName}}】";

    // ======================= CRM_BULK_SEND 群发管理 =======================

    String CRM_BULK_SEND_TYPE = "CRM 群发管理";
    String CRM_BULK_SEND_CREATE_SUB_TYPE = "创建群发";
    String CRM_BULK_SEND_CREATE_SUCCESS = "创建了群发{{#bulkSend.title}}";
    String CRM_BULK_SEND_UPDATE_SUB_TYPE = "更新群发";
    String CRM_BULK_SEND_UPDATE_SUCCESS = "更新了群发【{{#bulkSendName}}】: {_DIFF{#updateReqVO}}";
    String CRM_BULK_SEND_DELETE_SUB_TYPE = "删除群发";
    String CRM_BULK_SEND_DELETE_SUCCESS = "删除了群发【{{#bulkSendName}}】";
    String CRM_BULK_SEND_SUBMIT_SUB_TYPE = "提交群发审核";
    String CRM_BULK_SEND_SUBMIT_SUCCESS = "提交群发【{{#bulkSendName}}】审核成功";

    // ======================= CRM_RECEIVABLE_PLAN 回款计划 =======================

    String CRM_RECEIVABLE_PLAN_TYPE = "CRM 回款计划";
    String CRM_RECEIVABLE_PLAN_CREATE_SUB_TYPE = "创建回款计划";
    String CRM_RECEIVABLE_PLAN_CREATE_SUCCESS = "创建了合同【{getContractById{#receivablePlan.contractId}}】的第【{{#receivablePlan.period}}】期回款计划";
    String CRM_RECEIVABLE_PLAN_UPDATE_SUB_TYPE = "更新回款计划";
    String CRM_RECEIVABLE_PLAN_UPDATE_SUCCESS = "更新了合同【{getContractById{#receivablePlan.contractId}}】的第【{{#receivablePlan.period}}】期回款计划: {_DIFF{#updateReqVO}}";
    String CRM_RECEIVABLE_PLAN_DELETE_SUB_TYPE = "删除回款计划";
    String CRM_RECEIVABLE_PLAN_DELETE_SUCCESS = "删除了合同【{getContractById{#receivablePlan.contractId}}】的第【{{#receivablePlan.period}}】期回款计划";

    // ========== 工单管理 ==========
    interface WORK_ORDER {
        String type = "CRM-工单管理";
        String CREATE_SUB_TYPE = "创建工单";
        String CREATE_SUCCESS = "创建了工单【{{#title}}】";
        String UPDATE_SUB_TYPE = "修改工单";
        String UPDATE_SUCCESS = "修改了工单【{{#title}}】";
        String DELETE_SUB_TYPE = "删除工单";
        String DELETE_SUCCESS = "删除了工单【{{#title}}】";
        String PROCESS_SUB_TYPE = "处理工单";
        String PROCESS_SUCCESS = "开始处理工单【{{#title}}】";
        String COMPLETE_SUB_TYPE = "完结工单";
        String COMPLETE_SUCCESS = "完结了工单【{{#title}}】，解决方案：{{#solution}}";
        String RETURN_SUB_TYPE = "退回工单";
        String RETURN_SUCCESS = "退回了工单【{{#title}}】";
    }

}
