package com.meession.etm.module.oa.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 内部消息发送 Request VO")
@Data
public class OaMessageCreateReqVO {

    @Schema(description = "接收人用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "接收人不能为空")
    private Long receiverUserId;

    @Schema(description = "消息标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "会议通知")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "消息内容", example = "明日10点会议室A开会")
    private String content;

}