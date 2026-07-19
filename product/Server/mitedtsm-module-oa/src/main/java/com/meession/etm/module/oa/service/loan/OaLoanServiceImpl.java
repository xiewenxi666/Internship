package com.meession.etm.module.oa.service.loan;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.module.oa.dal.mysql.OaLoanMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LOAN_NOT_EXISTS;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LOAN_SUBMIT_FAIL_NOT_DRAFT;

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
                .setUserId(userId).setStatus(-1);
        loanMapper.insert(loan);
        return loan.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoan(Long id, OaLoanCreateReqVO updateReqVO) {
        validateExists(id);
        OaLoanDO loan = BeanUtils.toBean(updateReqVO, OaLoanDO.class).setId(id);
        loanMapper.updateById(loan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitLoan(Long id, Long userId) {
        OaLoanDO loan = loanMapper.selectById(id);
        if (loan == null) {
            throw ServiceExceptionUtil.exception(OA_LOAN_NOT_EXISTS);
        }
        if (loan.getStatus() != -1) {
            throw ServiceExceptionUtil.exception(OA_LOAN_SUBMIT_FAIL_NOT_DRAFT);
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("amount", loan.getAmount());
        variables.put("purpose", loan.getPurpose());

        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(variables).setBusinessKey(String.valueOf(id))
                        .setStartUserSelectAssignees(loan.getStartUserSelectAssignees() != null ?
                                parseAssignees(loan.getStartUserSelectAssignees()) : null));

        loanMapper.updateById(new OaLoanDO().setId(id)
                .setProcessInstanceId(processInstanceId).setStatus(1));
    }

    @SuppressWarnings("unchecked")
    private Map<String, java.util.List<Long>> parseAssignees(String json) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateLoanStatus(Long id, Integer status) {
        validateExists(id);
        loanMapper.updateById(new OaLoanDO().setId(id).setStatus(status));
    }

    @Override
    public void deleteLoan(Long id) {
        validateExists(id);
        loanMapper.deleteById(id);
    }

    private void validateExists(Long id) {
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