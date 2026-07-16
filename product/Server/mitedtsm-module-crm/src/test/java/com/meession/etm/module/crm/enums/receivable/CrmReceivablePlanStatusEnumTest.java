package com.meession.etm.module.crm.enums.receivable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrmReceivablePlanStatusEnumTest {

    @Test
    public void testValues() {
        CrmReceivablePlanStatusEnum[] values = CrmReceivablePlanStatusEnum.values();
        assertEquals(3, values.length);
    }

    @Test
    public void testGetStatus() {
        assertEquals(Integer.valueOf(1), CrmReceivablePlanStatusEnum.COMPLETED.getStatus());
        assertEquals(Integer.valueOf(2), CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus());
        assertEquals(Integer.valueOf(3), CrmReceivablePlanStatusEnum.OVERDUE.getStatus());
    }

    @Test
    public void testGetName() {
        assertEquals("已完成", CrmReceivablePlanStatusEnum.COMPLETED.getName());
        assertEquals("未完成", CrmReceivablePlanStatusEnum.UNCOMPLETED.getName());
        assertEquals("已逾期", CrmReceivablePlanStatusEnum.OVERDUE.getName());
    }

    @Test
    public void testArray() {
        Integer[] arr = CrmReceivablePlanStatusEnum.ARRAYS;
        assertEquals(3, arr.length);
        assertEquals(Integer.valueOf(1), arr[0]);
        assertEquals(Integer.valueOf(2), arr[1]);
        assertEquals(Integer.valueOf(3), arr[2]);
    }
}
