package com.meession.etm.module.oa.controller.admin.oa.vo.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 文档创建 Request VO")
@Data
public class OaDocumentCreateReqVO {

    @Schema(description = "父级ID（文件夹ID）")
    private Long parentId;

    @NotBlank(message = "名称不能为空")
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "项目文档")
    private String name;

    @NotNull(message = "类型不能为空")
    @Schema(description = "是否文件夹", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean isFolder;

    @Schema(description = "文件ID（infra_file.id），文件时必填")
    private Long fileId;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "备注")
    private String description;

}
