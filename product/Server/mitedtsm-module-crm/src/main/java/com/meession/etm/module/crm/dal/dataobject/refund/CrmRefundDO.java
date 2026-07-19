package com.meession.etm.module.crm.dal.dataobject.refund;

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
 * 退款 DO
 *
 * @author 赤焰
 */
@TableName("crm_refund")
@KeySequence("crm_refund_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmRefundDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 退款编号
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
     * 退款内容/原因
     */
    private String content;
    /**
     * 退款金额，单位：元
     */
    private BigDecimal price;
    /**
     * 退款类型
     *
     * 字典值 crm_refund_type
     */
    private Integer type;
    /**
     * 退款日期
     */
    private LocalDateTime refundDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 审批状态
     *
     * 枚举 {@link CrmAuditStatusEnum}
     */
    private Integer auditStatus;

}
