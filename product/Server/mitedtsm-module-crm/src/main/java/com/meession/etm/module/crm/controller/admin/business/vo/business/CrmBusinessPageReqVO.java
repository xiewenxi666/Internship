/**
 * 商机分页查询 Request VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.business.vo.business;

import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 商机分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmBusinessPageReqVO extends PageParam {

    /** 商机名称 */
    @Schema(description = "商机名称", example = "李四")
    private String name;

    /** 客户编号 */
    @Schema(description = "客户编号", example = "10795")
    private Long customerId;

    /** 联系人编号 */
    @Schema(description = "联系人编号", example = "10795")
    private Long contactId;

    /** 场景类型 */
    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType;

}
