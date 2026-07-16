// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.enums.workorder;

import cn.hutool.core.util.ArrayUtil;
import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * CRM 工单类型枚举
 */
@Getter
@AllArgsConstructor
public enum CrmWorkOrderTypeEnum implements ArrayValuable<Integer> {

    REPAIR(1, "报修"),
    MAINTENANCE(2, "保养"),
    INSPECTION(3, "巡检"),
    OTHER(4, "其他");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmWorkOrderTypeEnum::getType).toArray(Integer[]::new);

    private final Integer type;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static CrmWorkOrderTypeEnum valueOf(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }
}
