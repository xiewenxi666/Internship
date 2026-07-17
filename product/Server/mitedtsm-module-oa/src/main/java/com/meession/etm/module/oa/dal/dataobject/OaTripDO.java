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

@TableName("oa_trip")
@KeySequence("oa_trip_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaTripDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String type;
    private String destination;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long day;
    private String reason;
    private Integer status;
    private String processInstanceId;

}
