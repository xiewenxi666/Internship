// -23计算机科学与技术2班-龚小波
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

    /** 站内消息 */
    IN_APP(1, "站内消息"),

    /** 短信 */
    SMS(2, "短信"),

    /** 邮件 */
    EMAIL(3, "邮件");

    private final Integer channel;
    private final String name;

}
