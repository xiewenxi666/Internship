package com.meession.etm.module.oa.service.request;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.module.oa.dal.mysql.OaRequestMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REQUEST_CANCEL_FAIL_NOT_RUNNING;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REQUEST_NOT_EXISTS;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REQUEST_RECONSIDER_FAIL_NOT_REJECTED;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REQUEST_SUBMIT_FAIL_NOT_DRAFT;

@Service
@Validated
public class OaRequestServiceImpl implements OaRequestService {

    public static final String PROCESS_KEY = "oa_request";

    @Resource
    private OaRequestMapper requestMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRequest(Long userId, OaRequestCreateReqVO createReqVO) {
        OaRequestDO request = BeanUtils.toBean(createReqVO, OaRequestDO.class)
                .setUserId(userId).setStatus(-1);
        requestMapper.insert(request);
        return request.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRequest(Long id, OaRequestCreateReqVO updateReqVO) {
        validateExists(id);
        OaRequestDO req = BeanUtils.toBean(updateReqVO, OaRequestDO.class).setId(id);
        requestMapper.updateById(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRequest(Long id, Long userId) {
        OaRequestDO req = requestMapper.selectById(id);
        if (req == null) {
            throw ServiceExceptionUtil.exception(OA_REQUEST_NOT_EXISTS);
        }
        if (req.getStatus() != 1) {
            throw ServiceExceptionUtil.exception(OA_REQUEST_CANCEL_FAIL_NOT_RUNNING);
        }
        requestMapper.updateById(new OaRequestDO().setId(id).setStatus(4));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reconsiderRequest(Long id, Long userId) {
        OaRequestDO req = requestMapper.selectById(id);
        if (req == null) {
            throw ServiceExceptionUtil.exception(OA_REQUEST_NOT_EXISTS);
        }
        if (req.getStatus() != 3) {
            throw ServiceExceptionUtil.exception(OA_REQUEST_RECONSIDER_FAIL_NOT_REJECTED);
        }
        submitRequest(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitRequest(Long id, Long userId) {
        OaRequestDO req = requestMapper.selectById(id);
        if (req == null) {
            throw ServiceExceptionUtil.exception(OA_REQUEST_NOT_EXISTS);
        }
        if (req.getStatus() != -1) {
            throw ServiceExceptionUtil.exception(OA_REQUEST_SUBMIT_FAIL_NOT_DRAFT);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("urgency", req.getUrgency());
        if (req.getExpectedAmount() != null) {
            variables.put("expectedAmount", req.getExpectedAmount());
        }

        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(variables).setBusinessKey(String.valueOf(id))
                        .setStartUserSelectAssignees(req.getStartUserSelectAssignees() != null ?
                                parseStartUserSelectAssignees(req.getStartUserSelectAssignees()) : null));

        requestMapper.updateById(new OaRequestDO().setId(id).setProcessInstanceId(processInstanceId).setStatus(1));
    }

    @SuppressWarnings("unchecked")
    private Map<String, java.util.List<Long>> parseStartUserSelectAssignees(String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateRequestStatus(Long id, Integer status) {
        validateExists(id);
        requestMapper.updateById(new OaRequestDO().setId(id).setStatus(status));
    }

    @Override
    public void deleteRequest(Long id) {
        validateExists(id);
        requestMapper.deleteById(id);
    }

    private void validateExists(Long id) {
        if (requestMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_REQUEST_NOT_EXISTS);
        }
    }

    @Override
    public OaRequestDO getRequest(Long id) {
        return requestMapper.selectById(id);
    }

    @Override
    public PageResult<OaRequestDO> getRequestPage(Long userId, OaRequestPageReqVO pageReqVO) {
        return requestMapper.selectPage(userId, pageReqVO);
    }

}