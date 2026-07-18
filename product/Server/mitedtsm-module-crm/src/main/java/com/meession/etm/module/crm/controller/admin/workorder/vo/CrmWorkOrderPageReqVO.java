// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.controller.admin.workorder.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 工单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmWorkOrderPageReqVO extends PageParam {

    @Schema(description = "工单编号", example = "WO20240101")
    private String no;

    @Schema(description = "工单标题", example = "设备维修")
    private String title;

    @Schema(description = "工单类型", example = "1")
    private Integer type;

    @Schema(description = "优先级", example = "2")
    private Integer priority;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "合同编号", example = "1024")
    private Long contractId;

    @Schema(description = "客户编号", example = "2048")
    private Long customerId;

    @Schema(description = "负责人编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

}
