package com.meession.etm.module.oa.service.request;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.module.oa.dal.mapper.OaRequestMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REQUEST_NOT_EXISTS;

@Slf4j
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
                .setUserId(userId).setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        requestMapper.insert(request);

        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(request.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees()));

        requestMapper.updateById(new OaRequestDO().setId(request.getId()).setProcessInstanceId(processInstanceId));
        return request.getId();
    }

    @Override
    public void updateRequestStatus(Long id, Integer status) {
        validateRequestExists(id);
        requestMapper.updateById(new OaRequestDO().setId(id).setStatus(status));
    }

    private void validateRequestExists(Long id) {
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
