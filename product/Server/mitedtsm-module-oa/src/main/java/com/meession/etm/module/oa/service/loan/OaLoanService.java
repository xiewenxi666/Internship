package com.meession.etm.module.oa.service.loan;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import jakarta.validation.Valid;

public interface OaLoanService {

    Long createLoan(Long userId, @Valid OaLoanCreateReqVO createReqVO);

    void updateLoanStatus(Long id, Integer status);

    OaLoanDO getLoan(Long id);

    PageResult<OaLoanDO> getLoanPage(Long userId, OaLoanPageReqVO pageReqVO);

}
