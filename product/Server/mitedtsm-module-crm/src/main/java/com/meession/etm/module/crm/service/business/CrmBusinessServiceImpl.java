package com.meession.etm.module.crm.service.business;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.number.MoneyUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.business.vo.business.CrmBusinessPageReqVO;
import com.meession.etm.module.crm.controller.admin.business.vo.business.CrmBusinessSaveReqVO;
import com.meession.etm.module.crm.controller.admin.business.vo.business.CrmBusinessTransferReqVO;
import com.meession.etm.module.crm.controller.admin.business.vo.business.CrmBusinessUpdateStatusReqVO;
import com.meession.etm.module.crm.controller.admin.contact.vo.CrmContactBusinessReqVO;
import com.meession.etm.module.crm.controller.admin.statistics.vo.funnel.CrmStatisticsFunnelReqVO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessDO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessProductDO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessStatusDO;
import com.meession.etm.module.crm.dal.dataobject.contact.CrmContactBusinessDO;
import com.meession.etm.module.crm.dal.mysql.business.CrmBusinessMapper;
import com.meession.etm.module.crm.dal.mysql.business.CrmBusinessProductMapper;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.contact.CrmContactBusinessService;
import com.meession.etm.module.crm.service.contact.CrmContactService;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionTransferReqBO;
import com.meession.etm.module.crm.service.product.CrmProductService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.meession.etm.module.crm.enums.business.CrmBusinessEndStatusEnum;
import com.meession.etm.module.crm.event.CrmBusinessWonEvent;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.*;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;

/**
 * 商机 Service 实现类
 *
 * @author ljlleo
 */
@Service
@Validated
public class CrmBusinessServiceImpl implements CrmBusinessService {

    @Resource
    private CrmBusinessMapper businessMapper;
    @Resource
    private CrmBusinessProductMapper businessProductMapper;

    @Resource
    private CrmBusinessStatusService businessStatusService;
    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private CrmContractService contractService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private CrmContactService contactService;
    @Resource
    private CrmPermissionService permissionService;
    @Resource
    private CrmContactBusinessService contactBusinessService;
    @Resource
    private CrmProductService productService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 创建商机
     *
     * @param createReqVO 创建请求
     * @param userId 用户编号
     * @return 商机编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_CREATE_SUB_TYPE, bizNo = "{{#business.id}}",
            success = CRM_BUSINESS_CREATE_SUCCESS)
    public Long createBusiness(CrmBusinessSaveReqVO createReqVO, Long userId) {
        // 1.1 校验产品项的有效性
        List<CrmBusinessProductDO> businessProducts = validateBusinessProducts(createReqVO.getProducts());
        // 1.2 校验关联字段
        validateRelationDataExists(createReqVO);

        // 2.1 插入商机
        CrmBusinessDO business = BeanUtils.toBean(createReqVO, CrmBusinessDO.class);
        business.setStatusId(businessStatusService.getBusinessStatusListByTypeId(createReqVO.getStatusTypeId()).get(0).getId()); // 默认状态
        calculateTotalPrice(business, businessProducts);
        businessMapper.insert(business);
        // 2.2 插入商机关联商品
        if (CollUtil.isNotEmpty(businessProducts)) {
            businessProducts.forEach(item -> item.setBusinessId(business.getId()));
            businessProductMapper.insertBatch(businessProducts);
        }

        // 3. 创建数据权限
        permissionService.createPermission(new CrmPermissionCreateReqBO().setUserId(business.getOwnerUserId())
                .setBizType(CrmBizTypeEnum.CRM_BUSINESS.getType()).setBizId(business.getId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 4. 在联系人的详情页，如果直接【新建商机】，则需要关联下
        if (createReqVO.getContactId() != null) {
            contactBusinessService.createContactBusinessList(new CrmContactBusinessReqVO().setContactId(createReqVO.getContactId())
                    .setBusinessIds(Collections.singletonList(business.getId())));
        }

        // 5. 记录操作日志上下文
        LogRecordContext.putVariable("business", business);
        return business.getId();
    }

    /**
     * 更新商机
     *
     * @param updateReqVO 更新请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_BUSINESS_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateBusiness(CrmBusinessSaveReqVO updateReqVO) {
        updateReqVO.setOwnerUserId(null).setStatusTypeId(null); // 不允许更新的字段
        // 1.1 校验存在
        CrmBusinessDO oldBusiness = validateBusinessExists(updateReqVO.getId());
        // 1.2 校验产品项的有效性
        List<CrmBusinessProductDO> businessProducts = validateBusinessProducts(updateReqVO.getProducts());
        // 1.3 校验关联字段
        validateRelationDataExists(updateReqVO);

        // 2.1 更新商机
        CrmBusinessDO updateObj = BeanUtils.toBean(updateReqVO, CrmBusinessDO.class);
        calculateTotalPrice(updateObj, businessProducts);
        businessMapper.updateById(updateObj);
        // 2.2 更新商机关联商品
        updateBusinessProduct(updateObj.getId(), businessProducts);

        // 3. 记录操作日志上下文
        updateReqVO.setOwnerUserId(oldBusiness.getOwnerUserId()); // 避免操作日志出现“删除负责人”的情况
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldBusiness, CrmBusinessSaveReqVO.class));
        LogRecordContext.putVariable("businessName", oldBusiness.getName());
    }

    /**
     * 更新商机跟进信息
     *
     * @param id 商机编号
     * @param contactNextTime 下次联系时间
     * @param contactLastContent 最后跟进内容
     */
    @Override
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_FOLLOW_UP_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_BUSINESS_FOLLOW_UP_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#id", level = CrmPermissionLevelEnum.WRITE)
    public void updateBusinessFollowUp(Long id, LocalDateTime contactNextTime, String contactLastContent) {
        // 1. 校验存在
        CrmBusinessDO business = validateBusinessExists(id);

        // 2. 更新联系人的跟进信息
        businessMapper.updateById(new CrmBusinessDO().setId(id).setFollowUpStatus(true).setContactNextTime(contactNextTime)
                .setContactLastTime(LocalDateTime.now()));

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("businessName", business.getName());
    }

    /**
     * 批量更新商机的下次联系时间
     *
     * @param ids 商机编号集合
     * @param contactNextTime 下次联系时间
     */
    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#ids", level = CrmPermissionLevelEnum.WRITE)
    public void updateBusinessContactNextTime(Collection<Long> ids, LocalDateTime contactNextTime) {
        businessMapper.updateBatch(convertList(ids, id -> new CrmBusinessDO().setId(id).setContactNextTime(contactNextTime)));
    }

    /**
     * 差量更新商机关联商品
     *
     * @param id 商机编号
     * @param newList 新商品列表
     */
    private void updateBusinessProduct(Long id, List<CrmBusinessProductDO> newList) {
        List<CrmBusinessProductDO> oldList = businessProductMapper.selectListByBusinessId(id);
        List<List<CrmBusinessProductDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setBusinessId(id));
            businessProductMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            businessProductMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            businessProductMapper.deleteByIds(convertSet(diffList.get(2), CrmBusinessProductDO::getId));
        }
    }

    /**
     * 校验关联数据是否存在
     *
     * @param saveReqVO 请求
     */
    private void validateRelationDataExists(CrmBusinessSaveReqVO saveReqVO) {
        // 校验商机状态
        if (saveReqVO.getStatusTypeId() != null) {
            businessStatusService.validateBusinessStatusType(saveReqVO.getStatusTypeId());
        }
        // 校验客户
        if (saveReqVO.getCustomerId() != null) {
            customerService.validateCustomer(saveReqVO.getCustomerId());
        }
        // 校验联系人
        if (saveReqVO.getContactId() != null) {
            contactService.validateContact(saveReqVO.getContactId());
        }
        // 校验负责人
        if (saveReqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(saveReqVO.getOwnerUserId());
        }
    }

    /**
     * 校验商机商品列表有效性，并转换为 DO 列表
     *
     * @param list 商品列表
     * @return 商机商品 DO 列表
     */
    private List<CrmBusinessProductDO> validateBusinessProducts(List<CrmBusinessSaveReqVO.BusinessProduct> list) {
        // 1. 校验产品存在
        productService.validProductList(convertSet(list, CrmBusinessSaveReqVO.BusinessProduct::getProductId));
        // 2. 转化为 CrmBusinessProductDO 列表
        return convertList(list, o -> BeanUtils.toBean(o, CrmBusinessProductDO.class,
                item -> item.setTotalPrice(MoneyUtils.priceMultiply(item.getBusinessPrice(), item.getCount()))));
    }

    /**
     * 计算商机总价（含折扣）
     *
     * @param business 商机
     * @param businessProducts 商机商品列表
     */
    private void calculateTotalPrice(CrmBusinessDO business, List<CrmBusinessProductDO> businessProducts) {
        business.setTotalProductPrice(getSumValue(businessProducts, CrmBusinessProductDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO));
        BigDecimal discountPrice = MoneyUtils.priceMultiplyPercent(business.getTotalProductPrice(), business.getDiscountPercent());
        business.setTotalPrice(business.getTotalProductPrice().subtract(discountPrice));
    }

    /**
     * 更新商机状态
     *
     * @param reqVO 状态更新请求
     */
    @Override
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_UPDATE_STATUS_SUB_TYPE, bizNo = "{{#reqVO.id}}",
            success = CRM_BUSINESS_UPDATE_STATUS_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#reqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateBusinessStatus(CrmBusinessUpdateStatusReqVO reqVO) {
        // 1.1 校验存在
        CrmBusinessDO business = validateBusinessExists(reqVO.getId());
        // 1.2 校验商机未结束
        if (business.getEndStatus() != null) {
            throw exception(BUSINESS_UPDATE_STATUS_FAIL_END_STATUS);
        }
        // 1.3 校验商机状态
        CrmBusinessStatusDO status = null;
        if (reqVO.getStatusId() != null) {
            status = businessStatusService.validateBusinessStatus(business.getStatusTypeId(), reqVO.getStatusId());
        }
        // 1.4 校验是不是状态没变更
        if ((reqVO.getStatusId() != null && reqVO.getStatusId().equals(business.getStatusId()))
                || (reqVO.getEndStatus() != null && reqVO.getEndStatus().equals(business.getEndStatus()))) {
            throw exception(BUSINESS_UPDATE_STATUS_FAIL_STATUS_EQUALS);
        }

        // 2. 更新商机状态
        businessMapper.updateById(new CrmBusinessDO().setId(reqVO.getId()).setStatusId(reqVO.getStatusId())
                .setEndStatus(reqVO.getEndStatus()));

        // 2.1 商机成交时发布事件，自动创建订单
        if (CrmBusinessEndStatusEnum.WIN.getStatus().equals(reqVO.getEndStatus())) {
            eventPublisher.publishEvent(new CrmBusinessWonEvent(this, reqVO.getId()));
        }

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("businessName", business.getName());
        LogRecordContext.putVariable("oldStatusName", getBusinessStatusName(business.getEndStatus(),
                businessStatusService.getBusinessStatus(business.getStatusId())));
        LogRecordContext.putVariable("newStatusName", getBusinessStatusName(reqVO.getEndStatus(), status));
    }

    /**
     * 删除商机
     *
     * @param id 商机编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_BUSINESS_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteBusiness(Long id) {
        // 1.1 校验存在
        CrmBusinessDO business = validateBusinessExists(id);
        // 1.2 校验是否关联合同
        validateContractExists(id);

        // 删除商机
        businessMapper.deleteById(id);
        // 删除数据权限
        permissionService.deletePermission(CrmBizTypeEnum.CRM_BUSINESS.getType(), id);

        // 记录操作日志上下文
        LogRecordContext.putVariable("businessName", business.getName());
    }

    /**
     * 删除校验合同是关联合同
     *
     * @param businessId 商机id
     * @author lzxhqs
     */
    private void validateContractExists(Long businessId) {
        if (contractService.getContractCountByBusinessId(businessId) > 0) {
            throw exception(BUSINESS_DELETE_FAIL_CONTRACT_EXISTS);
        }
    }

    /**
     * 校验商机是否存在
     *
     * @param id 商机编号
     * @return 商机
     */
    private CrmBusinessDO validateBusinessExists(Long id) {
        CrmBusinessDO crmBusiness = businessMapper.selectById(id);
        if (crmBusiness == null) {
            throw exception(BUSINESS_NOT_EXISTS);
        }
        return crmBusiness;
    }


    /**
     * 转移商机负责人
     *
     * @param reqVO 转移请求
     * @param userId 当前用户编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_TRANSFER_SUB_TYPE, bizNo = "{{#reqVO.id}}",
            success = CRM_BUSINESS_TRANSFER_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#reqVO.id", level = CrmPermissionLevelEnum.OWNER)
    public void transferBusiness(CrmBusinessTransferReqVO reqVO, Long userId) {
        // 1 校验商机是否存在
        CrmBusinessDO business = validateBusinessExists(reqVO.getId());

        // 2.1 数据权限转移
        permissionService.transferPermission(new CrmPermissionTransferReqBO(userId, CrmBizTypeEnum.CRM_BUSINESS.getType(),
                reqVO.getId(), reqVO.getNewOwnerUserId(), reqVO.getOldOwnerPermissionLevel()));
        // 2.2 设置新的负责人
        businessMapper.updateOwnerUserIdById(reqVO.getId(), reqVO.getNewOwnerUserId());

        // 记录操作日志上下文
        LogRecordContext.putVariable("business", business);
    }

    //======================= 查询相关 =======================

    /**
     * 查询商机详情
     *
     * @param id 商机编号
     * @return 商机
     */
    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmBusinessDO getBusiness(Long id) {
        return businessMapper.selectById(id);
    }

    /**
     * 校验商机是否存在
     *
     * @param id 商机编号
     * @return 商机
     */
    @Override
    public CrmBusinessDO validateBusiness(Long id) {
        return validateBusinessExists(id);
    }

    /**
     * 查询商机列表
     *
     * @param ids 商机编号集合
     * @return 商机列表
     */
    @Override
    public List<CrmBusinessDO> getBusinessList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return businessMapper.selectByIds(ids);
    }

    /**
     * 根据商机编号查询关联商品列表
     *
     * @param businessId 商机编号
     * @return 商品列表
     */
    @Override
    public List<CrmBusinessProductDO> getBusinessProductListByBusinessId(Long businessId) {
        return businessProductMapper.selectListByBusinessId(businessId);
    }

    /**
     * 分页查询商机
     *
     * @param pageReqVO 分页请求
     * @param userId 用户编号
     * @return 分页结果
     */
    @Override
    public PageResult<CrmBusinessDO> getBusinessPage(CrmBusinessPageReqVO pageReqVO, Long userId) {
        return businessMapper.selectPage(pageReqVO, userId);
    }

    /**
     * 根据客户编号分页查询商机
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmBusinessDO> getBusinessPageByCustomerId(CrmBusinessPageReqVO pageReqVO) {
        return businessMapper.selectPageByCustomerId(pageReqVO);
    }

    /**
     * 根据联系人编号分页查询商机
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTACT, bizId = "#pageReqVO.contactId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmBusinessDO> getBusinessPageByContact(CrmBusinessPageReqVO pageReqVO) {
        // 1. 查询关联的商机编号
        List<CrmContactBusinessDO> contactBusinessList = contactBusinessService.getContactBusinessListByContactId(
                pageReqVO.getContactId());
        if (CollUtil.isEmpty(contactBusinessList)) {
            return PageResult.empty();
        }
        // 2. 查询商机分页
        return businessMapper.selectPageByContactId(pageReqVO,
                convertSet(contactBusinessList, CrmContactBusinessDO::getBusinessId));
    }

    /**
     * 根据客户编号统计商机数量
     *
     * @param customerId 客户编号
     * @return 商机数量
     */
    @Override
    public Long getBusinessCountByCustomerId(Long customerId) {
        return businessMapper.selectCount(CrmBusinessDO::getCustomerId, customerId);
    }

    /**
     * 根据状态类型编号统计商机数量
     *
     * @param statusTypeId 状态类型编号
     * @return 商机数量
     */
    @Override
    public Long getBusinessCountByStatusTypeId(Long statusTypeId) {
        return businessMapper.selectCountByStatusTypeId(statusTypeId);
    }

    /**
     * 根据客户编号和负责人编号查询商机列表
     *
     * @param customerId 客户编号
     * @param ownerUserId 负责人编号
     * @return 商机列表
     */
    @Override
    public List<CrmBusinessDO> getBusinessListByCustomerIdOwnerUserId(Long customerId, Long ownerUserId) {
        return businessMapper.selectListByCustomerIdOwnerUserId(customerId, ownerUserId);
    }

    /**
     * 根据日期范围分页查询商机（漏斗统计用）
     *
     * @param pageVO 统计请求
     * @return 分页结果
     */
    @Override
    public PageResult<CrmBusinessDO> getBusinessPageByDate(CrmStatisticsFunnelReqVO pageVO) {
        return businessMapper.selectPage(pageVO);
    }

}
