package com.meession.etm.module.oa.service.visit;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import com.meession.etm.module.oa.dal.mysql.OaVisitMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_VISIT_NOT_EXISTS;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_VISIT_SUBMIT_FAIL_NOT_DRAFT;

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
                .setUserId(userId).setStatus(-1);
        visitMapper.insert(visit);
        return visit.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVisit(Long id, OaVisitCreateReqVO updateReqVO) {
        validateExists(id);
        OaVisitDO visit = BeanUtils.toBean(updateReqVO, OaVisitDO.class).setId(id);
        visitMapper.updateById(visit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitVisit(Long id, Long userId) {
        OaVisitDO visit = visitMapper.selectById(id);
        if (visit == null) {
            throw ServiceExceptionUtil.exception(OA_VISIT_NOT_EXISTS);
        }
        if (visit.getStatus() != -1) {
            throw ServiceExceptionUtil.exception(OA_VISIT_SUBMIT_FAIL_NOT_DRAFT);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", visit.getCustomerName());
        variables.put("purpose", visit.getPurpose());

        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(variables).setBusinessKey(String.valueOf(id))
                        .setStartUserSelectAssignees(visit.getStartUserSelectAssignees() != null ?
                                parseAssignees(visit.getStartUserSelectAssignees()) : null));

        visitMapper.updateById(new OaVisitDO().setId(id)
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
    public void updateVisitStatus(Long id, Integer status) {
        validateExists(id);
        visitMapper.updateById(new OaVisitDO().setId(id).setStatus(status));
    }

    @Override
    public void deleteVisit(Long id) {
        validateExists(id);
        visitMapper.deleteById(id);
    }

    private void validateExists(Long id) {
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
