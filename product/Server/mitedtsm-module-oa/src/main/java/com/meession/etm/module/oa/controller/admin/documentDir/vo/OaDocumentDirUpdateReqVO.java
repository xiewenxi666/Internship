package com.meession.etm.module.oa.controller.admin.documentDir.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 文档目录更新 Request VO")
@Data
public class OaDocumentDirUpdateReqVO {

    @Schema(description = "父目录ID", example = "0")
    @NotNull(message = "父目录ID不能为空")
    private Long parentId;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "技术文档")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "描述/摘要", example = "存放技术相关文档")
    private String description;

    @Schema(description = "搜索关键字", example = "技术,开发,API")
    private String keywords;

    @Schema(description = "访问权限", example = "0")
    private Integer permission;

    @Schema(description = "文档附件地址")
    private String fileUrl;

    @Schema(description = "原始文件名")
    private String fileName;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

}
