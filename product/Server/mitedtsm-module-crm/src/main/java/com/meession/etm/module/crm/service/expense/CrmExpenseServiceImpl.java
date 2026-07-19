package com.meession.etm.module.crm.service.expense;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpensePageReqVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpenseSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import com.meession.etm.module.crm.dal.mysql.expense.CrmExpenseMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmBizNoPrefix;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;

@Service
@Validated
@Slf4j
public class CrmExpenseServiceImpl implements CrmExpenseService {

    @Resource
    private CrmExpenseMapper expenseMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmPermissionService permissionService;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_EXPENSE_TYPE, subType = CRM_EXPENSE_CREATE_SUB_TYPE, bizNo = "{{#expense.id}}",
            success = CRM_EXPENSE_CREATE_SUCCESS)
    public Long createExpense(CrmExpenseSaveReqVO createReqVO) {
        validateRelationDataExists(createReqVO);
        String no = noRedisDAO.generate(CrmBizNoPrefix.EXPENSE);
        if (expenseMapper.selectByNo(no) != null) {
            throw exception(EXPENSE_NO_EXISTS);
        }
        CrmExpenseDO expense = BeanUtils.toBean(createReqVO, CrmExpenseDO.class).setNo(no).setReimburseStatus(0);
        expenseMapper.insert(expense);
        permissionService.createPermission(new CrmPermissionCreateReqBO().setBizType(CrmBizTypeEnum.CRM_EXPENSE.getType())
                .setBizId(expense.getId()).setUserId(createReqVO.getOwnerUserId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));
        LogRecordContext.putVariable("expense", expense);
        return expense.getId();
    }

    private void validateRelationDataExists(CrmExpenseSaveReqVO reqVO) {
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_EXPENSE_TYPE, subType = CRM_EXPENSE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_EXPENSE_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_EXPENSE, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateExpense(CrmExpenseSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "费用编号不能为空");
        CrmExpenseDO oldExpense = validateExpenseExists(updateReqVO.getId());
        CrmExpenseDO updateObj = BeanUtils.toBean(updateReqVO, CrmExpenseDO.class);
        expenseMapper.updateById(updateObj);
        LogRecordContext.putVariable("oldExpense", oldExpense);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_EXPENSE_TYPE, subType = CRM_EXPENSE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_EXPENSE_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_EXPENSE, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteExpense(Long id) {
        CrmExpenseDO expense = validateExpenseExists(id);
        if (expense.getReimbursementId() != null) {
            throw exception(EXPENSE_DELETE_FAIL_HAS_REIMBURSEMENT);
        }
        expenseMapper.deleteById(id);
        permissionService.deletePermission(CrmBizTypeEnum.CRM_EXPENSE.getType(), id);
        LogRecordContext.putVariable("expense", expense);
    }

    private CrmExpenseDO validateExpenseExists(Long id) {
        CrmExpenseDO expense = expenseMapper.selectById(id);
        if (expense == null) {
            throw exception(EXPENSE_NOT_EXISTS);
        }
        return expense;
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_EXPENSE, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmExpenseDO getExpense(Long id) {
        return expenseMapper.selectById(id);
    }

    @Override
    public List<CrmExpenseDO> getExpenseList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return expenseMapper.selectByIds(ids);
    }

    @Override
    public PageResult<CrmExpenseDO> getExpensePage(CrmExpensePageReqVO pageReqVO, Long userId) {
        return expenseMapper.selectPage(pageReqVO, userId);
    }

    @Override
    public List<CrmExpenseDO> getExpenseListForExport(CrmExpensePageReqVO pageReqVO, Long userId) {
        return getExpensePage(pageReqVO, userId).getList();
    }

    @Override
    public List<CrmExpenseDO> getExpenseListByReimbursementId(Long reimbursementId) {
        if (reimbursementId == null) return java.util.Collections.emptyList();
        return expenseMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<CrmExpenseDO>()
                        .eq(CrmExpenseDO::getReimbursementId, reimbursementId));
    }

}
