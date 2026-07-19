package com.meession.etm.module.oa.convert.message;

import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageCreateReqVO;
import com.meession.etm.module.oa.controller.admin.message.vo.OaMessageRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaMessageDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;

import java.util.List;

public class OaMessageConvert {

    public static OaMessageRespVO convert(OaMessageDO bean) {
        return BeanUtils.toBean(bean, OaMessageRespVO.class);
    }

    public static List<OaMessageRespVO> convertList(List<OaMessageDO> list) {
        return CollectionUtils.convertList(list, OaMessageConvert::convert);
    }

    public static PageResult<OaMessageRespVO> convertPage(PageResult<OaMessageDO> page) {
        return BeanUtils.toBean(page, OaMessageRespVO.class);
    }

    public static OaMessageDO convert(OaMessageCreateReqVO reqVO) {
        return BeanUtils.toBean(reqVO, OaMessageDO.class);
    }

}
