package com.meession.etm.module.oa.dal.dataobject;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("oa_request")
@KeySequence("oa_request_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaRequestDO extends BaseDO {

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer urgency;
    private Integer status;
    private String processInstanceId;

}
