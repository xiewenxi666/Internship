package com.meession.etm.module.oa.convert.request;

import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestRespVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaRequestConvert {

    public static OaRequestRespVO convert(OaRequestDO bean) {
        return BeanUtils.toBean(bean, OaRequestRespVO.class);
    }

    public static List<OaRequestRespVO> convertList(List<OaRequestDO> list) {
        return CollectionUtils.convertList(list, OaRequestConvert::convert);
    }

    public static PageResult<OaRequestRespVO> convertPage(PageResult<OaRequestDO> page) {
        return BeanUtils.toBean(page, OaRequestRespVO.class);
    }

    public static OaRequestDO convert(OaRequestCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaRequestDO.class);
    }

    public static OaRequestDO convert(OaRequestUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaRequestDO.class);
    }

}
