package com.meession.etm.module.crm.controller.admin.customercare.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "管理后台 - 客户关怀配置 Request VO")
@Data
public class CrmCustomerCareConfigSaveReqVO {

    @Schema(description = "短信内容", example = "尊敬的客户，祝您生日快乐！")
    @DiffLogField(name = "短信内容")
    private String smsContent;

    @Schema(description = "邮件标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "生日祝福")
    @NotEmpty(message = "邮件标题不能为空")
    @DiffLogField(name = "邮件标题")
    private String emailTitle;

    @Schema(description = "邮件正文", requiredMode = Schema.RequiredMode.REQUIRED, example = "亲爱的客户，祝您生日快乐，万事如意！")
    @NotEmpty(message = "邮件正文不能为空")
    @DiffLogField(name = "邮件正文")
    private String emailBody;

    @Schema(description = "发件人邮箱", example = "noreply@company.com")
    @DiffLogField(name = "发件人邮箱")
    private String senderEmail;

    @Schema(description = "发送时间", example = "09:00")
    @DiffLogField(name = "发送时间")
    private String sendTime;

    @Schema(description = "测试收件人邮箱", example = "test@example.com")
    @DiffLogField(name = "测试收件人邮箱")
    private String testEmail;

    @Schema(description = "是否启用短信", example = "true")
    @DiffLogField(name = "是否启用短信")
    private Boolean smsEnabled;

    @Schema(description = "是否启用邮件", example = "true")
    @DiffLogField(name = "是否启用邮件")
    private Boolean emailEnabled;

    @Schema(description = "节假日列表（JSON 数组）", example = "[\"2024-01-01\",\"2024-10-01\"]")
    @DiffLogField(name = "节假日列表")
    private String holidayList;

}
