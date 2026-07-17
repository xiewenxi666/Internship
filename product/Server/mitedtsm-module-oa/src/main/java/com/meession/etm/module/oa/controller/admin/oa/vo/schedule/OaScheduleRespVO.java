package com.meession.etm.module.oa.controller.admin.oa.vo.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 日程 Response VO")
@Data
public class OaScheduleRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "日程标题", example = "项目评审会议")
    private String title;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "是否全天", example = "false")
    private Boolean allDay;

    @Schema(description = "地点", example = "3楼会议室")
    private String location;

    @Schema(description = "备注", example = "请提前准备材料")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
