package com.meession.etm.module.crm.controller.admin.refund.vo.refund;

import com.meession.etm.module.crm.framework.operatelog.core.*;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 退款新增/修改 Request VO")
@Data
public class CrmRefundSaveReqVO {

    @Schema(description = "编号", example = "25787")
    private Long id;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @DiffLogField(name = "负责人", function = SysAdminUserParseFunction.NAME)
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "客户编号", example = "2")
    @DiffLogField(name = "客户", function = CrmCustomerParseFunction.NAME)
    private Long customerId;

    @Schema(description = "关联订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @DiffLogField(name = "关联订单", function = CrmOrderParseFunction.NAME)
    @NotNull(message = "关联订单不能为空")
    private Long orderId;

    @Schema(description = "退款内容/原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "产品缺陷退款")
    @DiffLogField(name = "退款内容")
    @NotNull(message = "退款内容不能为空")
    private String content;

    @Schema(description = "退款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @DiffLogField(name = "退款金额")
    @NotNull(message = "退款金额不能为空")
    private BigDecimal price;

    @Schema(description = "退款类型", example = "1")
    @DiffLogField(name = "退款类型")
    private Integer type;

    @Schema(description = "退款日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @NotNull(message = "退款日期不能为空")
    @DiffLogField(name = "退款日期")
    private LocalDateTime refundDate;

    @Schema(description = "备注", example = "备注")
    @DiffLogField(name = "备注")
    private String remark;

}
