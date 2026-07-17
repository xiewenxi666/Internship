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

@TableName("oa_visit")
@KeySequence("oa_visit_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaVisitDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Long customerId;
    private String contactPerson;
    private String contactPhone;
    private LocalDateTime visitTime;
    private String location;
    private String purpose;
    private String notes;
    private Integer status;
    private String processInstanceId;

}
