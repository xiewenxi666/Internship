package com.meession.etm.module.crm.controller.admin.customer.vo.customer;

import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.annotations.ExcelColumnSelect;
import com.meession.etm.framework.excel.core.convert.AreaConvert;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.crm.framework.excel.core.AreaExcelColumnSelectFunction;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.meession.etm.module.crm.enums.DictTypeConstants.*;

/**
 * 客户 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrmCustomerImportExcelVO {

    /** 客户名称 */
    @ExcelProperty("客户名称")
    private String name;

    /** 手机 */
    @ExcelProperty("手机")
    private String mobile;

    /** 电话 */
    @ExcelProperty("电话")
    private String telephone;

    /** QQ */
    @ExcelProperty("QQ")
    private String qq;

    /** 微信 */
    @ExcelProperty("微信")
    private String wechat;

    /** 邮箱 */
    @ExcelProperty("邮箱")
    private String email;

    /** 地区 */
    @ExcelProperty(value = "地区", converter = AreaConvert.class)
    @ExcelColumnSelect(functionName = AreaExcelColumnSelectFunction.NAME)
    private Integer areaId;

    /** 详细地址 */
    @ExcelProperty("详细地址")
    private String detailAddress;

    /** 所属行业 */
    @ExcelProperty(value = "所属行业", converter = DictConvert.class)
    @DictFormat(CRM_CUSTOMER_INDUSTRY)
    @ExcelColumnSelect(dictType = CRM_CUSTOMER_INDUSTRY)
    private Integer industryId;

    /** 客户等级 */
    @ExcelProperty(value = "客户等级", converter = DictConvert.class)
    @DictFormat(CRM_CUSTOMER_LEVEL)
    @ExcelColumnSelect(dictType = CRM_CUSTOMER_LEVEL)
    private Integer level;

    /** 客户来源 */
    @ExcelProperty(value = "客户来源", converter = DictConvert.class)
    @DictFormat(CRM_CUSTOMER_SOURCE)
    @ExcelColumnSelect(dictType = CRM_CUSTOMER_SOURCE)
    private Integer source;

    /** 备注 */
    @ExcelProperty("备注")
    private String remark;

}
