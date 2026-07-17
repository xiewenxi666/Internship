package com.meession.etm.module.crm.controller.admin.refund.vo.refund;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 退款审批分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmRefundApprovalPageReqVO extends PageParam {

    @Schema(description = "场景类型", example = "1")
    private Integer sceneType;

    @Schema(description = "审批状态", example = "10")
    private Integer auditStatus;

    @Schema(description = "退款编号", example = "TK20240001")
    private String no;

}
