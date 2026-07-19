package com.meession.etm.module.oa.controller.admin.documentDir.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 文档目录 Response VO")
@Data
public class OaDocumentDirRespVO {

    @Schema(description = "文档目录主键", example = "1024")
    private Long id;

    @Schema(description = "父目录ID", example = "0")
    private Long parentId;

    @Schema(description = "名称", example = "技术文档")
    private String name;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "描述", example = "存放技术相关文档")
    private String description;

    @Schema(description = "搜索关键字", example = "技术,开发")
    private String keywords;

    @Schema(description = "所属人用户编号")
    private Long ownerUserId;

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

    @Schema(description = "版本号", example = "1")
    private Integer version;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}