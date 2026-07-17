package com.meession.etm.module.oa.controller.admin.oa.task;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskPageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskRespVO;
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

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - OA 任务")
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
    public CommonResult<Boolean> update(@Valid @RequestBody OaTaskCreateReqVO updateReqVO) {
        taskService.updateTask(BeanUtils.toBean(updateReqVO, OaTaskDO.class));
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:task:query')")
    @Operation(summary = "获得任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaTaskRespVO> getTask(@RequestParam("id") Long id) {
        OaTaskDO task = taskService.getTask(id);
        return success(BeanUtils.toBean(task, OaTaskRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:task:query')")
    @Operation(summary = "获得任务分页")
    public CommonResult<PageResult<OaTaskRespVO>> getTaskPage(@Valid OaTaskPageReqVO pageVO) {
        PageResult<OaTaskDO> pageResult = taskService.getTaskPage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaTaskRespVO.class));
    }

    @PutMapping("/complete")
    @PreAuthorize("@ss.hasPermission('oa:task:update')")
    @Operation(summary = "完成任务")
    public CommonResult<Boolean> complete(@RequestParam("id") Long id) {
        taskService.completeTask(id);
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

}
