package com.meession.etm.module.crm.enums.common;

import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 的审批状态
 *
 * @author 赤焰
 */
@RequiredArgsConstructor
@Getter
public enum CrmAuditStatusEnum implements ArrayValuable<Integer> {

    DRAFT(0, "未提交"),
    PROCESS(10, "待审批"),
    APPROVE(20, "已通过"),
    REJECT(30, "被驳回"),
    CANCEL(40, "已撤销"),
    VETO(50, "被否决");

    private final Integer status;
    private final String name;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmAuditStatusEnum::getStatus).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
