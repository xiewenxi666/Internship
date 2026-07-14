package com.meession.etm.module.bpm.api.task;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.service.task.BpmProcessInstanceService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Flowable 流程实例 Api 实现类
 *
 * @author 密讯
 * @author jason
 */
@Service
@Validated
public class BpmProcessInstanceApiImpl implements BpmProcessInstanceApi {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @Resource
    private TaskService taskService;

    @Override
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO reqDTO) {
        return processInstanceService.createProcessInstance(userId, reqDTO);
    }

    @Override
    public boolean hasActiveTask(String processDefinitionKey, String businessKey, Long userId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .processInstanceBusinessKey(businessKey)
                .taskCandidateOrAssigned(String.valueOf(userId))
                .active()
                .list();
        return CollUtil.isNotEmpty(tasks);
    }

    @Override
    public boolean hasActiveTaskByProcessInstanceId(String processInstanceId, Long userId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateOrAssigned(String.valueOf(userId))
                .active()
                .list();
        return CollUtil.isNotEmpty(tasks);
    }

}
