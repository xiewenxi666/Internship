package com.meession.etm.module.crm.controller.admin.statistics.vo.deal;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - CRM 成交商机分页 Request VO")
@Data
public class CrmDealPageReqVO extends PageParam {

    @Schema(description = "部门 id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long deptId;

    @Schema(description = "负责人用户 id", example = "1024")
    private Long userId;

    @Schema(description = "年份", example = "2025")
    private Integer year;

}
