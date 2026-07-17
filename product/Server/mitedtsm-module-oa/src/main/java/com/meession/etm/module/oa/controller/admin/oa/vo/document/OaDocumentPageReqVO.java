package com.meession.etm.module.oa.controller.admin.oa.vo.document;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 文档分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class OaDocumentPageReqVO extends PageParam {

    @Schema(description = "父级ID", example = "0")
    private Long parentId;

    @Schema(description = "名称，模糊匹配")
    private String name;

    @Schema(description = "是否文件夹")
    private Boolean isFolder;

}
