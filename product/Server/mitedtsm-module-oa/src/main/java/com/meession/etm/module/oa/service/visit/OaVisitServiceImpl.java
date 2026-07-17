package com.meession.etm.module.oa.service.visit;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import com.meession.etm.module.oa.dal.mapper.OaVisitMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_VISIT_NOT_EXISTS;

@Slf4j
@Service
@Validated
public class OaVisitServiceImpl implements OaVisitService {

    public static final String PROCESS_KEY = "oa_visit";

    @Resource
    private OaVisitMapper visitMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createVisit(Long userId, OaVisitCreateReqVO createReqVO) {
        OaVisitDO visit = BeanUtils.toBean(createReqVO, OaVisitDO.class)
                .setUserId(userId).setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        visitMapper.insert(visit);

        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(visit.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees()));

        visitMapper.updateById(new OaVisitDO().setId(visit.getId()).setProcessInstanceId(processInstanceId));
        return visit.getId();
    }

    @Override
    public void updateVisitStatus(Long id, Integer status) {
        validateVisitExists(id);
        visitMapper.updateById(new OaVisitDO().setId(id).setStatus(status));
    }

    private void validateVisitExists(Long id) {
        if (visitMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_VISIT_NOT_EXISTS);
        }
    }

    @Override
    public OaVisitDO getVisit(Long id) {
        return visitMapper.selectById(id);
    }

    @Override
    public PageResult<OaVisitDO> getVisitPage(Long userId, OaVisitPageReqVO pageReqVO) {
        return visitMapper.selectPage(userId, pageReqVO);
    }

}
