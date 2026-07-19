package com.meession.etm.module.oa.controller.admin.task;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.task.OaTaskConvert;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskPageReqVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.module.oa.service.task.OaTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - OA 任务管理")
@RestController
@RequestMapping("/oa/task")
@Validated
public class OaTaskController {

    @Resource
    private OaTaskService taskService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:task:create')")
    @Operation(summary = "创建任务")
    public CommonResult<Long> createTask(@Valid @RequestBody OaTaskCreateReqVO createReqVO) {
        return success(taskService.createTask(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:task:update')")
    @Operation(summary = "更新任务")
    public CommonResult<Boolean> updateTask(@Valid @RequestBody OaTaskCreateReqVO updateReqVO,
                                            @RequestParam("id") Long id) {
        taskService.updateTask(id, updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:task:delete')")
    @Operation(summary = "删除任务")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteTask(@RequestParam("id") Long id) {
        taskService.deleteTask(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:task:query')")
    @Operation(summary = "获得任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaTaskRespVO> getTask(@RequestParam("id") Long id) {
        OaTaskDO task = taskService.getTask(id);
        return success(OaTaskConvert.convert(task));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:task:query')")
    @Operation(summary = "获得任务分页")
    public CommonResult<PageResult<OaTaskRespVO>> getTaskPage(@Valid OaTaskPageReqVO pageVO) {
        PageResult<OaTaskDO> pageResult = taskService.getTaskPage(getLoginUserId(), pageVO);
        return success(OaTaskConvert.convertPage(pageResult));
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('oa:task:update')")
    @Operation(summary = "更新任务状态")
    public CommonResult<Boolean> updateTaskStatus(@RequestParam("id") Long id,
                                                   @RequestParam("status") Integer status) {
        taskService.updateTaskStatus(id, status);
        return success(true);
    }

    @PutMapping("/update-progress")
    @PreAuthorize("@ss.hasPermission('oa:task:update')")
    @Operation(summary = "更新任务进度")
    public CommonResult<Boolean> updateTaskProgress(@RequestParam("id") Long id,
                                                     @RequestParam("progress") Integer progress) {
        taskService.updateTaskProgress(id, progress);
        return success(true);
    }

    @PutMapping("/complete")
    @PreAuthorize("@ss.hasPermission('oa:task:complete')")
    @Operation(summary = "完成任务")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> completeTask(@RequestParam("id") Long id) {
        taskService.completeTask(id);
        return success(true);
    }

    @GetMapping("/list-by-assignee")
    @PreAuthorize("@ss.hasPermission('oa:task:query')")
    @Operation(summary = "获取指派人任务列表")
    @Parameter(name = "assigneeUserId", description = "执行人编号", required = true)
    @Parameter(name = "status", description = "任务状态")
    public CommonResult<List<OaTaskRespVO>> getTaskListByAssignee(@RequestParam("assigneeUserId") Long assigneeUserId,
                                                                   @RequestParam(value = "status", required = false) Integer status) {
        List<OaTaskDO> list = taskService.getTaskListByAssignee(assigneeUserId, status);
        return success(OaTaskConvert.convertList(list));
    }

    @GetMapping("/statistics")
    @PreAuthorize("@ss.hasPermission('oa:task:query')")
    @Operation(summary = "获取任务统计信息")
    public CommonResult<Map<String, Long>> getTaskStatistics() {
        return success(taskService.getTaskStatistics(getLoginUserId()));
    }

}