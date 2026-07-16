package com.meession.etm.module.crm.enums.receivable;

import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CrmReceivablePlanStatusEnum implements ArrayValuable<Integer> {

    COMPLETED(1, "已完成"),
    UNCOMPLETED(2, "未完成"),
    OVERDUE(3, "已逾期");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmReceivablePlanStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
