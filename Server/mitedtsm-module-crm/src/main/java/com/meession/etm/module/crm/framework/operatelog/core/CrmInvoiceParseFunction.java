package com.meession.etm.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CRM 发票的 {@link IParseFunction} 实现类
 * <p>
 * 用法：在 @LogRecord 注解中使用 #{getInvoiceById(#invoiceId)}
 *
 * @author 密讯
 */
@Component
@Slf4j
public class CrmInvoiceParseFunction implements IParseFunction {

    public static final String NAME = "getInvoiceById";

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
        // TODO: 财务域建表后，注入 CrmInvoiceMapper，通过 ID 查发票编号
        Long id = Long.parseLong(value.toString());
        log.debug("[CrmInvoiceParseFunction] 解析发票ID: {}", id);
        return "发票#" + id;
    }
}
