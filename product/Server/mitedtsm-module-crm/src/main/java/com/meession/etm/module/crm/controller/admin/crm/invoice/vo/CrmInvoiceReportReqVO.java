package com.meession.etm.module.crm.controller.admin.crm.invoice.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 发票记录报表查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmInvoiceReportReqVO extends PageParam {

    @Schema(description = "部门编号", example = "1")
    private Long deptId;

    @Schema(description = "订单所属人员", example = "1")
    private Long ownerUserId;

    @Schema(description = "年份", example = "2026")
    private Integer year;

}
