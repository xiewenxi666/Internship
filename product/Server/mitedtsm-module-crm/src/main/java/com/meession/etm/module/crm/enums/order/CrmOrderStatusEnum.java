package com.meession.etm.module.crm.enums.order;

import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 订单状态枚举
 *
 * 七种状态流转：
 * DRAFT → SUBMITTED → APPROVING → APPROVED → PROCESSING → COMPLETED
 *                ↓                      ↓
 *           DRAFT(回)               REJECTED → DRAFT(回)
 *                                              ↓
 *                                         CANCELLED
 *
 * @author 23计三倪雨晗
 */
@RequiredArgsConstructor
@Getter
public enum CrmOrderStatusEnum implements ArrayValuable<Integer> {

    DRAFT(0, "未提交"),
    SUBMITTED(10, "已提交"),
    APPROVING(15, "审批中"),
    APPROVED(20, "已通过"),
    REJECTED(30, "已拒绝"),
    PROCESSING(40, "处理中"),
    COMPLETED(60, "已完成"),
    CANCELLED(70, "已取消");

    private final Integer status;
    private final String name;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmOrderStatusEnum::getStatus).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static CrmOrderStatusEnum of(Integer status) {
        for (CrmOrderStatusEnum e : values()) {
            if (e.status.equals(status)) {
                return e;
            }
        }
        return null;
    }

    public static boolean isDraft(Integer status) {
        return DRAFT.status.equals(status);
    }

    public static boolean isSubmitted(Integer status) {
        return SUBMITTED.status.equals(status);
    }

    public static boolean isApproving(Integer status) {
        return APPROVING.status.equals(status);
    }

    public static boolean isApproved(Integer status) {
        return APPROVED.status.equals(status);
    }

    public static boolean isRejected(Integer status) {
        return REJECTED.status.equals(status);
    }

    public static boolean isProcessing(Integer status) {
        return PROCESSING.status.equals(status);
    }

    public static boolean isCompleted(Integer status) {
        return COMPLETED.status.equals(status);
    }

    public static boolean isCancelled(Integer status) {
        return CANCELLED.status.equals(status);
    }

}
