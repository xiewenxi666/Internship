package com.meession.etm.module.oa.controller.admin.oa.vo.schedule;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 日程分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class OaSchedulePageReqVO extends PageParam {

    @Schema(description = "日程标题，模糊匹配")
    private String title;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "查询开始时间")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "查询结束时间")
    private LocalDateTime endTime;

}
