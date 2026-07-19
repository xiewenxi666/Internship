package com.meession.etm.module.oa.dal.dataobject;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * OA 请假申请 DO
 *
 * {@link #day} 请假天数，目前先简单做。一般是分成请假上午和下午，可以是 1 整天，可以是 0.5 半天
 *
 * @author jason
 * @author 密讯
 */
@TableName("oa_leave")
@KeySequence("oa_leave_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaLeaveDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String type;
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long day;
    private Integer status;
    private String processInstanceId;

}
