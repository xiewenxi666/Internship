package com.meession.etm.module.crm.controller.admin.quotation.vo.quotation;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - CRM 报价单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmQuotationPageReqVO extends PageParam {

    @Schema(description = "负责人用户ID", example = "1000")
    private Long ownerUserId;

    @Schema(description = "客户ID", example = "200")
    private Long customerId;

    @Schema(description = "商机ID", example = "100")
    private Long businessId;

    @Schema(description = "状态：0-草稿 1-待审批 2-已通过 3-已拒绝 4-已作废", example = "0")
    private Integer status;

    @Schema(description = "报价单编号（模糊查询）", example = "QT-20260101")
    private String quotationNo;

    @Schema(description = "创建开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeStart;

    @Schema(description = "创建结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeEnd;

}
