package com.meession.etm.module.oa.service.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface OaTaskService {

    Long createTask(Long userId, @Valid OaTaskCreateReqVO createReqVO);

    void updateTask(Long id, @Valid OaTaskCreateReqVO updateReqVO);

    void deleteTask(Long id);

    OaTaskDO getTask(Long id);

    PageResult<OaTaskDO> getTaskPage(Long userId, OaTaskPageReqVO pageReqVO);

    List<OaTaskDO> getTaskListByAssignee(Long assigneeUserId, Integer status);

    Map<String, Long> getTaskStatistics(Long userId);

    void updateTaskStatus(Long id, Integer status);

    void updateTaskProgress(Long id, Integer progress);

    void completeTask(Long id);

}