package com.meession.etm.module.crm.dal.dataobject.bulksend;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("crm_marketing_bulk_send")
@KeySequence("crm_marketing_bulk_send_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmBulkSendDO extends BaseDO {

    @TableId
    private Long id;
    private String title;
    private Long campaignId;
    private Integer type;
    private Long templateId;
    private String content;
    private Integer targetType;
    private String targetIds;
    private Integer targetCount;
    private Integer successCount;
    private Integer failCount;
    private Integer status;
    private Long ownerUserId;
    private LocalDateTime sendTime;
}
