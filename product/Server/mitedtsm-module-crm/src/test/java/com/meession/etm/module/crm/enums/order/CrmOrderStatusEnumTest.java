package com.meession.etm.module.crm.enums.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CrmOrderStatusEnum} 的单元测试
 *
 * @author 23计三倪雨晗
 */
public class CrmOrderStatusEnumTest {

    @Test
    public void testValues() {
        assertEquals(7, CrmOrderStatusEnum.values().length);
    }

    @Test
    public void testDraft() {
        CrmOrderStatusEnum status = CrmOrderStatusEnum.DRAFT;
        assertEquals(0, status.getStatus().intValue());
        assertEquals("未提交", status.getName());
    }

    @Test
    public void testSubmitted() {
        CrmOrderStatusEnum status = CrmOrderStatusEnum.SUBMITTED;
        assertEquals(10, status.getStatus().intValue());
        assertEquals("已提交", status.getName());
    }

    @Test
    public void testApproving() {
        CrmOrderStatusEnum status = CrmOrderStatusEnum.APPROVING;
        assertEquals(15, status.getStatus().intValue());
        assertEquals("审批中", status.getName());
    }

    @Test
    public void testApproved() {
        CrmOrderStatusEnum status = CrmOrderStatusEnum.APPROVED;
        assertEquals(20, status.getStatus().intValue());
        assertEquals("已通过", status.getName());
    }

    @Test
    public void testRejected() {
        CrmOrderStatusEnum status = CrmOrderStatusEnum.REJECTED;
        assertEquals(30, status.getStatus().intValue());
        assertEquals("已拒绝", status.getName());
    }

    @Test
    public void testCompleted() {
        CrmOrderStatusEnum status = CrmOrderStatusEnum.COMPLETED;
        assertEquals(60, status.getStatus().intValue());
        assertEquals("已完成", status.getName());
    }

    @Test
    public void testCancelled() {
        CrmOrderStatusEnum status = CrmOrderStatusEnum.CANCELLED;
        assertEquals(70, status.getStatus().intValue());
        assertEquals("已取消", status.getName());
    }

    @Test
    public void testArray() {
        Integer[] array = CrmOrderStatusEnum.ARRAYS;
        assertNotNull(array);
        assertEquals(7, array.length);
        assertTrue(array[0] == 0);
        assertTrue(array[6] == 70);
    }

    @Test
    public void testArrayMethod() {
        Integer[] array = CrmOrderStatusEnum.DRAFT.array();
        assertNotNull(array);
        assertEquals(7, array.length);
    }

    @Test
    public void testIsDraft() {
        assertTrue(CrmOrderStatusEnum.isDraft(CrmOrderStatusEnum.DRAFT.getStatus()));
        assertFalse(CrmOrderStatusEnum.isDraft(CrmOrderStatusEnum.APPROVED.getStatus()));
    }

    @Test
    public void testIsApproved() {
        assertTrue(CrmOrderStatusEnum.isApproved(CrmOrderStatusEnum.APPROVED.getStatus()));
        assertFalse(CrmOrderStatusEnum.isApproved(CrmOrderStatusEnum.DRAFT.getStatus()));
    }

    @Test
    public void testIsRejected() {
        assertTrue(CrmOrderStatusEnum.isRejected(CrmOrderStatusEnum.REJECTED.getStatus()));
        assertFalse(CrmOrderStatusEnum.isRejected(CrmOrderStatusEnum.COMPLETED.getStatus()));
    }

    @Test
    public void testIsCancelled() {
        assertTrue(CrmOrderStatusEnum.isCancelled(CrmOrderStatusEnum.CANCELLED.getStatus()));
        assertFalse(CrmOrderStatusEnum.isCancelled(CrmOrderStatusEnum.APPROVING.getStatus()));
    }

    @Test
    public void testIsCompleted() {
        assertTrue(CrmOrderStatusEnum.isCompleted(CrmOrderStatusEnum.COMPLETED.getStatus()));
        assertFalse(CrmOrderStatusEnum.isCompleted(CrmOrderStatusEnum.DRAFT.getStatus()));
    }

    @Test
    public void testIsSubmitted() {
        assertTrue(CrmOrderStatusEnum.isSubmitted(CrmOrderStatusEnum.SUBMITTED.getStatus()));
        assertFalse(CrmOrderStatusEnum.isSubmitted(CrmOrderStatusEnum.REJECTED.getStatus()));
    }

    @Test
    public void testIsApproving() {
        assertTrue(CrmOrderStatusEnum.isApproving(CrmOrderStatusEnum.APPROVING.getStatus()));
        assertFalse(CrmOrderStatusEnum.isApproving(CrmOrderStatusEnum.DRAFT.getStatus()));
    }

}
