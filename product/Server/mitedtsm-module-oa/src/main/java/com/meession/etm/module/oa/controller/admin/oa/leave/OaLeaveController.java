package com.meession.etm.module.oa.controller.admin.oa.leave;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.leave.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.leave.OaLeavePageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.leave.OaLeaveRespVO;
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

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:leave:query')")
    @Operation(summary = "获得请假申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaLeaveRespVO> getLeave(@RequestParam("id") Long id) {
        OaLeaveDO leave = leaveService.getLeave(id);
        return success(BeanUtils.toBean(leave, OaLeaveRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:leave:query')")
    @Operation(summary = "获得请假申请分页")
    public CommonResult<PageResult<OaLeaveRespVO>> getLeavePage(@Valid OaLeavePageReqVO pageVO) {
        PageResult<OaLeaveDO> pageResult = leaveService.getLeavePage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaLeaveRespVO.class));
    }

}
