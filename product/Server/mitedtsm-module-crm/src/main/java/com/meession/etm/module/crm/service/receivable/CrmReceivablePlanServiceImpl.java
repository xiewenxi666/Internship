package com.meession.etm.module.crm.service.receivable;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.CrmReceivablePlanBatchCreateReqVO;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.CrmReceivablePlanPageReqVO;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.CrmReceivablePlanSaveReqVO;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.CrmReceivablePlanSummaryReqVO;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.CrmReceivablePlanSummaryRespVO;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.CrmReceivablePlanReportReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivablePlanMapper;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.enums.receivable.CrmReceivablePlanStatusEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.RECEIVABLE_PLAN_NOT_EXISTS;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.RECEIVABLE_PLAN_UPDATE_FAIL;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;

/**
 * 回款计划 Service 实现类
 *
 * @author 密讯
 */
@Service
@Validated
public class CrmReceivablePlanServiceImpl implements CrmReceivablePlanService {

    @Resource
    private CrmReceivablePlanMapper receivablePlanMapper;

    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmPermissionService permissionService;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_RECEIVABLE_PLAN_TYPE, subType = CRM_RECEIVABLE_PLAN_CREATE_SUB_TYPE, bizNo = "{{#receivablePlan.id}}",
            success = CRM_RECEIVABLE_PLAN_CREATE_SUCCESS)
    public Long createReceivablePlan(CrmReceivablePlanSaveReqVO createReqVO) {
        // 1. 校验关联数据是否存在
        validateRelationDataExists(createReqVO);

        // 2. 插入回款计划
        CrmReceivablePlanDO maxPeriodReceivablePlan = receivablePlanMapper.selectMaxPeriodByContractId(createReqVO.getContractId());
        int period = maxPeriodReceivablePlan == null ? 1 : maxPeriodReceivablePlan.getPeriod() + 1;
        CrmReceivablePlanDO receivablePlan = BeanUtils.toBean(createReqVO, CrmReceivablePlanDO.class).setPeriod(period);
        if (createReqVO.getReturnTime() != null && createReqVO.getRemindDays() != null) {
            receivablePlan.setRemindTime(createReqVO.getReturnTime().minusDays(createReqVO.getRemindDays()));
        }
        receivablePlanMapper.insert(receivablePlan);

        // 3. 创建数据权限
        permissionService.createPermission(new CrmPermissionCreateReqBO().setUserId(createReqVO.getOwnerUserId())
                .setBizType(CrmBizTypeEnum.CRM_RECEIVABLE_PLAN.getType()).setBizId(receivablePlan.getId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("receivablePlan", receivablePlan);
        return receivablePlan.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_RECEIVABLE_PLAN_TYPE, subType = CRM_RECEIVABLE_PLAN_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_RECEIVABLE_PLAN_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_RECEIVABLE_PLAN, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateReceivablePlan(CrmReceivablePlanSaveReqVO updateReqVO) {
        updateReqVO.setOwnerUserId(null).setCustomerId(null).setContractId(null); // 防止修改这些字段
        // 1.1 校验存在
        validateRelationDataExists(updateReqVO);
        // 1.2 校验关联数据是否存在
        CrmReceivablePlanDO oldReceivablePlan = validateReceivablePlanExists(updateReqVO.getId());
        // 1.3 如果已经有对应的回款，则不允许编辑
        if (Objects.nonNull(oldReceivablePlan.getReceivableId())) {
            throw exception(RECEIVABLE_PLAN_UPDATE_FAIL);
        }

        // 2. 更新回款计划
        CrmReceivablePlanDO updateObj = BeanUtils.toBean(updateReqVO, CrmReceivablePlanDO.class);
        if (updateReqVO.getReturnTime() != null && updateReqVO.getRemindDays() != null) {
            updateObj.setRemindTime(updateReqVO.getReturnTime().minusDays(updateReqVO.getRemindDays()));
        }
        receivablePlanMapper.updateById(updateObj);

        // 3. 记录操作日志上下文
        updateReqVO.setOwnerUserId(oldReceivablePlan.getOwnerUserId()); // 避免操作日志出现“删除负责人”的情况
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldReceivablePlan, CrmReceivablePlanSaveReqVO.class));
        LogRecordContext.putVariable("receivablePlan", oldReceivablePlan);
    }

    private void validateRelationDataExists(CrmReceivablePlanSaveReqVO reqVO) {
        // 校验负责人存在
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId());
        }
        // 校验合同存在
        if (reqVO.getContractId() != null) {
            CrmContractDO contract = contractService.getContract(reqVO.getContractId());
            reqVO.setCustomerId(contract.getCustomerId());
        }
    }

    @Override
    public void updateReceivablePlanReceivableId(Long id, Long receivableId) {
        // 校验存在
        validateReceivablePlanExists(id);
        // 更新回款计划
        receivablePlanMapper.updateById(new CrmReceivablePlanDO().setId(id).setReceivableId(receivableId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_RECEIVABLE_PLAN_TYPE, subType = CRM_RECEIVABLE_PLAN_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_RECEIVABLE_PLAN_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_RECEIVABLE_PLAN, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteReceivablePlan(Long id) {
        // 1. 校验存在
        CrmReceivablePlanDO receivablePlan = validateReceivablePlanExists(id);

        // 2. 删除
        receivablePlanMapper.deleteById(id);
        // 3. 删除数据权限
        permissionService.deletePermission(CrmBizTypeEnum.CRM_RECEIVABLE_PLAN.getType(), id);

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("receivablePlan", receivablePlan);
    }

    private CrmReceivablePlanDO validateReceivablePlanExists(Long id) {
        CrmReceivablePlanDO receivablePlan = receivablePlanMapper.selectById(id);
        if (receivablePlan == null) {
            throw exception(RECEIVABLE_PLAN_NOT_EXISTS);
        }
        return receivablePlan;
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_RECEIVABLE_PLAN, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmReceivablePlanDO getReceivablePlan(Long id) {
        return receivablePlanMapper.selectById(id);
    }

    @Override
    public List<CrmReceivablePlanDO> getReceivablePlanList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return receivablePlanMapper.selectByIds(ids);
    }

    @Override
    public PageResult<CrmReceivablePlanDO> getReceivablePlanPage(CrmReceivablePlanPageReqVO pageReqVO, Long userId) {
        return receivablePlanMapper.selectPage(pageReqVO, userId);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmReceivablePlanDO> getReceivablePlanPageByCustomerId(CrmReceivablePlanPageReqVO pageReqVO) {
        return receivablePlanMapper.selectPageByCustomerId(pageReqVO);
    }

    @Override
    public Long getReceivablePlanRemindCount(Long userId) {
        return receivablePlanMapper.selectReceivablePlanCountByRemind(userId);
    }

    /**
     * 计算回款计划状态
     */
    private Integer calcStatus(CrmReceivablePlanDO plan) {
        if (plan.getReceivableId() != null) {
            return CrmReceivablePlanStatusEnum.COMPLETED.getStatus();
        }
        if (plan.getReturnTime() != null && plan.getReturnTime().isBefore(LocalDateTime.now())) {
            return CrmReceivablePlanStatusEnum.OVERDUE.getStatus();
        }
        return CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus();
    }

    /**
     * 计算逾期天数
     */
    private Long calcOverdueDays(CrmReceivablePlanDO plan) {
        if (plan.getReceivableId() == null && plan.getReturnTime() != null && plan.getReturnTime().isBefore(LocalDateTime.now())) {
            return ChronoUnit.DAYS.between(plan.getReturnTime().toLocalDate(), LocalDate.now());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> batchCreateReceivablePlan(CrmReceivablePlanBatchCreateReqVO createReqVO) {
        // 校验负责人存在
        if (createReqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(createReqVO.getOwnerUserId());
        }
        // 校验合同存在
        CrmContractDO contract = contractService.getContract(createReqVO.getContractId());
        createReqVO.setCustomerId(contract.getCustomerId());

        CrmReceivablePlanDO maxPeriodPlan = receivablePlanMapper.selectMaxPeriodByContractId(createReqVO.getContractId());
        int startPeriod = maxPeriodPlan == null ? 1 : maxPeriodPlan.getPeriod() + 1;

        List<Long> ids = new ArrayList<>();
        BigDecimal totalPercent = BigDecimal.ZERO;

        if (CollUtil.isNotEmpty(createReqVO.getPlans())) {
            for (int i = 0; i < createReqVO.getPlans().size(); i++) {
                CrmReceivablePlanBatchCreateReqVO.PlanItem item = createReqVO.getPlans().get(i);
                CrmReceivablePlanDO plan = new CrmReceivablePlanDO();
                plan.setPeriod(startPeriod + i);
                plan.setContractId(createReqVO.getContractId());
                plan.setCustomerId(createReqVO.getCustomerId());
                plan.setOwnerUserId(createReqVO.getOwnerUserId());
                plan.setReturnTime(item.getReturnTime());
                plan.setPrice(item.getPrice());
                plan.setPercent(item.getPercent());
                plan.setReturnType(createReqVO.getReturnType());
                plan.setRemark(item.getRemark());
                plan.setStatus(CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus());
                if (Boolean.TRUE.equals(createReqVO.getRemindEnabled()) && createReqVO.getRemindDays() != null
                        && item.getReturnTime() != null) {
                    plan.setRemindDays(createReqVO.getRemindDays());
                    plan.setRemindTime(item.getReturnTime().minusDays(createReqVO.getRemindDays()));
                }
                receivablePlanMapper.insert(plan);
                permissionService.createPermission(new CrmPermissionCreateReqBO()
                        .setUserId(createReqVO.getOwnerUserId())
                        .setBizType(CrmBizTypeEnum.CRM_RECEIVABLE_PLAN.getType())
                        .setBizId(plan.getId())
                        .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));
                ids.add(plan.getId());
                if (item.getPercent() != null) {
                    totalPercent = totalPercent.add(item.getPercent());
                }
            }
        } else {
            BigDecimal perPrice = createReqVO.getTotalPrice().divide(BigDecimal.valueOf(createReqVO.getPeriodCount()), 2, RoundingMode.HALF_UP);
            BigDecimal perPercent = BigDecimal.valueOf(100).divide(BigDecimal.valueOf(createReqVO.getPeriodCount()), 2, RoundingMode.HALF_UP);
            BigDecimal priceSum = BigDecimal.ZERO;

            for (int i = 0; i < createReqVO.getPeriodCount(); i++) {
                CrmReceivablePlanDO plan = new CrmReceivablePlanDO();
                plan.setPeriod(startPeriod + i);
                plan.setContractId(createReqVO.getContractId());
                plan.setCustomerId(createReqVO.getCustomerId());
                plan.setOwnerUserId(createReqVO.getOwnerUserId());
                BigDecimal price;
                if (i == createReqVO.getPeriodCount() - 1) {
                    price = createReqVO.getTotalPrice().subtract(priceSum);
                } else {
                    price = perPrice;
                    priceSum = priceSum.add(perPrice);
                }
                plan.setPrice(price);
                plan.setPercent(perPercent);
                plan.setReturnType(createReqVO.getReturnType());
                plan.setStatus(CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus());
                if (Boolean.TRUE.equals(createReqVO.getRemindEnabled()) && createReqVO.getRemindDays() != null) {
                    plan.setRemindDays(createReqVO.getRemindDays());
                }
                receivablePlanMapper.insert(plan);
                permissionService.createPermission(new CrmPermissionCreateReqBO()
                        .setUserId(createReqVO.getOwnerUserId())
                        .setBizType(CrmBizTypeEnum.CRM_RECEIVABLE_PLAN.getType())
                        .setBizId(plan.getId())
                        .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));
                ids.add(plan.getId());
            }
        }

        LogRecordContext.putVariable("receivablePlan", ids);
        return ids;
    }

    @Override
    public List<CrmReceivablePlanSummaryRespVO> getReceivablePlanSummary(CrmReceivablePlanSummaryReqVO reqVO) {
        List<CrmReceivablePlanDO> plans = receivablePlanMapper.selectListForSummary(reqVO.getYear(), reqVO.getOwnerUserId());
        Map<String, List<CrmReceivablePlanDO>> monthMap = plans.stream()
                .collect(Collectors.groupingBy(p -> String.format("%d-%02d", p.getReturnTime().getYear(), p.getReturnTime().getMonthValue()),
                        LinkedHashMap::new, Collectors.toList()));

        List<CrmReceivablePlanSummaryRespVO> result = new ArrayList<>();
        for (Map.Entry<String, List<CrmReceivablePlanDO>> entry : monthMap.entrySet()) {
            CrmReceivablePlanSummaryRespVO vo = new CrmReceivablePlanSummaryRespVO();
            vo.setMonth(entry.getKey());
            List<CrmReceivablePlanDO> monthPlans = entry.getValue();
            BigDecimal targetPrice = monthPlans.stream().map(CrmReceivablePlanDO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal completedPrice = monthPlans.stream()
                    .filter(p -> p.getReceivableId() != null).map(CrmReceivablePlanDO::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setTargetPrice(targetPrice);
            vo.setCompletedPrice(completedPrice);
            vo.setUncompletedPrice(targetPrice.subtract(completedPrice));
            vo.setCompletionRate(targetPrice.compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO : completedPrice.multiply(BigDecimal.valueOf(100)).divide(targetPrice, 2, RoundingMode.HALF_UP));
            result.add(vo);
        }
        return result;
    }

    @Override
    public PageResult<CrmReceivablePlanDO> getReceivablePlanReport(CrmReceivablePlanReportReqVO reqVO) {
        List<CrmReceivablePlanDO> allPlans = receivablePlanMapper.selectListForReport(reqVO.getYear(), reqVO.getOwnerUserId());
        int total = allPlans.size();
        // 内存分页
        int fromIndex = (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        int toIndex = Math.min(fromIndex + reqVO.getPageSize(), total);
        if (fromIndex >= total) {
            return new PageResult<>(ListUtil.empty(), (long) total);
        }
        return new PageResult<>(allPlans.subList(fromIndex, toIndex), (long) total);
    }

}
