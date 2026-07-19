package com.meession.etm.module.crm.controller.admin.expense.vo.expense;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 费用分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmExpensePageReqVO extends PageParam {

    @Schema(description = "费用编号")
    private String no;

    @Schema(description = "费用类型", example = "1")
    private Integer type;

    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    @Schema(description = "申请日期开始", example = "2024-01-01 00:00:00")
    private LocalDateTime applyDateStart;

    @Schema(description = "申请日期结束", example = "2024-12-31 23:59:59")
    private LocalDateTime applyDateEnd;

    @Schema(description = "负责人", example = "1")
    private Long ownerUserId;

    @Schema(description = "场景类型", example = "1")
    private Integer sceneType;

}
