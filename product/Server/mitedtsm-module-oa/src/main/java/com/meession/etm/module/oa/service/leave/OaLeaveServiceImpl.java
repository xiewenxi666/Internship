package com.meession.etm.module.oa.service.leave;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeavePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import com.meession.etm.module.oa.dal.mysql.OaLeaveMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LEAVE_NOT_EXISTS;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LEAVE_SUBMIT_FAIL_NOT_DRAFT;

@Service
@Validated
public class OaLeaveServiceImpl implements OaLeaveService {

    public static final String PROCESS_KEY = "oa_leave";

    @Resource
    private OaLeaveMapper leaveMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLeave(Long userId, OaLeaveCreateReqVO createReqVO) {
        long days = LocalDateTimeUtil.between(createReqVO.getStartTime(), createReqVO.getEndTime()).toDays();
        OaLeaveDO leave = BeanUtils.toBean(createReqVO, OaLeaveDO.class)
                .setUserId(userId).setDay(BigDecimal.valueOf(days)).setStatus(-1);
        leaveMapper.insert(leave);
        return leave.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLeave(Long id, OaLeaveCreateReqVO updateReqVO) {
        validateLeaveExists(id);
        OaLeaveDO leave = BeanUtils.toBean(updateReqVO, OaLeaveDO.class).setId(id);
        leaveMapper.updateById(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitLeave(Long id, Long userId) {
        OaLeaveDO leave = leaveMapper.selectById(id);
        if (leave == null) {
            throw ServiceExceptionUtil.exception(OA_LEAVE_NOT_EXISTS);
        }
        if (leave.getStatus() != -1) {
            throw ServiceExceptionUtil.exception(OA_LEAVE_SUBMIT_FAIL_NOT_DRAFT);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("day", leave.getDay());
        variables.put("type", leave.getType());

        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(variables).setBusinessKey(String.valueOf(id))
                        .setStartUserSelectAssignees(leave.getStartUserSelectAssignees() != null ?
                                parseAssignees(leave.getStartUserSelectAssignees()) : null));

        leaveMapper.updateById(new OaLeaveDO().setId(id)
                .setProcessInstanceId(processInstanceId).setStatus(1));
    }

    @SuppressWarnings("unchecked")
    private Map<String, java.util.List<Long>> parseAssignees(String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateLeaveStatus(Long id, Integer status) {
        validateLeaveExists(id);
        leaveMapper.updateById(new OaLeaveDO().setId(id).setStatus(status));
    }

    @Override
    public void deleteLeave(Long id) {
        validateLeaveExists(id);
        leaveMapper.deleteById(id);
    }

    private void validateLeaveExists(Long id) {
        if (leaveMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_LEAVE_NOT_EXISTS);
        }
    }

    @Override
    public OaLeaveDO getLeave(Long id) {
        return leaveMapper.selectById(id);
    }

    @Override
    public PageResult<OaLeaveDO> getLeavePage(Long userId, OaLeavePageReqVO pageReqVO) {
        return leaveMapper.selectPage(userId, pageReqVO);
    }

}