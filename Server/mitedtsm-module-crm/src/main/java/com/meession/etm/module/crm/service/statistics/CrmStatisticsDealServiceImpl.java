package com.meession.etm.module.crm.service.statistics;

import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.statistics.vo.deal.*;
import com.meession.etm.module.crm.controller.admin.statistics.vo.forecast.CrmForecastSummaryByMonthRespVO;
import com.meession.etm.module.crm.controller.admin.statistics.vo.forecast.CrmForecastSummaryRespVO;
import com.meession.etm.module.crm.dal.mysql.statistics.CrmStatisticsDealMapper;
import com.meession.etm.module.system.api.dept.DeptApi;
import com.meession.etm.module.system.api.dept.dto.DeptRespDTO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertList;

/**
 * CRM 成交商机/漏斗报表/预测统计 Service 实现
 */
@Service
public class CrmStatisticsDealServiceImpl implements CrmStatisticsDealService {

    @Resource
    private CrmStatisticsDealMapper dealMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private DeptApi deptApi;

    @Override
    public CrmDealSummaryRespVO getDealSummary(Long deptId, Long userId, Integer year) {
        List<Long> userIds = getUserIds(deptId, userId);
        if (userIds.isEmpty()) {
            return new CrmDealSummaryRespVO();
        }
        Map<String, Object> result = dealMapper.selectSummaryByEndStatus(userIds, year, 1);
        CrmDealSummaryRespVO vo = new CrmDealSummaryRespVO();
        vo.setBusinessCount(((Number) result.getOrDefault("businessCount", 0L)).longValue());
        vo.setTotalPrice(((Number) result.getOrDefault("totalPrice", 0.0)).doubleValue());
        vo.setProbabilityPrice(((Number) result.getOrDefault("probabilityPrice", 0.0)).doubleValue());
        return vo;
    }

    @Override
    public PageResult<Map<String, Object>> getDealBusinessPage(Long deptId, Long userId, Integer year, Integer pageNo, Integer pageSize) {
        List<Long> userIds = getUserIds(deptId, userId);
        if (userIds.isEmpty()) {
            return PageResult.empty();
        }
        Long total = dealMapper.countDealBusiness(userIds, year);
        if (total == 0) {
            return PageResult.empty();
        }
        int offset = (pageNo - 1) * pageSize;
        List<Map<String, Object>> list = dealMapper.selectDealBusinessPage(userIds, year, offset, pageSize);
        return new PageResult<>(list, total);
    }

    @Override
    public CrmDealSummaryRespVO getFunnelReportSummary(Long deptId, Long userId, Integer year) {
        List<Long> userIds = getUserIds(deptId, userId);
        if (userIds.isEmpty()) {
            return new CrmDealSummaryRespVO();
        }
        Map<String, Object> result = dealMapper.selectSummaryByEndStatus(userIds, year, null);
        CrmDealSummaryRespVO vo = new CrmDealSummaryRespVO();
        vo.setBusinessCount(((Number) result.getOrDefault("businessCount", 0L)).longValue());
        vo.setTotalPrice(((Number) result.getOrDefault("totalPrice", 0.0)).doubleValue());
        vo.setProbabilityPrice(((Number) result.getOrDefault("probabilityPrice", 0.0)).doubleValue());
        return vo;
    }

    @Override
    public PageResult<Map<String, Object>> getActiveBusinessPage(Long deptId, Long userId, Integer year, Integer pageNo, Integer pageSize) {
        List<Long> userIds = getUserIds(deptId, userId);
        if (userIds.isEmpty()) {
            return PageResult.empty();
        }
        return PageResult.empty();
    }

    @Override
    public CrmForecastSummaryRespVO getForecastSummary(Long deptId, Long userId, Integer year) {
        List<Long> userIds = getUserIds(deptId, userId);
        if (userIds.isEmpty()) {
            return new CrmForecastSummaryRespVO();
        }
        Map<String, Object> result = dealMapper.selectSummaryByEndStatus(userIds, year, null);
        CrmForecastSummaryRespVO vo = new CrmForecastSummaryRespVO();
        vo.setBusinessCount(((Number) result.getOrDefault("businessCount", 0L)).longValue());
        vo.setTotalPrice(((Number) result.getOrDefault("totalPrice", 0.0)).doubleValue());
        vo.setProbabilityPrice(((Number) result.getOrDefault("probabilityPrice", 0.0)).doubleValue());
        return vo;
    }

    @Override
    public List<CrmForecastSummaryByMonthRespVO> getForecastByMonth(Long deptId, Long userId, Integer year) {
        List<Long> userIds = getUserIds(deptId, userId);
        if (userIds.isEmpty()) {
            return List.of();
        }
        return dealMapper.selectSummaryGroupByMonth(userIds, year, null).stream().map(m -> {
            CrmForecastSummaryByMonthRespVO vo = new CrmForecastSummaryByMonthRespVO();
            vo.setMonth((String) m.get("month"));
            vo.setBusinessCount(((Number) m.getOrDefault("businessCount", 0L)).longValue());
            vo.setTotalPrice(((Number) m.getOrDefault("totalPrice", 0.0)).doubleValue());
            vo.setProbabilityPrice(((Number) m.getOrDefault("probabilityPrice", 0.0)).doubleValue());
            return vo;
        }).toList();
    }

    private List<Long> getUserIds(Long deptId, Long userId) {
        if (ObjUtil.isNotNull(userId)) {
            return List.of(userId);
        }
        List<Long> deptIds = convertList(deptApi.getChildDeptList(deptId), DeptRespDTO::getId);
        deptIds.add(deptId);
        return convertList(adminUserApi.getUserListByDeptIds(deptIds), AdminUserRespDTO::getId);
    }

}
