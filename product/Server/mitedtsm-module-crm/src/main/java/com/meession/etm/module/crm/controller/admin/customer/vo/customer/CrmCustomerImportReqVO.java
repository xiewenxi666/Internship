package com.meession.etm.module.crm.controller.admin.customer.vo.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理后台 - 客户导入 Request VO
 */
@Schema(description = "管理后台 - 客户导入 Request VO")
@Data
@Builder
public class CrmCustomerImportReqVO {

    /** Excel 文件 */
    @Schema(description = "Excel 文件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Excel 文件不能为空")
    private MultipartFile file;

    /** 是否支持更新 */
    @Schema(description = "是否支持更新", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否支持更新不能为空")
    private Boolean updateSupport;

    /** 负责人 */
    @Schema(description = "负责人", example = "1")
    private Long ownerUserId;

}
