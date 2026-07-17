package com.meession.etm.module.oa.dal.dataobject;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@TableName("oa_report")
@KeySequence("oa_report_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaReportDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String type;
    private LocalDate reportDate;
    private String content;
    private String plan;

}
