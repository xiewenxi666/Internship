// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.controller.admin.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户关联数据响应 VO
 *
 * @author 密讯
 */
@Schema(description = "管理后台 - 客户关联数据 Response VO")
@Data
public class CustomerRelationRespVO {

    @Schema(description = "关联商机列表")
    private List<BusinessVO> businessList;

    @Schema(description = "关联合同列表")
    private List<ContractVO> contractList;

    @Schema(description = "关联回款列表")
    private List<ReceivableVO> receivableList;

    @Data
    @Builder
    public static class BusinessVO {
        private Long id;
        private String name;
        private Long statusTypeId;
        private String statusName;
        private BigDecimal totalPrice;
        private LocalDateTime createTime;
    }

    @Data
    @Builder
    public static class ContractVO {
        private Long id;
        private String no;
        private String name;
        private BigDecimal totalPrice;
        private Integer auditStatus;
        private String auditStatusName;
        private LocalDateTime createTime;
    }

    @Data
    @Builder
    public static class ReceivableVO {
        private Long id;
        private String no;
        private Long planId;
        private Integer period;
        private BigDecimal price;
        private LocalDateTime returnTime;
    }

}
