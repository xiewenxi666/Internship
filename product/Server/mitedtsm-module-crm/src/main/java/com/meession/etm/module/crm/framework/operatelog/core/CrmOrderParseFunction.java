package com.meession.etm.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderDO;
import com.meession.etm.module.crm.service.order.CrmOrderService;
import com.mzt.logapi.service.IParseFunction;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CRM 订单的 {@link IParseFunction} 实现类
 *
 * @author 23计三倪雨晗
 */
@Component
@Slf4j
public class CrmOrderParseFunction implements IParseFunction {

    public static final String NAME = "getOrderById";

    @Resource
    private CrmOrderService orderService;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }
        CrmOrderDO order = orderService.getOrder(Long.parseLong(value.toString()));
        return order == null ? "" : order.getName();
    }

}
