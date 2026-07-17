package com.meession.etm.module.oa.service.visit;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import jakarta.validation.Valid;

public interface OaVisitService {

    Long createVisit(Long userId, @Valid OaVisitCreateReqVO createReqVO);

    void updateVisitStatus(Long id, Integer status);

    OaVisitDO getVisit(Long id);

    PageResult<OaVisitDO> getVisitPage(Long userId, OaVisitPageReqVO pageReqVO);

}
