package com.meession.etm.module.oa.controller.admin.businessTrip;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.businessTrip.OaBusinessTripConvert;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripPageReqVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaBusinessTripDO;
import com.meession.etm.module.oa.service.businessTrip.OaBusinessTripService;
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

@Tag(name = "管理后台 - OA 出差申请")
@RestController
@RequestMapping("/oa/business-trip")
@Validated
public class OaBusinessTripController {

    @Resource
    private OaBusinessTripService businessTripService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:business-trip:create')")
    @Operation(summary = "创建出差申请")
    public CommonResult<Long> createBusinessTrip(@Valid @RequestBody OaBusinessTripCreateReqVO createReqVO) {
        return success(businessTripService.createBusinessTrip(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:business-trip:update')")
    @Operation(summary = "更新出差申请")
    public CommonResult<Boolean> updateBusinessTrip(@Valid @RequestBody OaBusinessTripCreateReqVO updateReqVO,
                                                    @RequestParam("id") Long id) {
        businessTripService.updateBusinessTrip(id, updateReqVO);
        return success(true);
    }

    @PutMapping("/submit")
    @PreAuthorize("@ss.hasPermission('oa:business-trip:submit')")
    @Operation(summary = "提交出差审批")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> submitBusinessTrip(@RequestParam("id") Long id) {
        businessTripService.submitBusinessTrip(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('oa:business-trip:update')")
    @Operation(summary = "更新出差状态")
    public CommonResult<Boolean> updateBusinessTripStatus(@RequestParam("id") Long id,
                                                          @RequestParam("status") Integer status) {
        businessTripService.updateBusinessTripStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:business-trip:delete')")
    @Operation(summary = "删除出差申请")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteBusinessTrip(@RequestParam("id") Long id) {
        businessTripService.deleteBusinessTrip(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:business-trip:query')")
    @Operation(summary = "获得出差申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaBusinessTripRespVO> getBusinessTrip(@RequestParam("id") Long id) {
        OaBusinessTripDO trip = businessTripService.getBusinessTrip(id);
        return success(OaBusinessTripConvert.convert(trip));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:business-trip:query')")
    @Operation(summary = "获得出差申请分页")
    public CommonResult<PageResult<OaBusinessTripRespVO>> getBusinessTripPage(@Valid OaBusinessTripPageReqVO pageVO) {
        PageResult<OaBusinessTripDO> pageResult = businessTripService.getBusinessTripPage(getLoginUserId(), pageVO);
        return success(OaBusinessTripConvert.convertPage(pageResult));
    }

}