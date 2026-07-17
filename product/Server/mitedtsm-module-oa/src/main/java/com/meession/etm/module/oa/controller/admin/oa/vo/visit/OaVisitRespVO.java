package com.meession.etm.module.oa.controller.admin.oa.vo.visit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 拜访申请 Response VO")
@Data
public class OaVisitRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "联系人", example = "张三")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13800138000")
    private String contactPhone;

    @Schema(description = "拜访时间")
    private LocalDateTime visitTime;

    @Schema(description = "拜访地点", example = "北京市朝阳区")
    private String location;

    @Schema(description = "拜访目的", example = "商务洽谈")
    private String purpose;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "流程编号")
    private String processInstanceId;

    @Schema(description = "审批结果", example = "1")
    private Integer status;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}
