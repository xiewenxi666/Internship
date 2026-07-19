package com.meession.etm.module.oa.controller.admin.loan;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.convert.loan.OaLoanConvert;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanPageReqVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.module.oa.service.loan.OaLoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - OA 借款申请")
@RestController
@RequestMapping("/oa/loan")
@Validated
public class OaLoanController {

    @Resource
    private OaLoanService loanService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('oa:loan:create')")
    @Operation(summary = "创建借款申请")
    public CommonResult<Long> createLoan(@Valid @RequestBody OaLoanCreateReqVO createReqVO) {
        return success(loanService.createLoan(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('oa:loan:update')")
    @Operation(summary = "更新借款申请")
    public CommonResult<Boolean> updateLoan(@Valid @RequestBody OaLoanCreateReqVO updateReqVO,
                                            @RequestParam("id") Long id) {
        loanService.updateLoan(id, updateReqVO);
        return success(true);
    }

    @PutMapping("/submit")
    @PreAuthorize("@ss.hasPermission('oa:loan:submit')")
    @Operation(summary = "提交借款审批")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> submitLoan(@RequestParam("id") Long id) {
        loanService.submitLoan(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('oa:loan:update')")
    @Operation(summary = "更新借款状态")
    public CommonResult<Boolean> updateLoanStatus(@RequestParam("id") Long id,
                                                  @RequestParam("status") Integer status) {
        loanService.updateLoanStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('oa:loan:delete')")
    @Operation(summary = "删除借款申请")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteLoan(@RequestParam("id") Long id) {
        loanService.deleteLoan(id);
        return success(true);
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:loan:query')")
    @Operation(summary = "获得借款申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaLoanRespVO> getLoan(@RequestParam("id") Long id) {
        OaLoanDO loan = loanService.getLoan(id);
        return success(OaLoanConvert.convert(loan));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:loan:query')")
    @Operation(summary = "获得借款申请分页")
    public CommonResult<PageResult<OaLoanRespVO>> getLoanPage(@Valid OaLoanPageReqVO pageVO) {
        PageResult<OaLoanDO> pageResult = loanService.getLoanPage(getLoginUserId(), pageVO);
        return success(OaLoanConvert.convertPage(pageResult));
    }

}