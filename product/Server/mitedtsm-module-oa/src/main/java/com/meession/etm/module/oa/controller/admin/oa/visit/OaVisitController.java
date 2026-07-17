package com.meession.etm.module.oa.controller.admin.oa.visit;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitPageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitRespVO;
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

@Tag(name = "管理后台 - OA 拜访申请")
@RestController
@RequestMapping("/oa/visit")
@Validated
public class OaVisitController {

    @Resource
    private OaVisitService visitService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:visit:create')")
    @Operation(summary = "创建拜访申请")
    public CommonResult<Long> createVisit(@Valid @RequestBody OaVisitCreateReqVO createReqVO) {
        return success(visitService.createVisit(getLoginUserId(), createReqVO));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:visit:query')")
    @Operation(summary = "获得拜访申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaVisitRespVO> getVisit(@RequestParam("id") Long id) {
        OaVisitDO visit = visitService.getVisit(id);
        return success(BeanUtils.toBean(visit, OaVisitRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:visit:query')")
    @Operation(summary = "获得拜访申请分页")
    public CommonResult<PageResult<OaVisitRespVO>> getVisitPage(@Valid OaVisitPageReqVO pageVO) {
        PageResult<OaVisitDO> pageResult = visitService.getVisitPage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaVisitRespVO.class));
    }

}
