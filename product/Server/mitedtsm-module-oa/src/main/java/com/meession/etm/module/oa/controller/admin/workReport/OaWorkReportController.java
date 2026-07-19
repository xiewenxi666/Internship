package com.meession.etm.module.oa.controller.admin.workReport;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.workReport.OaWorkReportConvert;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportPageReqVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaWorkReportDO;
import com.meession.etm.module.oa.service.workReport.OaWorkReportService;
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

@Tag(name = "管理后台 - OA 工作报告")
@RestController
@RequestMapping("/oa/work-report")
@Validated
public class OaWorkReportController {

    @Resource
    private OaWorkReportService workReportService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:work-report:create')")
    @Operation(summary = "创建工作报告")
    public CommonResult<Long> createWorkReport(@Valid @RequestBody OaWorkReportCreateReqVO createReqVO) {
        return success(workReportService.createWorkReport(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:work-report:update')")
    @Operation(summary = "更新工作报告")
    public CommonResult<Boolean> updateWorkReport(@Valid @RequestBody OaWorkReportCreateReqVO updateReqVO,
                                                  @RequestParam("id") Long id) {
        workReportService.updateWorkReport(id, updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:work-report:delete')")
    @Operation(summary = "删除工作报告")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteWorkReport(@RequestParam("id") Long id) {
        workReportService.deleteWorkReport(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:work-report:query')")
    @Operation(summary = "获得工作报告")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaWorkReportRespVO> getWorkReport(@RequestParam("id") Long id) {
        OaWorkReportDO report = workReportService.getWorkReport(id);
        return success(OaWorkReportConvert.convert(report));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:work-report:query')")
    @Operation(summary = "获得工作报告分页")
    public CommonResult<PageResult<OaWorkReportRespVO>> getWorkReportPage(@Valid OaWorkReportPageReqVO pageVO) {
        PageResult<OaWorkReportDO> pageResult = workReportService.getWorkReportPage(getLoginUserId(), pageVO);
        return success(OaWorkReportConvert.convertPage(pageResult));
    }

    @PutMapping("/submit")
    @PreAuthorize("@ss.hasPermission('oa:work-report:update')")
    @Operation(summary = "提交工作报告")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> submitWorkReport(@RequestParam("id") Long id) {
        workReportService.submitWorkReport(id);
        return success(true);
    }

    @PutMapping("/review")
    @PreAuthorize("@ss.hasPermission('oa:work-report:review')")
    @Operation(summary = "审阅工作报告")
    public CommonResult<Boolean> reviewWorkReport(@RequestParam("id") Long id,
                                                   @RequestParam("reviewContent") String reviewContent) {
        workReportService.reviewWorkReport(id, getLoginUserId(), reviewContent);
        return success(true);
    }

}