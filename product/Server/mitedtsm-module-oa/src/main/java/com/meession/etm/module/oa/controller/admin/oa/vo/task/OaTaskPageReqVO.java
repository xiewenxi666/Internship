package com.meession.etm.module.oa.controller.admin.oa.vo.task;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class OaTaskPageReqVO extends PageParam {

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "负责人ID")
    private Long assigneeId;

    @Schema(description = "任务标题，模糊匹配")
    private String title;

}
