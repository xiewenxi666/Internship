package com.meession.etm.module.oa.controller.admin.businessTrip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 出差申请 Response VO")
@Data
public class OaBusinessTripRespVO {

    @Schema(description = "出差表单主键", example = "1024")
    private Long id;

    @Schema(description = "目的地", example = "上海")
    private String destination;

    @Schema(description = "出差事由", example = "客户拜访")
    private String reason;

    @Schema(description = "同行人员", example = "张三,李四")
    private String companion;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "出差天数", example = "3.5")
    private BigDecimal day;

    @Schema(description = "交通工具", example = "高铁")
    private String vehicle;

    @Schema(description = "预估费用", example = "5000.00")
    private BigDecimal estimatedAmount;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "流程编号")
    private String processInstanceId;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}