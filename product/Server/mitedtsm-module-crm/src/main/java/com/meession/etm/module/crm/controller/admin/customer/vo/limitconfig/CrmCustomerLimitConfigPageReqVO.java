package com.meession.etm.module.crm.controller.admin.customer.vo.limitconfig;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 管理后台 - 客户限制配置分页 Request VO
 */
@Schema(description = "管理后台 - 客户限制配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerLimitConfigPageReqVO extends PageParam {

    /** 规则类型 */
    @Schema(description = "规则类型", example = "1")
    private Integer type;

}
