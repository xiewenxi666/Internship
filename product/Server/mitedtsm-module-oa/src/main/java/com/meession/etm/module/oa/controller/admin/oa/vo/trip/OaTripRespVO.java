package com.meession.etm.module.oa.controller.admin.oa.vo.trip;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 出差申请 Response VO")
@Data
public class OaTripRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "出差类型", example = "1")
    private Integer type;

    @Schema(description = "目的地", example = "北京")
    private String destination;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "出差原因", example = "客户拜访")
    private String reason;

    @Schema(description = "流程编号")
    private String processInstanceId;

    @Schema(description = "审批结果", example = "1")
    private Integer status;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}
