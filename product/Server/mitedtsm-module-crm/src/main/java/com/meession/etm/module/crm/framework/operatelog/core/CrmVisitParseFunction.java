package com.meession.etm.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CRM 拜访的 {@link IParseFunction} 实现类
 * <p>
 * 用法：在 @LogRecord 注解中使用 #{getVisitById(#visitId)}
 *
 * @author 密讯
 */
@Component
@Slf4j
public class CrmVisitParseFunction implements IParseFunction {

    public static final String NAME = "getVisitById";

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
        // TODO: OA域建表后，注入 CrmVisitMapper，通过 ID 查拜访信息
        Long id = Long.parseLong(value.toString());
        log.debug("[CrmVisitParseFunction] 解析拜访ID: {}", id);
        return "拜访#" + id;
    }
}
