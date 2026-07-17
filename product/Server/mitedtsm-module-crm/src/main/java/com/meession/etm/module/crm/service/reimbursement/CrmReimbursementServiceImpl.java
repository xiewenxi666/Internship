package com.meession.etm.module.crm.service.reimbursement;

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
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import com.meession.etm.module.crm.dal.mysql.reimbursement.CrmReimbursementMapper;
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
 * CRM 报销 Service 实现类
 *
 * @author 赤焰
 */
@Service
@Validated
@Slf4j
public class CrmReimbursementServiceImpl implements CrmReimbursementService {

    public static final String BPM_PROCESS_DEFINITION_KEY = "crm-reimbursement-audit";

    @Resource
    private CrmReimbursementMapper reimbursementMapper;

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
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_CREATE_SUB_TYPE, bizNo = "{{#reimbursement.id}}",
            success = CRM_REIMBURSEMENT_CREATE_SUCCESS)
    public Long createReimbursement(CrmReimbursementSaveReqVO createReqVO) {
        validateRelationDataExists(createReqVO);
        String no = noRedisDAO.generate(CrmBizNoPrefix.REIMBURSEMENT);
        if (reimbursementMapper.selectByNo(no) != null) {
            throw exception(REIMBURSEMENT_NO_EXISTS);
        }

        CrmReimbursementDO reimbursement = BeanUtils.toBean(createReqVO, CrmReimbursementDO.class)
                .setNo(no).setAuditStatus(CrmAuditStatusEnum.DRAFT.getStatus());
        reimbursementMapper.insert(reimbursement);

        permissionService.createPermission(new CrmPermissionCreateReqBO().setBizType(CrmBizTypeEnum.CRM_REIMBURSEMENT.getType())
                .setBizId(reimbursement.getId()).setUserId(createReqVO.getOwnerUserId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        LogRecordContext.putVariable("reimbursement", reimbursement);
        return reimbursement.getId();
    }

    private void validateRelationDataExists(CrmReimbursementSaveReqVO reqVO) {
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId());
        }
        if (reqVO.getContractId() != null) {
            CrmContractDO contract = contractService.validateContract(reqVO.getContractId());
            if (ObjectUtil.notEqual(contract.getAuditStatus(), CrmAuditStatusEnum.APPROVE.getStatus())) {
                throw exception(REIMBURSEMENT_CREATE_FAIL_CONTRACT_NOT_APPROVE);
            }
            reqVO.setCustomerId(contract.getCustomerId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_REIMBURSEMENT_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REIMBURSEMENT, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateReimbursement(CrmReimbursementSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "报销编号不能为空");
        updateReqVO.setOwnerUserId(null).setCustomerId(null).setContractId(null);
        CrmReimbursementDO oldReimbursement = validateReimbursementExists(updateReqVO.getId());
        updateReqVO.setOwnerUserId(oldReimbursement.getOwnerUserId()).setCustomerId(oldReimbursement.getCustomerId())
                .setContractId(oldReimbursement.getContractId());

        if (!ObjectUtils.equalsAny(oldReimbursement.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus(),
                CrmAuditStatusEnum.REJECT.getStatus(),
                CrmAuditStatusEnum.VETO.getStatus(), CrmAuditStatusEnum.CANCEL.getStatus())) {
            throw exception(REIMBURSEMENT_UPDATE_FAIL_EDITING_PROHIBITED);
        }

        CrmReimbursementDO updateObj = BeanUtils.toBean(updateReqVO, CrmReimbursementDO.class);
        reimbursementMapper.updateById(updateObj);

        updateReqVO.setOwnerUserId(oldReimbursement.getOwnerUserId());
        LogRecordContext.putVariable("oldReimbursement", oldReimbursement);
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldReimbursement, CrmReimbursementSaveReqVO.class));
    }

    @Override
    public void updateReimbursementAuditStatus(Long id, Integer bpmResult) {
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            log.error("[updateReimbursementAuditStatus][reimbursement({}) 不处于审批中，无法更新审批结果({})]",
                    reimbursement.getId(), bpmResult);
            throw exception(REIMBURSEMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        Integer auditStatus = convertBpmResultToAuditStatus(bpmResult);
        reimbursementMapper.updateById(new CrmReimbursementDO().setId(id).setAuditStatus(auditStatus));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REIMBURSEMENT_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REIMBURSEMENT, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteReimbursement(Long id) {
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.equal(reimbursement.getAuditStatus(), CrmAuditStatusEnum.APPROVE.getStatus())) {
            throw exception(REIMBURSEMENT_DELETE_FAIL_IS_APPROVE);
        }

        reimbursementMapper.deleteById(id);
        permissionService.deletePermission(CrmBizTypeEnum.CRM_REIMBURSEMENT.getType(), id);

        LogRecordContext.putVariable("reimbursement", reimbursement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REIMBURSEMENT_SUBMIT_SUCCESS)
    public void submitReimbursement(Long id, Long userId) {
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus())
                && ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.REJECT.getStatus())
                && ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.VETO.getStatus())
                && ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.CANCEL.getStatus())) {
            throw exception(REIMBURSEMENT_SUBMIT_FAIL_NOT_DRAFT);
        }

        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(BPM_PROCESS_DEFINITION_KEY).setBusinessKey(String.valueOf(id)));

        reimbursementMapper.updateById(new CrmReimbursementDO().setId(id).setProcessInstanceId(processInstanceId)
                .setAuditStatus(CrmAuditStatusEnum.PROCESS.getStatus()));

        LogRecordContext.putVariable("reimbursementNo", reimbursement.getNo());
    }

    private CrmReimbursementDO validateReimbursementExists(Long id) {
        CrmReimbursementDO reimbursement = reimbursementMapper.selectById(id);
        if (reimbursement == null) {
            throw exception(REIMBURSEMENT_NOT_EXISTS);
        }
        return reimbursement;
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REIMBURSEMENT, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmReimbursementDO getReimbursement(Long id) {
        return reimbursementMapper.selectById(id);
    }

    @Override
    public List<CrmReimbursementDO> getReimbursementList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return reimbursementMapper.selectByIds(ids);
    }

    @Override
    public PageResult<CrmReimbursementDO> getReimbursementPage(CrmReimbursementPageReqVO pageReqVO, Long userId) {
        return reimbursementMapper.selectPage(pageReqVO, userId);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmReimbursementDO> getReimbursementPageByCustomerId(CrmReimbursementPageReqVO pageReqVO) {
        return reimbursementMapper.selectPageByCustomerId(pageReqVO);
    }

    @Override
    public Long getAuditReimbursementCount(Long userId) {
        return reimbursementMapper.selectCountByAudit(userId);
    }

    @Override
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_CANCEL_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REIMBURSEMENT_CANCEL_SUCCESS)
    public void cancelReimbursement(Long id, String reason) {
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REIMBURSEMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        reimbursementMapper.updateById(new CrmReimbursementDO().setId(id).setAuditStatus(CrmAuditStatusEnum.CANCEL.getStatus())
                .setProcessInstanceId(null));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("reimbursementNo", reimbursement.getNo());
    }

    @Override
    public PageResult<CrmReimbursementDO> getReimbursementApprovalPage(CrmReimbursementApprovalPageReqVO pageReqVO, Long userId) {
        return reimbursementMapper.selectPageForApproval(pageReqVO, userId);
    }

    @Override
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_APPROVE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REIMBURSEMENT_APPROVE_SUCCESS)
    public void approveReimbursement(Long id, String reason) {
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REIMBURSEMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        reimbursementMapper.updateById(new CrmReimbursementDO().setId(id)
                .setAuditStatus(CrmAuditStatusEnum.APPROVE.getStatus()));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("reimbursementNo", reimbursement.getNo());
    }

    @Override
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_REJECT_AUDIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REIMBURSEMENT_REJECT_AUDIT_SUCCESS)
    public void rejectReimbursement(Long id, String reason) {
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REIMBURSEMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        reimbursementMapper.updateById(new CrmReimbursementDO().setId(id)
                .setAuditStatus(CrmAuditStatusEnum.REJECT.getStatus()));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("reimbursementNo", reimbursement.getNo());
    }

    @Override
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_VETO_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_REIMBURSEMENT_VETO_SUCCESS)
    public void vetoReimbursement(Long id, String reason) {
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(REIMBURSEMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }
        reimbursementMapper.updateById(new CrmReimbursementDO().setId(id)
                .setAuditStatus(CrmAuditStatusEnum.VETO.getStatus()));
        LogRecordContext.putVariable("reason", reason != null && !reason.isEmpty() ? reason : null);
        LogRecordContext.putVariable("reimbursementNo", reimbursement.getNo());
    }

}
