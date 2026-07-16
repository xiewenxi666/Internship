package com.meession.etm.module.crm.dal.dataobject.campaign;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("crm_marketing_campaign")
@KeySequence("crm_marketing_campaign_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCampaignDO extends BaseDO {

    @TableId
    private Long id;
    private String title;
    private Integer type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal estimatedCost;
    private BigDecimal estimatedRevenue;
    private Long ownerUserId;
    private String participants;
    private String address;
    private String description;
    private Integer status;
}
