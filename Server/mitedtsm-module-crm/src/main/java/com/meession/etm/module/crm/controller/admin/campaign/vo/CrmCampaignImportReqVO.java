package com.meession.etm.module.crm.controller.admin.campaign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "管理后台 - 营销活动导入 Request VO")
@Data
public class CrmCampaignImportReqVO {

    @Schema(description = "Excel 文件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Excel 文件不能为空")
    private MultipartFile file;

}
