package com.meession.etm.module.oa.controller.admin.oa.report;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportPageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaReportDO;
import com.meession.etm.module.oa.service.report.OaReportService;
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
@RequestMapping("/oa/report")
@Validated
public class OaReportController {

    @Resource
    private OaReportService reportService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:report:create')")
    @Operation(summary = "创建工作报告")
    public CommonResult<Long> createReport(@Valid @RequestBody OaReportCreateReqVO createReqVO) {
        return success(reportService.createReport(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:report:update')")
    @Operation(summary = "更新工作报告")
    public CommonResult<Boolean> update(@Valid @RequestBody OaReportCreateReqVO updateReqVO) {
        reportService.updateReport(BeanUtils.toBean(updateReqVO, OaReportDO.class));
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:report:query')")
    @Operation(summary = "获得工作报告")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaReportRespVO> getReport(@RequestParam("id") Long id) {
        OaReportDO report = reportService.getReport(id);
        return success(BeanUtils.toBean(report, OaReportRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:report:query')")
    @Operation(summary = "获得工作报告分页")
    public CommonResult<PageResult<OaReportRespVO>> getReportPage(@Valid OaReportPageReqVO pageVO) {
        PageResult<OaReportDO> pageResult = reportService.getReportPage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaReportRespVO.class));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:report:delete')")
    @Operation(summary = "删除工作报告")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteReport(@RequestParam("id") Long id) {
        reportService.deleteReport(id);
        return success(true);
    }

}
