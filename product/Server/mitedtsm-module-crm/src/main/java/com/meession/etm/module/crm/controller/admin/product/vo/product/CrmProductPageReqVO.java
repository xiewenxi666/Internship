/**
 * 产品分页查询 Request VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.product.vo.product;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmProductPageReqVO extends PageParam {

    /** 产品名称 */
    @Schema(description = "产品名称", example = "李四")
    private String name;

    /** 状态 */
    @Schema(description = "状态", example = "1")
    private Integer status;

}
