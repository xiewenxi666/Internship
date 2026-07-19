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
    private Long projectId;
    private String title;
    private String description;
    private Integer priority;
    private Integer status;
    private Long assigneeUserId;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private LocalDateTime completedTime;
    private Integer progress;
    private String tags;
    private String attachment;

}