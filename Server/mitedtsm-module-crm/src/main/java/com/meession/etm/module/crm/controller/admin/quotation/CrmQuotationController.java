package com.meession.etm.module.crm.controller.admin.quotation;

import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationPageReqVO;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationRespVO;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.quotation.CrmQuotationDO;
import com.meession.etm.module.crm.dal.mysql.quotation.CrmQuotationProductMapper;
import com.meession.etm.module.crm.dal.dataobject.quotation.CrmQuotationProductDO;
import com.meession.etm.module.crm.service.quotation.CrmQuotationService;
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
import java.util.List;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 报价单")
@RestController
@RequestMapping("/crm/quotation")
@Validated
public class CrmQuotationController {

    @Resource
    private CrmQuotationService quotationService;

    @Resource
    private CrmQuotationProductMapper quotationProductMapper;

    @PostMapping("/create")
    @Operation(summary = "创建报价单")
    @PreAuthorize("@ss.hasPermission('crm:quotation:create')")
    public CommonResult<Long> createQuotation(@Valid @RequestBody CrmQuotationSaveReqVO createReqVO) {
        return success(quotationService.createQuotation(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报价单")
    @PreAuthorize("@ss.hasPermission('crm:quotation:update')")
    public CommonResult<Boolean> updateQuotation(@Valid @RequestBody CrmQuotationSaveReqVO updateReqVO) {
        quotationService.updateQuotation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报价单")
    @Parameter(name = "id", description = "报价单ID", required = true)
    @PreAuthorize("@ss.hasPermission('crm:quotation:delete')")
    public CommonResult<Boolean> deleteQuotation(@RequestParam("id") Long id) {
        quotationService.deleteQuotation(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报价单")
    @Parameter(name = "id", description = "报价单ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:quotation:query')")
    public CommonResult<CrmQuotationRespVO> getQuotation(@RequestParam("id") Long id) {
        CrmQuotationDO quotation = quotationService.getQuotation(id);
        CrmQuotationRespVO respVO = BeanUtils.toBean(quotation, CrmQuotationRespVO.class);
        // 加载报价单明细
        List<CrmQuotationProductDO> products = quotationProductMapper.selectListByQuotationId(id);
        respVO.setProducts(BeanUtils.toBean(products, CrmQuotationRespVO.ProductItemVO.class));
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得报价单分页")
    @PreAuthorize("@ss.hasPermission('crm:quotation:query')")
    public CommonResult<PageResult<CrmQuotationRespVO>> getQuotationPage(@Valid CrmQuotationPageReqVO pageVO) {
        PageResult<CrmQuotationDO> pageResult = quotationService.getQuotationPage(pageVO);
        return success(BeanUtils.toBean(pageResult, CrmQuotationRespVO.class));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交报价单审批")
    @Parameter(name = "id", description = "报价单ID", required = true)
    @PreAuthorize("@ss.hasPermission('crm:quotation:update')")
    public CommonResult<Boolean> submitQuotation(@RequestParam("id") Long id) {
        quotationService.submitQuotation(id);
        return success(true);
    }

    @PutMapping("/confirm")
    @Operation(summary = "确认报价单")
    @Parameter(name = "id", description = "报价单ID", required = true)
    @PreAuthorize("@ss.hasPermission('crm:quotation:update')")
    public CommonResult<Boolean> confirmQuotation(@RequestParam("id") Long id,
                                                  @RequestParam(value = "auditRemark", required = false) String auditRemark) {
        quotationService.confirmQuotation(id, getLoginUserId(), auditRemark);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "拒绝报价单")
    @Parameter(name = "id", description = "报价单ID", required = true)
    @PreAuthorize("@ss.hasPermission('crm:quotation:update')")
    public CommonResult<Boolean> rejectQuotation(@RequestParam("id") Long id,
                                                 @RequestParam(value = "auditRemark", required = false) String auditRemark) {
        quotationService.rejectQuotation(id, getLoginUserId(), auditRemark);
        return success(true);
    }

    @PutMapping("/void")
    @Operation(summary = "作废报价单")
    @Parameter(name = "id", description = "报价单ID", required = true)
    @PreAuthorize("@ss.hasPermission('crm:quotation:update')")
    public CommonResult<Boolean> voidQuotation(@RequestParam("id") Long id) {
        quotationService.voidQuotation(id);
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出报价单 Excel")
    @PreAuthorize("@ss.hasPermission('crm:quotation:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportQuotationExcel(@Valid CrmQuotationPageReqVO exportReqVO,
                                     HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<CrmQuotationDO> pageResult = quotationService.getQuotationPage(exportReqVO);
        List<CrmQuotationRespVO> list = BeanUtils.toBean(pageResult.getList(), CrmQuotationRespVO.class);
        ExcelUtils.write(response, "报价单.xls", "数据", CrmQuotationRespVO.class, list);
    }

}
