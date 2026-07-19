package com.meession.etm.module.oa.service.request;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import jakarta.validation.Valid;

public interface OaRequestService {

    Long createRequest(Long userId, @Valid OaRequestCreateReqVO createReqVO);

    void updateRequest(Long id, @Valid OaRequestCreateReqVO updateReqVO);

    void updateRequestStatus(Long id, Integer status);

    void deleteRequest(Long id);

    OaRequestDO getRequest(Long id);

    PageResult<OaRequestDO> getRequestPage(Long userId, OaRequestPageReqVO pageReqVO);

    void submitRequest(Long id, Long userId);

    void cancelRequest(Long id, Long userId);

    void reconsiderRequest(Long id, Long userId);

}