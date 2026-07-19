package com.meession.etm.module.oa.dal.dataobject;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("oa_work_report")
@KeySequence("oa_work_report_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaWorkReportDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private Integer type;
    private String title;
    private String content;
    private String plan;
    private String summary;
    private LocalDate reportDate;
    private Integer status;
    private Long reviewerUserId;
    private LocalDateTime reviewTime;
    private String reviewContent;

}