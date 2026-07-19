package com.meession.etm.module.crm.dal.dataobject.expense;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("crm_expense")
@KeySequence("crm_expense_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmExpenseDO extends BaseDO {

    @TableId
    private Long id;

    private String no;

    private Long customerId;

    private Long contractId;

    private Long ownerUserId;

    private String content;

    private BigDecimal price;

    private Integer type;

    private LocalDateTime applyDate;

    private String remark;

    private Long reimbursementId;

    private Integer reimburseStatus;

}
