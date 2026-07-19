package com.meession.etm.module.oa.controller.admin.message.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 内部消息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OaMessagePageReqVO extends PageParam {

    @Schema(description = "阅读状态", example = "0")
    private Integer readStatus;

}