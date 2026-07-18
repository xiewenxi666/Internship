package com.meession.etm.module.crm.controller.admin.marketing;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.crm.dal.mysql.bulksend.CrmBulkSendMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meession.etm.module.crm.dal.dataobject.bulksend.CrmBulkSendDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发送分析")
@RestController
@RequestMapping("/crm/marketing-analysis")
@Validated
public class MarketingAnalysisController {

    @Resource
    private CrmBulkSendMapper bulkSendMapper;

    @GetMapping("/stats")
    @Operation(summary = "获取发送统计数据")
    @PreAuthorize("@ss.hasPermission('crm:marketing-analysis:query')")
    public CommonResult<Map<String, Object>> getStats() {
        List<CrmBulkSendDO> all = bulkSendMapper.selectList();
        long totalSent = all.stream().mapToLong(b -> b.getTargetCount() != null ? b.getTargetCount() : 0).sum();
        if (totalSent == 0) totalSent = all.stream().mapToLong(b -> (b.getSuccessCount() != null ? b.getSuccessCount() : 0) + (b.getFailCount() != null ? b.getFailCount() : 0)).sum();
        long totalSuccess = all.stream().mapToLong(b -> b.getSuccessCount() != null ? b.getSuccessCount() : 0).sum();
        long totalFail = all.stream().mapToLong(b -> b.getFailCount() != null ? b.getFailCount() : 0).sum();
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalSent", totalSent);
        stats.put("totalDelivered", totalSuccess);
        stats.put("deliveryRate", totalSent > 0 ? Math.round(totalSuccess * 10000.0 / totalSent) / 100.0 : 0);
        stats.put("totalConverted", totalSuccess);
        stats.put("conversionRate", totalSent > 0 ? Math.round(totalSuccess * 10000.0 / totalSent) / 100.0 : 0);
        // 月度统计：按创建月份分组聚合
        Map<String, long[]> monthly = new LinkedHashMap<>();
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM");
        for (CrmBulkSendDO b : all) {
            if (b.getCreateTime() != null) {
                String month = b.getCreateTime().format(fmt);
                long[] counts = monthly.computeIfAbsent(month, k -> new long[4]);
                long t = b.getTargetCount() != null ? b.getTargetCount() : 0;
                if (t == 0) t = (b.getSuccessCount() != null ? b.getSuccessCount() : 0) + (b.getFailCount() != null ? b.getFailCount() : 0);
                counts[0] += t;
                counts[1] += (b.getSuccessCount() != null ? b.getSuccessCount() : 0);
                counts[2] += (b.getFailCount() != null ? b.getFailCount() : 0);
            }
        }
        java.util.List<Map<String, Object>> monthlyData = new java.util.ArrayList<>();
        for (Map.Entry<String, long[]> e : monthly.entrySet()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("month", e.getKey());
            long[] c = e.getValue();
            row.put("sent", c[0]);
            row.put("delivered", c[1]);
            row.put("deliveryRate", c[0] > 0 ? Math.round(c[1] * 10000.0 / c[0]) / 100.0 : 0);
            row.put("converted", c[1]);
            row.put("conversionRate", c[0] > 0 ? Math.round(c[1] * 10000.0 / c[0]) / 100.0 : 0);
            monthlyData.add(row);
        }
        stats.put("smsCount", all.stream().filter(b -> b.getType() != null && b.getType() == 1).count());
        stats.put("emailCount", all.stream().filter(b -> b.getType() != null && b.getType() == 2).count());
        stats.put("monthlyData", monthlyData);
        return success(stats);
    }

}
