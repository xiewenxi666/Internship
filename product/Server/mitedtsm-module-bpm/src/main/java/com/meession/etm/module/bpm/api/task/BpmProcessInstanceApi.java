package com.meession.etm.module.bpm.api.task;

import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import jakarta.validation.Valid;

/**
 * 流程实例 Api 接口
 *
 * @author 密讯
 */
public interface BpmProcessInstanceApi {

    /**
     * 创建流程实例（提供给内部）
     *
     * @param userId 用户编号
     * @param reqDTO 创建信息
     * @return 实例的编号
     */
    String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO reqDTO);

    /**
     * 校验用户是否有该业务的活跃审批任务
     *
     * @param processDefinitionKey 流程定义标识
     * @param businessKey 业务编号
     * @param userId 用户编号
     * @return 是否有活跃的审批任务
     */
    boolean hasActiveTask(String processDefinitionKey, String businessKey, Long userId);

    /**
     * 通过流程实例 ID 校验用户是否有活跃的审批任务
     *
     * @param processInstanceId 流程实例编号
     * @param userId 用户编号
     * @return 是否有活跃的审批任务
     */
    boolean hasActiveTaskByProcessInstanceId(String processInstanceId, Long userId);

    /**
     * 判断流程实例是否正在运行（是否有活跃任务，不分用户）
     *
     * @param processInstanceId 流程实例编号
     * @return 是否在运行中
     */
    boolean isProcessRunning(String processInstanceId);

    /**
     * 删除流程实例（撤回/取消）
     *
     * @param processInstanceId 流程实例编号
     * @param reason 删除原因
     */
    void deleteProcessInstance(String processInstanceId, String reason);

}
