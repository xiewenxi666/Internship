package com.meession.etm.module.crm.controller.admin.crm.invoice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 发票新增/修改 Request VO")
@Data
public class CrmInvoiceSaveReqVO {

    @Schema(description = "编号", example = "25787")
    private Long id;

    @Schema(description = "关联订单ID", example = "1024")
    private Long orderId;

    @Schema(description = "关联订单编号", example = "DD202401010001")
    private String orderNo;

    @Schema(description = "关联订单名称", example = "XX项目订单")
    private String orderName;

    @Schema(description = "开票日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @NotNull(message = "开票日期不能为空")
    private LocalDateTime invoiceDate;

    @Schema(description = "票据类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "票据类型不能为空")
    private Integer type;

    @Schema(description = "开票金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @NotNull(message = "开票金额不能为空")
    private BigDecimal price;

    @Schema(description = "税务发票号码", example = "1234567890")
    private String invoiceNo;

    @Schema(description = "票据内容", example = "软件开发服务费")
    private String content;

    @Schema(description = "订单所属人员", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "订单所属人员不能为空")
    private Long ownerUserId;

    @Schema(description = "发票经手人员", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "发票经手人员不能为空")
    private Long handlerUserId;

    @Schema(description = "备注", example = "备注")
    private String remark;

}
