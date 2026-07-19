package com.meession.etm.module.crm.controller.admin.expense;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractRespVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpensePageReqVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpenseRespVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpenseSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.expense.CrmExpenseService;
import com.meession.etm.module.system.api.dept.DeptApi;
import com.meession.etm.module.system.api.dept.dto.DeptRespDTO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.*;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 费用")
@RestController
@RequestMapping("/crm/expense")
@Validated
public class CrmExpenseController {

    @Resource
    private CrmExpenseService expenseService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private CrmContractService contractService;

    @PostMapping("/create")
    @Operation(summary = "创建费用")
    @PreAuthorize("@ss.hasPermission('crm:expense:create')")
    public CommonResult<Long> createExpense(@Valid @RequestBody CrmExpenseSaveReqVO createReqVO) {
        return success(expenseService.createExpense(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新费用")
    @PreAuthorize("@ss.hasPermission('crm:expense:update')")
    public CommonResult<Boolean> updateExpense(@Valid @RequestBody CrmExpenseSaveReqVO updateReqVO) {
        expenseService.updateExpense(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除费用")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:expense:delete')")
    public CommonResult<Boolean> deleteExpense(@RequestParam("id") Long id) {
        expenseService.deleteExpense(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得费用")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:expense:query')")
    public CommonResult<CrmExpenseRespVO> getExpense(@RequestParam("id") Long id) {
        CrmExpenseDO expense = expenseService.getExpense(id);
        return success(buildExpenseDetail(expense));
    }

    private CrmExpenseRespVO buildExpenseDetail(CrmExpenseDO expense) {
        if (expense == null) {
            return null;
        }
        return buildExpenseDetailList(Collections.singletonList(expense)).get(0);
    }

    @GetMapping("/page")
    @Operation(summary = "获得费用分页")
    @PreAuthorize("@ss.hasPermission('crm:expense:query')")
    public CommonResult<PageResult<CrmExpenseRespVO>> getExpensePage(@Valid CrmExpensePageReqVO pageReqVO) {
        PageResult<CrmExpenseDO> pageResult = expenseService.getExpensePage(pageReqVO, getLoginUserId());
        return success(new PageResult<>(buildExpenseDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出费用 Excel")
    @PreAuthorize("@ss.hasPermission('crm:expense:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExpenseExcel(@Valid CrmExpensePageReqVO exportReqVO,
                                    HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmExpenseDO> list = expenseService.getExpenseListForExport(exportReqVO, getLoginUserId());
        ExcelUtils.write(response, "费用.xls", "数据", CrmExpenseRespVO.class,
                buildExpenseDetailList(list));
    }

    private List<CrmExpenseRespVO> buildExpenseDetailList(List<CrmExpenseDO> expenseList) {
        if (CollUtil.isEmpty(expenseList)) {
            return Collections.emptyList();
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(expenseList,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId())));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(expenseList, CrmExpenseDO::getCustomerId));
        Map<Long, CrmContractDO> contractMap = contractService.getContractMap(
                convertSet(expenseList, CrmExpenseDO::getContractId));
        return BeanUtils.toBean(expenseList, CrmExpenseRespVO.class, (expenseVO) -> {
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(expenseVO.getCreator()),
                    user -> expenseVO.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, expenseVO.getOwnerUserId(), user -> {
                expenseVO.setOwnerUserName(user.getNickname());
                MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> expenseVO.setOwnerUserDeptName(dept.getName()));
            });
            MapUtils.findAndThen(customerMap, expenseVO.getCustomerId(),
                    customer -> expenseVO.setCustomerName(customer.getName()));
            MapUtils.findAndThen(contractMap, expenseVO.getContractId(),
                    contract -> expenseVO.setContract(BeanUtils.toBean(contract, CrmContractRespVO.class)));
        });
    }

}
