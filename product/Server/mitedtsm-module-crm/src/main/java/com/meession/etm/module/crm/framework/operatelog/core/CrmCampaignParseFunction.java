package com.meession.etm.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CRM 营销活动的 {@link IParseFunction} 实现类
 * <p>
 * 用法：在 @LogRecord 注解中使用 #{getCampaignById(#campaignId)}
 *
 * @author 密讯
 */
@Component
@Slf4j
public class CrmCampaignParseFunction implements IParseFunction {

    public static final String NAME = "getCampaignById";

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
        // TODO: 营销域建表后，注入 CrmCampaignMapper，通过 ID 查活动名称
        Long id = Long.parseLong(value.toString());
        log.debug("[CrmCampaignParseFunction] 解析活动ID: {}", id);
        return "活动#" + id;
    }
}
