package com.meession.etm.module.oa.controller.admin.oa.request;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestPageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.module.oa.service.request.OaRequestService;
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

@Tag(name = "管理后台 - OA 请示申请")
@RestController
@RequestMapping("/oa/request")
@Validated
public class OaRequestController {

    @Resource
    private OaRequestService requestService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:request:create')")
    @Operation(summary = "创建请示申请")
    public CommonResult<Long> createRequest(@Valid @RequestBody OaRequestCreateReqVO createReqVO) {
        return success(requestService.createRequest(getLoginUserId(), createReqVO));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:request:query')")
    @Operation(summary = "获得请示申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaRequestRespVO> getRequest(@RequestParam("id") Long id) {
        OaRequestDO request = requestService.getRequest(id);
        return success(BeanUtils.toBean(request, OaRequestRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:request:query')")
    @Operation(summary = "获得请示申请分页")
    public CommonResult<PageResult<OaRequestRespVO>> getRequestPage(@Valid OaRequestPageReqVO pageVO) {
        PageResult<OaRequestDO> pageResult = requestService.getRequestPage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaRequestRespVO.class));
    }

}
