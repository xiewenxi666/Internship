package com.meession.etm.module.oa.dal.dataobject;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String purpose;
    private Long amount;
    private LocalDateTime expectedRepayTime;
    private String reason;
    private Integer status;
    private String processInstanceId;

}
