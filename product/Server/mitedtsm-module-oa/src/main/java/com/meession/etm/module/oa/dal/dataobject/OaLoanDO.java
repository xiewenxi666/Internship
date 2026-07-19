package com.meession.etm.module.oa.dal.dataobject;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("oa_loan")
@KeySequence("oa_loan_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaLoanDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String purpose;
    private String repaymentPlan;
    private LocalDateTime expectedRepaymentTime;
    private LocalDateTime actualRepaymentTime;
    private Integer status;
    private String processInstanceId;
    private String startUserSelectAssignees;

}