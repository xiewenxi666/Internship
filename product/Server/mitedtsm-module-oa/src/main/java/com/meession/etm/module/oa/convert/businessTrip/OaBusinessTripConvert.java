package com.meession.etm.module.oa.convert.businessTrip;

import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripRespVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripUpdateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaBusinessTripDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaBusinessTripConvert {

    public static OaBusinessTripRespVO convert(OaBusinessTripDO bean) {
        return BeanUtils.toBean(bean, OaBusinessTripRespVO.class);
    }

    public static List<OaBusinessTripRespVO> convertList(List<OaBusinessTripDO> list) {
        return CollectionUtils.convertList(list, OaBusinessTripConvert::convert);
    }

    public static PageResult<OaBusinessTripRespVO> convertPage(PageResult<OaBusinessTripDO> page) {
        return BeanUtils.toBean(page, OaBusinessTripRespVO.class);
    }

    public static OaBusinessTripDO convert(OaBusinessTripCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaBusinessTripDO.class);
    }

    public static OaBusinessTripDO convert(OaBusinessTripUpdateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaBusinessTripDO.class);
    }

}
