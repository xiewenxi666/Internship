// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.enums.workorder;

import cn.hutool.core.util.ArrayUtil;
import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * CRM 工单状态枚举
 * 状态流转: 发起 -> 处理中 -> 完结
 *                  \-> 退回（可重回处理中）
 */
@Getter
@AllArgsConstructor
public enum CrmWorkOrderStatusEnum implements ArrayValuable<Integer> {

    INITIATED(1, "发起"),
    PROCESSING(2, "处理中"),
    COMPLETED(3, "完结"),
    RETURNED(4, "退回");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmWorkOrderStatusEnum::getStatus).toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static CrmWorkOrderStatusEnum valueOf(Integer status) {
        return ArrayUtil.firstMatch(o -> o.getStatus().equals(status), values());
    }

    /**
     * 判断是否可以进行处理操作
     */
    public static boolean canProcess(Integer status) {
        return INITIATED.getStatus().equals(status);
    }

    /**
     * 判断是否可以完结
     */
    public static boolean canComplete(Integer status) {
        return PROCESSING.getStatus().equals(status);
    }

    /**
     * 判断是否可以退回
     */
    public static boolean canReturn(Integer status) {
        return INITIATED.getStatus().equals(status) || PROCESSING.getStatus().equals(status);
    }
}
