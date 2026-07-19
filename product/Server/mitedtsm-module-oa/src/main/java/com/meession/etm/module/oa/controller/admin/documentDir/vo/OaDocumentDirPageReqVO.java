package com.meession.etm.module.oa.controller.admin.documentDir.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 文档目录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OaDocumentDirPageReqVO extends PageParam {

    @Schema(description = "父目录ID", example = "0")
    private Long parentId;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "名称", example = "技术")
    private String name;

}