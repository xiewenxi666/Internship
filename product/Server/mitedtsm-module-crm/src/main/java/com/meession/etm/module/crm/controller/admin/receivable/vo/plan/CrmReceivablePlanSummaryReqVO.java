package com.meession.etm.module.crm.controller.admin.receivable.vo.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - CRM 回款计划汇总统计查询 Request VO")
@Data
public class CrmReceivablePlanSummaryReqVO {

    @Schema(description = "年份", example = "2026")
    private Integer year;

    @Schema(description = "部门编号", example = "1")
    private Long deptId;

    @Schema(description = "负责人编号", example = "1")
    private Long ownerUserId;

}
