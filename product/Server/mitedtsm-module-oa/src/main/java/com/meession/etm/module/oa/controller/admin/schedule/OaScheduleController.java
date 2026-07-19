package com.meession.etm.module.oa.controller.admin.schedule;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.schedule.OaScheduleConvert;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaSchedulePageReqVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.service.schedule.OaScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - OA 日程管理")
@RestController
@RequestMapping("/oa/schedule")
@Validated
public class OaScheduleController {

    @Resource
    private OaScheduleService scheduleService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:schedule:create')")
    @Operation(summary = "创建日程")
    public CommonResult<Long> createSchedule(@Valid @RequestBody OaScheduleCreateReqVO createReqVO) {
        return success(scheduleService.createSchedule(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:schedule:update')")
    @Operation(summary = "更新日程")
    public CommonResult<Boolean> updateSchedule(@Valid @RequestBody OaScheduleCreateReqVO updateReqVO,
                                                @RequestParam("id") Long id) {
        scheduleService.updateSchedule(id, updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:schedule:delete')")
    @Operation(summary = "删除日程")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteSchedule(@RequestParam("id") Long id) {
        scheduleService.deleteSchedule(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:schedule:query')")
    @Operation(summary = "获得日程")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaScheduleRespVO> getSchedule(@RequestParam("id") Long id) {
        OaScheduleDO schedule = scheduleService.getSchedule(id);
        return success(OaScheduleConvert.convert(schedule));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:schedule:query')")
    @Operation(summary = "获得日程分页")
    public CommonResult<PageResult<OaScheduleRespVO>> getSchedulePage(@Valid OaSchedulePageReqVO pageVO) {
        PageResult<OaScheduleDO> pageResult = scheduleService.getSchedulePage(getLoginUserId(), pageVO);
        return success(OaScheduleConvert.convertPage(pageResult));
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('oa:schedule:update')")
    @Operation(summary = "更新日程状态")
    public CommonResult<Boolean> updateScheduleStatus(@RequestParam("id") Long id,
                                                       @RequestParam("status") Integer status) {
        scheduleService.updateScheduleStatus(id, status);
        return success(true);
    }

    @GetMapping("/calendar")
    @PreAuthorize("@ss.hasPermission('oa:schedule:query')")
    @Operation(summary = "获取日程日历事件列表")
    @Parameter(name = "startTime", description = "开始时间", required = true)
    @Parameter(name = "endTime", description = "结束时间", required = true)
    @Parameter(name = "type", description = "日程类型")
    public CommonResult<List<OaScheduleRespVO>> getScheduleCalendarList(
            @RequestParam("startTime") @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime endTime,
            @RequestParam(value = "type", required = false) Integer type) {
        List<OaScheduleDO> list = scheduleService.getScheduleCalendarList(getLoginUserId(), startTime, endTime, type);
        return success(OaScheduleConvert.convertList(list));
    }

}