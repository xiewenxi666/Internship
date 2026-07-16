/**
 * 订单分页查询 Request VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.order.vo.order;

import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import com.meession.etm.module.crm.enums.order.CrmOrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmOrderPageReqVO extends PageParam {

    /** 订单编号 */
    @Schema(description = "订单编号", example = "DD20260101000001")
    private String no;

    /** 订单名称 */
    @Schema(description = "订单名称", example = "XX项目订单")
    private String name;

    /** 客户编号 */
    @Schema(description = "客户编号", example = "18336")
    private Long customerId;

    /** 商机编号 */
    @Schema(description = "商机编号", example = "10864")
    private Long businessId;

    /** 订单状态 */
    @Schema(description = "订单状态", example = "20")
    @InEnum(CrmOrderStatusEnum.class)
    private Integer status;

    /** 场景类型 */
    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType;

}
