package com.meession.etm.module.crm.controller.admin.crm.invoice.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 发票分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmInvoicePageReqVO extends PageParam {

    @Schema(description = "发票编号")
    private String no;

    @Schema(description = "关联订单编号", example = "DD202401010001")
    private String orderNo;

    @Schema(description = "票据类型", example = "1")
    private Integer type;

    @Schema(description = "开票日期开始", example = "2024-01-01 00:00:00")
    private LocalDateTime invoiceDateStart;

    @Schema(description = "开票日期结束", example = "2024-12-31 23:59:59")
    private LocalDateTime invoiceDateEnd;

    @Schema(description = "订单所属人员", example = "1")
    private Long ownerUserId;

    @Schema(description = "发票经手人员", example = "1")
    private Long handlerUserId;

    @Schema(description = "场景类型", example = "1")
    private Integer sceneType;

}
