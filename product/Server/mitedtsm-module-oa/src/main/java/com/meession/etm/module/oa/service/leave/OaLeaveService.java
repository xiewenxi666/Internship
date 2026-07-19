package com.meession.etm.module.oa.service.leave;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeavePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import jakarta.validation.Valid;

public interface OaLeaveService {

    Long createLeave(Long userId, @Valid OaLeaveCreateReqVO createReqVO);

    void updateLeave(Long id, @Valid OaLeaveCreateReqVO updateReqVO);

    void updateLeaveStatus(Long id, Integer status);

    void deleteLeave(Long id);

    OaLeaveDO getLeave(Long id);

    PageResult<OaLeaveDO> getLeavePage(Long userId, OaLeavePageReqVO pageReqVO);

    void submitLeave(Long id, Long userId);

}