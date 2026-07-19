package com.meession.etm.module.oa.controller.admin.workReport.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 工作报告分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OaWorkReportPageReqVO extends PageParam {

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "报告类型", example = "1")
    private Integer type;

}