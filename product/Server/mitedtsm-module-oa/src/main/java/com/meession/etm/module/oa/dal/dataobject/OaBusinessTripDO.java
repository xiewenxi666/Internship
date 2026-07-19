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

@TableName("oa_business_trip")
@KeySequence("oa_business_trip_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaBusinessTripDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String destination;
    private String reason;
    private String companion;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal day;
    private String vehicle;
    private BigDecimal estimatedAmount;
    private Integer status;
    private String processInstanceId;
    private String startUserSelectAssignees;

}