// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.framework.notification;

import cn.hutool.core.collection.CollUtil;
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
 * 各域只需注入此服务，调用 send(NotifyEvent) 即可发送通知
 *
 * @author 密讯
 */
@Service
@Slf4j
public class NotificationService {

    @Resource
    private SmsSendApi smsSendApi;

    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    /**
     * 发送通知（异步）
     *
     * @param event 通知事件
     */
    @Async
    public void send(NotifyEvent event) {
        if (event == null || CollUtil.isEmpty(event.getTargetUserIds())) {
            log.warn("[NotificationService][send] 通知事件为空或接收人为空，跳过发送");
            return;
        }

        // 发送站内消息
        sendInAppMessage(event);

        // 可触发短信
        if (Boolean.TRUE.equals(event.getSendSms())) {
            sendSmsMessage(event);
        }
    }

    /**
     * 发送站内消息
     */
    private void sendInAppMessage(NotifyEvent event) {
        for (Long userId : event.getTargetUserIds()) {
            try {
                NotifySendSingleToUserReqDTO reqDTO = new NotifySendSingleToUserReqDTO();
                reqDTO.setUserId(userId);
                reqDTO.setTemplateCode(event.getNotifyTemplateCode());
                reqDTO.setTemplateParams(event.getNotifyTemplateParams());
                notifyMessageSendApi.sendSingleMessageToAdmin(reqDTO);
            } catch (Exception e) {
                log.error("[NotificationService][sendInAppMessage] 发送站内消息失败，userId={}, eventType={}",
                        userId, event.getEventType(), e);
            }
        }
    }

    /**
     * 发送短信
     */
    private void sendSmsMessage(NotifyEvent event) {
        for (Long userId : event.getTargetUserIds()) {
            try {
                SmsSendSingleToUserReqDTO reqDTO = new SmsSendSingleToUserReqDTO();
                reqDTO.setUserId(userId);
                reqDTO.setTemplateCode(event.getSmsTemplateCode());
                reqDTO.setTemplateParams(event.getSmsTemplateParams());
                smsSendApi.sendSingleSmsToAdmin(reqDTO);
            } catch (Exception e) {
                log.error("[NotificationService][sendSmsMessage] 发送短信失败，userId={}, eventType={}",
                        userId, event.getEventType(), e);
            }
        }
    }

}
