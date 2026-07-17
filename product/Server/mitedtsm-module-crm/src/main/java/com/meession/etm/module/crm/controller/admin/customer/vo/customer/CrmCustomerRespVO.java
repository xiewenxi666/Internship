package com.meession.etm.module.crm.controller.admin.customer.vo.customer;

import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.infra.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理后台 - CRM 客户 Response VO
 */
@Schema(description = "管理后台 - CRM 客户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmCustomerRespVO {

    /** 编号 */
    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty("编号")
    private Long id;

    /** 客户名称 */
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty("客户名称")
    private String name;

    /** 跟进状态 */
    @Schema(description = "跟进状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "跟进状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean followUpStatus;

    /** 最后跟进时间 */
    @Schema(description = "最后跟进时间")
    @ExcelProperty("最后跟进时间")
    private LocalDateTime contactLastTime;

    /** 最后跟进内容 */
    @Schema(description = "最后跟进内容", example = "吃饭、睡觉、打逗逗")
    @ExcelProperty("最后跟进内容")
    private String contactLastContent;

    /** 下次联系时间 */
    @Schema(description = "下次联系时间")
    @ExcelProperty("下次联系时间")
    private LocalDateTime contactNextTime;

    /** 负责人的用户编号 */
    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("负责人的用户编号")
    private Long ownerUserId;
    /** 负责人名字 */
    @Schema(description = "负责人名字", example = "25682")
    @ExcelProperty("负责人名字")
    private String ownerUserName;
    /** 负责人部门 */
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    /** 锁定状态 */
    @Schema(description = "锁定状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "锁定状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean lockStatus;

    /** 成交状态 */
    @Schema(description = "成交状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "成交状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean dealStatus;

    /** 手机 */
    @Schema(description = "手机", example = "25682")
    @ExcelProperty("手机")
    private String mobile;

    /** 电话 */
    @Schema(description = "电话", example = "25682")
    @ExcelProperty("电话")
    private String telephone;

    /** QQ */
    @Schema(description = "QQ", example = "25682")
    @ExcelProperty("QQ")
    private String qq;

    /** 微信 */
    @Schema(description = "wechat", example = "25682")
    @ExcelProperty("wechat")
    private String wechat;

    /** 邮箱 */
    @Schema(description = "email", example = "25682")
    @ExcelProperty("email")
    private String email;

    /** 地区编号 */
    @Schema(description = "地区编号", example = "1024")
    @ExcelProperty("地区编号")
    private Integer areaId;
    /** 地区名称 */
    @Schema(description = "地区名称", example = "北京市")
    @ExcelProperty("地区名称")
    private String areaName;
    /** 详细地址 */
    @Schema(description = "详细地址", example = "北京市成华大道")
    @ExcelProperty("详细地址")
    private String detailAddress;

    /** 所属行业 */
    @Schema(description = "所属行业", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "所属行业", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_CUSTOMER_INDUSTRY)
    private Integer industryId;

    /** 客户等级 */
    @Schema(description = "客户等级", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "客户等级", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_CUSTOMER_LEVEL)
    private Integer level;

    /** 客户来源 */
    @Schema(description = "客户来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "客户来源", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_CUSTOMER_SOURCE)
    private Integer source;

    /** 备注 */
    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("备注")
    private String remark;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    /** 创建人 */
    @Schema(description = "创建人", example = "1024")
    @ExcelProperty("创建人")
    private String creator;
    /** 创建人名字 */
    @Schema(description = "创建人名字", example = "密讯")
    @ExcelProperty("创建人名字")
    private String creatorName;

    /** 距离加入公海时间 */
    @Schema(description = "距离加入公海时间", example = "1")
    private Long poolDay;

}
