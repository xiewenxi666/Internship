package com.meession.etm.module.oa.service.trip;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTripDO;
import jakarta.validation.Valid;

public interface OaTripService {

    Long createTrip(Long userId, @Valid OaTripCreateReqVO createReqVO);

    void updateTripStatus(Long id, Integer status);

    OaTripDO getTrip(Long id);

    PageResult<OaTripDO> getTripPage(Long userId, OaTripPageReqVO pageReqVO);

}
