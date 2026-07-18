// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.framework.notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 通知事件对象
 *
 * @author 密讯
 */
@Data
@Builder
public class NotifyEvent {

    /**
     * 事件类型（如：工单分配、拜访提醒）
     */
    private String eventType;

    /**
     * 接收人用户 ID 列表
     */
    private List<Long> targetUserIds;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否发送短信
     */
    private Boolean sendSms;

    /**
     * 短信模板编码
     */
    private String smsTemplateCode;

    /**
     * 短信模板参数
     */
    private Map<String, Object> smsTemplateParams;

    /**
     * 站内信模板编码
     */
    private String notifyTemplateCode;

    /**
     * 站内信模板参数
     */
    private Map<String, Object> notifyTemplateParams;

}
