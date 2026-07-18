package com.meession.etm.module.crm.service.statistics;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.statistics.vo.deal.*;
import com.meession.etm.module.crm.controller.admin.statistics.vo.forecast.*;

import java.util.List;
import java.util.Map;

/**
 * CRM 成交商机/漏斗报表/预测统计 Service
 */
public interface CrmStatisticsDealService {

    /**
     * 成交商机汇总（商机数 + 总金额）
     */
    CrmDealSummaryRespVO getDealSummary(Long deptId, Long userId, Integer year);

    /**
     * 成交商机报表分页
     */
    PageResult<Map<String, Object>> getDealBusinessPage(Long deptId, Long userId, Integer year, Integer pageNo, Integer pageSize);

    /**
     * 销售漏斗报表汇总（活跃商机数 + 总金额 + 加权金额）
     */
    CrmDealSummaryRespVO getFunnelReportSummary(Long deptId, Long userId, Integer year);

    /**
     * 活跃商机分页
     */
    PageResult<Map<String, Object>> getActiveBusinessPage(Long deptId, Long userId, Integer year, Integer pageNo, Integer pageSize);

    /**
     * 销售预测汇总（活跃商机数 + 总金额 + 加权金额）
     */
    CrmForecastSummaryRespVO getForecastSummary(Long deptId, Long userId, Integer year);

    /**
     * 销售预测按月数据
     */
    List<CrmForecastSummaryByMonthRespVO> getForecastByMonth(Long deptId, Long userId, Integer year);

}
