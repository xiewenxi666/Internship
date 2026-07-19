package com.meession.etm.module.oa.controller.admin.visit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 拜访记录 Response VO")
@Data
public class OaVisitRespVO {

    @Schema(description = "拜访表单主键", example = "1024")
    private Long id;

    @Schema(description = "客户名称", example = "密讯科技")
    private String customerName;

    @Schema(description = "联系人", example = "张三")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13800138000")
    private String contactPhone;

    @Schema(description = "拜访地址", example = "上海市浦东新区")
    private String visitAddress;

    @Schema(description = "拜访时间")
    private LocalDateTime visitTime;

    @Schema(description = "拜访目的", example = "产品演示")
    private String purpose;

    @Schema(description = "拜访结果", example = "客户表示感兴趣")
    private String result;

    @Schema(description = "下次拜访时间")
    private LocalDateTime nextVisitTime;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}