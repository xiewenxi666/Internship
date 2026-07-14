package com.meession.etm.module.crm.controller.admin.campaign.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 营销活动分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCampaignPageReqVO extends PageParam {

    @Schema(description = "活动标题", example = "春季促销")
    private String title;

    @Schema(description = "活动类型", example = "1")
    private Integer type;

    @Schema(description = "活动状态", example = "1")
    private Integer status;

}
