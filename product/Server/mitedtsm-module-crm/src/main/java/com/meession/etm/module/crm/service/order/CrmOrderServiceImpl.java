package com.meession.etm.module.crm.service.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.number.MoneyUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.common.util.object.ObjectUtils;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderSaveReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderTransferReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderUpdateStatusReqVO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderDO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderItemDO;
import com.meession.etm.module.crm.dal.mysql.order.CrmOrderItemMapper;
import com.meession.etm.module.crm.dal.mysql.order.CrmOrderMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.order.CrmOrderStatusEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionTransferReqBO;
import com.meession.etm.module.crm.service.product.CrmProductService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.*;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.CRM_ORDER_FOLLOW_UP_SUB_TYPE;
import static com.meession.etm.module.crm.enums.LogRecordConstants.CRM_ORDER_FOLLOW_UP_SUCCESS;

/**
 * CRM 订单 Service 实现类
 *
 * @author 23计三倪雨晗
 */
@Service
@Validated
@Slf4j
public class CrmOrderServiceImpl implements CrmOrderService {

    /**
     * BPM 订单审批流程标识
     */
    public static final String BPM_PROCESS_DEFINITION_KEY = "crm-order-audit";

    @Resource
    private CrmOrderMapper orderMapper;
    @Resource
    private CrmOrderItemMapper orderItemMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;
    @Resource
    private CrmPermissionService crmPermissionService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private CrmProductService productService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_ORDER_TYPE, subType = CRM_ORDER_CREATE_SUB_TYPE, bizNo = "{{#order.no}}",
            success = CRM_ORDER_CREATE_SUCCESS)
    public Long createOrder(CrmOrderSaveReqVO createReqVO, Long userId) {
        List<CrmOrderItemDO> orderItems = validateOrderProducts(createReqVO.getProducts());
        validateRelationDataExists(createReqVO);

        String no = noRedisDAO.generate(CrmNoRedisDAO.ORDER_NO_PREFIX);
        if (orderMapper.selectByNo(no) != null) {
            throw exception(ORDER_NO_EXISTS);
        }

        CrmOrderDO order = BeanUtils.toBean(createReqVO, CrmOrderDO.class)
                .setNo(no).setStatus(CrmOrderStatusEnum.DRAFT.getStatus());
        calculateTotalPrice(order, orderItems);
        orderMapper.insert(order);

        if (CollUtil.isNotEmpty(orderItems)) {
            orderItems.forEach(item -> item.setOrderId(order.getId()));
            orderItemMapper.insertBatch(orderItems);
        }

        crmPermissionService.createPermission(new CrmPermissionCreateReqBO()
                .setUserId(order.getOwnerUserId())
                .setBizType(CrmBizTypeEnum.CRM_ORDER.getType())
                .setBizId(order.getId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        LogRecordContext.putVariable("order", order);
        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_ORDER_TYPE, subType = CRM_ORDER_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_ORDER_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_ORDER, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateOrder(CrmOrderSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "订单编号不能为空");
        updateReqVO.setOwnerUserId(null);

        CrmOrderDO oldOrder = validateOrderExists(updateReqVO.getId());
        if (!ObjectUtils.equalsAny(oldOrder.getStatus(), CrmOrderStatusEnum.DRAFT.getStatus(),
                CrmOrderStatusEnum.SUBMITTED.getStatus())) {
            throw exception(ORDER_UPDATE_FAIL);
        }
        List<CrmOrderItemDO> orderItems = validateOrderProducts(updateReqVO.getProducts());
        validateRelationDataExists(updateReqVO);

        CrmOrderDO updateObj = BeanUtils.toBean(updateReqVO, CrmOrderDO.class);
        calculateTotalPrice(updateObj, orderItems);
        orderMapper.updateById(updateObj);
        updateOrderItem(updateObj.getId(), orderItems);

        updateReqVO.setOwnerUserId(oldOrder.getOwnerUserId());
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldOrder, CrmOrderSaveReqVO.class));
        LogRecordContext.putVariable("orderName", oldOrder.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_ORDER_TYPE, subType = CRM_ORDER_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_ORDER_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_ORDER, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteOrder(Long id) {
        CrmOrderDO order = validateOrderExists(id);
        if (!ObjectUtils.equalsAny(order.getStatus(), CrmOrderStatusEnum.DRAFT.getStatus(),
                CrmOrderStatusEnum.REJECTED.getStatus(), CrmOrderStatusEnum.CANCELLED.getStatus())) {
            throw exception(ORDER_DELETE_FAIL);
        }
        orderMapper.deleteById(id);
        orderItemMapper.delete(new LambdaQueryWrapperX<CrmOrderItemDO>().eq(CrmOrderItemDO::getOrderId, id));
        crmPermissionService.deletePermission(CrmBizTypeEnum.CRM_ORDER.getType(), id);
        LogRecordContext.putVariable("orderName", order.getName());
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_ORDER, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmOrderDO getOrder(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public CrmOrderDO validateOrder(Long id) {
        return validateOrderExists(id);
    }

    @Override
    public List<CrmOrderDO> getOrderList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return orderMapper.selectByIds(ids);
    }

    @Override
    public PageResult<CrmOrderDO> getOrderPage(CrmOrderPageReqVO pageReqVO, Long userId) {
        return orderMapper.selectPage(pageReqVO, userId);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmOrderDO> getOrderPageByCustomerId(CrmOrderPageReqVO pageReqVO) {
        return orderMapper.selectPageByCustomerId(pageReqVO);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#pageReqVO.businessId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmOrderDO> getOrderPageByBusinessId(CrmOrderPageReqVO pageReqVO) {
        return orderMapper.selectPageByBusinessId(pageReqVO);
    }

    @Override
    public Long getAuditOrderCount(Long userId) {
        return orderMapper.selectCountByAudit(userId);
    }

    @Override
    public List<CrmOrderItemDO> getOrderProductListByOrderId(Long orderId) {
        return orderItemMapper.selectListByOrderId(orderId);
    }

    // ======================== 额外功能 ========================

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_ORDER_TYPE, subType = CRM_ORDER_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_ORDER_SUBMIT_SUCCESS)
    public void submitOrder(Long id, Long userId) {
        CrmOrderDO order = validateOrderExists(id);
        if (ObjUtil.notEqual(order.getStatus(), CrmOrderStatusEnum.DRAFT.getStatus())) {
            throw exception(ORDER_SUBMIT_FAIL_NOT_DRAFT);
        }

        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(BPM_PROCESS_DEFINITION_KEY)
                        .setBusinessKey(String.valueOf(id)));

        orderMapper.updateById(new CrmOrderDO().setId(id)
                .setProcessInstanceId(processInstanceId)
                .setStatus(CrmOrderStatusEnum.APPROVING.getStatus()));
        LogRecordContext.putVariable("orderName", order.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderAuditStatus(Long id, Integer bpmResult) {
        CrmOrderDO order = validateOrderExists(id);
        if (ObjUtil.notEqual(order.getStatus(), CrmOrderStatusEnum.APPROVING.getStatus())) {
            log.error("[updateOrderAuditStatus][order({}) 不处于审批中，无法更新审批结果({})]",
                    order.getId(), bpmResult);
            throw exception(ORDER_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        Integer newStatus = BpmTaskStatusEnum.APPROVE.getStatus().equals(bpmResult)
                ? CrmOrderStatusEnum.APPROVED.getStatus()
                : BpmTaskStatusEnum.REJECT.getStatus().equals(bpmResult)
                ? CrmOrderStatusEnum.REJECTED.getStatus()
                : BpmTaskStatusEnum.CANCEL.getStatus().equals(bpmResult)
                ? CrmOrderStatusEnum.CANCELLED.getStatus()
                : null;
        Assert.notNull(newStatus, "BPM 审批结果({}) 转换失败", bpmResult);

        orderMapper.updateById(new CrmOrderDO().setId(id).setStatus(newStatus));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_ORDER_TYPE, subType = CRM_ORDER_TRANSFER_SUB_TYPE, bizNo = "{{#reqVO.id}}",
            success = CRM_ORDER_TRANSFER_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_ORDER, bizId = "#reqVO.id", level = CrmPermissionLevelEnum.OWNER)
    public void transferOrder(CrmOrderTransferReqVO reqVO, Long userId) {
        CrmOrderDO order = validateOrderExists(reqVO.getId());
        if (ObjUtil.equal(reqVO.getNewOwnerUserId(), order.getOwnerUserId())) {
            throw exception(ORDER_TRANSFER_FAIL);
        }
        adminUserApi.validateUser(reqVO.getNewOwnerUserId());

        crmPermissionService.transferPermission(new CrmPermissionTransferReqBO(userId,
                CrmBizTypeEnum.CRM_ORDER.getType(), reqVO.getId(),
                reqVO.getNewOwnerUserId(), reqVO.getOldOwnerPermissionLevel()));
        orderMapper.updateById(new CrmOrderDO().setId(reqVO.getId())
                .setOwnerUserId(reqVO.getNewOwnerUserId()));

        LogRecordContext.putVariable("order", order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_ORDER_TYPE, subType = CRM_ORDER_UPDATE_STATUS_SUB_TYPE, bizNo = "{{#reqVO.id}}",
            success = CRM_ORDER_UPDATE_STATUS_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_ORDER, bizId = "#reqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateOrderStatus(CrmOrderUpdateStatusReqVO reqVO) {
        CrmOrderDO order = validateOrderExists(reqVO.getId());
        Integer oldStatus = order.getStatus();
        Integer newStatus = reqVO.getStatus();
        if (ObjUtil.equal(oldStatus, newStatus)) {
            throw exception(ORDER_UPDATE_STATUS_FAIL);
        }
        // 状态机校验：只允许特定转换
        if (!isValidStatusTransition(oldStatus, newStatus)) {
            throw exception(ORDER_UPDATE_STATUS_FAIL);
        }
        orderMapper.updateById(new CrmOrderDO().setId(reqVO.getId()).setStatus(newStatus));
        LogRecordContext.putVariable("orderName", order.getName());
        LogRecordContext.putVariable("newStatusName", CrmOrderStatusEnum.of(newStatus).getName());
    }

    private boolean isValidStatusTransition(Integer oldStatus, Integer newStatus) {
        if (oldStatus == null || newStatus == null) {
            return false;
        }
        // 草稿 → 审批中（通过 submitOrder），不能手动
        if (CrmOrderStatusEnum.isDraft(oldStatus)) {
            return CrmOrderStatusEnum.isApproved(newStatus) || CrmOrderStatusEnum.isRejected(newStatus)
                    || CrmOrderStatusEnum.isCancelled(newStatus);
        }
        // 审批通过 → 已完成
        if (CrmOrderStatusEnum.isApproved(oldStatus)) {
            return CrmOrderStatusEnum.isCompleted(newStatus) || CrmOrderStatusEnum.isCancelled(newStatus);
        }
        // 已拒绝 → 草稿/已取消
        if (CrmOrderStatusEnum.isRejected(oldStatus)) {
            return CrmOrderStatusEnum.isDraft(newStatus) || CrmOrderStatusEnum.isCancelled(newStatus);
        }
        // 已完成 → 审批通过（撤销完成）
        if (CrmOrderStatusEnum.isCompleted(oldStatus)) {
            return CrmOrderStatusEnum.isApproved(newStatus);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_ORDER_TYPE, subType = CRM_ORDER_FOLLOW_UP_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_ORDER_FOLLOW_UP_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_ORDER, bizId = "#id", level = CrmPermissionLevelEnum.WRITE)
    public void updateOrderFollowUp(Long id, LocalDateTime contactNextTime) {
        CrmOrderDO order = validateOrderExists(id);
        orderMapper.updateById(new CrmOrderDO().setId(id)
                .setContactLastTime(LocalDateTime.now())
                .setContactNextTime(contactNextTime));
        LogRecordContext.putVariable("orderName", order.getName());
    }

    @Override
    public Long getOrderCountByCustomerId(Long customerId) {
        return orderMapper.selectCount(CrmOrderDO::getCustomerId, customerId);
    }

    @Override
    public Long getOrderCountByBusinessId(Long businessId) {
        return orderMapper.selectCount(CrmOrderDO::getBusinessId, businessId);
    }

    // ======================= 私有方法 =======================

    private void validateRelationDataExists(CrmOrderSaveReqVO reqVO) {
        if (reqVO.getCustomerId() != null) {
            customerService.validateCustomer(reqVO.getCustomerId());
        }
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId());
        }
    }

    private List<CrmOrderItemDO> validateOrderProducts(List<CrmOrderSaveReqVO.Product> products) {
        if (CollUtil.isEmpty(products)) {
            return ListUtil.empty();
        }
        productService.validProductList(convertSet(products, CrmOrderSaveReqVO.Product::getProductId));
        return convertList(products, o -> BeanUtils.toBean(o, CrmOrderItemDO.class,
                item -> item.setTotalPrice(MoneyUtils.priceMultiply(item.getOrderPrice(), item.getCount()))));
    }

    private void calculateTotalPrice(CrmOrderDO order, List<CrmOrderItemDO> orderItems) {
        if (CollUtil.isEmpty(orderItems)) {
            order.setTotalProductPrice(BigDecimal.ZERO);
            order.setTotalPrice(BigDecimal.ZERO);
            return;
        }
        order.setTotalProductPrice(getSumValue(orderItems, CrmOrderItemDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO));
        BigDecimal discountPrice = MoneyUtils.priceMultiplyPercent(order.getTotalProductPrice(), order.getDiscountPercent());
        order.setTotalPrice(order.getTotalProductPrice().subtract(discountPrice));
    }

    private void updateOrderItem(Long orderId, List<CrmOrderItemDO> newList) {
        List<CrmOrderItemDO> oldList = orderItemMapper.selectListByOrderId(orderId);
        List<List<CrmOrderItemDO>> diffList = diffList(oldList, newList,
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setOrderId(orderId));
            orderItemMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            orderItemMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            orderItemMapper.deleteByIds(convertSet(diffList.get(2), CrmOrderItemDO::getId));
        }
    }

    private CrmOrderDO validateOrderExists(Long id) {
        CrmOrderDO order = orderMapper.selectById(id);
        if (order == null) {
            throw exception(ORDER_NOT_EXISTS);
        }
        return order;
    }

}
