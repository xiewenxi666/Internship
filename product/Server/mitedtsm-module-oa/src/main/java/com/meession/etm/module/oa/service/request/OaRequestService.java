package com.meession.etm.module.oa.service.request;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import jakarta.validation.Valid;

public interface OaRequestService {

    Long createRequest(Long userId, @Valid OaRequestCreateReqVO createReqVO);

    void updateRequestStatus(Long id, Integer status);

    OaRequestDO getRequest(Long id);

    PageResult<OaRequestDO> getRequestPage(Long userId, OaRequestPageReqVO pageReqVO);

}
