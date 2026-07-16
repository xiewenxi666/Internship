package com.meession.etm.module.crm.controller.admin.contact.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 管理后台 - CRM 联系人分页 Request VO
 */
@Schema(description = "管理后台 - CRM 联系人分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmContactPageReqVO extends PageParam {

    /** 姓名 */
    @Schema(description = "姓名", example = "芋艿")
    private String name;

    /** 客户编号 */
    @Schema(description = "客户编号", example = "10795")
    private Long customerId;

    /** 手机号 */
    @Schema(description = "手机号", example = "13898273941")
    private String mobile;

    /** 电话 */
    @Schema(description = "电话", example = "021-383773")
    private String telephone;

    /** 电子邮箱 */
    @Schema(description = "电子邮箱", example = "111@22.com")
    private String email;

    /** QQ */
    @Schema(description = "QQ", example = "3882872")
    private Long qq;

    /** 微信 */
    @Schema(description = "微信", example = "zzZ98373")
    private String wechat;

    /** 场景类型 */
    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType; // 场景类型，为 null 时则表示全部

    /** 商机编号 */
    @Schema(description = "商机编号", example = "10430")
    private Long businessId;

}
