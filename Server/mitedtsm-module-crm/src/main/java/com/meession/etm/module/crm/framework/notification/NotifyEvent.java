package com.meession.etm.module.crm.framework.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * CRM 通知事件对象
 * <p>
 * 各域业务触发通知时构造此对象，调用 {@link NotificationService#send(NotifyEvent)}
 *
 * @author 密讯
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyEvent {

    /** 事件类型标识（如：工单分配、审批通过、拜访提醒、关怀发送） */
    private String eventType;

    /** 接收人用户ID列表 */
    private List<Long> targetUserIds;

    /** 站内消息标题 */
    private String title;

    /** 站内消息内容（模板变量替换后） */
    private String content;

    /** 是否同步发送短信 */
    @Builder.Default
    private boolean sendSms = false;

    /** 短信模板编码 */
    private String smsTemplateCode;

    /** 模板变量 */
    private Map<String, Object> templateParams;
}
