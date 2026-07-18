package com.meession.etm.module.crm.service.bulksend;

import com.meession.etm.framework.common.exception.ErrorCode;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendPageReqVO;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.bulksend.CrmBulkSendDO;
import com.meession.etm.module.crm.dal.mysql.bulksend.CrmBulkSendMapper;
import com.meession.etm.module.system.api.mail.MailSendApi;
import com.meession.etm.module.system.api.mail.dto.MailSendSingleToUserReqDTO;
import com.meession.etm.module.system.api.sms.SmsSendApi;
import com.meession.etm.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Objects;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.BULK_SEND_NOT_EXISTS;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.BULK_SEND_SUBMIT_FAIL_NOT_DRAFT;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;
import static com.meession.etm.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;

/**
 * 群发管理 Service 实现类
 *
 * @author Wanwan
 */
@Service
@Validated
@Slf4j
public class CrmBulkSendServiceImpl implements CrmBulkSendService {

    @Resource
    private CrmBulkSendMapper bulkSendMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private SmsSendApi smsSendApi;

    @Resource
    private MailSendApi mailSendApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BULK_SEND_TYPE, subType = CRM_BULK_SEND_CREATE_SUB_TYPE, bizNo = "{{#bulkSend.id}}",
            success = CRM_BULK_SEND_CREATE_SUCCESS)
    public Long createBulkSend(CrmBulkSendSaveReqVO createReqVO) {
        // 1. 校验负责人是否存在
        adminUserApi.validateUser(createReqVO.getOwnerUserId());

        // 2. 插入群发
        CrmBulkSendDO bulkSend = BeanUtils.toBean(createReqVO, CrmBulkSendDO.class);
        bulkSendMapper.insert(bulkSend);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("bulkSend", bulkSend);
        return bulkSend.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BULK_SEND_TYPE, subType = CRM_BULK_SEND_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_BULK_SEND_UPDATE_SUCCESS)
    public void updateBulkSend(CrmBulkSendSaveReqVO updateReqVO) {
        // 1. 校验群发是否存在
        CrmBulkSendDO oldBulkSend = validateBulkSendExists(updateReqVO.getId());
        // 2. 校验负责人是否存在
        adminUserApi.validateUser(updateReqVO.getOwnerUserId());

        // 3. 更新群发
        CrmBulkSendDO updateObj = BeanUtils.toBean(updateReqVO, CrmBulkSendDO.class);
        bulkSendMapper.updateById(updateObj);

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldBulkSend, CrmBulkSendSaveReqVO.class));
        LogRecordContext.putVariable("bulkSendName", oldBulkSend.getTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BULK_SEND_TYPE, subType = CRM_BULK_SEND_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_BULK_SEND_DELETE_SUCCESS)
    public void deleteBulkSend(Long id) {
        // 1. 校验群发是否存在
        CrmBulkSendDO bulkSend = validateBulkSendExists(id);

        // 2. 删除群发
        bulkSendMapper.deleteById(id);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("bulkSendName", bulkSend.getTitle());
    }

    @Override
    public CrmBulkSendDO getBulkSend(Long id) {
        return bulkSendMapper.selectById(id);
    }

    @Override
    public PageResult<CrmBulkSendDO> getBulkSendPage(CrmBulkSendPageReqVO pageReqVO) {
        return bulkSendMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BULK_SEND_TYPE, subType = CRM_BULK_SEND_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_BULK_SEND_SUBMIT_SUCCESS)
    public void submitForApproval(Long id) {
        // 1. 校验群发是否存在
        CrmBulkSendDO bulkSend = validateBulkSendExists(id);

        // 2. 校验状态：只能从草稿(1)提交到审核中(2)
        if (!Objects.equals(bulkSend.getStatus(), 1)) {
            throw exception(BULK_SEND_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 3. 更新状态
        CrmBulkSendDO updateObj = new CrmBulkSendDO();
        updateObj.setId(id);
        updateObj.setStatus(2);
        bulkSendMapper.updateById(updateObj);

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("bulkSendName", bulkSend.getTitle());
    }

    @Override
    @Transactional
    public void approve(Long id) {
        CrmBulkSendDO bulkSend = validateBulkSendExists(id);
        if (!Objects.equals(bulkSend.getStatus(), 2)) {
            throw exception(new ErrorCode(1_020_016_002, "只能审批待审核的群发"));
        }
        // 3. 发送（按选中客户数决定发送次数，实际发给负责人作演示）
        int count = (bulkSend.getTargetCount() != null && bulkSend.getTargetCount() > 0) ? bulkSend.getTargetCount() : 1;
        int success = 0, fail = 0;
        for (int i = 0; i < count; i++) {
            try {
                if (Objects.equals(bulkSend.getType(), 1)) {
                    SmsSendSingleToUserReqDTO smsReq = new SmsSendSingleToUserReqDTO();
                    smsReq.setUserId(bulkSend.getOwnerUserId());
                    smsReq.setTemplateCode("PROMOTION");
                    smsReq.setTemplateParams(new HashMap<>());
                    smsSendApi.sendSingleSmsToAdmin(smsReq);
                } else {
                    MailSendSingleToUserReqDTO mailReq = new MailSendSingleToUserReqDTO();
                    mailReq.setUserId(bulkSend.getOwnerUserId());
                    mailReq.setTemplateCode("PROMOTION");
                    mailReq.setTemplateParams(new HashMap<>());
                    mailSendApi.sendSingleMailToAdmin(mailReq);
                }
                success++;
            } catch (Exception e) {
                fail++;
            }
        }
        // 4. 更新状态
        CrmBulkSendDO updateObj = new CrmBulkSendDO();
        updateObj.setId(id);
        updateObj.setStatus(4);
        updateObj.setSuccessCount(success);
        updateObj.setFailCount(fail);
        updateObj.setTargetCount(bulkSend.getTargetCount() == null || bulkSend.getTargetCount() == 0 ? 1 : bulkSend.getTargetCount());
        updateObj.setSendTime(java.time.LocalDateTime.now());
        bulkSendMapper.updateById(updateObj);
        LogRecordContext.putVariable("bulkSendName", bulkSend.getTitle());
    }

    @Override
    @Transactional
    public void reject(Long id) {
        CrmBulkSendDO bulkSend = validateBulkSendExists(id);
        if (!Objects.equals(bulkSend.getStatus(), 2)) {
            throw exception(new ErrorCode(1_020_016_003, "只能驳回待审核的群发"));
        }
        CrmBulkSendDO updateObj = new CrmBulkSendDO();
        updateObj.setId(id);
        updateObj.setStatus(5);
        bulkSendMapper.updateById(updateObj);
        LogRecordContext.putVariable("bulkSendName", bulkSend.getTitle());
    }

    private CrmBulkSendDO validateBulkSendExists(Long id) {
        CrmBulkSendDO bulkSendDO = bulkSendMapper.selectById(id);
        if (bulkSendDO == null) {
            throw exception(BULK_SEND_NOT_EXISTS);
        }
        return bulkSendDO;
    }

}
