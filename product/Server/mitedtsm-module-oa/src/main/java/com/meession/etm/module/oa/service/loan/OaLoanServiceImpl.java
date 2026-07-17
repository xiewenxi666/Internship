package com.meession.etm.module.oa.service.loan;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.module.oa.dal.mapper.OaLoanMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LOAN_NOT_EXISTS;

@Slf4j
@Service
@Validated
public class OaLoanServiceImpl implements OaLoanService {

    public static final String PROCESS_KEY = "oa_loan";

    @Resource
    private OaLoanMapper loanMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLoan(Long userId, OaLoanCreateReqVO createReqVO) {
        OaLoanDO loan = BeanUtils.toBean(createReqVO, OaLoanDO.class)
                .setUserId(userId).setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        loanMapper.insert(loan);

        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(loan.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees()));

        loanMapper.updateById(new OaLoanDO().setId(loan.getId()).setProcessInstanceId(processInstanceId));
        return loan.getId();
    }

    @Override
    public void updateLoanStatus(Long id, Integer status) {
        validateLoanExists(id);
        loanMapper.updateById(new OaLoanDO().setId(id).setStatus(status));
    }

    private void validateLoanExists(Long id) {
        if (loanMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_LOAN_NOT_EXISTS);
        }
    }

    @Override
    public OaLoanDO getLoan(Long id) {
        return loanMapper.selectById(id);
    }

    @Override
    public PageResult<OaLoanDO> getLoanPage(Long userId, OaLoanPageReqVO pageReqVO) {
        return loanMapper.selectPage(userId, pageReqVO);
    }

}
