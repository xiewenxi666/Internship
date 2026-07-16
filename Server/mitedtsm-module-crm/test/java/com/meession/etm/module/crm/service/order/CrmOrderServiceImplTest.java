package com.meession.etm.module.crm.service.order;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderSaveReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderTransferReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderUpdateStatusReqVO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderDO;
import com.meession.etm.module.crm.dal.mysql.order.CrmOrderMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.order.CrmOrderStatusEnum;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.product.CrmProductService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
  * {@link CrmOrderServiceImpl} 的单元测试
 *
 * @author 23计三倪雨晗
 */
@Import(CrmOrderServiceImpl.class)
public class CrmOrderServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CrmOrderServiceImpl orderService;

    @Resource
    private CrmOrderMapper orderMapper;

    @MockitoBean
    private CrmNoRedisDAO noRedisDAO;

    @MockitoBean
    private CrmPermissionService crmPermissionService;

    @MockitoBean
    private CrmCustomerService customerService;

    @MockitoBean
    private AdminUserApi adminUserApi;

    @MockitoBean
    private CrmProductService productService;

    @MockitoBean
    private com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi bpmProcessInstanceApi;

    // ==================== createOrder ====================

    @Test
    public void testCreateOrder_success() {
        // 准备 mock: 编号生成
        when(noRedisDAO.generate(eq(CrmNoRedisDAO.ORDER_NO_PREFIX))).thenReturn("DD20260714000001");
        // 准备参数
        CrmOrderSaveReqVO reqVO = randomPojo(CrmOrderSaveReqVO.class, o -> {
            o.setId(null);
            o.setOwnerUserId(1L);
            o.setCustomerId(1L);
            o.setOrderDate(LocalDateTime.now());
            o.setDiscountPercent(BigDecimal.ZERO);
            o.setProducts(null);
        });

        // 调用
        Long orderId = orderService.createOrder(reqVO, 1L);

        // 断言
        assertNotNull(orderId);
        CrmOrderDO order = orderMapper.selectById(orderId);
        assertNotNull(order);
        assertEquals("DD20260714000001", order.getNo());
        assertEquals(CrmOrderStatusEnum.DRAFT.getStatus(), order.getStatus());
        assertEquals(reqVO.getName(), order.getName());
        assertEquals(reqVO.getCustomerId(), order.getCustomerId());
        assertEquals(reqVO.getOwnerUserId(), order.getOwnerUserId());
    }

    @Test
    public void testCreateOrder_noDuplicate() {
        // mock 编号生成
        when(noRedisDAO.generate(eq(CrmNoRedisDAO.ORDER_NO_PREFIX)))
                .thenReturn("DD20260714000001")
                .thenReturn("DD20260714000002");

        CrmOrderSaveReqVO reqVO1 = randomPojo(CrmOrderSaveReqVO.class, o -> {
            o.setId(null);
            o.setOwnerUserId(1L);
            o.setCustomerId(1L);
            o.setDiscountPercent(BigDecimal.ZERO);
            o.setProducts(null);
        });
        CrmOrderSaveReqVO reqVO2 = randomPojo(CrmOrderSaveReqVO.class, o -> {
            o.setId(null);
            o.setOwnerUserId(1L);
            o.setCustomerId(1L);
            o.setDiscountPercent(BigDecimal.ZERO);
            o.setProducts(null);
        });

        Long id1 = orderService.createOrder(reqVO1, 1L);
        Long id2 = orderService.createOrder(reqVO2, 1L);

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
        assertEquals("DD20260714000001", orderMapper.selectById(id1).getNo());
        assertEquals("DD20260714000002", orderMapper.selectById(id2).getNo());
    }

    // ==================== getOrder ====================

    @Test
    public void testGetOrder_success() {
        // 准备数据
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> {
            o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus());
            o.setTotalProductPrice(null);
            o.setDiscountPercent(null);
            o.setTotalPrice(null);
        });
        orderMapper.insert(order);

        // 调用
        CrmOrderDO result = orderService.getOrder(order.getId());

        // 断言（手动比较避免 H2 decimal 精度问题）
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getNo(), result.getNo());
        assertEquals(order.getName(), result.getName());
        assertEquals(order.getCustomerId(), result.getCustomerId());
        assertEquals(order.getStatus(), result.getStatus());
    }

    @Test
    public void testGetOrder_notExists() {
        // 调用 & 断言
        CrmOrderDO result = orderService.getOrder(99999L);
        assertNull(result);
    }

    // ==================== deleteOrder ====================

    @Test
    public void testDeleteOrder_success() {
        // 准备数据
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        orderMapper.insert(order);

        // 调用
        orderService.deleteOrder(order.getId());

        // 断言
        assertNull(orderMapper.selectById(order.getId()));
    }

    @Test
    public void testDeleteOrder_notExists() {
        // 调用 & 断言异常
        assertServiceException(
                () -> orderService.deleteOrder(99999L),
                ORDER_NOT_EXISTS
        );
    }

    // ==================== updateOrder ====================

    @Test
    public void testUpdateOrder_success() {
        // 准备数据: 先插入一个草稿订单
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        orderMapper.insert(order);
        // 准备更新参数
        CrmOrderSaveReqVO updateReqVO = randomPojo(CrmOrderSaveReqVO.class, o -> {
            o.setId(order.getId());
            o.setName("更新后的订单名称");
            o.setOwnerUserId(null); // 不允许更新负责人
            o.setProducts(null);
        });

        // 调用
        orderService.updateOrder(updateReqVO);

        // 断言
        CrmOrderDO updated = orderMapper.selectById(order.getId());
        assertEquals("更新后的订单名称", updated.getName());
        // 其余字段应保持不变
        assertEquals(order.getOwnerUserId(), updated.getOwnerUserId());
    }

    @Test
    public void testUpdateOrder_fail_notDraft() {
        // 准备数据: 插入一个已完成订单（不可编辑）
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.COMPLETED.getStatus()));
        orderMapper.insert(order);
        CrmOrderSaveReqVO updateReqVO = randomPojo(CrmOrderSaveReqVO.class, o -> {
            o.setId(order.getId());
            o.setProducts(null);
        });

        // 调用 & 断言异常
        assertServiceException(
                () -> orderService.updateOrder(updateReqVO),
                ORDER_UPDATE_FAIL
        );
    }

    @Test
    public void testUpdateOrder_fail_notExists() {
        CrmOrderSaveReqVO updateReqVO = randomPojo(CrmOrderSaveReqVO.class, o -> {
            o.setId(99999L);
            o.setProducts(null);
        });

        assertServiceException(
                () -> orderService.updateOrder(updateReqVO),
                ORDER_NOT_EXISTS
        );
    }

    // ==================== getOrderPage ====================

    @Test
    public void testGetOrderPage_success() {
        // 准备数据: 插入多个订单
        CrmOrderDO order1 = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        CrmOrderDO order2 = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.APPROVED.getStatus()));
        orderMapper.insert(order1);
        orderMapper.insert(order2);

        CrmOrderPageReqVO pageReqVO = new CrmOrderPageReqVO();
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(10);

        // 调用
        PageResult<CrmOrderDO> pageResult = orderService.getOrderPage(pageReqVO, 1L);

        // 断言
        assertNotNull(pageResult);
        assertTrue(pageResult.getList().size() >= 2);
    }

    // ==================== validateOrder ====================

    @Test
    public void testValidateOrder_success() {
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        orderMapper.insert(order);

        CrmOrderDO result = orderService.validateOrder(order.getId());
        assertNotNull(result);
    }

    @Test
    public void testValidateOrder_notExists() {
        assertServiceException(
                () -> orderService.validateOrder(99999L),
                ORDER_NOT_EXISTS
        );
    }

    // ==================== deleteOrder - status check ====================

    @Test
    public void testDeleteOrder_fail_notDraft() {
        // 准备数据：已完成状态不可删除
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.APPROVED.getStatus()));
        orderMapper.insert(order);

        assertServiceException(
                () -> orderService.deleteOrder(order.getId()),
                ORDER_DELETE_FAIL
        );
    }

    // ==================== transferOrder ====================

    @Test
    public void testTransferOrder_fail_sameOwner() {
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setOwnerUserId(1L).setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        orderMapper.insert(order);
        CrmOrderTransferReqVO reqVO = new CrmOrderTransferReqVO();
        reqVO.setId(order.getId());
        reqVO.setNewOwnerUserId(1L); // 相同负责人

        assertServiceException(
                () -> orderService.transferOrder(reqVO, 1L),
                ORDER_TRANSFER_FAIL
        );
    }

    // ==================== updateOrderStatus ====================

    @Test
    public void testUpdateOrderStatus_success_draftToApproved() {
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        orderMapper.insert(order);
        CrmOrderUpdateStatusReqVO reqVO = new CrmOrderUpdateStatusReqVO();
        reqVO.setId(order.getId());
        reqVO.setStatus(CrmOrderStatusEnum.APPROVED.getStatus());

        orderService.updateOrderStatus(reqVO);

        CrmOrderDO updated = orderMapper.selectById(order.getId());
        assertEquals(CrmOrderStatusEnum.APPROVED.getStatus(), updated.getStatus());
    }

    @Test
    public void testUpdateOrderStatus_fail_sameStatus() {
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        orderMapper.insert(order);
        CrmOrderUpdateStatusReqVO reqVO = new CrmOrderUpdateStatusReqVO();
        reqVO.setId(order.getId());
        reqVO.setStatus(CrmOrderStatusEnum.DRAFT.getStatus());

        assertServiceException(
                () -> orderService.updateOrderStatus(reqVO),
                ORDER_UPDATE_STATUS_FAIL
        );
    }

    @Test
    public void testUpdateOrderStatus_fail_invalidTransition() {
        // DRAFT 不能直接到 COMPLETED（跳过中间状态）
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.DRAFT.getStatus()));
        orderMapper.insert(order);
        CrmOrderUpdateStatusReqVO reqVO = new CrmOrderUpdateStatusReqVO();
        reqVO.setId(order.getId());
        reqVO.setStatus(CrmOrderStatusEnum.COMPLETED.getStatus());

        assertServiceException(
                () -> orderService.updateOrderStatus(reqVO),
                ORDER_UPDATE_STATUS_FAIL
        );
    }

    // ==================== updateOrderFollowUp ====================

    @Test
    public void testUpdateOrderFollowUp_success() {
        CrmOrderDO order = randomPojo(CrmOrderDO.class, o -> o.setStatus(CrmOrderStatusEnum.APPROVED.getStatus()));
        orderMapper.insert(order);

        LocalDateTime nextTime = LocalDateTime.now().plusDays(7);
        orderService.updateOrderFollowUp(order.getId(), nextTime);

        CrmOrderDO updated = orderMapper.selectById(order.getId());
        assertNotNull(updated.getContactLastTime());
        assertNotNull(updated.getContactNextTime());
    }

    @Test
    public void testUpdateOrderFollowUp_notExists() {
        assertServiceException(
                () -> orderService.updateOrderFollowUp(99999L, LocalDateTime.now()),
                ORDER_NOT_EXISTS
        );
    }

}
