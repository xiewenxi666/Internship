package com.meession.etm.module.crm.controller.admin.common.vo;

import com.meession.etm.module.crm.controller.admin.business.vo.business.CrmBusinessRespVO;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractRespVO;
import com.meession.etm.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 客户关联数据响应 VO
 * <p>
 * 查询一个客户关联的所有 CRM 数据
 *
 * @author 密讯
 */
@Schema(description = "客户关联数据 Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRelationRespVO {

    @Schema(description = "关联的商机列表")
    private List<CrmBusinessRespVO> businessList;

    @Schema(description = "关联的合同列表")
    private List<CrmContractRespVO> contractList;

    @Schema(description = "关联的回款列表")
    private List<CrmReceivableRespVO> receivableList;

    // 以下 VO 由工单域/拜访域后续补充
    // @Schema(description = "关联的工单列表")
    // private List<CrmWorkOrderRespVO> workOrderList;

    // @Schema(description = "关联的拜访记录列表")
    // private List<CrmVisitRespVO> visitList;
}
