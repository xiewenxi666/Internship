package com.meession.etm.module.crm.controller.admin.refund;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderRespVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundReportReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundRespVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderDO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import com.meession.etm.module.crm.service.order.CrmOrderService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.refund.CrmRefundService;
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

@Tag(name = "管理后台 - CRM 退款")
@RestController
@RequestMapping("/crm/refund")
@Validated
public class CrmRefundController {

    @Resource
    private CrmRefundService refundService;
    @Resource
    private CrmOrderService orderService;
    @Resource
    private CrmCustomerService customerService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;

    @PostMapping("/create")
    @Operation(summary = "创建退款")
    @PreAuthorize("@ss.hasPermission('crm:refund:create')")
    public CommonResult<Long> createRefund(@Valid @RequestBody CrmRefundSaveReqVO createReqVO) {
        return success(refundService.createRefund(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新退款")
    @PreAuthorize("@ss.hasPermission('crm:refund:update')")
    public CommonResult<Boolean> updateRefund(@Valid @RequestBody CrmRefundSaveReqVO updateReqVO) {
        refundService.updateRefund(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除退款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:refund:delete')")
    public CommonResult<Boolean> deleteRefund(@RequestParam("id") Long id) {
        refundService.deleteRefund(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得退款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:refund:query')")
    public CommonResult<CrmRefundRespVO> getRefund(@RequestParam("id") Long id) {
        CrmRefundDO refund = refundService.getRefund(id);
        return success(buildRefundDetail(refund));
    }

    private CrmRefundRespVO buildRefundDetail(CrmRefundDO refund) {
        if (refund == null) {
            return null;
        }
        return buildRefundDetailList(Collections.singletonList(refund)).get(0);
    }

    @GetMapping("/page")
    @Operation(summary = "获得退款分页")
    @PreAuthorize("@ss.hasPermission('crm:refund:query')")
    public CommonResult<PageResult<CrmRefundRespVO>> getRefundPage(@Valid CrmRefundPageReqVO pageReqVO) {
        PageResult<CrmRefundDO> pageResult = refundService.getRefundPage(pageReqVO, getLoginUserId());
        return success(new PageResult<>(buildRefundDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/page-by-customer")
    @Operation(summary = "获得退款分页，基于指定客户")
    public CommonResult<PageResult<CrmRefundRespVO>> getRefundPageByCustomer(@Valid CrmRefundPageReqVO pageReqVO) {
        Assert.notNull(pageReqVO.getCustomerId(), "客户编号不能为空");
        PageResult<CrmRefundDO> pageResult = refundService.getRefundPageByCustomerId(pageReqVO);
        return success(new PageResult<>(buildRefundDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出退款 Excel")
    @PreAuthorize("@ss.hasPermission('crm:refund:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRefundExcel(@Valid CrmRefundPageReqVO exportReqVO,
                                  HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmRefundDO> list = refundService.getRefundPage(exportReqVO, getLoginUserId()).getList();
        ExcelUtils.write(response, "退款.xls", "数据", CrmRefundRespVO.class,
                buildRefundDetailList(list));
    }

    private List<CrmRefundRespVO> buildRefundDetailList(List<CrmRefundDO> refundList) {
        if (CollUtil.isEmpty(refundList)) {
            return Collections.emptyList();
        }
        // 1.1 获取客户列表
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(refundList, CrmRefundDO::getCustomerId));
        // 1.2 获取创建人、负责人列表
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(refundList,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId())));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        // get order list
        Map<Long, CrmOrderDO> orderMap = orderService.getOrderMap(
                convertSet(refundList, CrmRefundDO::getOrderId));
        // build result
        return BeanUtils.toBean(refundList, CrmRefundRespVO.class, (refundVO) -> {
            findAndThen(customerMap, refundVO.getCustomerId(), customer -> refundVO.setCustomerName(customer.getName()));
            // 2.2 拼接负责人、创建人名称
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(refundVO.getCreator()),
                    user -> refundVO.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, refundVO.getOwnerUserId(), user -> {
                refundVO.setOwnerUserName(user.getNickname());
                MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> refundVO.setOwnerUserDeptName(dept.getName()));
            });
            // 拼接订单信息
            findAndThen(orderMap, refundVO.getOrderId(), order ->
                    refundVO.setOrder(BeanUtils.toBean(order, CrmOrderRespVO.class)));
        });
    }

    @PutMapping("/submit")
    @Operation(summary = "提交退款审批")
    @PreAuthorize("@ss.hasPermission('crm:refund:update')")
    public CommonResult<Boolean> submitRefund(@RequestParam("id") Long id) {
        refundService.submitRefund(id, getLoginUserId());
        return success(true);
    }

    @GetMapping("/audit-count")
    @Operation(summary = "获得待审核退款数量")
    @PreAuthorize("@ss.hasPermission('crm:refund:query')")
    public CommonResult<Long> getAuditRefundCount() {
        return success(refundService.getAuditRefundCount(getLoginUserId()));
    }

    @GetMapping("/report")
    @Operation(summary = "获得退款记录报表")
    @PreAuthorize("@ss.hasPermission('crm:refund:query')")
    public CommonResult<PageResult<CrmRefundRespVO>> getRefundReport(@Valid CrmRefundReportReqVO reqVO) {
        PageResult<CrmRefundDO> pageResult = refundService.getRefundReport(reqVO);
        return success(new PageResult<>(buildRefundDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @PutMapping("/cancel")
    @Operation(summary = "撤销退款审批")
    @PreAuthorize("@ss.hasPermission('crm:refund:update')")
    public CommonResult<Boolean> cancelRefund(@RequestParam("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        refundService.cancelRefund(id, reason);
        return success(true);
    }

    @GetMapping("/approval-page")
    @Operation(summary = "获得退款审批分页")
    @PreAuthorize("@ss.hasPermission('crm:refund:query')")
    public CommonResult<PageResult<CrmRefundRespVO>> getRefundApprovalPage(@Valid CrmRefundApprovalPageReqVO pageReqVO) {
        PageResult<CrmRefundDO> pageResult = refundService.getRefundApprovalPage(pageReqVO, getLoginUserId());
        return success(new PageResult<>(buildRefundDetailList(pageResult.getList()), pageResult.getTotal()));
    }

}
