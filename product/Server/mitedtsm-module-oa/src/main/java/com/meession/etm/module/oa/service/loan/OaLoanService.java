package com.meession.etm.module.oa.service.loan;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import jakarta.validation.Valid;

public interface OaLoanService {

    Long createLoan(Long userId, @Valid OaLoanCreateReqVO createReqVO);

    void updateLoan(Long id, @Valid OaLoanCreateReqVO updateReqVO);

    void updateLoanStatus(Long id, Integer status);

    void deleteLoan(Long id);

    OaLoanDO getLoan(Long id);

    PageResult<OaLoanDO> getLoanPage(Long userId, OaLoanPageReqVO pageReqVO);

    void submitLoan(Long id, Long userId);

}