package com.meession.etm.module.crm.controller.admin.finance;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.crm.controller.admin.finance.vo.CrmFinanceMonthlyVO;
import com.meession.etm.module.crm.controller.admin.finance.vo.CrmFinanceSummaryRespVO;
import com.meession.etm.module.crm.controller.admin.finance.vo.CrmFinanceTypeVO;
import com.meession.etm.module.crm.dal.dataobject.crm.CrmInvoiceDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivableDO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import com.meession.etm.module.crm.dal.mysql.crm.CrmInvoiceMapper;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivableMapper;
import com.meession.etm.module.crm.dal.mysql.refund.CrmRefundMapper;
import com.meession.etm.module.crm.dal.mysql.reimbursement.CrmReimbursementMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - CRM 财务数据分析")
@RestController
@RequestMapping("/crm/finance")
@Validated
public class CrmFinanceController {

    @Resource
    private CrmReceivableMapper receivableMapper;
    @Resource
    private CrmInvoiceMapper invoiceMapper;
    @Resource
    private CrmReimbursementMapper reimbursementMapper;
    @Resource
    private CrmRefundMapper refundMapper;

    @GetMapping("/summary")
    @Operation(summary = "财务数据汇总")
    @PreAuthorize("@ss.hasPermission('crm:finance:query')")
    public CommonResult<CrmFinanceSummaryRespVO> getSummary(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "ownerUserId", required = false) Long ownerUserId) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        List<CrmReceivableDO> receivables = receivableMapper.selectListForReport(year, ownerUserId);
        List<CrmInvoiceDO> invoices = invoiceMapper.selectListForReport(year, ownerUserId);
        List<CrmReimbursementDO> reimbursements = reimbursementMapper.selectListForReport(year, ownerUserId);
        List<CrmRefundDO> refunds = refundMapper.selectListForReport(year, ownerUserId);

        CrmFinanceSummaryRespVO respVO = new CrmFinanceSummaryRespVO();
        respVO.setReceivableCount((long) receivables.size());
        respVO.setInvoiceCount((long) invoices.size());
        respVO.setReimbursementCount((long) reimbursements.size());
        respVO.setRefundCount((long) refunds.size());

        respVO.setTotalReceivablePrice(sumPrice(receivables.stream()
                .filter(r -> r.getPrice() != null).map(CrmReceivableDO::getPrice)));
        respVO.setTotalInvoicePrice(sumPrice(invoices.stream()
                .filter(i -> i.getPrice() != null).map(CrmInvoiceDO::getPrice)));
        respVO.setTotalReimbursementPrice(sumPrice(reimbursements.stream()
                .filter(r -> r.getPrice() != null).map(CrmReimbursementDO::getPrice)));
        respVO.setTotalRefundPrice(sumPrice(refunds.stream()
                .filter(r -> r.getPrice() != null).map(CrmRefundDO::getPrice)));

        respVO.setMonthlyStats(buildMonthlyStats(receivables, invoices, reimbursements, refunds));
        respVO.setTypeStats(buildTypeStats(receivables, invoices, reimbursements, refunds));

        return success(respVO);
    }

    private BigDecimal sumPrice(java.util.stream.Stream<BigDecimal> stream) {
        return stream.reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<CrmFinanceMonthlyVO> buildMonthlyStats(List<CrmReceivableDO> receivables,
                                                          List<CrmInvoiceDO> invoices,
                                                          List<CrmReimbursementDO> reimbursements,
                                                          List<CrmRefundDO> refunds) {
        return IntStream.rangeClosed(1, 12).mapToObj(month -> {
            CrmFinanceMonthlyVO vo = new CrmFinanceMonthlyVO();
            vo.setMonth(month);
            vo.setReceivablePrice(sumPrice(receivables.stream()
                    .filter(r -> r.getReturnTime() != null && r.getReturnTime().getMonthValue() == month && r.getPrice() != null)
                    .map(CrmReceivableDO::getPrice)));
            vo.setInvoicePrice(sumPrice(invoices.stream()
                    .filter(i -> i.getInvoiceDate() != null && i.getInvoiceDate().getMonthValue() == month && i.getPrice() != null)
                    .map(CrmInvoiceDO::getPrice)));
            vo.setReimbursementPrice(sumPrice(reimbursements.stream()
                    .filter(r -> r.getApplyDate() != null && r.getApplyDate().getMonthValue() == month && r.getPrice() != null)
                    .map(CrmReimbursementDO::getPrice)));
            vo.setRefundPrice(sumPrice(refunds.stream()
                    .filter(r -> r.getRefundDate() != null && r.getRefundDate().getMonthValue() == month && r.getPrice() != null)
                    .map(CrmRefundDO::getPrice)));
            return vo;
        }).collect(Collectors.toList());
    }

    private List<CrmFinanceTypeVO> buildTypeStats(List<CrmReceivableDO> receivables,
                                                   List<CrmInvoiceDO> invoices,
                                                   List<CrmReimbursementDO> reimbursements,
                                                   List<CrmRefundDO> refunds) {
        List<CrmFinanceTypeVO> typeStats = new ArrayList<>();

        receivables.stream()
                .filter(r -> r.getReturnType() != null && r.getPrice() != null)
                .collect(Collectors.groupingBy(CrmReceivableDO::getReturnType,
                        Collectors.reducing(BigDecimal.ZERO, CrmReceivableDO::getPrice, BigDecimal::add)))
                .forEach((type, price) -> {
                    CrmFinanceTypeVO vo = new CrmFinanceTypeVO();
                    vo.setModule("receivable");
                    vo.setName("回款方式-" + type);
                    vo.setPrice(price);
                    typeStats.add(vo);
                });

        invoices.stream()
                .filter(i -> i.getType() != null && i.getPrice() != null)
                .collect(Collectors.groupingBy(CrmInvoiceDO::getType,
                        Collectors.reducing(BigDecimal.ZERO, CrmInvoiceDO::getPrice, BigDecimal::add)))
                .forEach((type, price) -> {
                    CrmFinanceTypeVO vo = new CrmFinanceTypeVO();
                    vo.setModule("invoice");
                    vo.setName("票据类型-" + type);
                    vo.setPrice(price);
                    typeStats.add(vo);
                });

        reimbursements.stream()
                .filter(r -> r.getType() != null && r.getPrice() != null)
                .collect(Collectors.groupingBy(CrmReimbursementDO::getType,
                        Collectors.reducing(BigDecimal.ZERO, CrmReimbursementDO::getPrice, BigDecimal::add)))
                .forEach((type, price) -> {
                    CrmFinanceTypeVO vo = new CrmFinanceTypeVO();
                    vo.setModule("reimbursement");
                    vo.setName("报销类型-" + type);
                    vo.setPrice(price);
                    typeStats.add(vo);
                });

        refunds.stream()
                .filter(r -> r.getType() != null && r.getPrice() != null)
                .collect(Collectors.groupingBy(CrmRefundDO::getType,
                        Collectors.reducing(BigDecimal.ZERO, CrmRefundDO::getPrice, BigDecimal::add)))
                .forEach((type, price) -> {
                    CrmFinanceTypeVO vo = new CrmFinanceTypeVO();
                    vo.setModule("refund");
                    vo.setName("退款类型-" + type);
                    vo.setPrice(price);
                    typeStats.add(vo);
                });

        return typeStats;
    }

}
