package com.meession.etm.module.oa.service.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;

public interface OaTaskService {

    Long createTask(Long userId, OaTaskCreateReqVO createReqVO);

    void updateTask(OaTaskDO task);

    void deleteTask(Long id);

    OaTaskDO getTask(Long id);

    PageResult<OaTaskDO> getTaskPage(Long userId, OaTaskPageReqVO pageReqVO);

    void completeTask(Long id);

}
