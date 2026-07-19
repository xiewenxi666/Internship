package com.meession.etm.module.oa.dal.dataobject;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("oa_schedule")
@KeySequence("oa_schedule_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaScheduleDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isAllDay;
    private String location;
    private Integer type;
    private Integer priority;
    private Integer status;
    private Integer reminderTime;
    private String color;

}