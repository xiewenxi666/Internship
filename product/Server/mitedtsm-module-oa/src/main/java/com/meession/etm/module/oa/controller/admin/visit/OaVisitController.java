package com.meession.etm.module.oa.controller.admin.visit;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.visit.OaVisitConvert;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitPageReqVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import com.meession.etm.module.oa.service.visit.OaVisitService;
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

@Tag(name = "管理后台 - OA 拜访记录")
@RestController
@RequestMapping("/oa/visit")
@Validated
public class OaVisitController {

    @Resource
    private OaVisitService visitService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:visit:create')")
    @Operation(summary = "创建拜访记录")
    public CommonResult<Long> createVisit(@Valid @RequestBody OaVisitCreateReqVO createReqVO) {
        return success(visitService.createVisit(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:visit:update')")
    @Operation(summary = "更新拜访记录")
    public CommonResult<Boolean> updateVisit(@Valid @RequestBody OaVisitCreateReqVO updateReqVO,
                                              @RequestParam("id") Long id) {
        visitService.updateVisit(id, updateReqVO);
        return success(true);
    }

    @PutMapping("/submit")
    @PreAuthorize("@ss.hasPermission('oa:visit:submit')")
    @Operation(summary = "提交拜访审批")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> submitVisit(@RequestParam("id") Long id) {
        visitService.submitVisit(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('oa:visit:update')")
    @Operation(summary = "更新拜访状态")
    public CommonResult<Boolean> updateVisitStatus(@RequestParam("id") Long id,
                                                   @RequestParam("status") Integer status) {
        visitService.updateVisitStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:visit:delete')")
    @Operation(summary = "删除拜访记录")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteVisit(@RequestParam("id") Long id) {
        visitService.deleteVisit(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:visit:query')")
    @Operation(summary = "获得拜访记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaVisitRespVO> getVisit(@RequestParam("id") Long id) {
        OaVisitDO visit = visitService.getVisit(id);
        return success(OaVisitConvert.convert(visit));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:visit:query')")
    @Operation(summary = "获得拜访记录分页")
    public CommonResult<PageResult<OaVisitRespVO>> getVisitPage(@Valid OaVisitPageReqVO pageVO) {
        PageResult<OaVisitDO> pageResult = visitService.getVisitPage(getLoginUserId(), pageVO);
        return success(OaVisitConvert.convertPage(pageResult));
    }

}
