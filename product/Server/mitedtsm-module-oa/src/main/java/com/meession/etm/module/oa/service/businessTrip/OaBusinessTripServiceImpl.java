package com.meession.etm.module.oa.service.businessTrip;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaBusinessTripDO;
import com.meession.etm.module.oa.dal.mysql.OaBusinessTripMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_BUSINESS_TRIP_NOT_EXISTS;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_BUSINESS_TRIP_SUBMIT_FAIL_NOT_DRAFT;

@Service
@Validated
public class OaBusinessTripServiceImpl implements OaBusinessTripService {

    public static final String PROCESS_KEY = "oa_business_trip";

    @Resource
    private OaBusinessTripMapper businessTripMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBusinessTrip(Long userId, OaBusinessTripCreateReqVO createReqVO) {
        long days = LocalDateTimeUtil.between(createReqVO.getStartTime(), createReqVO.getEndTime()).toDays();
        OaBusinessTripDO trip = BeanUtils.toBean(createReqVO, OaBusinessTripDO.class)
                .setUserId(userId).setDay(BigDecimal.valueOf(days)).setStatus(-1);
        businessTripMapper.insert(trip);
        return trip.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBusinessTrip(Long id, OaBusinessTripCreateReqVO updateReqVO) {
        validateExists(id);
        OaBusinessTripDO trip = BeanUtils.toBean(updateReqVO, OaBusinessTripDO.class).setId(id);
        businessTripMapper.updateById(trip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitBusinessTrip(Long id, Long userId) {
        OaBusinessTripDO trip = businessTripMapper.selectById(id);
        if (trip == null) {
            throw ServiceExceptionUtil.exception(OA_BUSINESS_TRIP_NOT_EXISTS);
        }
        if (trip.getStatus() != -1) {
            throw ServiceExceptionUtil.exception(OA_BUSINESS_TRIP_SUBMIT_FAIL_NOT_DRAFT);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("day", trip.getDay());
        variables.put("destination", trip.getDestination());
        if (trip.getEstimatedAmount() != null) {
            variables.put("estimatedAmount", trip.getEstimatedAmount());
        }

        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(variables).setBusinessKey(String.valueOf(id))
                        .setStartUserSelectAssignees(trip.getStartUserSelectAssignees() != null ?
                                parseAssignees(trip.getStartUserSelectAssignees()) : null));

        businessTripMapper.updateById(new OaBusinessTripDO().setId(id)
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
    public void updateBusinessTripStatus(Long id, Integer status) {
        validateExists(id);
        businessTripMapper.updateById(new OaBusinessTripDO().setId(id).setStatus(status));
    }

    @Override
    public void deleteBusinessTrip(Long id) {
        validateExists(id);
        businessTripMapper.deleteById(id);
    }

    private void validateExists(Long id) {
        if (businessTripMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_BUSINESS_TRIP_NOT_EXISTS);
        }
    }

    @Override
    public OaBusinessTripDO getBusinessTrip(Long id) {
        return businessTripMapper.selectById(id);
    }

    @Override
    public PageResult<OaBusinessTripDO> getBusinessTripPage(Long userId, OaBusinessTripPageReqVO pageReqVO) {
        return businessTripMapper.selectPage(userId, pageReqVO);
    }

}