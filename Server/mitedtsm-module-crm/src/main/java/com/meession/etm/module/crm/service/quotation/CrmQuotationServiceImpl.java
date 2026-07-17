package com.meession.etm.module.crm.service.quotation;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationPageReqVO;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.quotation.CrmQuotationDO;
import com.meession.etm.module.crm.dal.dataobject.quotation.CrmQuotationProductDO;
import com.meession.etm.module.crm.dal.mysql.quotation.CrmQuotationMapper;
import com.meession.etm.module.crm.dal.mysql.quotation.CrmQuotationProductMapper;
import com.meession.etm.module.crm.dal.dataobject.product.CrmProductDO;
import com.meession.etm.module.crm.dal.redis.no.CrmBizNoPrefix;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.crm.service.product.CrmProductService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;

/**
 * CRM 报价单 Service 实现类
 *
 * @author engineer
 */
@Service
@Validated
public class CrmQuotationServiceImpl implements CrmQuotationService {

    @Resource
    private CrmQuotationMapper quotationMapper;

    @Resource
    private CrmQuotationProductMapper quotationProductMapper;

    @Resource
    private CrmProductService productService;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmPermissionService crmPermissionService;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_QUOTATION_TYPE, subType = CRM_QUOTATION_CREATE_SUB_TYPE, bizNo = "{{#quotation.id}}",
            success = CRM_QUOTATION_CREATE_SUCCESS)
    public Long createQuotation(CrmQuotationSaveReqVO createReqVO) {
        // 1. 校验用户
        adminUserApi.validateUserList(Collections.singleton(createReqVO.getOwnerUserId()));

        // 2. 生成报价单编号
        String quotationNo = noRedisDAO.generate(CrmBizNoPrefix.QUOTATION);

        // 3. 计算金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<CrmQuotationProductDO> productList = new ArrayList<>();
        if (CollUtil.isNotEmpty(createReqVO.getProducts())) {
            List<Long> productIds = createReqVO.getProducts().stream()
                    .map(CrmQuotationSaveReqVO.ProductItem::getProductId)
                    .toList();
            Map<Long, CrmProductDO> productMap = productService.getProductMap(productIds);

            for (CrmQuotationSaveReqVO.ProductItem item : createReqVO.getProducts()) {
                CrmProductDO product = productMap.get(item.getProductId());
                if (product == null) {
                    throw exception(PRODUCT_NOT_EXISTS);
                }
                BigDecimal itemTotal = item.getQuotationPrice().multiply(item.getCount());
                totalAmount = totalAmount.add(itemTotal);

                productList.add(CrmQuotationProductDO.builder()
                        .productId(item.getProductId())
                        .productName(product.getName())
                        .productCode(product.getNo())
                        .productPrice(product.getPrice())
                        .quotationPrice(item.getQuotationPrice())
                        .count(item.getCount())
                        .totalPrice(itemTotal)
                        .remark(item.getRemark())
                        .build());
            }
        }

        // 4. 计算折后金额
        BigDecimal discountPercent = createReqVO.getDiscountPercent() != null ? createReqVO.getDiscountPercent() : BigDecimal.valueOf(100);
        BigDecimal finalAmount = totalAmount.multiply(discountPercent).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);

        // 5. 计算过期时间
        Integer validDays = createReqVO.getValidDays() != null ? createReqVO.getValidDays() : 30;
        LocalDateTime expireTime = LocalDateTime.now().plusDays(validDays);

        // 6. 插入报价单
        CrmQuotationDO quotation = CrmQuotationDO.builder()
                .quotationNo(quotationNo)
                .businessId(createReqVO.getBusinessId())
                .customerId(createReqVO.getCustomerId())
                .contactId(createReqVO.getContactId())
                .ownerUserId(createReqVO.getOwnerUserId())
                .totalAmount(totalAmount)
                .discountPercent(discountPercent)
                .finalAmount(finalAmount)
                .status(0) // 草稿
                .validDays(validDays)
                .expireTime(expireTime)
                .remark(createReqVO.getRemark())
                .build();
        quotationMapper.insert(quotation);

        // 7. 插入报价单明细
        for (CrmQuotationProductDO product : productList) {
            product.setQuotationId(quotation.getId());
            quotationProductMapper.insert(product);
        }

        // 8. 初始化数据权限（负责人为 OWNER）
        crmPermissionService.createPermission(new CrmPermissionCreateReqBO()
                .setUserId(createReqVO.getOwnerUserId())
                .setBizType(CrmBizTypeEnum.CRM_QUOTATION.getType())
                .setBizId(quotation.getId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 9. 记录操作日志上下文
        LogRecordContext.putVariable("quotation", quotation);
        return quotation.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_QUOTATION_TYPE, subType = CRM_QUOTATION_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_QUOTATION_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_QUOTATION, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateQuotation(CrmQuotationSaveReqVO updateReqVO) {
        // 1. 校验报价单存在
        CrmQuotationDO existedQuotation = validateQuotationExists(updateReqVO.getId());

        // 2. 只能修改草稿状态的报价单
        if (existedQuotation.getStatus() != 0) {
            throw exception(QUOTATION_STATUS_NOT_DRAFT);
        }

        // 3. 计算金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<CrmQuotationProductDO> productList = new ArrayList<>();
        if (CollUtil.isNotEmpty(updateReqVO.getProducts())) {
            List<Long> productIds = updateReqVO.getProducts().stream()
                    .map(CrmQuotationSaveReqVO.ProductItem::getProductId)
                    .toList();
            Map<Long, CrmProductDO> productMap = productService.getProductMap(productIds);

            for (CrmQuotationSaveReqVO.ProductItem item : updateReqVO.getProducts()) {
                CrmProductDO product = productMap.get(item.getProductId());
                if (product == null) {
                    throw exception(PRODUCT_NOT_EXISTS);
                }
                BigDecimal itemTotal = item.getQuotationPrice().multiply(item.getCount());
                totalAmount = totalAmount.add(itemTotal);

                productList.add(CrmQuotationProductDO.builder()
                        .quotationId(updateReqVO.getId())
                        .productId(item.getProductId())
                        .productName(product.getName())
                        .productCode(product.getNo())
                        .productPrice(product.getPrice())
                        .quotationPrice(item.getQuotationPrice())
                        .count(item.getCount())
                        .totalPrice(itemTotal)
                        .remark(item.getRemark())
                        .build());
            }
        }

        // 4. 计算折后金额
        BigDecimal discountPercent = updateReqVO.getDiscountPercent() != null ? updateReqVO.getDiscountPercent() : BigDecimal.valueOf(100);
        BigDecimal finalAmount = totalAmount.multiply(discountPercent).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);

        // 5. 更新报价单
        CrmQuotationDO updateObj = CrmQuotationDO.builder()
                .id(updateReqVO.getId())
                .businessId(updateReqVO.getBusinessId())
                .customerId(updateReqVO.getCustomerId())
                .contactId(updateReqVO.getContactId())
                .totalAmount(totalAmount)
                .discountPercent(discountPercent)
                .finalAmount(finalAmount)
                .validDays(updateReqVO.getValidDays())
                .remark(updateReqVO.getRemark())
                .build();
        quotationMapper.updateById(updateObj);

        // 6. 删除旧的明细，插入新的明细
        quotationProductMapper.deleteByQuotationId(updateReqVO.getId());
        for (CrmQuotationProductDO product : productList) {
            quotationProductMapper.insert(product);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_QUOTATION_TYPE, subType = CRM_QUOTATION_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_QUOTATION_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_QUOTATION, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteQuotation(Long id) {
        // 校验存在
        CrmQuotationDO quotation = validateQuotationExists(id);
        // 删除报价单明细
        quotationProductMapper.deleteByQuotationId(id);
        // 删除报价单
        quotationMapper.deleteById(id);
        // 删除数据权限
        crmPermissionService.deletePermission(CrmBizTypeEnum.CRM_QUOTATION.getType(), id);
    }

    @Override
    public CrmQuotationDO getQuotation(Long id) {
        return quotationMapper.selectById(id);
    }

    @Override
    public PageResult<CrmQuotationDO> getQuotationPage(CrmQuotationPageReqVO pageReqVO) {
        return quotationMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CrmQuotationDO> getQuotationListByBusinessId(Long businessId) {
        return quotationMapper.selectList(CrmQuotationDO::getBusinessId, businessId);
    }

    @Override
    public List<CrmQuotationDO> getQuotationListByCustomerId(Long customerId) {
        return quotationMapper.selectList(CrmQuotationDO::getCustomerId, customerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_QUOTATION_TYPE, subType = CRM_QUOTATION_CONFIRM_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_QUOTATION_CONFIRM_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_QUOTATION, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public void confirmQuotation(Long id, Long auditUserId, String auditRemark) {
        // 1. 校验报价单存在
        CrmQuotationDO quotation = validateQuotationExists(id);

        // 2. 只能审批待审批状态的报价单
        if (quotation.getStatus() != 1) {
            throw exception(QUOTATION_STATUS_NOT_PENDING);
        }

        // 3. 更新状态
        CrmQuotationDO updateObj = CrmQuotationDO.builder()
                .id(id)
                .status(2) // 已通过
                .auditUserId(auditUserId)
                .auditTime(LocalDateTime.now())
                .auditRemark(auditRemark)
                .build();
        quotationMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_QUOTATION_TYPE, subType = CRM_QUOTATION_REJECT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_QUOTATION_REJECT_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_QUOTATION, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public void rejectQuotation(Long id, Long auditUserId, String auditRemark) {
        // 1. 校验报价单存在
        CrmQuotationDO quotation = validateQuotationExists(id);

        // 2. 只能审批待审批状态的报价单
        if (quotation.getStatus() != 1) {
            throw exception(QUOTATION_STATUS_NOT_PENDING);
        }

        // 3. 更新状态
        CrmQuotationDO updateObj = CrmQuotationDO.builder()
                .id(id)
                .status(3) // 已拒绝
                .auditUserId(auditUserId)
                .auditTime(LocalDateTime.now())
                .auditRemark(auditRemark)
                .build();
        quotationMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_QUOTATION_TYPE, subType = CRM_QUOTATION_VOID_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_QUOTATION_VOID_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_QUOTATION, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void voidQuotation(Long id) {
        // 1. 校验报价单存在
        CrmQuotationDO quotation = validateQuotationExists(id);

        // 2. 只能作废草稿或已拒绝状态的报价单
        if (quotation.getStatus() != 0 && quotation.getStatus() != 3) {
            throw exception(QUOTATION_STATUS_CANNOT_VOID);
        }

        // 3. 更新状态
        CrmQuotationDO updateObj = CrmQuotationDO.builder()
                .id(id)
                .status(4) // 已作废
                .build();
        quotationMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_QUOTATION_TYPE, subType = CRM_QUOTATION_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_QUOTATION_SUBMIT_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_QUOTATION, bizId = "#id", level = CrmPermissionLevelEnum.WRITE)
    public void submitQuotation(Long id) {
        // 1. 校验报价单存在
        CrmQuotationDO quotation = validateQuotationExists(id);

        // 2. 只能提交草稿状态的报价单
        if (quotation.getStatus() != 0) {
            throw exception(QUOTATION_STATUS_NOT_DRAFT_FOR_SUBMIT);
        }

        // 3. 更新状态为待审批
        CrmQuotationDO updateObj = CrmQuotationDO.builder()
                .id(id)
                .status(1) // 待审批
                .build();
        quotationMapper.updateById(updateObj);
    }

    @Override
    public Long getQuotationCountByBusinessId(Long businessId) {
        return quotationMapper.selectCountByBusinessId(businessId);
    }

    private CrmQuotationDO validateQuotationExists(Long id) {
        CrmQuotationDO quotation = quotationMapper.selectById(id);
        if (quotation == null) {
            throw exception(QUOTATION_NOT_EXISTS);
        }
        return quotation;
    }

    // 编号生成已改用 CrmNoRedisDAO.generate("QT")

}
