package com.meession.etm.module.crm.controller.admin.receivable.vo.plan;

import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 管理后台 - CRM 回款计划分页 Request VO
 */
@Schema(description = "管理后台 - CRM 回款计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmReceivablePlanPageReqVO extends PageParam {

    /**
     * 提醒类型 - 待回款
     */
    public final static Integer REMIND_TYPE_NEEDED = 1;
    /**
     * 提醒类型 - 已逾期
     */
    public final static Integer REMIND_TYPE_EXPIRED = 2;
    /**
     * 提醒类型 - 已回款
     */
    public final static Integer REMIND_TYPE_RECEIVED = 3;

    /** 客户编号 */
    @Schema(description = "客户编号", example = "18026")
    private Long customerId;

    /** 合同编号（合同号） */
    @Schema(description = "合同编号", example = "H3473")
    private String contractNo;

    /** 合同编号 */
    @Schema(description = "合同编号", example = "3473")
    private Long contractId;

    /** 场景类型 */
    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType; // 场景类型，为 null 时则表示全部

    /** 提醒类型 */
    @Schema(description = "提醒类型", example = "1")
    private Integer remindType; // 提醒类型，为 null 时则表示全部

}
