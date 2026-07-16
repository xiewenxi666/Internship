package com.meession.etm.module.crm.controller.admin.followup.vo;

import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.module.crm.controller.admin.business.vo.business.CrmBusinessRespVO;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.module.crm.enums.DictTypeConstants.CRM_FOLLOW_UP_TYPE;

/**
 * 管理后台 - 跟进记录 Response VO
 */
@Schema(description = "管理后台 - 跟进记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmFollowUpRecordRespVO {

    /** 编号 */
    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "28800")
    private Long id;

    /** 数据类型 */
    @Schema(description = "数据类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer bizType;

    /** 数据编号 */
    @Schema(description = "数据编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5564")
    private Long bizId;

    /** 跟进类型 */
    @Schema(description = "跟进类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @DictFormat(CRM_FOLLOW_UP_TYPE)
    private Integer type;

    /** 跟进内容 */
    @Schema(description = "跟进内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    /** 下次联系时间 */
    @Schema(description = "下次联系时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime nextTime;

    /** 关联的商机编号数组 */
    @Schema(description = "关联的商机编号数组")
    private List<Long> businessIds;
    /** 关联的商机数组 */
    @Schema(description = "关联的商机数组")
    private List<CrmBusinessRespVO> businesses;

    /** 关联的联系人编号数组 */
    @Schema(description = "关联的联系人编号数组")
    private List<Long> contactIds;
    /** 关联的联系人名称数组 */
    @Schema(description = "关联的联系人名称数组")
    private List<CrmBusinessRespVO> contacts;

    /** 图片 */
    @Schema(description = "图片")
    private List<String> picUrls;
    /** 附件 */
    @Schema(description = "附件")
    private List<String> fileUrls;

    /** 创建人 */
    @Schema(description = "创建人", example = "1024")
    @ExcelProperty("创建人")
    private String creator;
    /** 创建人名字 */
    @Schema(description = "创建人名字", example = "密讯")
    @ExcelProperty("创建人名字")
    private String creatorName;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
