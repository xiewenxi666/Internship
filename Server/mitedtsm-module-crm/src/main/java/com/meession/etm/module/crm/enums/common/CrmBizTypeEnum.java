package com.meession.etm.module.crm.enums.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 业务类型枚举
 *
 * @author HUIHUI
 */
@RequiredArgsConstructor
@Getter
public enum CrmBizTypeEnum implements ArrayValuable<Integer> {

    CRM_CLUE(1, "线索"),
    CRM_CUSTOMER(2, "客户"),
    CRM_CONTACT(3, "联系人"),
    CRM_BUSINESS(4, "商机"),
    CRM_CONTRACT(5, "合同"),
    CRM_PRODUCT(6, "产品"),
    CRM_RECEIVABLE(7, "回款"),
    CRM_RECEIVABLE_PLAN(8, "回款计划"),
    CRM_WORK_ORDER(9, "工单"),
    CRM_VISIT(10, "拜访"),
    CRM_INVOICE(11, "发票"),
    CRM_REIMBURSEMENT(12, "报销"),
    CRM_REFUND(13, "退款"),
    CRM_CAMPAIGN(14, "营销活动"),
    CRM_TASK(15, "任务"),
    CRM_ORDER(16, "订单")
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmBizTypeEnum::getType).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名称
     */
    private final String name;

    public static String getNameByType(Integer type) {
        CrmBizTypeEnum typeEnum = CollUtil.findOne(CollUtil.newArrayList(CrmBizTypeEnum.values()),
                item -> ObjUtil.equal(item.type, type));
        return typeEnum == null ? null : typeEnum.getName();
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
