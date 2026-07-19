package com.meession.etm.module.crm.controller.admin.crm.invoice;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoiceReportReqVO;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoiceRespVO;
import com.meession.etm.module.crm.controller.admin.crm.invoice.vo.CrmInvoiceSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.crm.CrmInvoiceDO;
import com.meession.etm.module.crm.service.crm.CrmInvoiceService;
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

@Tag(name = "管理后台 - CRM 发票")
@RestController
@RequestMapping("/crm/invoice")
@Validated
public class CrmInvoiceController {

    @Resource
    private CrmInvoiceService invoiceService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;

    @PostMapping("/create")
    @Operation(summary = "创建发票")
    @PreAuthorize("@ss.hasPermission('crm:invoice:create')")
    public CommonResult<Long> createInvoice(@Valid @RequestBody CrmInvoiceSaveReqVO createReqVO) {
        return success(invoiceService.createInvoice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发票")
    @PreAuthorize("@ss.hasPermission('crm:invoice:update')")
    public CommonResult<Boolean> updateInvoice(@Valid @RequestBody CrmInvoiceSaveReqVO updateReqVO) {
        invoiceService.updateInvoice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发票")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:invoice:delete')")
    public CommonResult<Boolean> deleteInvoice(@RequestParam("id") Long id) {
        invoiceService.deleteInvoice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发票")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:invoice:query')")
    public CommonResult<CrmInvoiceRespVO> getInvoice(@RequestParam("id") Long id) {
        CrmInvoiceDO invoice = invoiceService.getInvoice(id);
        return success(buildInvoiceDetail(invoice));
    }

    private CrmInvoiceRespVO buildInvoiceDetail(CrmInvoiceDO invoice) {
        if (invoice == null) {
            return null;
        }
        return buildInvoiceDetailList(Collections.singletonList(invoice)).get(0);
    }

    @GetMapping("/page")
    @Operation(summary = "获得发票分页")
    @PreAuthorize("@ss.hasPermission('crm:invoice:query')")
    public CommonResult<PageResult<CrmInvoiceRespVO>> getInvoicePage(@Valid CrmInvoicePageReqVO pageReqVO) {
        PageResult<CrmInvoiceDO> pageResult = invoiceService.getInvoicePage(pageReqVO, getLoginUserId());
        return success(new PageResult<>(buildInvoiceDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/report")
    @Operation(summary = "获得发票记录报表")
    @PreAuthorize("@ss.hasPermission('crm:invoice:query')")
    public CommonResult<PageResult<CrmInvoiceRespVO>> getInvoiceReport(@Valid CrmInvoiceReportReqVO reqVO) {
        PageResult<CrmInvoiceDO> pageResult = invoiceService.getInvoiceReport(reqVO);
        return success(new PageResult<>(buildInvoiceDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export")
    @Operation(summary = "导出发票 Excel")
    @PreAuthorize("@ss.hasPermission('crm:invoice:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInvoiceExcel(@Valid CrmInvoicePageReqVO exportReqVO,
                                    HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmInvoiceDO> list = invoiceService.getInvoiceListForExport(exportReqVO, getLoginUserId());
        ExcelUtils.write(response, "发票.xls", "数据", CrmInvoiceRespVO.class,
                buildInvoiceDetailList(list));
    }

    private List<CrmInvoiceRespVO> buildInvoiceDetailList(List<CrmInvoiceDO> invoiceList) {
        if (CollUtil.isEmpty(invoiceList)) {
            return Collections.emptyList();
        }
        // 1. 获取创建人、负责人、经手人列表
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(invoiceList,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId(), contact.getHandlerUserId())));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        // 2. 拼接结果
        return BeanUtils.toBean(invoiceList, CrmInvoiceRespVO.class, (invoiceVO) -> {
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(invoiceVO.getCreator()),
                    user -> invoiceVO.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, invoiceVO.getOwnerUserId(), user -> {
                invoiceVO.setOwnerUserName(user.getNickname());
                MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> invoiceVO.setOwnerUserDeptName(dept.getName()));
            });
            MapUtils.findAndThen(userMap, invoiceVO.getHandlerUserId(), user -> {
                invoiceVO.setHandlerUserName(user.getNickname());
                MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> invoiceVO.setHandlerUserDeptName(dept.getName()));
            });
        });
    }

}
