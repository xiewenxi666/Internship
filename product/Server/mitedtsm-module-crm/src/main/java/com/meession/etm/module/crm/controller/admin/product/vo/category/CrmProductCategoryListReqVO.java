/**
 * 产品分类列表 Request VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.product.vo.category;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 产品分类列表 Request VO")
@Data
public class CrmProductCategoryListReqVO {

    /** 名称 */
    @ExcelProperty("名称")
    private String name;

    /** 父级 id */
    @ExcelProperty("父级 id")
    private Long parentId;

    /** 创建时间 */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
