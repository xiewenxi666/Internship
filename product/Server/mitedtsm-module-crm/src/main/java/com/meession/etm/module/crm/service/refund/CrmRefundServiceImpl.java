package com.meession.etm.module.crm.service.refund;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.common.util.object.ObjectUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundReportReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import com.meession.etm.module.crm.dal.mysql.refund.CrmRefundMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmBizNoPrefix;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;
import static com.meession.etm.module.crm.util.CrmAuditStatusUtils.convertBpmResultToAuditStatus;

/**
 * CRM 退款 Service 实现类
 *
 * @author 赤焰
 */
@Service
@Validated
@Slf4j
public class CrmRefundServiceImpl implements CrmRefundService {

    /**
     * BPM 退款审批流程标识
     */
    public static final String BPM_PROCESS_DEFINITION_KEY = "crm-refund-audit";

    @Resource
    private CrmRefundMapper refundMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmPermissionService permissionService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_CREATE_SUB_TYPE, bizNo = "{{#refund.id}}",
            success = CRM_REFUND_CREATE_SUCCESS)
    public Long createRefund(CrmRefundSaveReqVO createReqVO) {
        // 1.1 校验关联数据存在
        validateRelationDataExists(createReqVO);
        // 1.2 生成退款编号
        String no = noRedisDAO.generate(CrmBizNoPrefix.REFUND);
        if (refundMapper.selectByNo(no) != null) {
            throw exception(REFUND_NO_EXISTS);
        }

        // 2. 插入退款
        CrmRefundDO refund = BeanUtils.toBean(createReqVO, CrmRefundDO.class)
                .setNo(no).setAuditStatus(CrmAuditStatusEnum.DRAFT.getStatus());
        refundMapper.insert(refund);

        // 3. 创建数据权限
        permissionService.createPermission(new CrmPermissionCreateReqBO().setBizType(CrmBizTypeEnum.CRM_REFUND.getType())
                .setBizId(refund.getId()).setUserId(createReqVO.getOwnerUserId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("refund", refund);
        return refund.getId();
    }

    private void validateRelationDataExists(CrmRefundSaveReqVO reqVO) {
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId());
        }
        if (reqVO.getContractId() != null) {
            CrmContractDO contract = contractService.validateContract(reqVO.getContractId());
            if (ObjectUtil.notEqual(contract.getAuditStatus(), CrmAuditStatusEnum.APPROVE.getStatus())) {
                throw exception(REFUND_CREATE_FAIL_CONTRACT_NOT_APPROVE);
            }
            reqVO.setCustomerId(contract.getCustomerId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_REFUND_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REFUND, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateRefund(CrmRefundSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "退款编号不能为空");
        updateReqVO.setOwnerUserId(null).setCustomerId(null).setContractId(null);
        // 1.1 校验存在
        CrmRefundDO oldRefund = validateRefundExists(updateReqVO.getId());
        updateReqVO.setOwnerUserId(oldRefund.getOwnerUserId()).setCustomerId(oldRefund.getCustomerId())
                .setContractId(oldRefund.getContractId());

        // 1.2 只有草稿、被驳回、被否决、已撤销，可以编辑
        if (!ObjectUtils.equalsAny(oldRefund.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus(),
                CrmAuditStatusEnum.REJECT.getStatus(),
                CrmAuditStatusEnum.VETO.getStatus(), CrmAuditStatusEnum.CANCEL.getStatus())) {
            throw exception(REFUND_UPDATE_FAIL_EDITING_PROHIBITED);
        }

        // 2. 更新退款
        CrmRefundDO updateObj = BeanUtils.toBean(updateReqVO, CrmRefundDO.class);
        refundMapper.updateById(updateObj);

        // 3. 记录操作日志上下文
        updateReqVO.setOwnerUserId(oldRefund.getOwnerUserId());
        LogRecordContext.putVariable("oldRefund", oldRefund);
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldRefund, CrmRefundSaveReqVO.class));
    }

    @Override
    public void updateRefundAuditStatus(Long id, Integer bpmResult) {
        // 1.1 校验存在
        CrmRefundDO refund = validateRefundExists(id);
        // 1.2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            log.error("[updateRefundAuditStatus][refund({}) 不处于审批中，无法更新审批结果({})]",
                    refund.getId(), bpmResult);
            throw exception(REFUND_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        // 2. 更新退款审批状态
        Integer auditStatus = convertBpmResultToAuditStatus(bpmResult);
        refundMapper.updateById(new CrmRefundDO().setId(id).setAuditStatus(auditStatus));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REFUND_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REFUND, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteRefund(Long id) {
        // 1.1 校验存在
        CrmRefundDO refund = validateRefundExists(id);
        // 1.2 审批通过时，不允许删除
        if (ObjUtil.equal(refund.getAuditStatus(), CrmAuditStatusEnum.APPROVE.getStatus())) {
            throw exception(REFUND_DELETE_FAIL_IS_APPROVE);
        }

        // 2.1 删除退款
        refundMapper.deleteById(id);
        // 2.2 删除数据权限
        permissionService.deletePermission(CrmBizTypeEnum.CRM_REFUND.getType(), id);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("refund", refund);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REFUND_SUBMIT_SUCCESS)
    public void submitRefund(Long id, Long userId) {
        CrmRefundDO refund = validateRefundExists(id);
        if (ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus())
                && ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.REJECT.getStatus())
                && ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.VETO.getStatus())
                && ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.CANCEL.getStatus())) {
            throw exception(REFUND_SUBMIT_FAIL_NOT_DRAFT);
        }

        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(BPM_PROCESS_DEFINITION_KEY).setBusinessKey(String.valueOf(id)));

        refundMapper.updateById(new CrmRefundDO().setId(id).setProcessInstanceId(processInstanceId)
                .setAuditStatus(CrmAuditStatusEnum.PROCESS.getStatus()));

        LogRecordContext.putVariable("refundNo", refund.getNo());
    }

    private CrmRefundDO validateRefundExists(Long id) {
        CrmRefundDO refund = refundMapper.selectById(id);
        if (refund == null) {
            throw exception(REFUND_NOT_EXISTS);
        }
        return refund;
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REFUND, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmRefundDO getRefund(Long id) {
        return refundMapper.selectById(id);
    }

    @Override
    public List<CrmRefundDO> getRefundList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return refundMapper.selectByIds(ids);
    }

    @Override
    public PageResult<CrmRefundDO> getRefundPage(CrmRefundPageReqVO pageReqVO, Long userId) {
        return refundMapper.selectPage(pageReqVO, userId);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmRefundDO> getRefundPageByCustomerId(CrmRefundPageReqVO pageReqVO) {
        return refundMapper.selectPageByCustomerId(pageReqVO);
    }

    @Override
    public Long getAuditRefundCount(Long userId) {
        return refundMapper.selectCountByAudit(userId);
    }

    @Override
    public PageResult<CrmRefundDO> getRefundReport(CrmRefundReportReqVO reqVO) {
        List<CrmRefundDO> allRefunds = refundMapper.selectListForReport(reqVO.getYear(), reqVO.getOwnerUserId());
        int total = allRefunds.size();
        int fromIndex = (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        int toIndex = Math.min(fromIndex + reqVO.getPageSize(), total);
        if (fromIndex >= total) {
            return new PageResult<>(ListUtil.empty(), (long) total);
        }
        return new PageResult<>(allRefunds.subList(fromIndex, toIndex), (long) total);
    }

    @Override
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_CANCEL_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REFUND_CANCEL_SUCCESS)
    public void cancelRefund(Long id, String reason) {
        CrmRefundDO refund = validateRefundExists(id);
        if (ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REFUND_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        refundMapper.updateById(new CrmRefundDO().setId(id).setAuditStatus(CrmAuditStatusEnum.CANCEL.getStatus())
                .setProcessInstanceId(null));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("refundNo", refund.getNo());
    }

    @Override
    public PageResult<CrmRefundDO> getRefundApprovalPage(CrmRefundApprovalPageReqVO pageReqVO, Long userId) {
        return refundMapper.selectPageForApproval(pageReqVO, userId);
    }

    @Override
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_APPROVE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REFUND_APPROVE_SUCCESS)
    public void approveRefund(Long id, String reason) {
        CrmRefundDO refund = validateRefundExists(id);
        if (ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REFUND_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        refundMapper.updateById(new CrmRefundDO().setId(id)
                .setAuditStatus(CrmAuditStatusEnum.APPROVE.getStatus()));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("refundNo", refund.getNo());
    }

    @Override
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_REJECT_AUDIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REFUND_REJECT_AUDIT_SUCCESS)
    public void rejectRefund(Long id, String reason) {
        CrmRefundDO refund = validateRefundExists(id);
        if (ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REFUND_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        refundMapper.updateById(new CrmRefundDO().setId(id)
                .setAuditStatus(CrmAuditStatusEnum.REJECT.getStatus()));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("refundNo", refund.getNo());
    }

    @Override
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_VETO_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REFUND_VETO_SUCCESS)
    public void vetoRefund(Long id, String reason) {
        CrmRefundDO refund = validateRefundExists(id);
        if (ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REFUND_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        refundMapper.updateById(new CrmRefundDO().setId(id)
                .setAuditStatus(CrmAuditStatusEnum.VETO.getStatus()));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("refundNo", refund.getNo());
    }

}
