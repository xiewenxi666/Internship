package com.meession.etm.module.crm.framework.notification;

import com.meession.etm.module.system.api.notify.NotifyMessageSendApi;
import com.meession.etm.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import com.meession.etm.module.system.api.sms.SmsSendApi;
import com.meession.etm.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * CRM 统一通知服务
 * <p>
 * 封装站内消息 + 短信发送，各业务域只需调用 {@link #send(NotifyEvent)}
 * <p>
 * 使用示例：
 * <pre>{@code
 *   notificationService.send(NotifyEvent.builder()
 *       .eventType("工单分配")
 *       .targetUserIds(List.of(1L, 2L))
 *       .title("新工单待处理")
 *       .content("您有一条新工单需要处理，工单编号：GD20260714000001")
 *       .sendSms(true)
 *       .smsTemplateCode("WORK_ORDER_ASSIGN")
 *       .templateParams(Map.of("workOrderNo", "GD20260714000001"))
 *       .build());
 * }</pre>
 *
 * @author 密讯
 */
@Service
@Slf4j
public class NotificationService {

    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Resource
    private SmsSendApi smsSendApi;

    /**
     * 发送通知（站内消息，可选短信）
     * <p>
     * 异步发送，不阻塞主业务流程
     */
    @Async
    public void send(NotifyEvent event) {
        if (event.getTargetUserIds() == null || event.getTargetUserIds().isEmpty()) {
            log.warn("[NotificationService] 通知接收人为空，跳过发送: eventType={}", event.getEventType());
            return;
        }

        // 1. 站内消息
        try {
            for (Long userId : event.getTargetUserIds()) {
                NotifySendSingleToUserReqDTO req = new NotifySendSingleToUserReqDTO()
                        .setUserId(userId)
                        .setTemplateCode(event.getSmsTemplateCode())
                        .setTemplateParams(event.getTemplateParams());
                notifyMessageSendApi.sendSingleMessageToAdmin(req);
            }
            log.info("[NotificationService] 站内消息已发送: eventType={}, userIds={}",
                    event.getEventType(), event.getTargetUserIds());
        } catch (Exception e) {
            log.error("[NotificationService] 站内消息发送失败: eventType={}", event.getEventType(), e);
        }

        // 2. 短信（可选）
        if (event.isSendSms() && event.getSmsTemplateCode() != null) {
            try {
                for (Long userId : event.getTargetUserIds()) {
                    SmsSendSingleToUserReqDTO req = new SmsSendSingleToUserReqDTO()
                            .setUserId(userId)
                            .setTemplateCode(event.getSmsTemplateCode())
                            .setTemplateParams(event.getTemplateParams());
                    smsSendApi.sendSingleSmsToAdmin(req);
                }
                log.info("[NotificationService] 短信已发送: eventType={}, userIds={}",
                        event.getEventType(), event.getTargetUserIds());
            } catch (Exception e) {
                log.error("[NotificationService] 短信发送失败: eventType={}", event.getEventType(), e);
            }
        }
    }
}
