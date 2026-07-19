package com.meession.etm.module.oa.convert.visit;

import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitRespVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaVisitConvert {

    public static OaVisitRespVO convert(OaVisitDO bean) {
        return BeanUtils.toBean(bean, OaVisitRespVO.class);
    }

    public static List<OaVisitRespVO> convertList(List<OaVisitDO> list) {
        return CollectionUtils.convertList(list, OaVisitConvert::convert);
    }

    public static PageResult<OaVisitRespVO> convertPage(PageResult<OaVisitDO> page) {
        return BeanUtils.toBean(page, OaVisitRespVO.class);
    }

    public static OaVisitDO convert(OaVisitCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaVisitDO.class);
    }

    public static OaVisitDO convert(OaVisitUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaVisitDO.class);
    }

}
