// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.service.workorder.CrmWorkOrderService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * CRM 工单的 {@link IParseFunction} 实现类
 *
 * @author 密讯
 */
@Component
@Slf4j
public class CrmWorkOrderParseFunction implements IParseFunction {

    public static final String NAME = "getWorkOrderById";

    @Resource
    private CrmWorkOrderService workOrderService;

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }
        CrmWorkOrderDO workOrder = workOrderService.getWorkOrder(Long.parseLong(value.toString()));
        return workOrder == null ? "" : workOrder.getTitle();
    }
}
