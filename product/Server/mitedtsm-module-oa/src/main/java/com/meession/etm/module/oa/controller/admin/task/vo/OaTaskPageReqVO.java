package com.meession.etm.module.oa.controller.admin.task.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OaTaskPageReqVO extends PageParam {

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "优先级", example = "1")
    private Integer priority;

    @Schema(description = "标题", example = "OA")
    private String title;

    @Schema(description = "执行人用户编号", example = "2")
    private Long assigneeUserId;

}