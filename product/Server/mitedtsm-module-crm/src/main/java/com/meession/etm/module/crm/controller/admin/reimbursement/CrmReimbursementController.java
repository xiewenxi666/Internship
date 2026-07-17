package com.meession.etm.module.crm.controller.admin.reimbursement;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementRespVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementSaveReqVO;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractRespVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.reimbursement.CrmReimbursementService;
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
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertSet;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 报销")
@RestController
@RequestMapping("/crm/reimbursement")
@Validated
public class CrmReimbursementController {

    @Resource
    private CrmReimbursementService reimbursementService;
    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmCustomerService customerService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;

    @PostMapping("/create")
    @Operation(summary = "创建报销")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:create')")
    public CommonResult<Long> createReimbursement(@Valid @RequestBody CrmReimbursementSaveReqVO createReqVO) {
        return success(reimbursementService.createReimbursement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报销")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:update')")
    public CommonResult<Boolean> updateReimbursement(@Valid @RequestBody CrmReimbursementSaveReqVO updateReqVO) {
        reimbursementService.updateReimbursement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报销")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:delete')")
    public CommonResult<Boolean> deleteReimbursement(@RequestParam("id") Long id) {
        reimbursementService.deleteReimbursement(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报销")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:query')")
    public CommonResult<CrmReimbursementRespVO> getReimbursement(@RequestParam("id") Long id) {
        CrmReimbursementDO reimbursement = reimbursementService.getReimbursement(id);
        return success(buildReimbursementDetail(reimbursement));
    }

    private CrmReimbursementRespVO buildReimbursementDetail(CrmReimbursementDO reimbursement) {
        if (reimbursement == null) {
            return null;
        }
        return buildReimbursementDetailList(Collections.singletonList(reimbursement)).get(0);
    }

    @GetMapping("/page")
    @Operation(summary = "获得报销分页")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:query')")
    public CommonResult<PageResult<CrmReimbursementRespVO>> getReimbursementPage(@Valid CrmReimbursementPageReqVO pageReqVO) {
        PageResult<CrmReimbursementDO> pageResult = reimbursementService.getReimbursementPage(pageReqVO, getLoginUserId());
        return success(new PageResult<>(buildReimbursementDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/page-by-customer")
    @Operation(summary = "获得报销分页，基于指定客户")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:query')")
    public CommonResult<PageResult<CrmReimbursementRespVO>> getReimbursementPageByCustomer(@Valid CrmReimbursementPageReqVO pageReqVO) {
        PageResult<CrmReimbursementDO> pageResult = reimbursementService.getReimbursementPageByCustomerId(pageReqVO);
        return success(new PageResult<>(buildReimbursementDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出报销 Excel")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportReimbursementExcel(@Valid CrmReimbursementPageReqVO exportReqVO,
                                         HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmReimbursementDO> list = reimbursementService.getReimbursementPage(exportReqVO, getLoginUserId()).getList();
        ExcelUtils.write(response, "报销.xls", "数据", CrmReimbursementRespVO.class,
                buildReimbursementDetailList(list));
    }

    private List<CrmReimbursementRespVO> buildReimbursementDetailList(List<CrmReimbursementDO> reimbursementList) {
        if (CollUtil.isEmpty(reimbursementList)) {
            return Collections.emptyList();
        }
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(reimbursementList, CrmReimbursementDO::getCustomerId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(reimbursementList,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId())));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        Map<Long, CrmContractDO> contractMap = contractService.getContractMap(
                convertSet(reimbursementList, CrmReimbursementDO::getContractId));
        return BeanUtils.toBean(reimbursementList, CrmReimbursementRespVO.class, (reimbursementVO) -> {
            findAndThen(customerMap, reimbursementVO.getCustomerId(), customer -> reimbursementVO.setCustomerName(customer.getName()));
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(reimbursementVO.getCreator()),
                    user -> reimbursementVO.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, reimbursementVO.getOwnerUserId(), user -> {
                reimbursementVO.setOwnerUserName(user.getNickname());
                MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> reimbursementVO.setOwnerUserDeptName(dept.getName()));
            });
            findAndThen(contractMap, reimbursementVO.getContractId(), contract ->
                    reimbursementVO.setContract(BeanUtils.toBean(contract, CrmContractRespVO.class)));
        });
    }

    @PutMapping("/submit")
    @Operation(summary = "提交报销审批")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:update')")
    public CommonResult<Boolean> submitReimbursement(@RequestParam("id") Long id) {
        reimbursementService.submitReimbursement(id, getLoginUserId());
        return success(true);
    }

    @GetMapping("/audit-count")
    @Operation(summary = "获得待审核报销数量")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:query')")
    public CommonResult<Long> getAuditReimbursementCount() {
        return success(reimbursementService.getAuditReimbursementCount(getLoginUserId()));
    }

    @PutMapping("/cancel")
    @Operation(summary = "撤销报销审批")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:update')")
    public CommonResult<Boolean> cancelReimbursement(@RequestParam("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        reimbursementService.cancelReimbursement(id, reason);
        return success(true);
    }

    @GetMapping("/approval-page")
    @Operation(summary = "获得报销审批分页")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:query')")
    public CommonResult<PageResult<CrmReimbursementRespVO>> getReimbursementApprovalPage(@Valid CrmReimbursementApprovalPageReqVO pageReqVO) {
        PageResult<CrmReimbursementDO> pageResult = reimbursementService.getReimbursementApprovalPage(pageReqVO, getLoginUserId());
        return success(new PageResult<>(buildReimbursementDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @PutMapping("/approve")
    @Operation(summary = "审批通过报销")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:update')")
    public CommonResult<Boolean> approveReimbursement(@RequestParam("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        reimbursementService.approveReimbursement(id, reason);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "驳回报销审批")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:update')")
    public CommonResult<Boolean> rejectReimbursement(@RequestParam("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        reimbursementService.rejectReimbursement(id, reason);
        return success(true);
    }

    @PutMapping("/veto")
    @Operation(summary = "否决报销审批")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:update')")
    public CommonResult<Boolean> vetoReimbursement(@RequestParam("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        reimbursementService.vetoReimbursement(id, reason);
        return success(true);
    }

}
