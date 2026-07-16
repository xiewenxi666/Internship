package com.meession.etm.module.crm.dal.dataobject.customercare;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

@TableName("crm_marketing_customer_care")
@KeySequence("crm_marketing_customer_care_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCustomerCareConfigDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * 短信内容
     */
    private String smsContent;

    /**
     * 邮件标题
     */
    private String emailTitle;

    /**
     * 邮件正文
     */
    private String emailBody;

    /**
     * 发件人邮箱
     */
    private String senderEmail;

    /**
     * 发送时间（格式如 "09:00"）
     */
    private String sendTime;

    /**
     * 是否启用短信
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Boolean smsEnabled;

    /**
     * 是否启用邮件
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Boolean emailEnabled;

    /**
     * 节假日列表（JSON 数组字符串）
     */
    private String holidayList;

}
