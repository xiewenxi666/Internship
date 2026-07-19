package com.meession.etm.module.oa.convert.loan;

import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanRespVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaLoanConvert {

    public static OaLoanRespVO convert(OaLoanDO bean) {
        return BeanUtils.toBean(bean, OaLoanRespVO.class);
    }

    public static List<OaLoanRespVO> convertList(List<OaLoanDO> list) {
        return CollectionUtils.convertList(list, OaLoanConvert::convert);
    }

    public static PageResult<OaLoanRespVO> convertPage(PageResult<OaLoanDO> page) {
        return BeanUtils.toBean(page, OaLoanRespVO.class);
    }

    public static OaLoanDO convert(OaLoanCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaLoanDO.class);
    }

    public static OaLoanDO convert(OaLoanUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaLoanDO.class);
    }

}
