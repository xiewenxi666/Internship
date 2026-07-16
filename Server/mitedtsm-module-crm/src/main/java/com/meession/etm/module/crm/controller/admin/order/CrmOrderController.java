/**
 * 订单 Controller（Admin）
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.order;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderRespVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderSaveReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderTransferReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderUpdateStatusReqVO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderDO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderItemDO;
import com.meession.etm.module.crm.dal.dataobject.product.CrmProductDO;
import com.meession.etm.module.crm.service.business.CrmBusinessService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.order.CrmOrderService;
import com.meession.etm.module.crm.service.product.CrmProductService;
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
import static com.meession.etm.framework.common.util.collection.CollectionUtils.*;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 订单")
@RestController
@RequestMapping("/crm/order")
@Validated
public class CrmOrderController {

    @Resource
    private CrmOrderService orderService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private CrmBusinessService businessService;

    @Resource
    private CrmProductService productService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;

    // ==================== 订单 CRUD ====================

    /**
     * 创建订单
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @PreAuthorize("@ss.hasPermission('crm:order:create')")
    public CommonResult<Long> createOrder(@Valid @RequestBody CrmOrderSaveReqVO createReqVO) {
        return success(orderService.createOrder(createReqVO, getLoginUserId()));
    }

    /**
     * 更新订单
     */
    @PutMapping("/update")
    @Operation(summary = "更新订单")
    @PreAuthorize("@ss.hasPermission('crm:order:update')")
    public CommonResult<Boolean> updateOrder(@Valid @RequestBody CrmOrderSaveReqVO updateReqVO) {
        orderService.updateOrder(updateReqVO);
        return success(true);
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:order:delete')")
    public CommonResult<Boolean> deleteOrder(@RequestParam("id") Long id) {
        orderService.deleteOrder(id);
        return success(true);
    }

    /**
     * 获得订单详情
     */
    @GetMapping("/get")
    @Operation(summary = "获得订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:order:query')")
    public CommonResult<CrmOrderRespVO> getOrder(@RequestParam("id") Long id) {
        CrmOrderDO order = orderService.getOrder(id);
        return success(buildOrderDetail(order));
    }

    private CrmOrderRespVO buildOrderDetail(CrmOrderDO order) {
        if (order == null) {
            return null;
        }
        List<CrmOrderRespVO> list = buildOrderDetailList(Collections.singletonList(order));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 获得订单分页
     */
    @GetMapping("/page")
    @Operation(summary = "获得订单分页")
    @PreAuthorize("@ss.hasPermission('crm:order:query')")
    public CommonResult<PageResult<CrmOrderRespVO>> getOrderPage(@Valid CrmOrderPageReqVO pageVO) {
        PageResult<CrmOrderDO> pageResult = orderService.getOrderPage(pageVO, getLoginUserId());
        return success(BeanUtils.toBean(pageResult, CrmOrderRespVO.class).setList(buildOrderDetailList(pageResult.getList())));
    }

    /**
     * 获得订单分页，基于指定客户
     */
    @GetMapping("/page-by-customer")
    @Operation(summary = "获得订单分页，基于指定客户")
    public CommonResult<PageResult<CrmOrderRespVO>> getOrderPageByCustomer(@Valid CrmOrderPageReqVO pageVO) {
        PageResult<CrmOrderDO> pageResult = orderService.getOrderPageByCustomerId(pageVO);
        return success(BeanUtils.toBean(pageResult, CrmOrderRespVO.class).setList(buildOrderDetailList(pageResult.getList())));
    }

    /**
     * 获得订单分页，基于指定商机
     */
    @GetMapping("/page-by-business")
    @Operation(summary = "获得订单分页，基于指定商机")
    public CommonResult<PageResult<CrmOrderRespVO>> getOrderPageByBusiness(@Valid CrmOrderPageReqVO pageVO) {
        PageResult<CrmOrderDO> pageResult = orderService.getOrderPageByBusinessId(pageVO);
        return success(BeanUtils.toBean(pageResult, CrmOrderRespVO.class).setList(buildOrderDetailList(pageResult.getList())));
    }

    /**
     * 导出订单 Excel
     */
    @GetMapping("/export-excel")
    @Operation(summary = "导出订单 Excel")
    @PreAuthorize("@ss.hasPermission('crm:order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderExcel(@Valid CrmOrderPageReqVO exportReqVO,
                                 HttpServletResponse response) throws IOException {
        PageResult<CrmOrderDO> pageResult = orderService.getOrderPage(exportReqVO, getLoginUserId());
        ExcelUtils.write(response, "订单.xls", "数据", CrmOrderRespVO.class,
                BeanUtils.toBean(pageResult.getList(), CrmOrderRespVO.class));
    }

    /**
     * 获得订单精简列表（用于下拉选项）
     */
    @GetMapping("/simple-list")
    @Operation(summary = "获得订单精简列表", description = "主要用于前端的下拉选项")
    @Parameter(name = "customerId", description = "客户编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:order:query')")
    public CommonResult<List<CrmOrderRespVO>> getOrderSimpleList(@RequestParam("customerId") Long customerId) {
        CrmOrderPageReqVO pageReqVO = new CrmOrderPageReqVO().setCustomerId(customerId);
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<CrmOrderDO> pageResult = orderService.getOrderPageByCustomerId(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(Collections.emptyList());
        }
        return success(convertList(pageResult.getList(), order -> new CrmOrderRespVO()
                .setId(order.getId()).setNo(order.getNo()).setName(order.getName())
                .setStatus(order.getStatus()).setTotalPrice(order.getTotalPrice())));
    }

    /**
     * 获得待审核订单数量
     */
    @GetMapping("/audit-count")
    @Operation(summary = "获得待审核订单数量")
    @PreAuthorize("@ss.hasPermission('crm:order:query')")
    public CommonResult<Long> getAuditOrderCount() {
        return success(orderService.getAuditOrderCount(getLoginUserId()));
    }

    // ==================== 订单操作 ====================

    /**
     * 提交订单审批
     */
    @PutMapping("/submit")
    @Operation(summary = "提交订单审批")
    @PreAuthorize("@ss.hasPermission('crm:order:update')")
    public CommonResult<Boolean> submitOrder(@RequestParam("id") Long id) {
        orderService.submitOrder(id, getLoginUserId());
        return success(true);
    }

    /**
     * 撤回订单审批
     */
    @PutMapping("/withdraw")
    @Operation(summary = "撤回订单审批")
    @PreAuthorize("@ss.hasPermission('crm:order:update')")
    public CommonResult<Boolean> withdrawOrder(@RequestParam("id") Long id) {
        orderService.withdrawOrder(id, getLoginUserId());
        return success(true);
    }

    /**
     * 转移订单
     */
    @PutMapping("/transfer")
    @Operation(summary = "转移订单")
    @PreAuthorize("@ss.hasPermission('crm:order:update')")
    public CommonResult<Boolean> transferOrder(@Valid @RequestBody CrmOrderTransferReqVO reqVO) {
        orderService.transferOrder(reqVO, getLoginUserId());
        return success(true);
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/update-status")
    @Operation(summary = "更新订单状态")
    @PreAuthorize("@ss.hasPermission('crm:order:update')")
    public CommonResult<Boolean> updateOrderStatus(@Valid @RequestBody CrmOrderUpdateStatusReqVO reqVO) {
        orderService.updateOrderStatus(reqVO);
        return success(true);
    }

    private List<CrmOrderRespVO> buildOrderDetailList(List<CrmOrderDO> orderList) {
        if (CollUtil.isEmpty(orderList)) {
            return Collections.emptyList();
        }
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(orderList, CrmOrderDO::getCustomerId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(orderList,
                order -> Stream.of(NumberUtils.parseLong(order.getCreator()), order.getOwnerUserId())));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        Map<Long, com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessDO> businessMap =
                businessService.getBusinessMap(convertSet(orderList, CrmOrderDO::getBusinessId));

        return BeanUtils.toBean(orderList, CrmOrderRespVO.class, orderVO -> {
            findAndThen(customerMap, orderVO.getCustomerId(), customer -> orderVO.setCustomerName(customer.getName()));
            findAndThen(userMap, NumberUtils.parseLong(orderVO.getCreator()), user -> orderVO.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, orderVO.getOwnerUserId(), user -> {
                orderVO.setOwnerUserName(user.getNickname());
                MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> orderVO.setOwnerUserDeptName(dept.getName()));
            });
            findAndThen(businessMap, orderVO.getBusinessId(), business -> orderVO.setBusinessName(business.getName()));
            List<CrmOrderItemDO> items = orderService.getOrderProductListByOrderId(orderVO.getId());
            Map<Long, CrmProductDO> productMap = convertMap(
                    productService.getProductList(convertSet(items, CrmOrderItemDO::getProductId)),
                    CrmProductDO::getId);
            orderVO.setProducts(BeanUtils.toBean(items, CrmOrderRespVO.Product.class, productVO ->
                    findAndThen(productMap, productVO.getProductId(),
                            product -> productVO.setProductName(product.getName())
                                    .setProductNo(product.getNo()).setProductUnit(product.getUnit()))));
        });
    }

}
