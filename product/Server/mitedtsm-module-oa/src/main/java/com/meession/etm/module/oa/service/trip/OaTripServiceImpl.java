package com.meession.etm.module.oa.service.trip;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTripDO;
import com.meession.etm.module.oa.dal.mapper.OaTripMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_TRIP_NOT_EXISTS;

@Slf4j
@Service
@Validated
public class OaTripServiceImpl implements OaTripService {

    public static final String PROCESS_KEY = "oa_trip";

    @Resource
    private OaTripMapper tripMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTrip(Long userId, OaTripCreateReqVO createReqVO) {
        long day = LocalDateTimeUtil.between(createReqVO.getStartTime(), createReqVO.getEndTime()).toDays();
        OaTripDO trip = BeanUtils.toBean(createReqVO, OaTripDO.class)
                .setUserId(userId).setDay(day).setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        tripMapper.insert(trip);

        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("day", day);
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(trip.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees()));

        tripMapper.updateById(new OaTripDO().setId(trip.getId()).setProcessInstanceId(processInstanceId));
        return trip.getId();
    }

    @Override
    public void updateTripStatus(Long id, Integer status) {
        validateTripExists(id);
        tripMapper.updateById(new OaTripDO().setId(id).setStatus(status));
    }

    private void validateTripExists(Long id) {
        if (tripMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_TRIP_NOT_EXISTS);
        }
    }

    @Override
    public OaTripDO getTrip(Long id) {
        return tripMapper.selectById(id);
    }

    @Override
    public PageResult<OaTripDO> getTripPage(Long userId, OaTripPageReqVO pageReqVO) {
        return tripMapper.selectPage(userId, pageReqVO);
    }

}
