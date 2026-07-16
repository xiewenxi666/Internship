/**
 * CRM 统一通知模块
 * <p>
 * 封装站内消息 + 短信发送，各业务域通过 {@link NotificationService#send} 发通知，
 * 不直接依赖 system 模块的 SmsSendApi / NotifyMessageSendApi
 *
 * @author 密讯
 */
package com.meession.etm.module.crm.framework.notification;
