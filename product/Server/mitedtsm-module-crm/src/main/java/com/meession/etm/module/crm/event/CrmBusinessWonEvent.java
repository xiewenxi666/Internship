package com.meession.etm.module.crm.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 商机成交事件
 *
 * 当商机状态变更为“赢单”时发布，触发监听器自动创建订单
 *
 * @author 23计三倪雨晗
 */
@Getter
public class CrmBusinessWonEvent extends ApplicationEvent {

    /** 商机编号 */
    private final Long businessId;

    public CrmBusinessWonEvent(Object source, Long businessId) {
        super(source);
        this.businessId = businessId;
    }

}
