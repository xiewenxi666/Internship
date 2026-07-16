package com.meession.etm.module.crm.controller.admin.contact.vo;

import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.infra.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 管理后台 - CRM 联系人 Response VO
 */
@Schema(description = "管理后台 - CRM 联系人 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmContactRespVO {

    /** 主键 */
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3167")
    private Long id;

    /** 联系人姓名 */
    @Schema(description = "联系人姓名", example = "芋艿")
    @ExcelProperty(value = "联系人姓名", order = 1)
    private String name;

    /** 客户编号 */
    @Schema(description = "客户编号", example = "10795")
    private Long customerId;
    /** 客户名字 */
    @ExcelProperty(value = "客户名称", order = 2)
    @Schema(description = "客户名字", example = "test")
    private String customerName;

    /** 最后跟进时间 */
    @Schema(description = "最后跟进时间")
    @ExcelProperty(value = "最后跟进时间", order = 6)
    private LocalDateTime contactLastTime;

    /** 最后跟进内容 */
    @Schema(description = "最后跟进内容")
    @ExcelProperty(value = "最后跟进内容", order = 6)
    private String contactLastContent;

    /** 下次联系时间 */
    @Schema(description = "下次联系时间")
    @ExcelProperty(value = "下次联系时间", order = 6)
    private LocalDateTime contactNextTime;

    /** 负责人编号 */
    @Schema(description = "负责人编号")
    private Long ownerUserId;
    /** 负责人名字 */
    @Schema(description = "负责人名字", example = "25682")
    @ExcelProperty("负责人名字")
    private String ownerUserName;
    /** 负责人部门 */
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    /** 手机号 */
    @Schema(description = "手机号", example = "1387171766")
    @ExcelProperty(value = "手机号", order = 4)
    private String mobile;

    /** 电话 */
    @Schema(description = "电话", example = "021-0029922")
    @ExcelProperty(value = "电话", order = 4)
    private String telephone;

    /** 电子邮箱 */
    @Schema(description = "电子邮箱", example = "1111@22.com")
    @ExcelProperty(value = "邮箱", order = 4)
    private String email;

    /** QQ */
    @Schema(description = "QQ", example = "197272662")
    @ExcelProperty(value = "QQ", order = 4)
    private Long qq;

    /** 微信 */
    @Schema(description = "微信", example = "zzz3883")
    @ExcelProperty(value = "微信", order = 4)
    private String wechat;

    /** 地区编号 */
    @Schema(description = "地区编号", example = "20158")
    private Integer areaId;
    /** 地区名 */
    @Schema(description = "地区名", example = "上海上海市浦东新区")
    @ExcelProperty(value = "地区", order = 5)
    private String areaName;

    /** 地址 */
    @Schema(description = "地址")
    @ExcelProperty(value = "地址", order = 5)
    private String detailAddress;

    /** 性别 */
    @Schema(description = "性别")
    @ExcelProperty(value = "性别", converter = DictConvert.class, order = 3)
    @DictFormat(com.meession.etm.module.system.enums.DictTypeConstants.USER_SEX)
    private Integer sex;

    /** 是否关键决策人 */
    @Schema(description = "是否关键决策人")
    @ExcelProperty(value = "是否关键决策人", converter = DictConvert.class, order = 3)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean master;

    /** 职位 */
    @Schema(description = "职位")
    @ExcelProperty(value = "职位", order = 3)
    private String post;

    /** 直属上级 */
    @Schema(description = "直属上级", example = "23457")
    private Long parentId;
    /** 直属上级名 */
    @Schema(description = "直属上级名", example = "芋头")
    @ExcelProperty(value = "直属上级", order = 4)
    private String parentName;

    /** 备注 */
    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty(value = "备注", order = 6)
    private String remark;

    /** 创建人 */
    @Schema(description = "创建人", example = "25682")
    private String creator;
    /** 创建人名字 */
    @Schema(description = "创建人名字", example = "test")
    @ExcelProperty(value = "创建人", order = 8)
    private String creatorName;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}
