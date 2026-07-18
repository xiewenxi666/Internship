// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.enums.workorder;

import cn.hutool.core.util.ArrayUtil;
import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * CRM 工单优先级枚举
 */
@Getter
@AllArgsConstructor
public enum CrmWorkOrderPriorityEnum implements ArrayValuable<Integer> {

    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    URGENT(4, "紧急");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmWorkOrderPriorityEnum::getPriority).toArray(Integer[]::new);

    private final Integer priority;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static CrmWorkOrderPriorityEnum valueOf(Integer priority) {
        return ArrayUtil.firstMatch(o -> o.getPriority().equals(priority), values());
    }
}
