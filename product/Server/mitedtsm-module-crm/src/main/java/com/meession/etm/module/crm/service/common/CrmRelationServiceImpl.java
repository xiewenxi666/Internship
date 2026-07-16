// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.service.common;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO;
import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO.BusinessVO;
import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO.ContractVO;
import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO.ReceivableVO;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessDO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivableDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO;
import com.meession.etm.module.crm.dal.mysql.business.CrmBusinessMapper;
import com.meession.etm.module.crm.dal.mysql.contract.CrmContractMapper;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivableMapper;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivablePlanMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CRM 关联查询服务实现
 *
 * @author 密讯
 */
@Service
@Validated
public class CrmRelationServiceImpl implements CrmRelationService {

    @Resource
    private CrmBusinessMapper businessMapper;

    @Resource
    private CrmContractMapper contractMapper;

    @Resource
    private CrmReceivableMapper receivableMapper;

    @Resource
    private CrmReceivablePlanMapper receivablePlanMapper;

    @Override
    public CustomerRelationRespVO getCustomerRelation(Long customerId) {
        CustomerRelationRespVO respVO = new CustomerRelationRespVO();

        // 查询关联商机（取最近 20 条）
        List<CrmBusinessDO> businessList = businessMapper.selectList(
                CrmBusinessDO::getCustomerId, customerId);
        if (CollUtil.isNotEmpty(businessList)) {
            List<CrmBusinessDO> topList = businessList.stream()
                    .sorted(Comparator.comparing(CrmBusinessDO::getId).reversed())
                    .limit(20).collect(Collectors.toList());
            respVO.setBusinessList(topList.stream().map(b -> BusinessVO.builder()
                    .id(b.getId()).name(b.getName())
                    .statusTypeId(b.getStatusTypeId())
                    .totalPrice(b.getTotalPrice())
                    .createTime(b.getCreateTime())
                    .build()).collect(Collectors.toList()));
        } else {
            respVO.setBusinessList(Collections.emptyList());
        }

        // 查询关联合同（取最近 20 条）
        List<CrmContractDO> contractList = contractMapper.selectList(
                CrmContractDO::getCustomerId, customerId);
        if (CollUtil.isNotEmpty(contractList)) {
            List<CrmContractDO> topList = contractList.stream()
                    .sorted(Comparator.comparing(CrmContractDO::getId).reversed())
                    .limit(20).collect(Collectors.toList());
            respVO.setContractList(topList.stream().map(c -> ContractVO.builder()
                    .id(c.getId()).no(c.getNo()).name(c.getName())
                    .totalPrice(c.getTotalPrice())
                    .auditStatus(c.getAuditStatus())
                    .createTime(c.getCreateTime())
                    .build()).collect(Collectors.toList()));
        } else {
            respVO.setContractList(Collections.emptyList());
        }

        // 查询关联回款（取最近 20 条）
        List<CrmReceivableDO> receivableList = receivableMapper.selectList(
                CrmReceivableDO::getCustomerId, customerId);
        if (CollUtil.isNotEmpty(receivableList)) {
            // 批量查询回款计划，获取期数
            List<Long> planIds = receivableList.stream()
                    .map(CrmReceivableDO::getPlanId).filter(Objects::nonNull)
                    .distinct().collect(Collectors.toList());
            Map<Long, Integer> planPeriodMap = Collections.emptyMap();
            if (CollUtil.isNotEmpty(planIds)) {
                List<CrmReceivablePlanDO> plans = receivablePlanMapper.selectBatchIds(planIds);
                planPeriodMap = new HashMap<>();
                for (CrmReceivablePlanDO plan : plans) {
                    planPeriodMap.put(plan.getId(), plan.getPeriod());
                }
            }
            final Map<Long, Integer> finalPlanPeriodMap = planPeriodMap;
            List<CrmReceivableDO> topList = receivableList.stream()
                    .sorted(Comparator.comparing(CrmReceivableDO::getId).reversed())
                    .limit(20).collect(Collectors.toList());
            respVO.setReceivableList(topList.stream().map(r -> ReceivableVO.builder()
                    .id(r.getId()).no(r.getNo())
                    .planId(r.getPlanId())
                    .period(finalPlanPeriodMap.getOrDefault(r.getPlanId(), 0))
                    .price(r.getPrice())
                    .returnTime(r.getReturnTime())
                    .build()).collect(Collectors.toList()));
        } else {
            respVO.setReceivableList(Collections.emptyList());
        }

        return respVO;
    }

}
