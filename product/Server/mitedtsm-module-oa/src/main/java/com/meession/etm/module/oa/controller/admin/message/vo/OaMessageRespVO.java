package com.meession.etm.module.oa.controller.admin.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 内部消息 Response VO")
@Data
public class OaMessageRespVO {

    @Schema(description = "消息主键", example = "1024")
    private Long id;

    @Schema(description = "发送人用户编号", example = "1")
    private Long senderUserId;

    @Schema(description = "接收人用户编号", example = "1024")
    private Long receiverUserId;

    @Schema(description = "标题", example = "会议通知")
    private String title;

    @Schema(description = "内容", example = "明日10点会议室A开会")
    private String content;

    @Schema(description = "阅读状态", example = "0")
    private Integer readStatus;

    @Schema(description = "阅读时间")
    private LocalDateTime readTime;

    @Schema(description = "发送时间")
    private LocalDateTime createTime;

}