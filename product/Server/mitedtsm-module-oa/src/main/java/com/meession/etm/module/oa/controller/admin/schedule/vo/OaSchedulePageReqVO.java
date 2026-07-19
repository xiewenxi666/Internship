package com.meession.etm.module.oa.controller.admin.schedule.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 日程分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OaSchedulePageReqVO extends PageParam {

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "日程类型", example = "1")
    private Integer type;

}