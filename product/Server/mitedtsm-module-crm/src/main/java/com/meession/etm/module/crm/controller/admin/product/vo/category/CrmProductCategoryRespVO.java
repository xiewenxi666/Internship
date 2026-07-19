/**
 * 产品分类 Response VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.product.vo.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 产品分类 Response VO")
@Data
public class CrmProductCategoryRespVO {

    /** 分类编号 */
    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23902")
    private Long id;

    /** 分类名称 */
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String name;

    /** 父级编号 */
    @Schema(description = "父级编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4680")
    private Long parentId;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
