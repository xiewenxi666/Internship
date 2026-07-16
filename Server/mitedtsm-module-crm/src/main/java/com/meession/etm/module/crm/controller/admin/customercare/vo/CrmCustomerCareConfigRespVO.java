package com.meession.etm.module.crm.controller.admin.customercare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 客户关怀配置 Response VO")
@Data
public class CrmCustomerCareConfigRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "短信内容", example = "尊敬的客户，祝您生日快乐！")
    private String smsContent;

    @Schema(description = "邮件标题", example = "生日祝福")
    private String emailTitle;

    @Schema(description = "邮件正文", example = "亲爱的客户，祝您生日快乐，万事如意！")
    private String emailBody;

    @Schema(description = "发件人邮箱", example = "noreply@company.com")
    private String senderEmail;

    @Schema(description = "发送时间", example = "09:00")
    private String sendTime;

    @Schema(description = "是否启用短信", example = "true")
    private Boolean smsEnabled;

    @Schema(description = "是否启用邮件", example = "true")
    private Boolean emailEnabled;

    @Schema(description = "节假日列表（JSON 数组）", example = "[\"2024-01-01\",\"2024-10-01\"]")
    private String holidayList;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}
