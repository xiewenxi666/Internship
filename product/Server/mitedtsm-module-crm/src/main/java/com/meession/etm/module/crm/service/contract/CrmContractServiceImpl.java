package com.meession.etm.module.crm.service.contract;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.number.MoneyUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.common.util.object.ObjectUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractPageReqVO;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractSaveReqVO;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractTransferReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractConfigDO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractProductDO;
import com.meession.etm.module.crm.dal.mysql.contract.CrmContractMapper;
import com.meession.etm.module.crm.dal.mysql.contract.CrmContractProductMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.business.CrmBusinessService;
import com.meession.etm.module.crm.service.contact.CrmContactService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionTransferReqBO;
import com.meession.etm.module.crm.service.product.CrmProductService;
import com.meession.etm.module.crm.service.receivable.CrmReceivableService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
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
import static com.meession.etm.module.crm.util.CrmAuditStatusUtils.convertBpmResultToAuditStatus;

/**
 * CRM 合同 Service 实现类
 *
 * @author dhb52
 */
@Service
@Validated
@Slf4j
public class CrmContractServiceImpl implements CrmContractService {

    /**
     * BPM 合同审批流程标识
     */
    public static final String BPM_PROCESS_DEFINITION_KEY = "crm-contract-audit";

    @Resource
    private CrmContractMapper contractMapper;
    @Resource
    private CrmContractProductMapper contractProductMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmPermissionService crmPermissionService;
    @Resource
    private CrmProductService productService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private CrmBusinessService businessService;
    @Resource
    private CrmContactService contactService;
    @Resource
    private CrmContractConfigService contractConfigService;
    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private CrmReceivableService receivableService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    /**
     * 创建合同
     *
     * @param createReqVO 创建请求
     * @param userId 用户编号
     * @return 合同编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_CREATE_SUB_TYPE, bizNo = "{{#contract.id}}",
            success = CRM_CONTRACT_CREATE_SUCCESS)
    public Long createContract(CrmContractSaveReqVO createReqVO, Long userId) {
        // 1.1 校验产品项的有效性
        List<CrmContractProductDO> contractProducts = validateContractProducts(createReqVO.getProducts());
        // 1.2 校验关联字段
        validateRelationDataExists(createReqVO);
        // 1.3 生成序号
        String no = noRedisDAO.generate(CrmNoRedisDAO.CONTRACT_NO_PREFIX);
        if (contractMapper.selectByNo(no) != null) {
            throw exception(CONTRACT_NO_EXISTS);
        }

        // 2.1 插入合同
        CrmContractDO contract = BeanUtils.toBean(createReqVO, CrmContractDO.class).setNo(no);
        calculateTotalPrice(contract, contractProducts);
        contractMapper.insert(contract);
        // 2.2 插入合同关联商品
        if (CollUtil.isNotEmpty(contractProducts)) {
            contractProducts.forEach(item -> item.setContractId(contract.getId()));
            contractProductMapper.insertBatch(contractProducts);
        }

        // 3. 创建数据权限
        crmPermissionService.createPermission(new CrmPermissionCreateReqBO().setUserId(contract.getOwnerUserId())
                .setBizType(CrmBizTypeEnum.CRM_CONTRACT.getType()).setBizId(contract.getId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("contract", contract);
        return contract.getId();
    }

    /**
     * 更新合同
     *
     * @param updateReqVO 更新请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_CONTRACT_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateContract(CrmContractSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "合同编号不能为空");
        updateReqVO.setOwnerUserId(null); // 不允许更新的字段
        // 1.1 校验存在
        CrmContractDO oldContract = validateContractExists(updateReqVO.getId());
        // 1.2 只有草稿、审批中，可以编辑；
        if (!ObjectUtils.equalsAny(oldContract.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus(),
                CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(CONTRACT_UPDATE_FAIL_NOT_DRAFT);
        }
        // 1.3 校验产品项的有效性
        List<CrmContractProductDO> contractProducts = validateContractProducts(updateReqVO.getProducts());
        // 1.4 校验关联字段
        validateRelationDataExists(updateReqVO);

        // 2.1 更新合同
        CrmContractDO updateObj = BeanUtils.toBean(updateReqVO, CrmContractDO.class);
        calculateTotalPrice(updateObj, contractProducts);
        contractMapper.updateById(updateObj);
        // 2.2 更新合同关联商品
        updateContractProduct(updateReqVO.getId(), contractProducts);

        // 3. 记录操作日志上下文
        updateReqVO.setOwnerUserId(oldContract.getOwnerUserId()); // 避免操作日志出现“删除负责人”的情况
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldContract, CrmContractSaveReqVO.class));
        LogRecordContext.putVariable("contractName", oldContract.getName());
    }

    /**
     * 差量更新合同关联商品
     *
     * @param id 合同编号
     * @param newList 新商品列表
     */
    private void updateContractProduct(Long id, List<CrmContractProductDO> newList) {
        List<CrmContractProductDO> oldList = contractProductMapper.selectListByContractId(id);
        List<List<CrmContractProductDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setContractId(id));
            contractProductMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            contractProductMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            contractProductMapper.deleteByIds(convertSet(diffList.get(2), CrmContractProductDO::getId));
        }
    }

    /**
     * 校验关联数据是否存在
     *
     * @param reqVO 请求
     */
    private void validateRelationDataExists(CrmContractSaveReqVO reqVO) {
        // 1. 校验客户
        if (reqVO.getCustomerId() != null) {
            customerService.validateCustomer(reqVO.getCustomerId());
        }
        // 2. 校验负责人
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId());
        }
        // 3. 如果有关联商机，则需要校验存在
        if (reqVO.getBusinessId() != null) {
            businessService.validateBusiness(reqVO.getBusinessId());
        }
        // 4. 校验签约相关字段
        if (reqVO.getSignContactId() != null) {
            contactService.validateContact(reqVO.getSignContactId());
        }
        if (reqVO.getSignUserId() != null) {
            adminUserApi.validateUser(reqVO.getSignUserId());
        }
    }

    /**
     * 校验合同商品列表有效性，并转换为 DO 列表
     *
     * @param list 商品列表
     * @return 合同商品 DO 列表
     */
    private List<CrmContractProductDO> validateContractProducts(List<CrmContractSaveReqVO.Product> list) {
        // 1. 校验产品存在
        productService.validProductList(convertSet(list, CrmContractSaveReqVO.Product::getProductId));
        // 2. 转化为 CrmContractProductDO 列表
        return convertList(list, o -> BeanUtils.toBean(o, CrmContractProductDO.class,
                item -> item.setTotalPrice(MoneyUtils.priceMultiply(item.getContractPrice(), item.getCount()))));
    }

    /**
     * 计算合同总价（含折扣）
     *
     * @param contract 合同
     * @param contractProducts 合同商品列表
     */
    private void calculateTotalPrice(CrmContractDO contract, List<CrmContractProductDO> contractProducts) {
        contract.setTotalProductPrice(getSumValue(contractProducts, CrmContractProductDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO));
        BigDecimal discountPrice = MoneyUtils.priceMultiplyPercent(contract.getTotalProductPrice(), contract.getDiscountPercent());
        contract.setTotalPrice(contract.getTotalProductPrice().subtract(discountPrice));
    }

    /**
     * 删除合同
     *
     * @param id 合同编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_CONTRACT_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteContract(Long id) {
        // 1.1 校验存在
        CrmContractDO contract = validateContractExists(id);
        // 1.2 如果被 CrmReceivableDO 所使用，则不允许删除
        if (receivableService.getReceivableCountByContractId(contract.getId()) > 0) {
            throw exception(CONTRACT_DELETE_FAIL);
        }

        // 2.1 删除合同
        contractMapper.deleteById(id);
        // 2.2 删除数据权限
        crmPermissionService.deletePermission(CrmBizTypeEnum.CRM_CONTRACT.getType(), id);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("contractName", contract.getName());
    }

    /**
     * 校验合同是否存在
     *
     * @param id 合同编号
     * @return 合同
     */
    private CrmContractDO validateContractExists(Long id) {
        CrmContractDO contract = contractMapper.selectById(id);
        if (contract == null) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        return contract;
    }

    /**
     * 转移合同负责人
     *
     * @param reqVO 转移请求
     * @param userId 当前用户编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_TRANSFER_SUB_TYPE, bizNo = "{{#reqVO.id}}",
            success = CRM_CONTRACT_TRANSFER_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#reqVO.id", level = CrmPermissionLevelEnum.OWNER)
    public void transferContract(CrmContractTransferReqVO reqVO, Long userId) {
        // 1. 校验合同是否存在
        CrmContractDO contract = validateContractExists(reqVO.getId());

        // 2.1 数据权限转移
        crmPermissionService.transferPermission(new CrmPermissionTransferReqBO(userId, CrmBizTypeEnum.CRM_CONTRACT.getType(),
                reqVO.getId(), reqVO.getNewOwnerUserId(), reqVO.getOldOwnerPermissionLevel()));
        // 2.2 设置负责人
        contractMapper.updateById(new CrmContractDO().setId(reqVO.getId()).setOwnerUserId(reqVO.getNewOwnerUserId()));

        // 3. 记录转移日志
        LogRecordContext.putVariable("contract", contract);
    }

    /**
     * 更新合同跟进信息
     *
     * @param id 合同编号
     * @param contactNextTime 下次联系时间
     * @param contactLastContent 最后跟进内容
     */
    @Override
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_FOLLOW_UP_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_CONTRACT_FOLLOW_UP_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#id", level = CrmPermissionLevelEnum.WRITE)
    public void updateContractFollowUp(Long id, LocalDateTime contactNextTime, String contactLastContent) {
        // 1. 校验存在
        CrmContractDO contract = validateContractExists(id);

        // 2. 更新联系人的跟进信息
        contractMapper.updateById(new CrmContractDO().setId(id).setContactLastTime(LocalDateTime.now()));

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("contractName", contract.getName());
    }

    /**
     * 提交合同审批
     *
     * @param id 合同编号
     * @param userId 用户编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_CONTRACT_SUBMIT_SUCCESS)
    public void submitContract(Long id, Long userId) {
        // 1. 校验合同是否在审批
        CrmContractDO contract = validateContractExists(id);
        if (ObjUtil.notEqual(contract.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(CONTRACT_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建合同审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(BPM_PROCESS_DEFINITION_KEY).setBusinessKey(String.valueOf(id)));

        // 3. 更新合同工作流编号
        contractMapper.updateById(new CrmContractDO().setId(id).setProcessInstanceId(processInstanceId)
                .setAuditStatus(CrmAuditStatusEnum.PROCESS.getStatus()));

        // 3. 记录日志
        LogRecordContext.putVariable("contractName", contract.getName());
    }

    /**
     * 更新合同审批状态
     *
     * @param id 合同编号
     * @param bpmResult BPM 审批结果
     */
    @Override
    public void updateContractAuditStatus(Long id, Integer bpmResult) {
        // 1.1 校验合同是否存在
        CrmContractDO contract = validateContractExists(id);
        // 1.2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(contract.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            log.error("[updateContractAuditStatus][contract({}) 不处于审批中，无法更新审批结果({})]",
                    contract.getId(), bpmResult);
            throw exception(CONTRACT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        // 2. 更新合同审批结果
        Integer auditStatus = convertBpmResultToAuditStatus(bpmResult);
        contractMapper.updateById(new CrmContractDO().setId(id).setAuditStatus(auditStatus));
    }

    // ======================= 查询相关 =======================

    /**
     * 查询合同详情
     *
     * @param id 合同编号
     * @return 合同
     */
    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmContractDO getContract(Long id) {
        return contractMapper.selectById(id);
    }

    /**
     * 校验合同是否存在
     *
     * @param id 合同编号
     * @return 合同
     */
    @Override
    public CrmContractDO validateContract(Long id) {
        return validateContractExists(id);
    }

    /**
     * 查询合同列表
     *
     * @param ids 合同编号集合
     * @return 合同列表
     */
    @Override
    public List<CrmContractDO> getContractList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return contractMapper.selectByIds(ids);
    }

    /**
     * 分页查询合同（支持到期提醒筛选）
     *
     * @param pageReqVO 分页请求
     * @param userId 用户编号
     * @return 分页结果
     */
    @Override
    public PageResult<CrmContractDO> getContractPage(CrmContractPageReqVO pageReqVO, Long userId) {
        // 1. 即将到期，需要查询合同配置
        CrmContractConfigDO config = null;
        if (CrmContractPageReqVO.EXPIRY_TYPE_ABOUT_TO_EXPIRE.equals(pageReqVO.getExpiryType())) {
            config = contractConfigService.getContractConfig();
            if (config != null && Boolean.FALSE.equals(config.getNotifyEnabled())) {
                config = null;
            }
            if (config == null) {
                return PageResult.empty();
            }
        }
        // 2. 查询分页
        return contractMapper.selectPage(pageReqVO, userId, config);
    }

    /**
     * 根据客户编号分页查询合同
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmContractDO> getContractPageByCustomerId(CrmContractPageReqVO pageReqVO) {
        return contractMapper.selectPageByCustomerId(pageReqVO);
    }

    /**
     * 根据商机编号分页查询合同
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#pageReqVO.businessId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmContractDO> getContractPageByBusinessId(CrmContractPageReqVO pageReqVO) {
        return contractMapper.selectPageByBusinessId(pageReqVO);
    }

    /**
     * 根据联系人编号统计合同数量
     *
     * @param contactId 联系人编号
     * @return 合同数量
     */
    @Override
    public Long getContractCountByContactId(Long contactId) {
        return contractMapper.selectCountByContactId(contactId);
    }

    /**
     * 根据客户编号统计合同数量
     *
     * @param customerId 客户编号
     * @return 合同数量
     */
    @Override
    public Long getContractCountByCustomerId(Long customerId) {
        return contractMapper.selectCount(CrmContractDO::getCustomerId, customerId);
    }

    /**
     * 根据商机编号统计合同数量
     *
     * @param businessId 商机编号
     * @return 合同数量
     */
    @Override
    public Long getContractCountByBusinessId(Long businessId) {
        return contractMapper.selectCountByBusinessId(businessId);
    }

    /**
     * 根据合同编号查询关联商品列表
     *
     * @param contactId 合同编号
     * @return 商品列表
     */
    @Override
    public List<CrmContractProductDO> getContractProductListByContractId(Long contactId) {
        return contractProductMapper.selectListByContractId(contactId);
    }

    /**
     * 获取待审核合同数量
     *
     * @param userId 用户编号
     * @return 待审核数量
     */
    @Override
    public Long getAuditContractCount(Long userId) {
        return contractMapper.selectCountByAudit(userId);
    }

    /**
     * 获取待提醒合同数量
     *
     * @param userId 用户编号
     * @return 待提醒数量
     */
    @Override
    public Long getRemindContractCount(Long userId) {
        CrmContractConfigDO config = contractConfigService.getContractConfig();
        if (config == null || Boolean.FALSE.equals(config.getNotifyEnabled())) {
            return 0L;
        }
        return contractMapper.selectCountByRemind(userId, config);
    }

    /**
     * 根据客户编号和负责人编号查询合同列表
     *
     * @param customerId 客户编号
     * @param ownerUserId 负责人编号
     * @return 合同列表
     */
    @Override
    public List<CrmContractDO> getContractListByCustomerIdOwnerUserId(Long customerId, Long ownerUserId) {
        return contractMapper.selectListByCustomerIdOwnerUserId(customerId, ownerUserId);
    }

}
