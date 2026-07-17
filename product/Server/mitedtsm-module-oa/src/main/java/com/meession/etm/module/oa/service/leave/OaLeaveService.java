package com.meession.etm.module.oa.service.leave;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.leave.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.leave.OaLeavePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import jakarta.validation.Valid;

public interface OaLeaveService {

    Long createLeave(Long userId, @Valid OaLeaveCreateReqVO createReqVO);

    void updateLeaveStatus(Long id, Integer status);

    OaLeaveDO getLeave(Long id);

    PageResult<OaLeaveDO> getLeavePage(Long userId, OaLeavePageReqVO pageReqVO);

}
