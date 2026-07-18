// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.controller.admin.workorder;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.workorder.vo.*;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.service.workorder.CrmWorkOrderService;
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
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.*;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;


@Tag(name = "管理后台 - CRM 工单管理")
@RestController
@RequestMapping("/crm/work-order")
@Validated
public class CrmWorkOrderController {

    @Resource
    private CrmWorkOrderService workOrderService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建工单")
    @PreAuthorize("@ss.hasPermission('crm:work-order:create')")
    public CommonResult<Long> createWorkOrder(@Valid @RequestBody CrmWorkOrderSaveReqVO createReqVO) {
        return success(workOrderService.createWorkOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工单")
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> updateWorkOrder(@Valid @RequestBody CrmWorkOrderSaveReqVO updateReqVO) {
        workOrderService.updateWorkOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:delete')")
    public CommonResult<Boolean> deleteWorkOrder(@RequestParam("id") Long id) {
        workOrderService.deleteWorkOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<CrmWorkOrderRespVO> getWorkOrder(@RequestParam("id") Long id) {
        CrmWorkOrderDO workOrder = workOrderService.getWorkOrder(id);
        return success(buildWorkOrderDetail(workOrder));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工单分页")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<PageResult<CrmWorkOrderRespVO>> getWorkOrderPage(@Valid CrmWorkOrderPageReqVO pageReqVO) {
        PageResult<CrmWorkOrderDO> pageResult = workOrderService.getWorkOrderPage(pageReqVO);
        return success(new PageResult<>(buildWorkOrderDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/page-by-customer")
    @Operation(summary = "获得工单分页（基于客户）")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<PageResult<CrmWorkOrderRespVO>> getWorkOrderPageByCustomer(@Valid CrmWorkOrderPageReqVO pageReqVO) {
        PageResult<CrmWorkOrderDO> pageResult = workOrderService.getWorkOrderPageByCustomer(pageReqVO);
        return success(new PageResult<>(buildWorkOrderDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工单 Excel")
    @PreAuthorize("@ss.hasPermission('crm:work-order:export')")
    public void exportWorkOrderExcel(@Valid CrmWorkOrderPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CrmWorkOrderDO> list = workOrderService.getWorkOrderPage(pageReqVO).getList();
        ExcelUtils.write(response, "工单.xls", "数据", CrmWorkOrderRespVO.class, buildWorkOrderDetailList(list));
    }

    @PutMapping("/process")
    @Operation(summary = "处理工单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> processWorkOrder(@RequestParam("id") Long id) {
        workOrderService.processWorkOrder(id);
        return success(true);
    }

    @PutMapping("/complete")
    @Operation(summary = "工单完结")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> completeWorkOrder(@RequestParam("id") Long id, @RequestParam("solution") String solution) {
        workOrderService.completeWorkOrder(id, solution);
        return success(true);
    }

    @PutMapping("/return")
    @Operation(summary = "工单退回")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> returnWorkOrder(@RequestParam("id") Long id) {
        workOrderService.returnWorkOrder(id);
        return success(true);
    }

    private CrmWorkOrderRespVO buildWorkOrderDetail(CrmWorkOrderDO workOrder) {
        if (workOrder == null) {
            return null;
        }
        return buildWorkOrderDetailList(Collections.singletonList(workOrder)).get(0);
    }

    private List<CrmWorkOrderRespVO> buildWorkOrderDetailList(List<CrmWorkOrderDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 批量查询用户信息（负责人 + 创建人）
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSetByFlatMap(list,
                        workOrder -> Stream.of(workOrder.getOwnerUserId(), convertCreatorToLong(workOrder.getCreator()))
                                .filter(Objects::nonNull),
                        Function.identity()));

        return BeanUtils.toBean(list, CrmWorkOrderRespVO.class, vo -> {
            findAndThen(userMap, vo.getOwnerUserId(), user -> vo.setOwnerUserName(user.getNickname()));
            findAndThen(userMap, convertCreatorToLong(vo.getCreator()), user -> vo.setCreatorName(user.getNickname()));
        });
    }

    private static Long convertCreatorToLong(String creator) {
        if (creator == null || creator.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(creator);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
