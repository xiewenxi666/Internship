package com.meession.etm.module.oa.controller.admin.oa.loan;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanPageReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanRespVO;
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

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('oa:loan:query')")
    @Operation(summary = "获得借款申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<OaLoanRespVO> getLoan(@RequestParam("id") Long id) {
        OaLoanDO loan = loanService.getLoan(id);
        return success(BeanUtils.toBean(loan, OaLoanRespVO.class));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('oa:loan:query')")
    @Operation(summary = "获得借款申请分页")
    public CommonResult<PageResult<OaLoanRespVO>> getLoanPage(@Valid OaLoanPageReqVO pageVO) {
        PageResult<OaLoanDO> pageResult = loanService.getLoanPage(getLoginUserId(), pageVO);
        return success(BeanUtils.toBean(pageResult, OaLoanRespVO.class));
    }

}
