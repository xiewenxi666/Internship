package com.meession.etm.module.oa.controller.admin.request;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.request.OaRequestConvert;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestPageReqVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestRespVO;
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

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:request:update')")
    @Operation(summary = "更新请示申请")
    public CommonResult<Boolean> updateRequest(@Valid @RequestBody OaRequestCreateReqVO updateReqVO,
                                               @RequestParam("id") Long id) {
        requestService.updateRequest(id, updateReqVO);
        return success(true);
    }

    @PutMapping("/submit")
    @PreAuthorize("@ss.hasPermission('oa:request:submit')")
    @Operation(summary = "提交请示审批")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> submitRequest(@RequestParam("id") Long id) {
        requestService.submitRequest(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/cancel")
    @PreAuthorize("@ss.hasPermission('oa:request:cancel')")
    @Operation(summary = "取消请示申请")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> cancelRequest(@RequestParam("id") Long id) {
        requestService.cancelRequest(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/reconsider")
    @PreAuthorize("@ss.hasPermission('oa:request:submit')")
    @Operation(summary = "重新提交被驳回的请示")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> reconsiderRequest(@RequestParam("id") Long id) {
        requestService.reconsiderRequest(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('oa:request:update')")
    @Operation(summary = "更新请示状态")
    public CommonResult<Boolean> updateRequestStatus(@RequestParam("id") Long id,
                                                     @RequestParam("status") Integer status) {
        requestService.updateRequestStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:request:delete')")
    @Operation(summary = "删除请示申请")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteRequest(@RequestParam("id") Long id) {
        requestService.deleteRequest(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:request:query')")
    @Operation(summary = "获得请示申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaRequestRespVO> getRequest(@RequestParam("id") Long id) {
        OaRequestDO req = requestService.getRequest(id);
        return success(OaRequestConvert.convert(req));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:request:query')")
    @Operation(summary = "获得请示申请分页")
    public CommonResult<PageResult<OaRequestRespVO>> getRequestPage(@Valid OaRequestPageReqVO pageVO) {
        PageResult<OaRequestDO> pageResult = requestService.getRequestPage(getLoginUserId(), pageVO);
        return success(OaRequestConvert.convertPage(pageResult));
    }

}