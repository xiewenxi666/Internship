package com.meession.etm.module.oa.controller.admin.oa.vo.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 文档 Response VO")
@Data
public class OaDocumentRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "名称", example = "项目文档")
    private String name;

    @Schema(description = "是否文件夹", example = "false")
    private Boolean isFolder;

    @Schema(description = "文件ID（infra_file.id）")
    private Long fileId;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
