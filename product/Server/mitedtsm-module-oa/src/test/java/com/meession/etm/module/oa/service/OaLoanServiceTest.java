package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.module.oa.dal.mysql.OaLoanMapper;
import com.meession.etm.module.oa.service.loan.OaLoanServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.annotation.Resource;
import java.math.BigDecimal;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LOAN_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaLoanServiceImpl.class)
public class OaLoanServiceTest extends BaseDbUnitTest {

    @Resource
    private OaLoanServiceImpl loanService;

    @Resource
    private OaLoanMapper loanMapper;

    @MockitoBean
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    public void testCreateLoan_success() {
        OaLoanCreateReqVO reqVO = randomPojo(OaLoanCreateReqVO.class, o -> {
            o.setAmount(BigDecimal.valueOf(1000));
        });
        Long userId = randomLongId();

        Long loanId = loanService.createLoan(userId, reqVO);

        assertNotNull(loanId);
        OaLoanDO loan = loanMapper.selectById(loanId);
        assertEquals(userId, loan.getUserId());
        assertEquals(-1, loan.getStatus());
    }

    @Test
    public void testUpdateLoan_success() {
        OaLoanDO dbLoan = randomPojo(OaLoanDO.class, o -> {
            o.setStatus(-1);
            o.setAmount(BigDecimal.valueOf(1000));
        });
        loanMapper.insert(dbLoan);

        OaLoanCreateReqVO updateReqVO = randomPojo(OaLoanCreateReqVO.class, o -> {
            o.setAmount(BigDecimal.valueOf(2000));
        });

        loanService.updateLoan(dbLoan.getId(), updateReqVO);

        OaLoanDO updated = loanMapper.selectById(dbLoan.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status",
                "processInstanceId", "startUserSelectAssignees",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId",
                "amount");
    }

    @Test
    public void testUpdateLoan_notExists() {
        OaLoanCreateReqVO reqVO = randomPojo(OaLoanCreateReqVO.class, o -> {
            o.setAmount(BigDecimal.valueOf(1000));
        });
        assertServiceException(() -> loanService.updateLoan(randomLongId(), reqVO), OA_LOAN_NOT_EXISTS);
    }

    @Test
    public void testDeleteLoan_success() {
        OaLoanDO dbLoan = randomPojo(OaLoanDO.class, o -> {
            o.setAmount(BigDecimal.valueOf(1000));
        });
        loanMapper.insert(dbLoan);

        loanService.deleteLoan(dbLoan.getId());

        assertNull(loanMapper.selectById(dbLoan.getId()));
    }

    @Test
    public void testUpdateLoanStatus() {
        OaLoanDO dbLoan = randomPojo(OaLoanDO.class, o -> {
            o.setStatus(-1);
            o.setAmount(BigDecimal.valueOf(1000));
        });
        loanMapper.insert(dbLoan);

        loanService.updateLoanStatus(dbLoan.getId(), 2);

        OaLoanDO updated = loanMapper.selectById(dbLoan.getId());
        assertEquals(2, (int) updated.getStatus());
    }
}
