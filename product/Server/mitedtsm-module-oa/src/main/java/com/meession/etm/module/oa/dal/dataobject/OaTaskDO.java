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

@TableName("oa_task")
@KeySequence("oa_task_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaTaskDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Long assigneeId;
    private String title;
    private String description;
    private Integer status;
    private Integer priority;
    private LocalDateTime deadline;
    private LocalDateTime completedTime;

}
