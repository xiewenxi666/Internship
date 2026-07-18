package com.meession.etm.module.crm.controller.admin.campaign.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销活动 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrmCampaignImportExcelVO {

    @ExcelProperty("活动标题")
    private String title;

    @ExcelProperty("活动类型")
    private Integer type;

    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @ExcelProperty("预计成本")
    private BigDecimal estimatedCost;

    @ExcelProperty("预计收入")
    private BigDecimal estimatedRevenue;

    @ExcelProperty("负责人编号")
    private Long ownerUserId;

    @ExcelProperty("参与人员")
    private String participants;

    @ExcelProperty("活动地址")
    private String address;

    @ExcelProperty("活动描述")
    private String description;

    @ExcelProperty("活动状态")
    private Integer status;

}
