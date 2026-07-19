package com.meession.etm.module.oa.controller.admin.leave;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.leave.OaLeaveConvert;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeavePageReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import com.meession.etm.module.oa.service.leave.OaLeaveService;
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

@Tag(name = "管理后台 - OA 请假申请")
@RestController
@RequestMapping("/oa/leave")
@Validated
public class OaLeaveController {

    @Resource
    private OaLeaveService leaveService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:leave:create')")
    @Operation(summary = "创建请假申请")
    public CommonResult<Long> createLeave(@Valid @RequestBody OaLeaveCreateReqVO createReqVO) {
        return success(leaveService.createLeave(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:leave:update')")
    @Operation(summary = "更新请假申请")
    public CommonResult<Boolean> updateLeave(@Valid @RequestBody OaLeaveCreateReqVO updateReqVO,
                                             @RequestParam("id") Long id) {
        leaveService.updateLeave(id, updateReqVO);
        return success(true);
    }

    @PutMapping("/submit")
    @PreAuthorize("@ss.hasPermission('oa:leave:submit')")
    @Operation(summary = "提交请假审批")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> submitLeave(@RequestParam("id") Long id) {
        leaveService.submitLeave(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('oa:leave:update')")
    @Operation(summary = "更新请假状态")
    public CommonResult<Boolean> updateLeaveStatus(@RequestParam("id") Long id,
                                                    @RequestParam("status") Integer status) {
        leaveService.updateLeaveStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:leave:delete')")
    @Operation(summary = "删除请假申请")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteLeave(@RequestParam("id") Long id) {
        leaveService.deleteLeave(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:leave:query')")
    @Operation(summary = "获得请假申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaLeaveRespVO> getLeave(@RequestParam("id") Long id) {
        OaLeaveDO leave = leaveService.getLeave(id);
        return success(OaLeaveConvert.convert(leave));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:leave:query')")
    @Operation(summary = "获得请假申请分页")
    public CommonResult<PageResult<OaLeaveRespVO>> getLeavePage(@Valid OaLeavePageReqVO pageVO) {
        PageResult<OaLeaveDO> pageResult = leaveService.getLeavePage(getLoginUserId(), pageVO);
        return success(OaLeaveConvert.convertPage(pageResult));
    }

}