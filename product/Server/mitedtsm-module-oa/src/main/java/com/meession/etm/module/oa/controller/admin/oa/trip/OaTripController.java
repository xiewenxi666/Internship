package com.meession.etm.module.oa.controller.admin.oa.trip;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripPageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaTripDO;
import com.meession.etm.module.oa.service.trip.OaTripService;
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
@RequestMapping("/oa/trip")
@Validated
public class OaTripController {

    @Resource
    private OaTripService tripService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:trip:create')")
    @Operation(summary = "创建出差申请")
    public CommonResult<Long> createTrip(@Valid @RequestBody OaTripCreateReqVO createReqVO) {
        return success(tripService.createTrip(getLoginUserId(), createReqVO));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:trip:query')")
    @Operation(summary = "获得出差申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaTripRespVO> getTrip(@RequestParam("id") Long id) {
        OaTripDO trip = tripService.getTrip(id);
        return success(BeanUtils.toBean(trip, OaTripRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:trip:query')")
    @Operation(summary = "获得出差申请分页")
    public CommonResult<PageResult<OaTripRespVO>> getTripPage(@Valid OaTripPageReqVO pageVO) {
        PageResult<OaTripDO> pageResult = tripService.getTripPage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaTripRespVO.class));
    }

}
