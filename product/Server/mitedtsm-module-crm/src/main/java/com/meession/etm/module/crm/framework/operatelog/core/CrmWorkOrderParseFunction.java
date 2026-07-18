package com.meession.etm.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CRM 工单的 {@link IParseFunction} 实现类
 * <p>
 * 用法：在 @LogRecord 注解中使用 #{getWorkOrderById(#workOrderId)}
 *
 * @author 密讯
 */
@Component
@Slf4j
public class CrmWorkOrderParseFunction implements IParseFunction {

    public static final String NAME = "getWorkOrderById";

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
        // TODO: 工单域建表后，注入 CrmWorkOrderMapper，通过 ID 查标题
        Long id = Long.parseLong(value.toString());
        log.debug("[CrmWorkOrderParseFunction] 解析工单ID: {}", id);
        return "工单#" + id;
    }
}
