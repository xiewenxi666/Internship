package com.meession.etm.module.crm.framework.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知渠道枚举
 *
 * @author 密讯
 */
@Getter
@AllArgsConstructor
public enum NotifyChannelEnum {

    INNER_MSG("站内消息"),
    SMS("短信"),
    MAIL("邮件");

    private final String desc;
}
