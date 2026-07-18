package com.meession.etm.module.crm.dal.mysql.statistics;

import com.meession.etm.module.crm.controller.admin.statistics.vo.deal.CrmDealPageReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * CRM 成交商机/漏斗报表/预测统计 Mapper
 */
@Mapper
public interface CrmStatisticsDealMapper {

    /**
     * 汇总：商机数、总价、加权金额（end_status 按条件传 null 或 1）
     */
    Map<String, Object> selectSummaryByEndStatus(@Param("userIds") List<Long> userIds,
                                                  @Param("year") Integer year,
                                                  @Param("endStatus") Integer endStatus);

    /**
     * 成交商机分页列表
     */
    List<Map<String, Object>> selectDealBusinessPage(@Param("userIds") List<Long> userIds,
                                                      @Param("year") Integer year,
                                                      @Param("offset") int offset,
                                                      @Param("pageSize") int pageSize);

    /**
     * 成交商机总数（用于分页）
     */
    Long countDealBusiness(@Param("userIds") List<Long> userIds,
                            @Param("year") Integer year);

    /**
     * 按月统计：group by month
     */
    List<Map<String, Object>> selectSummaryGroupByMonth(@Param("userIds") List<Long> userIds,
                                                         @Param("year") Integer year,
                                                         @Param("endStatus") Integer endStatus);

}
