package com.meession.etm.module.oa.service.businessTrip;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaBusinessTripDO;
import jakarta.validation.Valid;

public interface OaBusinessTripService {

    Long createBusinessTrip(Long userId, @Valid OaBusinessTripCreateReqVO createReqVO);

    void updateBusinessTrip(Long id, @Valid OaBusinessTripCreateReqVO updateReqVO);

    void updateBusinessTripStatus(Long id, Integer status);

    void deleteBusinessTrip(Long id);

    OaBusinessTripDO getBusinessTrip(Long id);

    PageResult<OaBusinessTripDO> getBusinessTripPage(Long userId, OaBusinessTripPageReqVO pageReqVO);

    void submitBusinessTrip(Long id, Long userId);

}