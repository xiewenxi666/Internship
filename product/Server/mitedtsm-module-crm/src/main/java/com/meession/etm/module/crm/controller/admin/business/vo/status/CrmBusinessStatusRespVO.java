/**
 * 商机状态 Response VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.business.vo.status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 商机状态 Response VO")
@Data
public class CrmBusinessStatusRespVO {

    /** 状态组编号 */
    @Schema(description = "状态组编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2934")
    private Long id;

    /** 状态组名字 */
    @Schema(description = "状态组名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String name;

    /** 使用的部门编号 */
    @Schema(description = "使用的部门编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> deptIds;
    /** 使用的部门名称 */
    @Schema(description = "使用的部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> deptNames;

    /** 创建人 */
    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    /** 状态集合 */
    @Schema(description = "状态集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Status> statuses;

    @Data
    public static class Status {

        /** 状态编号 */
        @Schema(description = "状态编号", example = "23899")
        private Long id;

        /** 状态名 */
        @Schema(description = "状态名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
        private String name;

        /** 赢单率 */
        @Schema(description = "赢单率", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
        private BigDecimal percent;

        /** 排序 */
        @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Integer sort;

    }

}
