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

@TableName("oa_message")
@KeySequence("oa_message_seq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaMessageDO extends BaseDO {

    @TableId
    private Long id;
    private Long senderUserId;
    private Long receiverUserId;
    private String title;
    private String content;
    private Integer readStatus;
    private LocalDateTime readTime;

}