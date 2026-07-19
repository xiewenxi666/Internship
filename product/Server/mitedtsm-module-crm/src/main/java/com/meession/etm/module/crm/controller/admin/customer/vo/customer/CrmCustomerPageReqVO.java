package com.meession.etm.module.crm.controller.admin.customer.vo.customer;

import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 管理后台 - CRM 客户分页 Request VO
 */
@Schema(description = "管理后台 - CRM 客户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerPageReqVO extends PageParam {

    /**
     * 联系状态 - 今日需联系
     */
    public static final int CONTACT_TODAY = 1;
    /**
     * 联系状态 - 已逾期
     */
    public static final int CONTACT_EXPIRED = 2;
    /**
     * 联系状态 - 已联系
     */
    public static final int CONTACT_ALREADY = 3;

    /** 客户名称 */
    @Schema(description = "客户名称", example = "赵六")
    private String name;

    /** 手机 */
    @Schema(description = "手机", example = "18000000000")
    private String mobile;

    /** 所属行业 */
    @Schema(description = "所属行业", example = "1")
    private Integer industryId;

    /** 客户等级 */
    @Schema(description = "客户等级", example = "1")
    private Integer level;

    /** 客户来源 */
    @Schema(description = "客户来源", example = "1")
    private Integer source;

    /** 场景类型 */
    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType;

    /** 是否为公海数据 */
    @Schema(description = "是否为公海数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean pool;

    /** 联系状态 */
    @Schema(description = "联系状态", example = "1")
    private Integer contactStatus;

    /** 跟进状态 */
    @Schema(description = "跟进状态", example = "true")
    private Boolean followUpStatus;

}
