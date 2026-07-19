package com.meession.etm.module.crm.dal.dataobject.reimbursement;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报销 DO
 *
 * @author 赤焰
 */
@TableName("crm_reimbursement")
@KeySequence("crm_reimbursement_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmReimbursementDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 报销编号
     */
    private String no;
    /**
     * 客户编号
     *
     * 关联 {@link CrmCustomerDO#getId()}
     */
    private Long customerId;
    /**
     * 合同编号
     *
     * 关联 {@link CrmContractDO#getId()}
     */
    private Long contractId;
    /**
     * 负责人编号，关联 {@link AdminUserRespDTO#getId()}
     */
    private Long ownerUserId;
    /**
     * 报销内容
     */
    private String content;
    /**
     * 报销金额，单位：元
     */
    private BigDecimal price;
    /**
     * 报销类型
     *
     * 字典值 crm_reimbursement_type
     */
    private Integer type;
    /**
     * 申请日期
     */
    private LocalDateTime applyDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 审批状态
     *
     * 枚举 {@link CrmAuditStatusEnum}
     */
    private Integer auditStatus;
    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

}
