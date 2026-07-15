package com.meession.etm.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CRM 任务的 {@link IParseFunction} 实现类
 * <p>
 * 用法：在 @LogRecord 注解中使用 #{getTaskById(#taskId)}
 *
 * @author 密讯
 */
@Component
@Slf4j
public class CrmTaskParseFunction implements IParseFunction {

    public static final String NAME = "getTaskById";

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
        // TODO: OA域建表后，注入 CrmTaskMapper，通过 ID 查任务标题
        Long id = Long.parseLong(value.toString());
        log.debug("[CrmTaskParseFunction] 解析任务ID: {}", id);
        return "任务#" + id;
    }
}
