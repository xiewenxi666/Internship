package com.meession.etm.module.crm.service.expense;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpensePageReqVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpenseSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertMap;

public interface CrmExpenseService {

    Long createExpense(@Valid CrmExpenseSaveReqVO createReqVO);

    void updateExpense(@Valid CrmExpenseSaveReqVO updateReqVO);

    void deleteExpense(Long id);

    CrmExpenseDO getExpense(Long id);

    List<CrmExpenseDO> getExpenseList(Collection<Long> ids);

    default Map<Long, CrmExpenseDO> getExpenseMap(Collection<Long> ids) {
        return convertMap(getExpenseList(ids), CrmExpenseDO::getId);
    }

    PageResult<CrmExpenseDO> getExpensePage(CrmExpensePageReqVO pageReqVO, Long userId);

    List<CrmExpenseDO> getExpenseListForExport(CrmExpensePageReqVO pageReqVO, Long userId);

    List<CrmExpenseDO> getExpenseListByReimbursementId(Long reimbursementId);

}
