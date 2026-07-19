package com.meession.etm.module.oa.service.visit;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import jakarta.validation.Valid;

public interface OaVisitService {

    Long createVisit(Long userId, @Valid OaVisitCreateReqVO createReqVO);

    void updateVisit(Long id, @Valid OaVisitCreateReqVO updateReqVO);

    void updateVisitStatus(Long id, Integer status);

    void submitVisit(Long id, Long userId);

    void deleteVisit(Long id);

    OaVisitDO getVisit(Long id);

    PageResult<OaVisitDO> getVisitPage(Long userId, OaVisitPageReqVO pageReqVO);

}
