// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.controller.admin.workorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 工单 Response VO")
@Data
public class CrmWorkOrderRespVO {

    @Schema(description = "工单编号", example = "1024")
    private Long id;

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

    @Schema(description = "合同名称", example = "设备采购合同")
    private String contractName;

    @Schema(description = "客户编号", example = "2048")
    private Long customerId;

    @Schema(description = "客户名称", example = "XX公司")
    private String customerName;

    @Schema(description = "负责人编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "负责人名称", example = "张三")
    private String ownerUserName;

    @Schema(description = "描述", example = "设备异响需要检修")
    private String description;

    @Schema(description = "解决方案", example = "更换轴承")
    private String solution;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "创建人", example = "admin")
    private String creator;

    @Schema(description = "创建人名称", example = "管理员")
    private String creatorName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
